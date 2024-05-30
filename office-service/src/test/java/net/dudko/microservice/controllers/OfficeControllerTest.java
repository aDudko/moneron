package net.dudko.microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.mapper.OfficeMapper;
import net.dudko.microservice.domain.repository.OfficeRepository;
import net.dudko.microservice.model.dto.OfficeDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OfficeControllerTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = "OFFICE-MICROSERVICE: OFFICE-CONTROLLER: ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OfficeRepository repository;

    private OfficeDto dto;

    private final Long id = 1L;

    @BeforeEach
    public void setup() {
        dto = TestUtil.getValidDto();
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Office when not exist duplicates")
    public void givenOfficeDto_whenCreateOffice_thenReturnCreatedOfficeDto() throws Exception {
        mockMvc.perform(post("/office")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.code", is(dto.getCode())))
                .andExpect(jsonPath("$.code", notNullValue()));

    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Office when exist duplicates")
    public void givenOfficeDto_whenCreateOffice_thenReturnException() throws Exception {
        repository.save(OfficeMapper.mapToOffice(dto));
        mockMvc.perform(post("/office")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Office by ID when Office exist")
    public void givenOfficeId_whenGetOfficeById_thenReturnOfficeDto() throws Exception {
        var inDb = repository.save(OfficeMapper.mapToOffice(dto));
        mockMvc.perform(get("/office/{id}", inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.code", is(dto.getCode())))
                .andExpect(jsonPath("$.code", notNullValue()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Office by ID when Office not exist")
    public void givenOfficeId_whenGetOfficeById_thenReturnException() throws Exception {
        mockMvc.perform(get("/office/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Office by code when Office exist")
    public void givenOfficeCode_whenGetOfficeByCode_thenReturnOfficeDto() throws Exception {
        var inDb = repository.save(OfficeMapper.mapToOffice(dto));
        mockMvc.perform(get("/office/code/{code}", inDb.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.code", is(dto.getCode())))
                .andExpect(jsonPath("$.code", notNullValue()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Office by code when Office not exist")
    public void givenOfficeCode_whenGetOfficeByCode_thenReturnException() throws Exception {
        mockMvc.perform(get("/office/code/{code}", dto.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

}