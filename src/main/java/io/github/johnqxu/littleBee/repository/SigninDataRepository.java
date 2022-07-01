package io.github.johnqxu.littleBee.repository;

import io.github.johnqxu.littleBee.model.entity.SigninDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SigninDataRepository extends JpaRepository<SigninDataEntity, Integer> {

    List<SigninDataEntity> findSigninDataEntitiesByProjectName(String projectName);

    List<SigninDataEntity> findSigninDataEntitiesByEmployName(String employName);
}
