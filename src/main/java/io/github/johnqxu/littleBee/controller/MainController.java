package io.github.johnqxu.littleBee.controller;

import io.github.johnqxu.littleBee.event.StartProcessEvent;
import io.github.johnqxu.littleBee.service.PerformService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
@Component
public class MainController implements Initializable, ApplicationListener<ApplicationEvent> {

    private final PerformService performService;
    @Resource
    ApplicationContext applicationContext;
    @FXML
    Button projectBtn;
    @FXML
    Button employBtn;
    @FXML
    Button projectEmployBtn;
    @FXML
    Button startBtn;
    @FXML
    Button exportBtn;
    @FXML
    VBox mainBox;
    @FXML
    ProgressBar progressBar;
    @FXML
    TextArea progressLog;
    private Alert alert;

    @Setter
    private File projectExcel;

    @Setter
    private File employExcel;

    @Setter
    private File signinExcel;

    public MainController(PerformService performService) {
        this.performService = performService;
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
        signinExcel = selectExcel();
    }

    private File selectExcel() {
        Stage primaryStage = (Stage) mainBox.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel文件 (*.xls|*.xlsx)", "*.txt", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(primaryStage);
    }

    public void performAction() {
        checkXlsFile(projectExcel, "课程");
        checkXlsFile(employExcel, "员工");
        checkXlsFile(signinExcel, "签到");
        applicationContext.publishEvent(new StartProcessEvent(this, employExcel, projectExcel, signinExcel));
    }

    private void checkXlsFile(File file, String fileDesc) {
        Optional.ofNullable(file).ifPresentOrElse(
                x -> {
                    if (!(file.exists()
                            && file.isFile()
                            && file.canRead()
                            && (file.getName().toLowerCase().endsWith("xls") || file.getName().toLowerCase().endsWith("xlsx")))) {
                        throw new IllegalArgumentException("错误的" + fileDesc + "文件");
                    }
                },
                () -> {
                    throw new IllegalArgumentException(fileDesc + "文件为空");
                }
        );
    }

    public void export() throws ParseException {
        exportBtn.setDisable(true);
        performService.export();
        exportBtn.setDisable(false);
    }

//    private void show(Alert.AlertType alertType, String message, ButtonType... buttons) {
//        if (alert == null) {
//            alert = new Alert(Alert.AlertType.INFORMATION);
//        }
//        alert.setAlertType(alertType);
//        alert.setContentText(message);
//        alert.getButtonTypes().removeAll(new ArrayList<>(alert.getButtonTypes()));
//        alert.getButtonTypes().addAll(buttons);
//        alert.showAndWait();
//    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
//        if (applicationEvent instanceof ProgressChangeEvent progressChangeEvent) {
//            if (progressChangeEvent.getProgress() > 0) {
//                progressBar.setProgress(progressChangeEvent.getProgress());
//            }
//            String log = progressLog.getText();
//            log = String.join("\n", progressChangeEvent.getProgressText(), log);
//            progressLog.setText(log);
//            if (progressChangeEvent.getProgress() == 1) {
//                startBtn.setDisable(false);
//            }
//        } else if (applicationEvent instanceof MessageEvent validationEvent) {
//            String log = progressLog.getText();
//            log = String.join("\n", validationEvent.getValidateMessage(), log);
//            progressLog.setText(log);
//        }
    }


}
