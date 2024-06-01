package net.dudko.microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.mapper.DepartmentMapper;
import net.dudko.microservice.domain.repository.DepartmentRepository;
import net.dudko.microservice.model.dto.DepartmentDto;
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
class DepartmentControllerTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "DepartmentController: ";
    private static final String BASE_URL = "/department";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentRepository repository;

    private DepartmentDto dto;

    @BeforeEach
    public void setup() {
        dto = TestUtil.getValidDto();
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Department when not exist duplicates")
    public void givenDepartmentDto_whenCreateDepartment_thenReturnCreatedDepartmentDto() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.code", is(dto.getCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Department when exist duplicates")
    public void givenDepartmentDto_whenCreateDepartment_thenReturnException() throws Exception {
        repository.save(DepartmentMapper.mapToDepartment(dto));
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Department by ID when Department exist")
    public void givenDepartmentId_whenGetDepartmentById_thenReturnDepartmentDto() throws Exception {
        var inDb = repository.save(DepartmentMapper.mapToDepartment(dto));
        mockMvc.perform(get(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.code", is(dto.getCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Department by ID when Department not exist")
    public void givenDepartmentId_whenGetDepartmentById_thenReturnException() throws Exception {
        mockMvc.perform(get(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Department by code when Department exist")
    public void givenDepartmentCode_whenGetDepartmentByCode_thenReturnDepartmentDto() throws Exception {
        var inDb = repository.save(DepartmentMapper.mapToDepartment(dto));
        mockMvc.perform(get(BASE_URL.concat("/code/{code}"), inDb.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.code", is(dto.getCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Department by code when Department not exist")
    public void givenDepartmentCode_whenGetDepartmentByCode_thenReturnException() throws Exception {
        mockMvc.perform(get(BASE_URL.concat("/code/{code}"), dto.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Departments when Departments exist")
    public void givenDepartments_whenGetAllDepartments_thenReturnListOfDepartmentDto() throws Exception {
        repository.save(DepartmentMapper.mapToDepartment(dto));
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Departments when Departments not exist")
    public void givenDepartments_whenGetAllDepartments_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Department when Department exist")
    public void givenDepartmentDto_whenUpdateDepartment_thenReturnUpdatedDepartmentDto() throws Exception {
        var inDb = repository.save(DepartmentMapper.mapToDepartment(dto));
        dto.setName("Updated Name");
        dto.setDescription("Updated Description");
        mockMvc.perform(put(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.code", is(dto.getCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Department when Department not exist")
    public void givenDepartmentDto_whenUpdateDepartment_thenReturnException() throws Exception {
        dto.setName("Updated Name");
        dto.setDescription("Updated Description");
        mockMvc.perform(put(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

}