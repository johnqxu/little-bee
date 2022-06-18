package io.github.johnqxu.littleBee.model;

import lombok.Data;

import java.util.Date;

@Data
public class TrainingListXlsData {

    private int seq;

    private String name;

    private String idType;

    private String idNo;

    private String mobile;

    private String companyName;

    private String examResult;

    private String isLocal;

    private String projectName;

    private int schoolHours;

    private Date projectStartDate;

    private Date projectEndDate;

    private Date trainerStartDate;

    private Date trainerEndDate;
}
