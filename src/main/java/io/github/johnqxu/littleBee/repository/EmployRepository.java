package io.github.johnqxu.littleBee.repository;

import io.github.johnqxu.littleBee.entity.EmployEntity;
import io.github.johnqxu.littleBee.model.Employ;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployRepository extends JpaRepository<EmployEntity, Integer>, JpaSpecificationExecutor<EmployEntity> {

    List<EmployEntity> findEmployEntitiesByEmployNameOrderByStartDateAsc(String employName);

    @Query("select distinct employName from EmployEntity ")
    List<String> findDistinctEmployName();

    EmployEntity findEmployEntityByEmployName(String employName);

}