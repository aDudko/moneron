package net.dudko.microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.mapper.StaffMapper;
import net.dudko.microservice.domain.repository.StaffRepository;
import net.dudko.microservice.model.dto.StaffDto;
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

    private static final String testNamePrefix = "STAFF-MICROSERVICE: STAFF-CONTROLLER: ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StaffRepository repository;

    private StaffDto dto;

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
    @DisplayName(testNamePrefix + "Test for create Staff when not exist duplicates")
    public void givenStaffDto_whenCreateStaff_thenReturnCreatedStaffDto() throws Exception {
        mockMvc.perform(post("/staff")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dto.getLastName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.status", is("CREATED")))
                .andExpect(jsonPath("$.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.officeCode", is(dto.getOfficeCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Staff when exist duplicates")
    public void givenStaffDto_whenCreateStaff_thenReturnException() throws Exception {
        repository.save(StaffMapper.mapToStaff(dto));
        mockMvc.perform(post("/staff")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Staff by ID when Staff exist")
    public void givenStaffId_whenGetStaffById_thenReturnStaffDto() throws Exception {
        var inDb = repository.save(StaffMapper.mapToStaff(dto));
        mockMvc.perform(get("/staff/{id}", inDb.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.staffDto.id", notNullValue()))
                .andExpect(jsonPath("$.staffDto.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.staffDto.lastName", is(dto.getLastName())))
                .andExpect(jsonPath("$.staffDto.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.staffDto.status", is("CREATED")))
                .andExpect(jsonPath("$.staffDto.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.staffDto.officeCode", is(dto.getOfficeCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Staff by ID when Staff not exist")
    public void givenStaffId_whenGetStaffById_thenReturnException() throws Exception {
        mockMvc.perform(get("/staff/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Staff by email when Staff exist")
    public void givenStaffCode_whenGetStaffByCode_thenReturnStaffDto() throws Exception {
        var inDb = repository.save(StaffMapper.mapToStaff(dto));
        mockMvc.perform(get("/staff/email/{email}", inDb.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dto.getLastName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.status", is("CREATED")))
                .andExpect(jsonPath("$.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.officeCode", is(dto.getOfficeCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Staff by email when Staff not exist")
    public void givenStaffCode_whenGetStaffByCode_thenReturnException() throws Exception {
        mockMvc.perform(get("/staff/email/{email}", dto.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Staffs when Staffs exist")
    public void givenStaffs_whenGetAllStaffs_thenReturnListOfStaffDto() throws Exception {
        repository.save(StaffMapper.mapToStaff(dto));
        mockMvc.perform(get("/staff")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Staffs when Staffs not exist")
    public void givenStaffs_whenGetAllStaffs_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get("/staff")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Staff when Staff exist")
    public void givenStaffDto_whenUpdateStaff_thenReturnUpdatedStaffDto() throws Exception {
        var inDb = repository.save(StaffMapper.mapToStaff(dto));
        dto.setFirstName("Updated first name");
        dto.setLastName("Updated last name");
        dto.setEmail("updated@mail.com");
        dto.setDepartmentCode("UpdatedDepartmentCode");
        dto.setOfficeCode("UpdatedOfficeCode");
        mockMvc.perform(put("/staff/{id}", inDb.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(dto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dto.getLastName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.status", is("CREATED")))
                .andExpect(jsonPath("$.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.officeCode", is(dto.getOfficeCode())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Staff when Staff not exist")
    public void givenStaffDto_whenUpdateStaff_thenReturnException() throws Exception {
        dto.setFirstName("Updated first name");
        dto.setLastName("Updated last name");
        dto.setEmail("updated@mail.com");
        dto.setDepartmentCode("UpdatedDepartmentCode");
        dto.setOfficeCode("UpdatedOfficeCode");
        mockMvc.perform(put("/staff/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Staff when Staff exist")
    public void givenStaffId_whenDeleteStaff_thenReturnUpdatedStaffDto() throws Exception {
        var inDb = repository.save(StaffMapper.mapToStaff(dto));
        mockMvc.perform(delete("/staff/{id}", inDb.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Employee deleted successfully");
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Staff when Staff not exist")
    public void givenStaffId_whenDeleteStaff_thenReturnException() throws Exception {
        mockMvc.perform(delete("/staff/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}