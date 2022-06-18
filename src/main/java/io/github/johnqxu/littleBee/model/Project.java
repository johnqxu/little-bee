package io.github.johnqxu.littleBee.model;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class Project {

    private int projectId;

    private String projectName;

    private Date startDate;

    private Date endDate;

    private int schoolHour;

    private int priority;
}
