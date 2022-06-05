package io.github.johnqxu.littleBee;

import io.github.johnqxu.littleBee.event.StageReadyEvent;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

@Slf4j
public class JavafxApplication extends Application {
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        ApplicationContextInitializer<GenericApplicationContext> initializer = genericApplicationContext -> {
            genericApplicationContext.registerBean(Application.class, () -> JavafxApplication.this);
            genericApplicationContext.registerBean(Parameters.class, this::getParameters);
            genericApplicationContext.registerBean(HostServices.class, this::getHostServices);
        };
        this.context = new SpringApplicationBuilder().sources(LittleBeeApplication.class)
                .initializers(initializer)
                .build().run(getParameters().getRaw().toArray(new String[0]));
        log.info("application init");
    }

    @Override
    public void start(Stage stage) {
        log.info("application start!");
        this.context.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        log.info("application stop!");
        context.close();
        Platform.exit();
    }
}
