package io.github.johnqxu.littleBee.event;

public class FinishAssignEvent extends ProgressEvent {

    public FinishAssignEvent(Object source) {
        super(source, ProgressEnum.FINISH_ASSIGN);
    }
}
