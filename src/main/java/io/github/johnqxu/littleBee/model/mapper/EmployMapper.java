package io.github.johnqxu.littleBee.model.mapper;

import io.github.johnqxu.littleBee.model.EmployXlsData;
import io.github.johnqxu.littleBee.model.dto.EmployDto;
import io.github.johnqxu.littleBee.model.entity.EmployEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployMapper {

    EmployDto toDtoFromEntity(EmployEntity employEntity);

    @Mapping(target = "status", constant = "normal")
    EmployDto toDtoFromXls(EmployXlsData employXlsData);
}
