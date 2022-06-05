package io.github.johnqxu.littleBee.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninData {

    private int seq;

    private String projectName;

    private String employName;
}
