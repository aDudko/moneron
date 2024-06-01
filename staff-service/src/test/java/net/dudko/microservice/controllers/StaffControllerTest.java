package net.dudko.microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.mapper.EmployeeMapper;
import net.dudko.microservice.domain.repository.StaffRepository;
import net.dudko.microservice.model.dto.EmployeeDto;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StaffControllerTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "StaffController: ";
    private static final String BASE_URL = "/staff";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StaffRepository repository;

    private EmployeeDto dto;

    @BeforeEach
    public void setup() {
        dto = TestUtil.getValidDto();
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Employee when not exist duplicates")
    public void givenEmployeeDto_whenCreateEmployee_thenReturnCreatedEmployeeDto() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dto.getLastName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.status", is(dto.getStatus().name())))
                .andExpect(jsonPath("$.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.officeCode", is(dto.getOfficeCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Employee when exist duplicates")
    public void givenEmployeeDto_whenCreateEmployee_thenReturnException() throws Exception {
        repository.save(EmployeeMapper.mapToEmployee(dto));
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Employee by ID when Employee exist")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeDto() throws Exception {
        var inDb = repository.save(EmployeeMapper.mapToEmployee(dto));
        mockMvc.perform(get(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeDto.id", notNullValue()))
                .andExpect(jsonPath("$.employeeDto.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.employeeDto.lastName", is(dto.getLastName())))
                .andExpect(jsonPath("$.employeeDto.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.employeeDto.status", is(dto.getStatus().name())))
                .andExpect(jsonPath("$.employeeDto.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.employeeDto.officeCode", is(dto.getOfficeCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Employee by ID when Employee not exist")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnException() throws Exception {
        mockMvc.perform(get(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Employee by email when Employee exist")
    public void givenEmployeeEmail_whenGetEmployeeByEmail_thenReturnEmployeeDto() throws Exception {
        var inDb = repository.save(EmployeeMapper.mapToEmployee(dto));
        mockMvc.perform(get(BASE_URL.concat("/email/{email}"), inDb.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dto.getLastName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.status", is(dto.getStatus().name())))
                .andExpect(jsonPath("$.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.officeCode", is(dto.getOfficeCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Employee by email when Employee not exist")
    public void givenEmployeeEmail_whenGetEmployeeByEmail_thenReturnException() throws Exception {
        mockMvc.perform(get(BASE_URL.concat("/email/{email}"), dto.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Staff when Staff exist")
    public void givenStaff_whenGetStaff_thenReturnListOfEmployeeDto() throws Exception {
        repository.save(EmployeeMapper.mapToEmployee(dto));
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Staff when Staff not exist")
    public void givenStaff_whenGetStaff_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Employee when Employee exist")
    public void givenEmployeeDto_whenUpdateEmployee_thenReturnUpdatedEmployeeDto() throws Exception {
        var inDb = repository.save(EmployeeMapper.mapToEmployee(dto));
        dto.setFirstName("Updated FirstName");
        dto.setLastName("Updated LastName");
        dto.setEmail("updated@mail.com");
        dto.setDepartmentCode("Updated Department Code");
        dto.setOfficeCode("Updated Office Code");
        mockMvc.perform(put(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dto.getLastName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.status", is(dto.getStatus().name())))
                .andExpect(jsonPath("$.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.officeCode", is(dto.getOfficeCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Employee when Employee not exist")
    public void givenEmployeeDto_whenUpdateEmployee_thenReturnException() throws Exception {
        dto.setFirstName("Updated FirstName");
        dto.setLastName("Updated LastName");
        dto.setEmail("updated@mail.com");
        dto.setDepartmentCode("Updated Department Code");
        dto.setOfficeCode("Updated Office Code");
        mockMvc.perform(put(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Employee when Employee exist")
    public void givenEmployeeId_whenDeleteEmployee_thenReturnUpdatedEmployeeDto() throws Exception {
        var inDb = repository.save(EmployeeMapper.mapToEmployee(dto));
        mockMvc.perform(delete(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Employee deleted successfully");
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Employee when Employee not exist")
    public void givenEmployeeId_whenDeleteEmployee_thenReturnException() throws Exception {
        mockMvc.perform(delete(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}