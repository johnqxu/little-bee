package io.github.johnqxu.littleBee.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import lombok.Data;

import java.util.Date;

@Data
@HeadFontStyle(fontHeightInPoints = 11,fontName = "宋体")
@ContentFontStyle(fontHeightInPoints = 11,fontName = "宋体")
public class SupplementaryXlsData {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("公司名称")
    private String companyName;

    @ColumnWidth(12)
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("社保开始日期")
    private Date employStartDate;

    @ColumnWidth(12)
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("社保结束日期")
    private Date employEndDate;

    @ExcelProperty("参训次数")
    private int trainingTimes;

    @ExcelProperty("课程编号")
    private int projectId;

    @ExcelProperty("课程名称")
    private String project;

    @ExcelProperty("课时数")
    private int schoolHours;

    @ColumnWidth(12)
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("培训开始日期")
    private Date projectStartDate;

    @ColumnWidth(12)
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("培训结束日期")
    private Date projectEndDate;
}
