package io.github.johnqxu.littleBee.service;

import io.github.johnqxu.littleBee.exception.IllegalDataException;
import io.github.johnqxu.littleBee.model.SigninXlsData;
import io.github.johnqxu.littleBee.model.dto.SigninDto;
import io.github.johnqxu.littleBee.model.entity.EmployEntity;
import io.github.johnqxu.littleBee.model.entity.ProjectEntity;
import io.github.johnqxu.littleBee.model.entity.SigninDataEntity;
import io.github.johnqxu.littleBee.model.mapper.SigninMapper;
import io.github.johnqxu.littleBee.repository.EmployRepository;
import io.github.johnqxu.littleBee.repository.ProjectRepository;
import io.github.johnqxu.littleBee.repository.SigninDataRepository;
import io.github.johnqxu.littleBee.util.XlsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class SigninService extends ProgressableService {

    private final SigninDataRepository signinRepository;
    private final ProjectRepository projectRepository;
    private final EmployRepository employRepository;
    private final SigninMapper signinMapper;

    @Resource
    ApplicationContext applicationContext;

    public SigninService(SigninDataRepository projectRepository, ProjectRepository projectRepository1, EmployRepository employRepository, SigninMapper signinMapper) {
        this.signinRepository = projectRepository;
        this.projectRepository = projectRepository1;
        this.employRepository = employRepository;
        this.signinMapper = signinMapper;
    }

    public void deleteAll() {
        signinRepository.deleteAll();
    }

    public void create(SigninDto signinDto) {
        SigninDataEntity signinDataEntity = SigninDataEntity.builder()
                .projectName(signinDto.getProjectName())
                .employName(signinDto.getEmployName())
                .adjustFlag("default")
                .build();
        signinRepository.save(signinDataEntity);
    }

    public void validate() throws IllegalDataException {
        List<SigninDataEntity> signinDataEntities = signinRepository.findAll();
        for (SigninDataEntity signinDataEntity : signinDataEntities) {
            ProjectEntity projectEntity = projectRepository.findProjectEntityByProjectName(signinDataEntity.getProjectName());
            if (projectEntity == null) {
                throw new IllegalDataException("签到信息错误，没有找到培训项目:" + signinDataEntity.getEmployName() + "-" + signinDataEntity.getProjectName());
            }
        }
    }

    public void assign() {
        List<EmployEntity> employEntities = employRepository.findAll();
        log.info("totle employs:{}",employEntities.size());
        for (EmployEntity employEntity : employEntities) {
            log.info("employ:{}",employEntity.getEmployName());
            Set<ProjectEntity> employProjects = fetchEmployProjects(employEntity);
            log.info("projects:{}",employProjects);
            employEntity.setProjects(employProjects);
            employRepository.save(employEntity);
        }
    }

    private Set<ProjectEntity> fetchEmployProjects(EmployEntity employ) {
        Set<ProjectEntity> ps = new HashSet<>();
        List<SigninDataEntity> signinDataEntities = signinRepository.findSigninDataEntitiesByEmployName(employ.getEmployName());
        for (SigninDataEntity signinData : signinDataEntities) {
            ProjectEntity signinProject = projectRepository.findProjectEntityByProjectName(signinData.getProjectName());
            if (signinProject.getStartDate().after(employ.getStartDate()) && (employ.getEndDate() == null || signinProject.getEndDate().before(employ.getEndDate()))) {
                ps.add(signinProject);
            }
        }
        Set<ProjectEntity> sortedProjects = new TreeSet<>(Comparator.reverseOrder());
        sortedProjects.addAll(ps);
        Iterator<ProjectEntity> ip = sortedProjects.iterator();
        int i = 0;
        while (ip.hasNext()) {
            i++;
            ProjectEntity p = ip.next();
            log.info("项目:{},课时:{}", p.getProjectName(), p.getSchoolHour());
            if (i > 3) {
                ip.remove();
                log.info("删除项目:{},当前容量:{}", p.getProjectName(), sortedProjects.size());
            }
        }
        return sortedProjects;
    }

    public void importSigninData(File signinExcel) {
        List<SigninXlsData> signinXlsDataList = XlsUtil.readXls(signinExcel, SigninXlsData.class, 1000);
        CompletableFuture.allOf(signinXlsDataList.stream().map(e -> CompletableFuture.supplyAsync(() -> {
            SigninDto signinDto = signinMapper.toDtoFromXls(e);
            this.create(signinDto);
            return signinDto;
        })).toArray(CompletableFuture[]::new)).join();
    }

}
