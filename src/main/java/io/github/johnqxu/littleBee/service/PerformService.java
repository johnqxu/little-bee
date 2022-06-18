package io.github.johnqxu.littleBee.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import io.github.johnqxu.littleBee.event.MessageEvent;
import io.github.johnqxu.littleBee.event.ProgressChangeEvent;
import io.github.johnqxu.littleBee.exception.IllegalDataException;
import io.github.johnqxu.littleBee.model.*;
import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
@Slf4j
public class PerformService {
    private final EmployService employService;
    private final ProjectService projectService;
    private final SigninService signinService;

    @Resource
    ApplicationContext applicationContext;

    public PerformService(EmployService employService, ProjectService projectService, SigninService signinService) {
        this.employService = employService;
        this.projectService = projectService;
        this.signinService = signinService;
    }

    private void setProgress(String progressLog, double progress) {
        applicationContext.publishEvent(new ProgressChangeEvent(this, progressLog, progress));
    }


    private void initDatabase() {
        employService.deleteAll();
        projectService.deleteAll();
        signinService.deleteAll();
    }

    private void validate() {
        try {
            //校验员工信息
            employService.validate();
            //校验项目信息
            projectService.validate();
            //校验签到信息
            signinService.validate();

        } catch (IllegalDataException e) {
            applicationContext.publishEvent(new MessageEvent(this,e.getMessage(), Alert.AlertType.ERROR));
        }
    }

    void assign() {
        signinService.assign();
    }

    @Async
    public void perform(File projectExcel, File employExcel, File projectEmployExcel) {
        setProgress("开始初始化数据库", 0);
        initDatabase();
        setProgress("完成数据库初始化", 0.1);

        //导入项目信息
        setProgress("开始导入项目数据", 0.1);
        if (projectExcel != null) {
            EasyExcel.read(projectExcel, ProjectXlsData.class, new PageReadListener<ProjectXlsData>(dataList -> {
                for (ProjectXlsData projectData : dataList) {
                    Project project = Project.builder()
                            .projectName(projectData.getProjectName().trim())
                            .startDate(projectData.getStartDate())
                            .endDate(projectData.getEndDate())
                            .schoolHour(projectData.getSchoolHour())
                            .goalOfParticipants(projectData.getGoalOfParticipants())
                            .build();
                    projectService.create(project);
                }
            })).sheet().doRead();
        }
        setProgress("项目数据导入完成", 0.2);

        //导入员工花名册
        setProgress("开始导入员工数据", 0.2);
        if (employExcel != null) {
            EasyExcel.read(employExcel, EmployXlsData.class, new PageReadListener<EmployXlsData>(dataList -> {
                for (EmployXlsData employData : dataList) {
                    log.info("处理:{}",employData.getEmployName());
                    Employ employ = Employ.builder()
                            .employName(employData.getEmployName())
                            .companyName(employData.getCompanyName())
                            .startDate(employData.getStartDate())
                            .endDate(employData.getEndDate())
                            .idNo(employData.getIdNo())
                            .mobile(employData.getMobile())
                            .status("normal")
                            .build();
                    employService.create(employ);
                }
            })).sheet().doRead();
        }
        setProgress("完成员工数据导入", 0.3);
        setProgress("准备导入签到数据", 0.3);
        if (projectEmployExcel != null) {
            EasyExcel.read(projectEmployExcel, SigninXlsData.class, new PageReadListener<SigninXlsData>(dataList -> {
                for (SigninXlsData signinXlsData : dataList) {
                    SigninData signinData = SigninData.builder()
                            .projectName(signinXlsData.getProjectName().trim())
                            .employName(signinXlsData.getEmployName().trim())
                            .build();
                    signinService.create(signinData);
                }
            })).sheet().doRead();
        }
        setProgress("完成签到数据导入", 0.4);

        //校验数据
        setProgress("准备数据校验", 0.4);
        validate();
        setProgress("完成数据校验", 0.5);

        //处理数据
        setProgress("准备数据处理", 0.5);
        assign();
        setProgress("完成数据处理", 1.0);

    }

}
