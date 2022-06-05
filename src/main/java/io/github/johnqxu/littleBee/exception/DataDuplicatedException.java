package io.github.johnqxu.littleBee.exception;

public class DataDuplicatedException extends RuntimeException {

    public DataDuplicatedException(String dataType) {
        super("数据重复:" + dataType);
    }
}
