package io.github.johnqxu.littleBee.model;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectXlsData {

    private int seq;

    private String projectName;

    private Date startDate;

    private Date endDate;

    private int schoolHour;

    private int goalOfParticipants;
}
