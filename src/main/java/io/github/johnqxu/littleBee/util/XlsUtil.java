package io.github.johnqxu.littleBee.util;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XlsUtil {

    public static <T> List<T> readXls(File xlsFile, Class<T> clazz, int initSize) {
        List<T> xlsData = new ArrayList<>(initSize);
        if (xlsFile != null) {
            EasyExcel.read(xlsFile, clazz, new BeeXlsListener<T>(xlsData::addAll)).sheet().doRead();
        }
        return xlsData;
    }

}
