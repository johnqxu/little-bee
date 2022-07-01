package io.github.johnqxu.littleBee.model.mapper;

import io.github.johnqxu.littleBee.model.SigninXlsData;
import io.github.johnqxu.littleBee.model.dto.SigninDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SigninMapper {

    SigninDto toDtoFromXls(SigninXlsData signinXlsData);
}
