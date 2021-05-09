package com.factorypal.speedmetrics.service;

import com.factorypal.speedmetrics.entity.MachineDataEntity;
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
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MachineDataServiceTest {

    @Autowired
    MachineDataService machineDataService;

    @Test
    void givenAnInvalidFile_thenThrowsException() throws Exception {

        File txtFile = new File("src/test/resources/invalidTxtFile.txt");
        MockMultipartFile multipartTxtFile = new MockMultipartFile("file", "invalidTxtFile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                new FileInputStream(txtFile));

        Assertions.assertThrows(FactoryPalException.class, () -> {
            machineDataService.loadMachineDataFromFile(multipartTxtFile);
        });

        File csvFile = new File("src/test/resources/invalidCsvFile.csv");
        MockMultipartFile multipartCsvFile = new MockMultipartFile("file", "invalidCsvFile.csv",
                MediaType.TEXT_PLAIN_VALUE,
                new FileInputStream(csvFile));

        Assertions.assertThrows(FactoryPalException.class, () -> {
            machineDataService.loadMachineDataFromFile(multipartCsvFile);
        });
    }

    @Test
    void givenAValidFile_thenReturnMachineDataList() throws Exception {

        File csvFile = new File("src/test/resources/sample.csv");
        MockMultipartFile multipartCsvFile = new MockMultipartFile("file", "sample.csv",
                MediaType.TEXT_PLAIN_VALUE,
                new FileInputStream(csvFile));

        List<MachineDataEntity> machineDataEntityList = machineDataService.loadMachineDataFromFile(multipartCsvFile);

        Assertions.assertNotNull(machineDataEntityList);
        Assertions.assertEquals(3, machineDataEntityList.size(), "Invalid list length");
        Assertions.assertEquals("aufwickler", machineDataEntityList.get(0).getKey(), "Invalid returned key");
        Assertions.assertEquals("Aufwickler", machineDataEntityList.get(0).getName(), "Invalid returned name");

    }
}
