package com.factorypal.speedmetrics.controller;

import com.factorypal.speedmetrics.entity.MachineDataEntity;
import com.factorypal.speedmetrics.exceptions.FactoryPalException;
import com.factorypal.speedmetrics.service.MachineDataService;
import com.factorypal.speedmetrics.utils.CsvFileValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/machine/data")
public class MachineDataController {

    MachineDataService machineDataService;

    @PostMapping("/upload")
    ResponseEntity<List<MachineDataEntity>> loadMachineDataFromFile(@RequestParam("file") MultipartFile multipartFile) {

        try {

        } catch (FactoryPalException ex) {

        } catch (Exception ex) {

        }
        return null;
    }
}
