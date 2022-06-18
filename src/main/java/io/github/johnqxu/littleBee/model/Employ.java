package io.github.johnqxu.littleBee.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@Builder
public class Employ {

    private int employId;

    @NonNull
    private String employName;

    @NonNull
    private String companyName;

    @NonNull
    private String idNo;

    @NonNull
    private String mobile;

    @NonNull
    private Date startDate;

    private Date endDate;

    private String status;
}
