package io.github.johnqxu.littleBee.service;

import io.github.johnqxu.littleBee.entity.ProjectEntity;
import io.github.johnqxu.littleBee.exception.IllegalDataException;
import io.github.johnqxu.littleBee.model.Project;
import io.github.johnqxu.littleBee.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

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
                throw new IllegalDataException("项目时间错误:"+project.getProjectName());
            }
        }
    }

}
