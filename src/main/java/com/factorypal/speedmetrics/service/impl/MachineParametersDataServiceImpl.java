package com.factorypal.speedmetrics.service.impl;

import com.factorypal.speedmetrics.entity.MachineDataEntity;
import com.factorypal.speedmetrics.entity.MachineParametersEntity;
import com.factorypal.speedmetrics.exceptions.FactoryPalException;
import com.factorypal.speedmetrics.repository.MachineDataRepository;
import com.factorypal.speedmetrics.repository.MachineParametersDataRepository;
import com.factorypal.speedmetrics.service.MachineParametersDataService;
import com.factorypal.speedmetrics.utils.CsvFileValidator;
import com.factorypal.speedmetrics.utils.DateUtils;
import com.factorypal.speedmetrics.vo.MachineMetricsVO;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MachineParametersDataServiceImpl implements MachineParametersDataService {

    Logger logger = LoggerFactory.getLogger(MachineParametersDataServiceImpl.class);

    @Autowired
    MachineDataRepository machineDataRepository;

    @Autowired
    MachineParametersDataRepository machineParametersDataRepository;

    @Override
    public List<MachineParametersEntity> loadMachineParametersData(List<MachineParametersEntity> parametersEntityList) {

        try {
            validateMachineParametersEntity(parametersEntityList);
            machineParametersDataRepository.saveAll(parametersEntityList);
            return parametersEntityList;

        } catch (FactoryPalException ex) {
            logger.error(ex.getMessage());
            throw ex;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new FactoryPalException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<MachineParametersEntity> loadMachineParametersDataFromFile(MultipartFile file) throws FactoryPalException {

        CsvFileValidator.validate(file);

        try {
            List<MachineParametersEntity> machineParametersEntityList =
                    new CsvFileHandler().handleMachineParametersData(file.getInputStream());

            machineParametersEntityList = machineParametersEntityList.stream()
                    .filter(data -> {
                        MachineDataEntity machineDataEntity = machineDataRepository.findByKey(data.getMachineKey());
                        if (machineDataEntity == null) {
                            throw new FactoryPalException(String.format("Machine Key %s doesn't exist.", data.getMachineKey()));
                        }
                        return true;
                    }).collect(Collectors.toList());

            machineParametersDataRepository.saveAll(machineParametersEntityList);

            return machineParametersEntityList;

        } catch (FactoryPalException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new FactoryPalException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<MachineParametersEntity> loadLatestParameters(int numberOfParameters) throws FactoryPalException {

        try {
            List<MachineParametersEntity> machineParametersEntityList = machineParametersDataRepository
                    .findAll(Sort.by(Sort.Direction.DESC, "timestamp"));

            machineParametersEntityList.stream()
                .sorted((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()))
                .forEach(data -> {
                  List limitParametersList = data.getParameters().stream()
                          .limit(numberOfParameters).collect(Collectors.toList());
                  data.setParameters(limitParametersList);
                });

            return machineParametersEntityList;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new FactoryPalException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<MachineMetricsVO> getParametersMetricsFromMachines(long minutes) throws FactoryPalException {

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = localDateTime.minus(minutes, ChronoUnit.MINUTES);

        List<MachineParametersEntity> machineParametersEntityList = machineParametersDataRepository.findByTimestampGreaterThan(
                DateUtils.convertToDate(startDateTime));

        List<MachineMetricsVO> machineMetricsVOList = new ArrayList<>();

        if (machineParametersEntityList != null && !machineParametersEntityList.isEmpty()) {
            machineParametersEntityList.stream()
            .forEach(entity -> {
                entity.getParameters().forEach(parameter -> {
                    MachineMetricsVO machineMetricsVO = new MachineMetricsVO(
                            entity.getMachineKey(), parameter.getKey(), parameter.getValue());

                    int indexElement = machineMetricsVOList.indexOf(machineMetricsVO);
                    if (indexElement >= 0) {
                        machineMetricsVOList.get(indexElement).addValue(parameter.getValue());
                    } else {
                        machineMetricsVOList.add(machineMetricsVO);
                    }
                });
            });

            machineMetricsVOList.forEach(metric -> metric.calculate());
        }

        return machineMetricsVOList;
    }

    private void validateMachineParametersEntity(List<MachineParametersEntity> parametersEntityList) {

        if (parametersEntityList == null && parametersEntityList.isEmpty()) {
            throw new FactoryPalException("Empty MachineParametersEntity passed.");
        }

        parametersEntityList.stream().forEach(data -> {
            if (Strings.isBlank(data.getMachineKey())) {
                throw new FactoryPalException("Empty machine key passed.");
            }

            MachineDataEntity machineDataEntity = machineDataRepository.findByKey(data.getMachineKey());
            if (machineDataEntity == null) {
                throw new FactoryPalException(String.format("Machine Key %s doesn't exist.", data.getMachineKey()));
            }

            data.getParameters().forEach(parameter -> {
                if (Strings.isBlank(parameter.getKey())) throw new FactoryPalException("Empty parameter key passed.");
            });
        });
    }
}
