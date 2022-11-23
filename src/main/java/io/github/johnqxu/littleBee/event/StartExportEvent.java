package io.github.johnqxu.littleBee.event;

public class StartExportEvent extends ProgressEvent {

    public StartExportEvent(Object source) {
        super(source, ProgressEnum.START_EXPORT);
    }
}
