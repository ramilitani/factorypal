package com.factorypal.speedmetrics.service;

import com.factorypal.speedmetrics.entity.MachineDataEntity;
import com.factorypal.speedmetrics.entity.MachineParametersEntity;
import com.factorypal.speedmetrics.exceptions.FactoryPalException;

import java.io.InputStream;
import java.util.List;

public interface FileHandler {

    List<MachineDataEntity> handleMachineData(InputStream inputStream) throws FactoryPalException;

    List<MachineParametersEntity> handleMachineParametersData(InputStream inputStream) throws FactoryPalException;
}
