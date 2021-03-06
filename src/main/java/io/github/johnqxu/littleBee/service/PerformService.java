package io.github.johnqxu.littleBee.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.github.johnqxu.littleBee.model.dto.EmployDto;
import io.github.johnqxu.littleBee.model.dto.ProjectDto;
import io.github.johnqxu.littleBee.model.dto.SigninDto;
import io.github.johnqxu.littleBee.model.entity.EmployEntity;
import io.github.johnqxu.littleBee.model.entity.ProjectEntity;
import io.github.johnqxu.littleBee.event.MessageEvent;
import io.github.johnqxu.littleBee.event.ProgressChangeEvent;
import io.github.johnqxu.littleBee.exception.IllegalDataException;
import io.github.johnqxu.littleBee.model.*;
import io.github.johnqxu.littleBee.repository.EmployRepository;
import io.github.johnqxu.littleBee.repository.ProjectRepository;
import javafx.scene.control.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PerformService {
    private final EmployService employService;
    private final ProjectService projectService;
    private final SigninService signinService;

    private final EmployRepository employRepository;
    private final ProjectRepository projectRepository;

    @Resource
    ApplicationContext applicationContext;

    public PerformService(EmployService employService, ProjectService projectService, SigninService signinService, EmployRepository employRepository, ProjectRepository projectRepository) {
        this.employService = employService;
        this.projectService = projectService;
        this.signinService = signinService;
        this.employRepository = employRepository;
        this.projectRepository = projectRepository;
    }

    private void setProgress(String progressLog, double progress) {
        applicationContext.publishEvent(new ProgressChangeEvent(this, progressLog, progress));
    }


    public void initDatabase() {
        employService.deleteAll();
        projectService.deleteAll();
        signinService.deleteAll();
    }

    private void validate() {
        try {
            //??????????????????
            employService.validate();
            //??????????????????
            projectService.validate();
            //??????????????????
            signinService.validate();

        } catch (IllegalDataException e) {
            applicationContext.publishEvent(new MessageEvent(this, e.getMessage(), Alert.AlertType.ERROR));
        }
    }

    void assign() {
        signinService.assign();
    }

    @Async
    public void perform(File projectExcel, File employExcel, File projectEmployExcel) {
        setProgress("????????????????????????", 0);
        initDatabase();
        setProgress("????????????????????????", 0.1);

        //??????????????????
        setProgress("????????????????????????", 0.1);
        if (projectExcel != null) {
            EasyExcel.read(projectExcel, ProjectXlsData.class, new PageReadListener<ProjectXlsData>(dataList -> {
                for (ProjectXlsData projectData : dataList) {
                    ProjectDto projectDto = ProjectDto.builder()
                            .projectName(projectData.getProjectName().trim())
                            .startDate(projectData.getStartDate())
                            .endDate(projectData.getEndDate())
                            .schoolHour(projectData.getSchoolHour())
                            .priority(projectData.getPriority())
                            .build();
                    projectService.create(projectDto);
                }
            })).sheet().doRead();
        }
        setProgress("????????????????????????", 0.2);

        //?????????????????????
        setProgress("????????????????????????", 0.2);
        if (employExcel != null) {
            EasyExcel.read(employExcel, EmployXlsData.class, new PageReadListener<EmployXlsData>(dataList -> {
                for (EmployXlsData employData : dataList) {
                    log.info("??????:{}", employData.getEmployName());
                    EmployDto employDto = EmployDto.builder()
                            .employName(employData.getEmployName())
                            .companyName(employData.getCompanyName())
                            .startDate(employData.getStartDate())
                            .endDate(employData.getEndDate())
                            .idNo(employData.getIdNo())
                            .mobile(employData.getMobile())
                            .status("normal")
                            .build();
                    employService.create(employDto);
                }
            })).sheet().doRead();
        }
        setProgress("????????????????????????", 0.3);
        setProgress("????????????????????????", 0.3);
        if (projectEmployExcel != null) {
            EasyExcel.read(projectEmployExcel, SigninXlsData.class, new PageReadListener<SigninXlsData>(dataList -> {
                for (SigninXlsData signinXlsData : dataList) {
                    SigninDto signinDto = SigninDto.builder()
                            .projectName(signinXlsData.getProjectName().trim())
                            .employName(signinXlsData.getEmployName().trim())
                            .build();
                    signinService.create(signinDto);
                }
            })).sheet().doRead();
        }
        setProgress("????????????????????????", 0.4);

        //????????????
        setProgress("??????????????????", 0.4);
        validate();
        setProgress("??????????????????", 0.5);

        //????????????
        setProgress("??????????????????", 0.5);
        assign();
        setProgress("??????????????????", 1.0);

    }

    public void export() throws ParseException {
        String xlsFile = "????????????-" + System.currentTimeMillis() + ".xls";
        try (ExcelWriter excelWriter = EasyExcel.write(xlsFile).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(1, "????????????").head(TrainingListXlsData.class).build();
            List<TrainingListXlsData> trainingData = exportTrainingDetailData();
            excelWriter.write(trainingData, writeSheet);

            writeSheet = EasyExcel.writerSheet(2, "????????????").head(SupplementaryXlsData.class).build();
            List<SupplementaryXlsData> supplementaryXlsData = exportSupplementaryData();
            excelWriter.write(supplementaryXlsData, writeSheet);
        }
    }

    private List<TrainingListXlsData> exportTrainingDetailData() {
        List<TrainingListXlsData> list = ListUtils.newArrayList();
        List<EmployEntity> employs = employRepository.findAll();
        int i = 1;
        for (EmployEntity employEntity : employs) {
            if (employEntity.getProjects() != null) {
                for (ProjectEntity project : employEntity.getProjects()) {
                    TrainingListXlsData data = new TrainingListXlsData();
                    data.setCompanyName(employEntity.getCompanyName());
                    data.setIdNo(employEntity.getIdNo());
                    data.setMobile(employEntity.getMobile());
                    data.setExamResult("??????");
                    data.setIdType("?????????");
                    data.setName(employEntity.getEmployName());
                    data.setIsLocal("???");
                    data.setTrainerStartDate(employEntity.getStartDate());
                    data.setTrainerEndDate(employEntity.getEndDate());
                    data.setProjectName(project.getProjectName());
                    data.setProjectStartDate(project.getStartDate());
                    data.setProjectEndDate(project.getEndDate());
                    data.setSchoolHours(project.getSchoolHour());
                    data.setSeq(i++);
                    list.add(data);
                }
            }
        }
        return list;
    }

    private List<SupplementaryXlsData> exportSupplementaryData() throws ParseException {
        List<SupplementaryXlsData> dataList = ListUtils.newArrayList();
        List<EmployEntity> employs = employRepository.findAll();
        for (EmployEntity employ : employs) {
            if (employ.getProjects() == null || employ.getProjects().size() < 3) {
                Date startDate = employ.getStartDate();
                Date endDate = employ.getEndDate();
                if (endDate == null) {
                    endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2099-12-31");
                }
                List<ProjectEntity> projects = projectRepository.findProjectEntitiesByStartDateAfterAndEndDateBefore(startDate, endDate);
                for (ProjectEntity project : projects) {
                    if(!employ.getProjects().contains(project)) {
                        SupplementaryXlsData data = new SupplementaryXlsData();
                        data.setEmployEndDate(employ.getEndDate());
                        data.setEmployStartDate(employ.getStartDate());
                        data.setProject(project.getProjectName());
                        int trainingTimes = employ.getProjects() == null ? 0 : employ.getProjects().size();
                        data.setTrainingTimes(trainingTimes);
                        data.setName(employ.getEmployName());
                        data.setProjectEndDate(project.getEndDate());
                        data.setProjectStartDate(project.getStartDate());
                        data.setCompanyName(employ.getCompanyName());
                        data.setSchoolHours(project.getSchoolHour());
                        dataList.add(data);
                    }
                }
            }

        }
        return dataList;
    }

}
