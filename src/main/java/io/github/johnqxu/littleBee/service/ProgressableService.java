package io.github.johnqxu.littleBee.service;

import io.github.johnqxu.littleBee.event.ProgressChangeEvent;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

public class ProgressableService {

    @Resource
    ApplicationContext applicationContext;

    void setProgress(String progressLog, double progress) {
        applicationContext.publishEvent(new ProgressChangeEvent(this, progressLog, progress));
    }
}
