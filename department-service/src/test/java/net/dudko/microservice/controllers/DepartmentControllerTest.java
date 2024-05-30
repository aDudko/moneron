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

    private static final String testNamePrefix = "DEPARTMENT-MICROSERVICE: DEPARTMENT-CONTROLLER: ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentRepository repository;

    private DepartmentDto dto;

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
    @DisplayName(testNamePrefix + "Test for create Department when not exist duplicates")
    public void givenDepartmentDto_whenCreateDepartment_thenReturnCreatedDepartmentDto() throws Exception {
        mockMvc.perform(post("/department")
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
        mockMvc.perform(post("/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Department by ID when Department exist")
    public void givenDepartmentId_whenGetDepartmentById_thenReturnDepartmentDto() throws Exception {
        var inDb = repository.save(DepartmentMapper.mapToDepartment(dto));
        mockMvc.perform(get("/department/{id}", inDb.getId())
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
        mockMvc.perform(get("/department/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Department by code when Department exist")
    public void givenDepartmentCode_whenGetDepartmentByCode_thenReturnDepartmentDto() throws Exception {
        var inDb = repository.save(DepartmentMapper.mapToDepartment(dto));
        mockMvc.perform(get("/department/code/{code}", inDb.getCode())
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
        mockMvc.perform(get("/department/code/{code}", dto.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Departments when Departments exist")
    public void givenDepartments_whenGetAllDepartments_thenReturnListOfDepartmentDto() throws Exception {
        repository.save(DepartmentMapper.mapToDepartment(dto));
        mockMvc.perform(get("/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Departments when Departments not exist")
    public void givenDepartments_whenGetAllDepartments_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get("/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Department when Department exist")
    public void givenDepartmentDto_whenUpdateDepartment_thenReturnUpdatedDepartmentDto() throws Exception {
        var inDb = repository.save(DepartmentMapper.mapToDepartment(dto));
        dto.setName("updated name");
        dto.setDescription("updated description");
        mockMvc.perform(put("/department/{id}", inDb.getId())
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
        dto.setName("updated name");
        dto.setDescription("updated description");
         mockMvc.perform(put("/department/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

}