package io.github.johnqxu.littleBee.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class PromptEvent extends ApplicationEvent {

    private String prompt;

    public PromptEvent(Object source, String prompt) {
        super(source);
        this.prompt = prompt;
    }
}
