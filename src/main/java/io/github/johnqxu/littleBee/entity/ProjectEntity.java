package io.github.johnqxu.littleBee.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "project_name", nullable = false, length = 100)
    private String projectName;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "school_hour", nullable = false)
    private Integer schoolHour;

    @Column(name = "priority")
    private Integer priority;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "projects")
    private Set<EmployEntity> employs;

    @Override
    public int compareTo(ProjectEntity o) {
        int gapSchoolHour = this.schoolHour - o.getSchoolHour();
        if (gapSchoolHour != 0) {
            return gapSchoolHour;
        } else {
            int gapPriority = this.priority - o.getPriority();
            if(gapPriority != 0){
                return gapPriority;
            }else{
                return this.getProjectName().compareTo(o.projectName);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProjectEntity project = (ProjectEntity) o;
        return id != null && Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}