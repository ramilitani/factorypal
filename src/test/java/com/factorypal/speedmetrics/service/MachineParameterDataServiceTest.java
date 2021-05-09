package com.factorypal.speedmetrics.service;

import com.factorypal.speedmetrics.repository.MachineParametersDataRepository;
import com.factorypal.speedmetrics.utils.DateUtils;
import com.factorypal.speedmetrics.vo.MachineMetricsVO;
import com.factorypal.speedmetrics.entity.MachineParametersEntity;
import com.factorypal.speedmetrics.entity.Parameter;
import com.factorypal.speedmetrics.exceptions.FactoryPalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MachineParameterDataServiceTest {

    @Autowired
    MachineDataService machineDataService;

    @Autowired
    MachineParametersDataService machineParametersDataService;

    @Autowired
    MachineParametersDataRepository machineParametersDataRepository;

    @Test
    void givenAnInvalidFile_thenThrowsException() throws Exception {

        MockMultipartFile multipartTxtFile = createMockMultipartFile(new File("src/test/resources/invalidTxtFile.txt"),
                "invalidTxtFile.txt");

        Assertions.assertThrows(FactoryPalException.class, () -> {
            machineParametersDataService.loadMachineParametersDataFromFile(multipartTxtFile);
        });

        MockMultipartFile multipartCsvFile1 = createMockMultipartFile(new File("src/test/resources/invalidParameterCsvFile1.csv"),
                "invalidCsvFile1.csv");

        Assertions.assertThrows(FactoryPalException.class, () -> {
            machineParametersDataService.loadMachineParametersDataFromFile(multipartCsvFile1);
        });

        MockMultipartFile multipartCsvFile2 = createMockMultipartFile(new File("src/test/resources/invalidParameterCsvFile2.csv"),
                "invalidCsvFile2.csv" );

        Assertions.assertThrows(FactoryPalException.class, () -> {
            machineParametersDataService.loadMachineParametersDataFromFile(multipartCsvFile2);
        });

        MockMultipartFile multipartCsvFile3 = createMockMultipartFile(new File("src/test/resources/invalidParameterCsvFile3.csv"),
                "invalidCsvFile3.csv");

        Assertions.assertThrows(FactoryPalException.class, () -> {
            machineParametersDataService.loadMachineParametersDataFromFile(multipartCsvFile3);
        });
    }

    @Test
    void givenAValidFile_thenReturnMachineParameterDataList() throws Exception {

        loadMachineData();

        MockMultipartFile multipartCsvFile = createMockMultipartFile(new File("src/test/resources/sample_parameters.csv"),
                "sample_parameters.csv");
        List<MachineParametersEntity> machineParametersEntityList = machineParametersDataService.loadMachineParametersDataFromFile(multipartCsvFile);

        Assertions.assertNotNull(machineParametersEntityList);
        Assertions.assertEquals(3, machineParametersEntityList.size(), "Invalid list length");
        Assertions.assertEquals("ajoparametrit", machineParametersEntityList.get(0).getMachineKey());
        Assertions.assertEquals(5, machineParametersEntityList.get(0).getParameters().size());

        Assertions.assertEquals("aufwickler", machineParametersEntityList.get(1).getMachineKey());
        Assertions.assertEquals(2, machineParametersEntityList.get(1).getParameters().size());

        Assertions.assertEquals("wickelkopf", machineParametersEntityList.get(2).getMachineKey());
        Assertions.assertEquals(2, machineParametersEntityList.get(2).getParameters().size());

    }

    @Test
    void givenAnInvalidParametersFromAPI_thenThrowsException() throws Exception {

        loadMachineData();

        Assertions.assertThrows(FactoryPalException.class, () -> {
            machineParametersDataService.loadMachineParametersData(getInvalidMachineKeyData());
        });

        Assertions.assertThrows(FactoryPalException.class, () -> {
            machineParametersDataService.loadMachineParametersData(getMachineKeyThatNotExistData());
        });

        Assertions.assertThrows(FactoryPalException.class, () -> {
            machineParametersDataService.loadMachineParametersData(getInvalidMachineParameterKeyData());
        });
    }

    @Test
    void shouldPersistParametersDataFromAPI() throws Exception {

        loadMachineData();

        List<MachineParametersEntity> sampleMachineParameters = new ArrayList<>();

        List<Parameter> parametersFromMachine1 = new ArrayList<>();
        parametersFromMachine1.add(new Parameter("TS_setpoint_tail_length",15));
        parametersFromMachine1.add(new Parameter("perforation_length",16.5));
        parametersFromMachine1.add(new Parameter("core_interference",15));

        MachineParametersEntity result = machineParametersDataService.loadMachineParametersData(
                new MachineParametersEntity("ajoparametrit", parametersFromMachine1));

        Assertions.assertNotNull(result);
        Assertions.assertEquals("ajoparametrit", result.getMachineKey());
        Assertions.assertEquals(3, result.getParameters().size());


    }

    @Test
    void shouldRetrieveTheLatestParametersForEveryMachine() throws Exception {

        loadMachineData();
        loadMachineParametersData("src/test/resources/sample_parameters.csv", "sample_parameters.csv");

        List<MachineParametersEntity> machineParametersEntityList = machineParametersDataService.loadLatestParameters(3);

        Assertions.assertNotNull(machineParametersEntityList);
        Assertions.assertEquals(3, machineParametersEntityList.size(), "Invalid list length");

        Date dateFirst = machineParametersEntityList.get(0).getTimestamp();
        Date dateSecond = machineParametersEntityList.get(1).getTimestamp();

        Assertions.assertTrue(dateFirst.equals(dateSecond) || dateFirst.after(dateSecond));
    }

    @Test
    void shouldRetrieveTheParametersMetricsFromEveryMachine() throws Exception {

        loadMachineData();

        LocalDateTime currentTime = LocalDateTime.now();

        List<MachineParametersEntity> machineParametersEntityList = new ArrayList<>();

        List<Parameter> parameterList1 = new ArrayList<>();
        parameterList1.add(new Parameter("TS_setpoint_tail_length",15));
        parameterList1.add(new Parameter("number_of_sheets",17.7));
        parameterList1.add(new Parameter("speed",10.0));
        MachineParametersEntity machineParametersEntity1 = new MachineParametersEntity("ajoparametrit", parameterList1);
        machineParametersEntity1.setTimestamp(DateUtils.convertToDate(currentTime.minus(1, ChronoUnit.MINUTES)));


        List<Parameter> parameterList2 = new ArrayList<>();
        parameterList2.add(new Parameter("TS_setpoint_tail_length",10));
        parameterList2.add(new Parameter("number_of_sheets",20));
        parameterList2.add(new Parameter("speed",15.5));
        MachineParametersEntity machineParametersEntity2 = new MachineParametersEntity("ajoparametrit", parameterList2);
        machineParametersEntity1.setTimestamp(DateUtils.convertToDate(currentTime.minus(30, ChronoUnit.SECONDS)));

        machineParametersEntityList.add(machineParametersEntity1);
        machineParametersEntityList.add(machineParametersEntity2);

        machineParametersDataRepository.saveAll(machineParametersEntityList);

        List<MachineMetricsVO> machineMetricsVOList = machineParametersDataService.getParametersMetricsFromMachines(2);

        Assertions.assertNotNull(machineMetricsVOList);
        Assertions.assertEquals(3, machineMetricsVOList.size());

        MachineMetricsVO metric1 = machineMetricsVOList.get(0);
        Assertions.assertEquals("ajoparametrit", metric1.getMachine());
        Assertions.assertEquals("TS_setpoint_tail_length", metric1.getParameter());
        Assertions.assertEquals(12.5, metric1.getAverage());
        Assertions.assertEquals(12.5, metric1.getMedian());
        Assertions.assertEquals(10.0, metric1.getMin());
        Assertions.assertEquals(15.0, metric1.getMax());

        MachineMetricsVO metric2 = machineMetricsVOList.get(1);
        Assertions.assertEquals("ajoparametrit", metric2.getMachine());
        Assertions.assertEquals("number_of_sheets", metric2.getParameter());
        Assertions.assertEquals(18.85, metric2.getAverage());
        Assertions.assertEquals(18.85, metric2.getMedian());
        Assertions.assertEquals(17.7, metric2.getMin());
        Assertions.assertEquals(20.0, metric2.getMax());

        MachineMetricsVO metric3 = machineMetricsVOList.get(2);
        Assertions.assertEquals("ajoparametrit", metric3.getMachine());
        Assertions.assertEquals("speed", metric3.getParameter());
        Assertions.assertEquals(12.75, metric3.getAverage());
        Assertions.assertEquals(12.75, metric3.getMedian());
        Assertions.assertEquals(10.0, metric3.getMin());
        Assertions.assertEquals(15.5, metric3.getMax());

    }

    public MockMultipartFile createMockMultipartFile(File file, String originalName) throws Exception {
        return new MockMultipartFile("file", originalName,
                MediaType.TEXT_PLAIN_VALUE,
                new FileInputStream(file));
    }

    public void loadMachineData() throws Exception {
        File csvFile = new File("src/test/resources/sample.csv");
        MockMultipartFile multipartCsvFile = new MockMultipartFile("file", "sample.csv",
                MediaType.TEXT_PLAIN_VALUE,
                new FileInputStream(csvFile));

        machineDataService.loadMachineDataFromFile(multipartCsvFile);
    }

    public void loadMachineParametersData(String pathFile, String originalName) throws Exception {
        MockMultipartFile multipartCsvFile = createMockMultipartFile(new File(pathFile), originalName);
        machineParametersDataService.loadMachineParametersDataFromFile(multipartCsvFile);
    }

    private MachineParametersEntity getInvalidMachineKeyData() {
        List<Parameter> parametersFromMachine1 = new ArrayList<>();
        parametersFromMachine1.add(new Parameter("TS_setpoint_tail_length",15));
        parametersFromMachine1.add(new Parameter("perforation_length",16.5));
        return new MachineParametersEntity("", parametersFromMachine1);
    }

    private MachineParametersEntity getMachineKeyThatNotExistData() {
        List<Parameter> parametersFromMachine1 = new ArrayList<>();
        parametersFromMachine1.add(new Parameter("TS_setpoint_tail_length",15));
        parametersFromMachine1.add(new Parameter("perforation_length",16.5));
        return new MachineParametersEntity("not_exist", parametersFromMachine1);
    }

    private MachineParametersEntity getInvalidMachineParameterKeyData() {
        List<Parameter> parametersFromMachine1 = new ArrayList<>();
        parametersFromMachine1.add(new Parameter("",15));
        parametersFromMachine1.add(new Parameter("perforation_length",16.5));
        return new MachineParametersEntity("ajoparametrit", parametersFromMachine1);
    }
}
