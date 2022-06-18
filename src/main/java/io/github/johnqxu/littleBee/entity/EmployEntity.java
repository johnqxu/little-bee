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
@Table(name = "employ")
public class EmployEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2633963947978462906L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "employ_name", nullable = false, length = 10)
    private String employName;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "id_no", nullable = false, length = 100)
    private String idNo;

    @Column(name = "mobile", nullable = false, length = 100)
    private String mobile;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status", length = 10)
    private String status;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
    @JoinTable(
            name = "project_employ",
            joinColumns = @JoinColumn(name = "employ_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id")

    )
    private Set<ProjectEntity> projects;

}