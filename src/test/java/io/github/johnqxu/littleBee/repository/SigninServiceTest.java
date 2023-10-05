package io.github.johnqxu.littleBee.repository;

import io.github.johnqxu.littleBee.service.SigninService;
import jakarta.annotation.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;


@DataJpaTest
@Rollback(value = false)
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SigninServiceTest {

    @Autowired
    SigninService signinService;

    @Resource
    EmployRepository employRepository;

    @Resource
    ProjectRepository projectRepository;

    @Before
    public void init() {
        employRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    public void testAssign() {
//        ProjectEntity p1 = ProjectEntity.builder()
//                .projectName("药品管理法规培训")
//                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-5-21"))
//                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-5-23"))
//                .goalOfParticipants(10)
//                .schoolHour(12)
//                .build();
//        ProjectEntity p2 = ProjectEntity.builder()
//                .projectName("宝船质量体系介绍")
//                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-9-10"))
//                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-9-10"))
//                .goalOfParticipants(10)
//                .schoolHour(8)
//                .build();
//
//        ProjectEntity p3 = ProjectEntity.builder()
//                .projectName("产品知识培训")
//                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-23"))
//                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-23"))
//                .goalOfParticipants(10)
//                .schoolHour(8)
//                .build();
//        projectRepository.saveAll(List.of(p1, p2, p3));
//
//        EmployEntity e1 = EmployEntity.builder()
//                .employName("李祥超")
//                .companyName("宝船")
//                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-5-26"))
//                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2024-5-25"))
//                .status("defalut")
//                .idNo("371502199007074017")
//                .mobile("13262902318")
//                .projects(Set.of(p1,p2,p3))
//                .build();
//        employRepository.save(e1);
//
//        signinService.assign();
    }

}
