package io.github.johnqxu.littleBee.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "project")
public class ProjectEntity implements Serializable, Comparable<ProjectEntity> {
    @Serial
    private static final long serialVersionUID = 6690363990834406951L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "project_name", nullable = false, length = 100)
    private String projectName;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "school_hour", nullable = false)
    private Integer schoolHour;

    @Column(name = "goal_of_participants")
    private Integer goalOfParticipants;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "projects")
    private Set<EmployEntity> employs;

    @Override
    public int compareTo(ProjectEntity o) {
        int gap = this.schoolHour - o.getSchoolHour();
        if (gap != 0) {
            return gap;
        } else {
            return this.getProjectName().compareTo(o.projectName);
        }
    }
}