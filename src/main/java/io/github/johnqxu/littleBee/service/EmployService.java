package io.github.johnqxu.littleBee.service;

import io.github.johnqxu.littleBee.exception.IllegalDataException;
import io.github.johnqxu.littleBee.model.EmployXlsData;
import io.github.johnqxu.littleBee.model.dto.EmployDto;
import io.github.johnqxu.littleBee.model.entity.EmployEntity;
import io.github.johnqxu.littleBee.model.mapper.EmployMapper;
import io.github.johnqxu.littleBee.repository.EmployRepository;
import io.github.johnqxu.littleBee.util.XlsUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class EmployService {

    private final Executor executor;

    private final EmployRepository employRepository;

    private final EmployMapper employMapper;

    public EmployService(EmployRepository employRepository, EmployMapper employMapper, Executor executor) {
        this.employRepository = employRepository;
        this.employMapper = employMapper;
        this.executor = executor;
    }

    @Async
    public CompletableFuture<EmployEntity> create(EmployDto employDto) {
        EmployEntity employEntity = new EmployEntity();
        employEntity.setEmployName(employDto.getEmployName().trim());
        employEntity.setCompanyName(employDto.getCompanyName().trim());
        employEntity.setStartDate(employDto.getStartDate());
        if (employDto.getEndDate() != null) {
            employEntity.setEndDate(employDto.getEndDate());
        }
        employEntity.setIdNo(employDto.getIdNo());
        employEntity.setMobile(employDto.getMobile());
        employRepository.save(employEntity);
        return CompletableFuture.completedFuture(employEntity);
    }

    public void deleteAll() {
        employRepository.deleteAll();
    }

    public void validate() throws IllegalDataException {
        List<String> employNames = employRepository.findDistinctEmployName();
        CompletableFuture.allOf(
                employNames.stream().map(
                        e -> CompletableFuture.supplyAsync(() -> validateEmployData(e), executor)
                ).toArray(CompletableFuture[]::new)
        ).join();
    }

    @Async
    CompletableFuture<Boolean> validateEmployData(String employName) {
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
        return CompletableFuture.completedFuture(true);
    }

    public void importEmpolys(@NonNull File employExcel) {
        List<EmployXlsData> employXlsDataList = XlsUtil.readXls(employExcel, EmployXlsData.class, 200);
        CompletableFuture.allOf(employXlsDataList.stream().map(e -> CompletableFuture.supplyAsync(() -> {
            EmployDto employDto = employMapper.toDtoFromXls(e);
            this.create(employDto);
            return employDto;
        }, executor)).toArray(CompletableFuture[]::new)).join();
    }

}
