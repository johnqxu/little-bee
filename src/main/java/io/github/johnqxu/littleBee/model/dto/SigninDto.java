package io.github.johnqxu.littleBee.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninDto {

    private int seq;

    private String projectName;

    private String employName;
}
