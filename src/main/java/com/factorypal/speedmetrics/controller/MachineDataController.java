package com.factorypal.speedmetrics.controller;

import com.factorypal.speedmetrics.entity.MachineDataEntity;
import com.factorypal.speedmetrics.service.MachineDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/machine/data")
@Api(value = "Performs load machine data operation.")
public class MachineDataController {

    @Autowired
    MachineDataService machineDataService;

    @ApiOperation(value = "Load machine data from a CSV file and store it into the database")
    @PostMapping("/upload")
    ResponseEntity<List<MachineDataEntity>> loadMachineDataFromFile(@RequestParam("file") MultipartFile multipartFile) {
        List<MachineDataEntity> machineDataEntityList = machineDataService.loadMachineDataFromFile(multipartFile);
        return new ResponseEntity<>(machineDataEntityList, HttpStatus.CREATED) ;
    }
}
