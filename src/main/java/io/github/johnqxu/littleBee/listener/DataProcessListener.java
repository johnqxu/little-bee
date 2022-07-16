package io.github.johnqxu.littleBee.listener;

import io.github.johnqxu.littleBee.event.StartProcessEvent;
import io.github.johnqxu.littleBee.service.EmployService;
import io.github.johnqxu.littleBee.service.ProjectService;
import io.github.johnqxu.littleBee.service.SigninService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class DataProcessListener implements ApplicationListener<StartProcessEvent> {

    private final EmployService employService;
    private final ProjectService projectService;
    private final SigninService signinService;
    private File employExcel;
    private File projectExcel;
    private File signinExcel;

    public DataProcessListener(EmployService employService, ProjectService projectService, SigninService signinService) {
        this.employService = employService;
        this.projectService = projectService;
        this.signinService = signinService;
    }

    @Override
    public void onApplicationEvent(StartProcessEvent event) {
        this.employExcel = event.getEmployExcel();
        this.projectExcel = event.getProjectExcel();
        this.signinExcel = event.getSigninExcel();
        process();
    }

    private void process() {
        //初始化数据库
        employService.deleteAll();
        projectService.deleteAll();
        signinService.deleteAll();
        log.info("完成数据库初始化");

        //导入
        employService.importEmpolys(employExcel);
        projectService.importProjects(projectExcel);
        signinService.importSigninData(signinExcel);
        log.info("完成数据导入");

        //校验
        employService.validate();
        projectService.validate();
        signinService.validate();
        log.info("完成数据校验");
    }
}
