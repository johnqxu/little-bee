package io.github.johnqxu.littleBee.event;

public class StartAssignEvent extends ProgressEvent {

    public StartAssignEvent(Object source) {
        super(source, ProgressEnum.START_ASSIGN);
    }
}
