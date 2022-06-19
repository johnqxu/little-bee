package io.github.johnqxu.littleBee.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import lombok.Data;

import java.util.Date;

@Data
@HeadFontStyle(fontHeightInPoints = 10,fontName = "Arial")
public class TrainingListXlsData {

    @ColumnWidth(3)
    @ExcelProperty("序号")
    private int seq;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("证件类型")
    @NumberFormat
    private String idType;

    @ColumnWidth(20)
    @ExcelProperty("证件号码")
    private String idNo;

    @ColumnWidth(13)
    @ExcelProperty("手机")
    private String mobile;

    @ColumnWidth(10)
    @ExcelProperty("公司名称")
    private String companyName;

    @ExcelProperty("考核结果")
    private String examResult;

    @ExcelProperty("是否本市户籍")
    private String isLocal;

    @ColumnWidth(20)
    @ExcelProperty("课程名称")
    private String projectName;

    @ExcelProperty("课时")
    private int schoolHours;

    @ColumnWidth(12)
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("课程开始日期")
    private Date projectStartDate;

    @ColumnWidth(12)
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("课程结束日期")
    private Date projectEndDate;

    @ColumnWidth(12)
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("社保开始缴纳日期")
    private Date trainerStartDate;

    @ColumnWidth(12)
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("社保结束缴纳日期")
    private Date trainerEndDate;
}
