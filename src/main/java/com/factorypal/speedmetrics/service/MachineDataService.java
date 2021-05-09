package com.factorypal.speedmetrics.service;

import com.factorypal.speedmetrics.entity.MachineDataEntity;
import com.factorypal.speedmetrics.exceptions.FactoryPalException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MachineDataService {

    List<MachineDataEntity> loadMachineDataFromFile(MultipartFile file) throws FactoryPalException;
}
