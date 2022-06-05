package io.github.johnqxu.littleBee.repository;

import io.github.johnqxu.littleBee.entity.EmployEntity;
import io.github.johnqxu.littleBee.entity.ProjectEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@Rollback(value = false)
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployRepositoryTest {

    @Resource
    EmployRepository employRepository;

    @Resource
    ProjectRepository projectRepository;

    @Test
    public void testSave() {
        employRepository.deleteAll();
        projectRepository.deleteAll();
        ProjectEntity p1 = ProjectEntity.builder()
                .projectName("项目1")
                .startDate(new Date())
                .endDate(new Date())
                .goalOfParticipants(10)
                .schoolHour(1)
                .build();
        ProjectEntity p2 = ProjectEntity.builder()
                .projectName("项目2")
                .startDate(new Date())
                .endDate(new Date())
                .goalOfParticipants(20)
                .schoolHour(2)
                .build();
        ProjectEntity p3 = ProjectEntity.builder()
                .projectName("项目3")
                .startDate(new Date())
                .endDate(new Date())
                .goalOfParticipants(30)
                .schoolHour(3)
                .build();
        projectRepository.saveAll(List.of(p1, p2, p3));
        EmployEntity e1 = EmployEntity.builder()
                .employName("张三")
                .companyName("平安")
                .startDate(new Date())
                .status("defalut")
                .projects(Set.of(p1))
                .build();
        employRepository.save(e1);
        Set s  = new HashSet(e1.getProjects());
        s.add(p2);
        e1.setProjects(s);
        employRepository.save(e1);
        employRepository.save(e1);
//        e1.getProjects().add(p3);
//        employRepository.save(e1);

//        EmployEntity em = employRepository.findEmployEntityByEmployName("张三");
//        em.setProjects(Set.of(p2));
//        employRepository.save(em);
    }
}
