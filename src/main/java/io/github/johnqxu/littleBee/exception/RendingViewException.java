package io.github.johnqxu.littleBee.exception;

public class RendingViewException extends RuntimeException {

    public RendingViewException(Throwable e) {
        super("渲染界面出现问题", e);
    }

    public RendingViewException(String fxml, Throwable e) {
        super("渲染界面出现问题,fxml=".concat(fxml), e);
    }
}