package io.github.johnqxu.littleBee.repository;

import io.github.johnqxu.littleBee.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {

    ProjectEntity findProjectEntityByProjectName(String projectName);
}
