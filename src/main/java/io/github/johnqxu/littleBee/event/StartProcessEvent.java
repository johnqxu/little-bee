package io.github.johnqxu.littleBee.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.io.File;

@Data
public class StartProcessEvent extends ApplicationEvent {

    private File employExcel;
    private File projectExcel;
    private File signinExcel;

    public StartProcessEvent(Object source, File employExcel, File projectExcel, File signinExcel) {
        super(source);
        this.employExcel = employExcel;
        this.projectExcel = projectExcel;
        this.signinExcel = signinExcel;
    }
}
