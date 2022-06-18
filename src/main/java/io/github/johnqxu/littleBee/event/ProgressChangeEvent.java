package io.github.johnqxu.littleBee.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class ProgressChangeEvent extends ApplicationEvent {

    private String progressText;

    private double progress;

    public ProgressChangeEvent(Object source,String progressText, double progress) {
        super(source);
        this.progress = progress;
        this.progressText = progressText;
    }
}
