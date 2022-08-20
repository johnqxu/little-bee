package io.github.johnqxu.littleBee.event;

import io.github.johnqxu.littleBee.listener.PromptType;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class PromptEvent extends ApplicationEvent {

    private String prompt;
    private PromptType promptType;

    public PromptEvent(Object source, String prompt, PromptType promptType) {
        super(source);
        this.prompt = prompt;
        this.promptType = promptType;
    }
}
