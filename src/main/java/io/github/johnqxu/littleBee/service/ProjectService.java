package io.github.johnqxu.littleBee.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import io.github.johnqxu.littleBee.entity.ProjectEntity;
import io.github.johnqxu.littleBee.event.ProgressChangeEvent;
import io.github.johnqxu.littleBee.exception.IllegalDataException;
import io.github.johnqxu.littleBee.model.Project;
import io.github.johnqxu.littleBee.model.ProjectXlsData;
import io.github.johnqxu.littleBee.repository.ProjectRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

@Service
public class ProjectService {

    @Resource
    ApplicationContext applicationContext;

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void deleteAll() {
        projectRepository.deleteAll();
    }

    public void create(Project project) {
        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectName(project.getProjectName())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .schoolHour(project.getSchoolHour())
                .priority(project.getPriority())
                .build();
        projectRepository.save(projectEntity);
    }

    public void validate() throws IllegalDataException {
        List<ProjectEntity> projects = projectRepository.findAll();
        for (ProjectEntity project : projects) {
            if (project.getStartDate() == null
                    || project.getEndDate() == null
                    || project.getStartDate().after(project.getEndDate())) {
                throw new IllegalDataException("项目时间错误:" + project.getProjectName());
            }
        }
    }

    public void importProjects(File projectExcel) {
        Set<Future> futureSet = new HashSet<>(200);
        setProgress("开始导入项目数据", 0.1);
        if (projectExcel != null) {
            EasyExcel.read(projectExcel, ProjectXlsData.class, new PageReadListener<ProjectXlsData>(dataList -> {
                for (ProjectXlsData projectData : dataList) {
                    Future<String> result = this.asyncImportProject(projectData);
                }
            })).sheet().doRead();
        }
        while (!futureSet.isEmpty()){
            Iterator<Future> it = futureSet.iterator();
            while(it.hasNext()){
                Future f = it.next();
                if(f.isDone()){
                    it.remove();
                }
            }
        }
        setProgress("项目数据导入完成", 0.2);
    }

    @Async
    public Future<String> asyncImportProject(ProjectXlsData projectData) {
        Project project = Project.builder()
                .projectName(projectData.getProjectName().trim())
                .startDate(projectData.getStartDate())
                .endDate(projectData.getEndDate())
                .schoolHour(projectData.getSchoolHour())
                .priority(projectData.getPriority())
                .build();
        this.create(project);
        return new AsyncResult<>(projectData.getProjectName());
    }

    private void setProgress(String progressLog, double progress) {
        applicationContext.publishEvent(new ProgressChangeEvent(this, progressLog, progress));
    }

}
