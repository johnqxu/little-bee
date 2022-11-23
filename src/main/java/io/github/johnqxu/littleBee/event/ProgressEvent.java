package io.github.johnqxu.littleBee.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class ProgressEvent extends ApplicationEvent {

    ProgressEnum progress;

    public ProgressEvent(Object source, ProgressEnum progress) {
        super(source);
        this.progress = progress;
    }
}
