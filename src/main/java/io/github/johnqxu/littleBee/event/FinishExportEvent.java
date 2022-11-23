package io.github.johnqxu.littleBee.event;

public class FinishExportEvent extends ProgressEvent {
    public FinishExportEvent(Object source) {
        super(source, ProgressEnum.FINISH_EXPORT);
    }
}
