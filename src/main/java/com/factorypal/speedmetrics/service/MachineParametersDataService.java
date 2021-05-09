package com.factorypal.speedmetrics.service;

import com.factorypal.speedmetrics.entity.MachineParametersEntity;
import com.factorypal.speedmetrics.exceptions.FactoryPalException;
import com.factorypal.speedmetrics.vo.MachineMetricsVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MachineParametersDataService {

    MachineParametersEntity loadMachineParametersData(MachineParametersEntity parametersEntityList);

    List<MachineParametersEntity> loadMachineParametersDataFromFile(MultipartFile file) throws FactoryPalException;

    List<MachineParametersEntity> loadLatestParameters(int numberOfParameters) throws FactoryPalException;

    List<MachineMetricsVO> getParametersMetricsFromMachines(long minutes) throws FactoryPalException;
}
