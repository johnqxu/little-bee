package io.github.johnqxu.littleBee.model;

import lombok.Data;

import java.util.Date;

@Data
public class EmployXlsData {

    private int seq;

    private String employName;

    private String companyName;

    private String idNo;

    private String mobile;

    private Date startDate;

    private Date endDate;
}
