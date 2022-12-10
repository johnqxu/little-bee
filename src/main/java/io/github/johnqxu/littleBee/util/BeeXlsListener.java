package io.github.johnqxu.littleBee.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.PageReadListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class BeeXlsListener<T> extends PageReadListener<T> {

    public BeeXlsListener(Consumer<List<T>> consumer) {
        super(consumer);
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        if (exception instanceof ExcelDataConvertException excelDataConvertException) {
            log.error("第{}行，第{}列解析异常，数据为:{}，错误:{}", excelDataConvertException.getRowIndex(), excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData().getStringValue(), excelDataConvertException.getMessage());
        }
    }
}
