package io.github.johnqxu.littleBee.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.github.johnqxu.littleBee.model.SupplementaryXlsData;
import io.github.johnqxu.littleBee.model.TrainingListXlsData;
import io.github.johnqxu.littleBee.model.entity.EmployEntity;
import io.github.johnqxu.littleBee.model.entity.ProjectEntity;
import io.github.johnqxu.littleBee.repository.EmployRepository;
import io.github.johnqxu.littleBee.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public void export() throws ParseException {
        String xlsFile = "导出数据-" + System.currentTimeMillis() + ".xls";
        try (ExcelWriter excelWriter = EasyExcel.write(xlsFile).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(1, "参训明细").head(TrainingListXlsData.class).build();
            List<TrainingListXlsData> trainingData = exportTrainingDetailData();
            excelWriter.write(trainingData, writeSheet);

            writeSheet = EasyExcel.writerSheet(2, "补充建议").head(SupplementaryXlsData.class).build();
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
                    data.setExamResult("合格");
                    data.setIdType("身份证");
                    data.setName(employEntity.getEmployName());
                    data.setIsLocal("是");
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
