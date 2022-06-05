package io.github.johnqxu.littleBee.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import io.github.johnqxu.littleBee.exception.IllegalDataException;
import io.github.johnqxu.littleBee.model.*;
import io.github.johnqxu.littleBee.service.EmployService;
import io.github.johnqxu.littleBee.service.ProjectService;
import io.github.johnqxu.littleBee.service.SigninService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MainController implements Initializable {

    private final EmployService employService;
    private final ProjectService projectService;
    private final SigninService signinService;

    @FXML
    Button projectBtn;

    @FXML
    Button employBtn;

    @FXML
    Button projectEmployBtn;

    @FXML
    Button startBtn;

    @FXML
    VBox mainBox;

    @FXML
    ProgressBar progressBar;

    private Alert alert;

    private File projectExcel, employExcel, projectEmployExcel;

    public MainController(EmployService employService, ProjectService projectService, SigninService signinService) {
        this.employService = employService;
        this.projectService = projectService;
        this.signinService = signinService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void selectProjectExcel() {
        projectExcel = selectExcel();
    }

    public void selectEmployExcel() {
        employExcel = selectExcel();
    }

    public void selectProjectEmployExcel() {
        projectEmployExcel = selectExcel();
    }

    private File selectExcel() {
        Stage primaryStage = (Stage) mainBox.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel文件 (*.xls|*.xlsx)", "*.txt", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(primaryStage);
    }

    public void performAction() {
        progressBar.setProgress(0);
        //初始化数据库
        initDatabase();

        //导入项目信息
        if(projectExcel!=null) {
            progressBar.setProgress(0.1);
            log.info("开始导入项目信息");
            EasyExcel.read(projectExcel, ProjectXlsData.class, new PageReadListener<ProjectXlsData>(dataList -> {
                for (ProjectXlsData projectData : dataList) {
                    Project project = Project.builder()
                            .projectName(projectData.getProjectName().trim())
                            .startDate(projectData.getStartDate())
                            .endDate(projectData.getEndDate())
                            .schoolHour(projectData.getSchoolHour())
                            .goalOfParticipants(projectData.getGoalOfParticipants())
                            .build();
                    projectService.create(project);
                }
            })).sheet().doRead();
        }

        //导入员工花名册
        if(employExcel!=null) {
            progressBar.setProgress(0.2);
            log.info("开始导入员工信息");
            EasyExcel.read(employExcel, EmployXlsData.class, new PageReadListener<EmployXlsData>(dataList -> {
                for (EmployXlsData employData : dataList) {
                    Employ employ = Employ.builder()
                            .employName(employData.getEmployName())
                            .companyName(employData.getCompanyName())
                            .startDate(employData.getStartDate())
                            .endDate(employData.getEndDate())
                            .status("normal")
                            .build();
                    employService.create(employ);
                }
            })).sheet().doRead();
        }

        //导入项目签到信息
        if(projectEmployExcel!=null) {
            progressBar.setProgress(0.3);
            log.info("开始导入项目签到信息");
            EasyExcel.read(projectEmployExcel, SigninXlsData.class, new PageReadListener<SigninXlsData>(dataList -> {
                for (SigninXlsData signinXlsData : dataList) {
//                    log.info("读取到一条数据{}", signinXlsData);
                    SigninData signinData = SigninData.builder()
                            .projectName(signinXlsData.getProjectName().trim())
                            .employName(signinXlsData.getEmployName().trim())
                            .build();
                    signinService.create(signinData);
                }
            })).sheet().doRead();
        }

        //校验数据
        validate();

        //处理数据
        assign();
    }

    private void initDatabase() {
        log.info("开始初始化数据库");
        employService.deleteAll();
        projectService.deleteAll();
        signinService.deleteAll();
        log.info("完成数据库初始化");
    }

    private void validate(){
        try {
            //校验员工信息
            employService.validate();
            //校验项目信息
            projectService.validate();
            //校验签到信息
            signinService.validate();

        } catch (IllegalDataException e){
            show(Alert.AlertType.ERROR,e.getMessage(),ButtonType.OK);
        }

        show(Alert.AlertType.INFORMATION,"完成数据验证",ButtonType.OK);
    }

    private void assign(){
        signinService.assign();
        show(Alert.AlertType.INFORMATION,"完成合并",ButtonType.OK);
    }

    private void show(Alert.AlertType alertType, String message, ButtonType... buttons) {
        if (alert == null) {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        alert.setAlertType(alertType);
        alert.setContentText(message);
        alert.getButtonTypes().removeAll(alert.getButtonTypes().stream().collect(Collectors.toList()));
        alert.getButtonTypes().addAll(buttons);
        alert.showAndWait();
    }
}
