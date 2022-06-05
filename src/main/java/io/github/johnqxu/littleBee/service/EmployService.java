package io.github.johnqxu.littleBee.service;

import io.github.johnqxu.littleBee.entity.EmployEntity;
import io.github.johnqxu.littleBee.exception.IllegalDataException;
import io.github.johnqxu.littleBee.model.Employ;
import io.github.johnqxu.littleBee.repository.EmployRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class EmployService {

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
        employRepository.save(employEntity);
    }

    public void deleteAll() {
        employRepository.deleteAll();
    }

    public void validate() throws IllegalDataException{
        List<String> employNames = employRepository.findDistinctEmployName();
        log.info("" + employNames);
        for (int i = 0; i < employNames.size(); i++) {
            List<EmployEntity> employEntities = employRepository.findEmployEntitiesByEmployNameOrderByStartDateAsc(employNames.get(i));
            Date lastEndDate = null;
            for (int j = 0; j < employEntities.size(); j++) {
//                log.info("employId:{}",employEntities.get(j).getId());
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

}
