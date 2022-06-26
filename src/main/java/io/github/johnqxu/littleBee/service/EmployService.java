package io.github.johnqxu.littleBee.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import io.github.johnqxu.littleBee.entity.EmployEntity;
import io.github.johnqxu.littleBee.event.ProgressChangeEvent;
import io.github.johnqxu.littleBee.exception.IllegalDataException;
import io.github.johnqxu.littleBee.model.Employ;
import io.github.johnqxu.littleBee.model.EmployXlsData;
import io.github.johnqxu.littleBee.repository.EmployRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployService extends ProgressableService {

    private final EmployRepository employRepository;

    public EmployService(EmployRepository employRepository) {
        this.employRepository = employRepository;
    }

    public void create(Employ employ) {
        EmployEntity employEntity = new EmployEntity();
        employEntity.setEmployName(employ.getEmployName().trim());
        employEntity.setCompanyName(employ.getCompanyName().trim());
        employEntity.setStartDate(employ.getStartDate());
        if (employ.getEndDate() != null) {
            employEntity.setEndDate(employ.getEndDate());
        }
        employEntity.setIdNo(employ.getIdNo());
        employEntity.setMobile(employ.getMobile());
        employRepository.save(employEntity);
    }

    public void deleteAll() {
        employRepository.deleteAll();
    }

    public void validate() throws IllegalDataException {
        List<String> employNames = employRepository.findDistinctEmployName();
        log.info("" + employNames);
        for (String employName : employNames) {
            List<EmployEntity> employEntities = employRepository.findEmployEntitiesByEmployNameOrderByStartDateAsc(employName);
            Date lastEndDate = null;
            for (int j = 0; j < employEntities.size(); j++) {
                if (j > 0) {
                    if (lastEndDate == null || (employEntities.get(j).getStartDate() != null && employEntities.get(j).getStartDate().before(lastEndDate))) {
                        log.info("lastDate:{},startDate:{},endDate{}", lastEndDate, employEntities.get(j).getStartDate(), employEntities.get(j).getEndDate());
                        throw new IllegalDataException("员工社保日期错误:" + employEntities.get(j).getEmployName());
                    }
                }
                lastEndDate = employEntities.get(j).getEndDate();
            }
        }
    }

//    public void importEmpolys(File employExcel) {
//        Set<Future> futureSet = new HashSet<>(200);
//        setProgress("开始导入员工数据", 0.2);
//        if (employExcel != null) {
//            EasyExcel.read(employExcel, EmployXlsData.class, new PageReadListener<EmployXlsData>(dataList -> {
//                for (EmployXlsData employData : dataList) {
//                    this.asyncImportEmpoly(employData);
//                }
//            })).sheet().doRead();
//        }
//        while (!futureSet.isEmpty()){
//            Iterator<Future> it = futureSet.iterator();
//            while(it.hasNext()){
//                Future f = it.next();
//                if(f.isDone()){
//                    it.remove();
//                }
//            }
//        }
//        setProgress("完成员工数据导入", 0.3);
//    }

    public void importEmpolys(File employExcel) {
        List<EmployXlsData> employXlsDataList = new ArrayList<>(200);
        List<CompletableFuture> employFutures = new ArrayList<>(200);
        if (employExcel != null) {
            EasyExcel.read(employExcel, EmployXlsData.class, new PageReadListener<EmployXlsData>(dataList -> {
                for (EmployXlsData employData : dataList) {
                    employXlsDataList.add(employData);
                }
            })).sheet().doRead();
        }
        employFutures = employXlsDataList.stream().map(e -> {
            CompletableFuture employFuture = CompletableFuture.supplyAsync(() -> {
                Employ employ = Employ.builder()
                        .employName(e.getEmployName())
                        .companyName(e.getCompanyName())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .idNo(e.getIdNo())
                        .mobile(e.getMobile())
                        .status("normal")
                        .build();
                this.create(employ);
                return employ;
            });
            return employFuture;
        }).collect(Collectors.toList());

        CompletableFuture.allOf(employFutures.toArray(new CompletableFuture[employFutures.size()]));
    }

    @Async
    public Future asyncImportEmpoly(EmployXlsData employData) {
        Employ employ = Employ.builder()
                .employName(employData.getEmployName())
                .companyName(employData.getCompanyName())
                .startDate(employData.getStartDate())
                .endDate(employData.getEndDate())
                .idNo(employData.getIdNo())
                .mobile(employData.getMobile())
                .status("normal")
                .build();
        this.create(employ);
        return new AsyncResult(employ.getEmployName());
    }

}
