package io.github.johnqxu.littleBee.controller;

import io.github.johnqxu.littleBee.event.ProgressChangeEvent;
import io.github.johnqxu.littleBee.event.MessageEvent;
import io.github.johnqxu.littleBee.service.PerformService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Slf4j
@Component
public class MainController implements Initializable, ApplicationListener<ApplicationEvent> {

    private final PerformService performService;
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
    @FXML
    TextArea progressLog;
    private Alert alert;

    private File projectExcel, employExcel, projectEmployExcel;

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
        performService.perform(projectExcel, employExcel, projectEmployExcel);
        startBtn.setDisable(true);
    }

    public void export(){

    }

    private void show(Alert.AlertType alertType, String message, ButtonType... buttons) {
        if (alert == null) {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        alert.setAlertType(alertType);
        alert.setContentText(message);
        alert.getButtonTypes().removeAll(new ArrayList<>(alert.getButtonTypes()));
        alert.getButtonTypes().addAll(buttons);
        alert.showAndWait();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ProgressChangeEvent progressChangeEvent) {
            progressBar.setProgress(progressChangeEvent.getProgress());
            String log = progressLog.getText();
            log = String.join("\n", progressChangeEvent.getProgressText(), log);
            progressLog.setText(log);
            if(progressChangeEvent.getProgress()==1){
                startBtn.setDisable(false);
            }
        } else if (applicationEvent instanceof MessageEvent validationEvent) {
            String log = progressLog.getText();
            log = String.join("\n", validationEvent.getValidateMessage(), log);
            progressLog.setText(log);
        }

    }
}
