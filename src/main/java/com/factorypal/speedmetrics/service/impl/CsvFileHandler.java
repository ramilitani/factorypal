package com.factorypal.speedmetrics.service.impl;

import com.factorypal.speedmetrics.entity.MachineDataEntity;
import com.factorypal.speedmetrics.entity.MachineParametersEntity;
import com.factorypal.speedmetrics.entity.Parameter;
import com.factorypal.speedmetrics.exceptions.FactoryPalException;
import com.factorypal.speedmetrics.repository.MachineDataRepository;
import com.factorypal.speedmetrics.service.FileHandler;
import com.factorypal.speedmetrics.utils.CsvFileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CsvFileHandler implements FileHandler {

    private static final String HEADER = "key,name";
    private static final String HEADER_PARAMETERS_FILE = "key,value,machine_key";
    private static final int MACHINE_KEY_INDEX = 2;
    private static final int PARAMETER_KEY_INDEX = 0;
    private static final int PARAMETER_VALUE_INDEX = 1;

    @Autowired
    MachineDataRepository machineDataRepository;

    @Override
    public List<MachineDataEntity> handleMachineData(InputStream inputStream) throws FactoryPalException {

        List<MachineDataEntity> machineDataEntityList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(HEADER)) continue;

                String[] keyValue = CsvFileValidator.validate(line, 2);
                machineDataEntityList.add(new MachineDataEntity(keyValue[0], keyValue[1]));
            }

            return machineDataEntityList;

        } catch (FactoryPalException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FactoryPalException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<MachineParametersEntity> handleMachineParametersData(InputStream inputStream) throws FactoryPalException {

        HashMap<String, List<Parameter>> machineParametersData = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(HEADER) || line.equals(HEADER_PARAMETERS_FILE)) continue;

                String[] keyValue = CsvFileValidator.validate(line, 3);
                machineParametersData.putIfAbsent(keyValue[MACHINE_KEY_INDEX], new ArrayList<>());

                Parameter parameter = new Parameter(keyValue[PARAMETER_KEY_INDEX],
                        Double.valueOf(keyValue[PARAMETER_VALUE_INDEX]));

                if (!machineParametersData.get(keyValue[MACHINE_KEY_INDEX]).contains(parameter)) {
                    machineParametersData.get(keyValue[MACHINE_KEY_INDEX]).add(parameter);
                }
            }

            return machineParametersData.keySet().stream().map(key ->
                    new MachineParametersEntity(key, machineParametersData.get(key))).collect(Collectors.toList());

        } catch (FactoryPalException ex) {
            throw ex;
        } catch (NumberFormatException ex) {
            throw new FactoryPalException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            throw new FactoryPalException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
