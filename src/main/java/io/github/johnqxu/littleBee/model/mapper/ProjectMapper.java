package io.github.johnqxu.littleBee.model.mapper;

import io.github.johnqxu.littleBee.model.ProjectXlsData;
import io.github.johnqxu.littleBee.model.dto.ProjectDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDto toDtoFromXls(ProjectXlsData projectXlsData);
}
