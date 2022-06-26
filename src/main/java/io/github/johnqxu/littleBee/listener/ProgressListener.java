package io.github.johnqxu.littleBee.listener;

import io.github.johnqxu.littleBee.event.HandlingProcessEvent;
import io.github.johnqxu.littleBee.event.ProgressChangeEvent;
import io.github.johnqxu.littleBee.service.EmployService;
import io.github.johnqxu.littleBee.service.PerformService;
import io.github.johnqxu.littleBee.service.ProjectService;
import io.github.johnqxu.littleBee.service.SigninService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ProgressListener implements ApplicationListener<HandlingProcessEvent> {

    private final EmployService employService;
    private final ProjectService projectService;
    private final SigninService signinService;
    private final PerformService performService;

    @Resource
    ApplicationContext applicationContext;

    public ProgressListener(EmployService employService, ProjectService projectService, SigninService signinService, PerformService performService) {
        this.employService = employService;
        this.projectService = projectService;
        this.signinService = signinService;
        this.performService = performService;
    }

    @Override
    public void onApplicationEvent(HandlingProcessEvent event) {
        File projectXlsFile = (File) applicationContext.getBean("projectXlsFile");
        File employXlsFile = (File) applicationContext.getBean("employXlsFile");
        File signinXlsFile = (File) applicationContext.getBean("signinXlsFile");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowString = formatter.format(new Date(System.currentTimeMillis()));
        String log = String.format("%s --- 开始%s", nowString, event.getHandler().getHandlerName());
        applicationContext.publishEvent(new ProgressChangeEvent(this, log));
        switch (event.getHandler()) {
            case INIT_DB:
                performService.initDatabase();
                break;
            case IMPORT_EMPLOY:
                employService.importEmpolys(employXlsFile);
                break;
            case IMPORT_PROJECTS:
                projectService.importProjects(projectXlsFile);
                break;
            case IMPORT_SIGNIN:
                break;
            case VALIDATE_EMPLOY:
                break;
            case VALIDATE_PROJECT:
                break;
            case VALIDATE_SIGNIN:
                break;
            case ASIGN:
                break;
            default:
                throw new RuntimeException("未知handler");

        }
        if (event.nextHandler() != null) {
            applicationContext.publishEvent(new HandlingProcessEvent(this, event.nextHandler()));
        }
        nowString = formatter.format(new Date(System.currentTimeMillis()));
        log = String.format("%s --- 完成%s", nowString, event.getHandler().getHandlerName());
        applicationContext.publishEvent(new ProgressChangeEvent(this, log, event.getHandler().getPercentage()));
    }
}
