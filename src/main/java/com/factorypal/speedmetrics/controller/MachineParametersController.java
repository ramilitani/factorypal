package com.factorypal.speedmetrics.controller;

import com.factorypal.speedmetrics.vo.MachineMetricsVO;
import com.factorypal.speedmetrics.entity.MachineParametersEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machine/parameters")
public class MachineParametersController {

    @PostMapping("/store")
    ResponseEntity<MachineParametersEntity> insertMachineParameters(@RequestBody MachineParametersEntity parameters) {
        return null;
    }

    @GetMapping("/latest")
    ResponseEntity<List<MachineParametersEntity>> getLatestParameters(@RequestParam("size") int size) {
        return null;
    }

    @GetMapping("/metrics/{minutes}")
    ResponseEntity<List<MachineMetricsVO>> getMetrics(@PathVariable("minutes") int minutes) {
        return null;
    }

}
