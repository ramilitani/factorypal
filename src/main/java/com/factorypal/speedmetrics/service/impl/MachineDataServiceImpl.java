package com.factorypal.speedmetrics.service.impl;

import com.factorypal.speedmetrics.entity.MachineDataEntity;
import com.factorypal.speedmetrics.exceptions.FactoryPalException;
import com.factorypal.speedmetrics.repository.MachineDataRepository;
import com.factorypal.speedmetrics.service.MachineDataService;
import com.factorypal.speedmetrics.utils.CsvFileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MachineDataServiceImpl implements MachineDataService {

    Logger logger = LoggerFactory.getLogger(MachineDataServiceImpl.class);

    @Autowired
    MachineDataRepository machineDataRepository;

    @Override
    public List<MachineDataEntity> loadMachineDataFromFile(MultipartFile uploadFile) throws FactoryPalException {

        CsvFileValidator.validate(uploadFile);

        try {
            List<MachineDataEntity> machineDataEntityList = new CsvFileHandler().handleMachineData(uploadFile.getInputStream());
            machineDataEntityList = machineDataEntityList.stream()
                .filter(data -> machineDataRepository.findByKey(data.getKey()) == null)
                .distinct()
                .collect(Collectors.toList());

            machineDataRepository.saveAll(machineDataEntityList);
            return machineDataEntityList;

        } catch (FactoryPalException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new FactoryPalException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
