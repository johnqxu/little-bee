package io.github.johnqxu.littleBee.model.mapper;

import io.github.johnqxu.littleBee.model.ProjectXlsData;
import io.github.johnqxu.littleBee.model.dto.ProjectDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "projectXlsData.seq", target = "projectId")
    ProjectDto toDtoFromXls(ProjectXlsData projectXlsData);
}
