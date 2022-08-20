package io.github.johnqxu.littleBee.controller;

import io.github.johnqxu.littleBee.event.PromptEvent;
import io.github.johnqxu.littleBee.event.StartProcessEvent;
import io.github.johnqxu.littleBee.service.PerformService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
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
public class MainController implements Initializable, ApplicationListener<PromptEvent> {

    private final PerformService performService;

    private File uploadPath;
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
    TextFlow progressLog;

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
        setUploadPath(projectExcel);
    }

    public void selectEmployExcel() {
        employExcel = selectExcel();
        setUploadPath(employExcel);
    }

    public void selectProjectEmployExcel() {
        signinExcel = selectExcel();
        setUploadPath(signinExcel);
    }

    private void setUploadPath(File file) {
        this.uploadPath = file != null ? file.getParentFile() : null;
    }

    private File selectExcel() {
        Stage primaryStage = (Stage) mainBox.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(this.uploadPath);
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

    @Override
    public void onApplicationEvent(PromptEvent event) {
        Text text = new Text();
        switch (event.getPromptType()) {
            case INFO:
                text.setStyle("-fx-fill: #1b143b");
                break;
            case WARNING:
                text.setStyle("-fx-fill: #44B03B");
                break;
            case ERROR:
                text.setStyle("-fx-fill: red");
                break;
            default:
                text.setStyle("-fx-fill: green");
        }
        text.setText(String.format("%s%n", event.getPrompt()));
        progressLog.getChildren().add(text);
    }
}
