package com.factorypal.speedmetrics.controller;

import com.factorypal.speedmetrics.entity.MachineParametersEntity;
import com.factorypal.speedmetrics.service.MachineParametersDataService;
import com.factorypal.speedmetrics.vo.MachineMetricsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/machine/parameters")
@Api(value = "Performs load machine parameter data operations.")
public class MachineParametersDataController {

    @Autowired
    MachineParametersDataService machineParametersDataService;

    @ApiOperation(value = "Add parameters to a existed machine")
    @PostMapping("/add")
    ResponseEntity<MachineParametersEntity> insertMachineParameters(@RequestBody MachineParametersEntity machineParametersEntity) {
        MachineParametersEntity result = machineParametersDataService.loadMachineParametersData(machineParametersEntity);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Load machine parameters data from a CSV file and store it into the database")
    @PostMapping("/upload")
    ResponseEntity<List<MachineParametersEntity>> loadMachineDataFromFile(@RequestParam("file") MultipartFile multipartFile) {
        List<MachineParametersEntity> machineParametersEntityList = machineParametersDataService.loadMachineParametersDataFromFile(multipartFile);
        return new ResponseEntity<>(machineParametersEntityList, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get the latest parameters from every machine stored in the database. " +
            "The size of the parameters list is defined in the size RequestParam")
    @GetMapping("/latest")
    ResponseEntity<List<MachineParametersEntity>> getLatestParameters(@RequestParam("size") int size) {
        List<MachineParametersEntity> machineParametersEntityList = machineParametersDataService.loadLatestParameters(size);
        return new ResponseEntity<>(machineParametersEntityList, HttpStatus.OK);
    }

    @ApiOperation(value = "Get the parameters metrics from every machine in X minutes.")
    @GetMapping("/metrics/{minutes}")
    ResponseEntity<List<MachineMetricsVO>> getMetrics(@PathVariable("minutes") int minutes) {
        List<MachineMetricsVO> machineMetricsVOList = machineParametersDataService.getParametersMetricsFromMachines(minutes);
        return new ResponseEntity<>(machineMetricsVOList, HttpStatus.OK);
    }
}
