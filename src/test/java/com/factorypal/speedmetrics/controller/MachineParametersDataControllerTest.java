package com.factorypal.speedmetrics.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MachineParametersDataControllerTest {

    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeAll
    void initialize() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        loadMachineData();
    }

    @Test
    void whenFileIsNotPresent_thenReturnBadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/machine/parameters/upload")
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenAFile_thenShouldReturnSuccess() throws Exception {
        loadMachineParametersData();
    }

    @Test
    void shouldRetrieveTheLatestParameters() throws Exception {
        loadMachineParametersData();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/machine/parameters/latest?size=3"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void shouldRetrieveMetrics() throws Exception {
        loadMachineParametersData();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/machine/parameters/metrics/2"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }


    void loadMachineData() throws Exception {
        File file = new File("src/test/resources/sample.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "sample.csv",
                MediaType.TEXT_PLAIN_VALUE,
                new FileInputStream(file));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/machine/data/upload")
                .file(multipartFile))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    void loadMachineParametersData() throws Exception {

        File file = new File("src/test/resources/sample_parameters.csv");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "sample_parameters.csv",
                MediaType.TEXT_PLAIN_VALUE,
                new FileInputStream(file));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/machine/parameters/upload")
                .file(multipartFile))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

}
