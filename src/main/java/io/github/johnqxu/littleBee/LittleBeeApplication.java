package io.github.johnqxu.littleBee;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
class LittleBeeApplication {

    public static void main(String[] args) {
        Application.launch(JavafxApplication.class, args);
    }

}