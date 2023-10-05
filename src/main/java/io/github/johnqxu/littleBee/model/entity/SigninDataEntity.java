package io.github.johnqxu.littleBee.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "signin_data")
public class SigninDataEntity implements Serializable {
    private static final long serialVersionUID = 4958819738843713957L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "project_name", nullable = false, length = 100)
    private String projectName;

    @Column(name = "employ_name", nullable = false, length = 10)
    private String employName;

    @Column(name = "adjust_flag", nullable = false, length = 10)
    private String adjustFlag;

}