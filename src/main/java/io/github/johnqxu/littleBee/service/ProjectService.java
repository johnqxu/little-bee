package io.github.johnqxu.littleBee.service;

import io.github.johnqxu.littleBee.exception.IllegalDataException;
import io.github.johnqxu.littleBee.model.ProjectXlsData;
import io.github.johnqxu.littleBee.model.dto.ProjectDto;
import io.github.johnqxu.littleBee.model.entity.ProjectEntity;
import io.github.johnqxu.littleBee.model.mapper.ProjectMapper;
import io.github.johnqxu.littleBee.repository.ProjectRepository;
import io.github.johnqxu.littleBee.util.XlsUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProjectService extends ProgressableService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public void deleteAll() {
        projectRepository.deleteAll();
    }

    public void create(ProjectDto projectDto) {
        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectName(projectDto.getProjectName())
                .startDate(projectDto.getStartDate())
                .endDate(projectDto.getEndDate())
                .schoolHour(projectDto.getSchoolHour())
                .priority(projectDto.getPriority())
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
        List<ProjectXlsData> projectXlsDataList = XlsUtil.readXls(projectExcel, ProjectXlsData.class, 200);
        CompletableFuture.allOf(projectXlsDataList.stream().map(e -> CompletableFuture.supplyAsync(() -> {
            ProjectDto projectDto = projectMapper.toDtoFromXls(e);
            this.create(projectDto);
            return projectDto;
        })).toArray(CompletableFuture[]::new)).join();
    }
}
