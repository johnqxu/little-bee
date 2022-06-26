package io.github.johnqxu.littleBee.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class HandlingProcessEvent extends ApplicationEvent {

    private HandlerEnum handler;

    public HandlingProcessEvent(Object source, HandlerEnum handler) {
        super(source);
        this.handler = handler;
    }

    public HandlerEnum nextHandler() {
        switch (this.getHandler()) {
            case INIT_DB:
                return HandlerEnum.IMPORT_EMPLOY;
            case IMPORT_EMPLOY:
                return HandlerEnum.IMPORT_PROJECTS;
            case IMPORT_PROJECTS:
                return HandlerEnum.IMPORT_SIGNIN;
            case IMPORT_SIGNIN:
                return HandlerEnum.VALIDATE_EMPLOY;
            case VALIDATE_EMPLOY:
                return HandlerEnum.VALIDATE_PROJECT;
            case VALIDATE_PROJECT:
                return HandlerEnum.VALIDATE_SIGNIN;
            case VALIDATE_SIGNIN:
                return HandlerEnum.ASIGN;
        }
        return null;
    }

}
