package io.github.johnqxu.littleBee.event;

import javafx.scene.control.Alert;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class MessageEvent extends ApplicationEvent {

    private String validateMessage;
    private Alert.AlertType alertType;

    public MessageEvent(Object source, String validateMessage, Alert.AlertType alertType) {
        super(source);
        this.alertType = alertType;
        this.validateMessage = validateMessage;
    }
}
