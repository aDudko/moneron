package net.dudko.microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.mapper.TaskMapper;
import net.dudko.microservice.domain.repository.TaskRepository;
import net.dudko.microservice.model.dto.TaskDto;
import net.dudko.microservice.model.dto.TaskStatus;
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
class TaskControllerTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = "TASK-MICROSERVICE: TASK-CONTROLLER: ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository repository;

    private TaskDto dto;

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
    @DisplayName(testNamePrefix + "Test for create Task")
    public void givenTaskDto_whenCreateTask_thenReturnCreatedTaskDto() throws Exception {
        mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is(dto.getTitle())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.status", is("TODO")))
                .andExpect(jsonPath("$.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.officeCode", is(dto.getOfficeCode())))
                .andExpect(jsonPath("$.employeeEmail", is(dto.getEmployeeEmail())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Task by ID when Task exist")
    public void givenTaskId_whenGetTaskById_thenReturnTaskDto() throws Exception {
        var inDb = repository.save(TaskMapper.mapToTask(dto));
        mockMvc.perform(get("/task/{id}", inDb.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskDto.id", notNullValue()))
                .andExpect(jsonPath("$.taskDto.title", is(dto.getTitle())))
                .andExpect(jsonPath("$.taskDto.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.taskDto.status", is("TODO")))
                .andExpect(jsonPath("$.taskDto.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.taskDto.officeCode", is(dto.getOfficeCode())))
                .andExpect(jsonPath("$.taskDto.employeeEmail", is(dto.getEmployeeEmail())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Task by ID when Task not exist")
    public void givenTaskId_whenGetTaskById_thenReturnException() throws Exception {
        mockMvc.perform(get("/task/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Tasks when Tasks exist")
    public void givenTasks_whenGetAllTasksByEmployeeEmail_thenReturnListOfTaskDto() throws Exception {
        repository.save(TaskMapper.mapToTask(dto));
        mockMvc.perform(get("/task/email/{email}", dto.getEmployeeEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Tasks when Tasks not exist")
    public void givenTasks_whenGetAllTasks_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get("/task/email/{email}", dto.getEmployeeEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Task when Task exist")
    public void givenTaskDto_whenUpdateTask_thenReturnUpdatedTaskDto() throws Exception {
        var inDb = repository.save(TaskMapper.mapToTask(dto));
        dto.setTitle("Updated Title");
        dto.setDescription("Updated Description");
        dto.setStatus(TaskStatus.DONE);
        dto.setDepartmentCode("UpdatedDepartmentCode");
        dto.setOfficeCode("UpdatedOfficeCode");
        dto.setEmployeeEmail("updated@mail.com");
        mockMvc.perform(put("/task/{id}", inDb.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is(dto.getTitle())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.status", is("DONE")))
                .andExpect(jsonPath("$.departmentCode", is(dto.getDepartmentCode())))
                .andExpect(jsonPath("$.officeCode", is(dto.getOfficeCode())))
                .andExpect(jsonPath("$.employeeEmail", is(dto.getEmployeeEmail())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Task when Task not exist")
    public void givenTaskDto_whenUpdateTask_thenReturnException() throws Exception {
        dto.setTitle("Updated Title");
        dto.setDescription("Updated Description");
        dto.setStatus(TaskStatus.DONE);
        dto.setDepartmentCode("UpdatedDepartmentCode");
        dto.setOfficeCode("UpdatedOfficeCode");
        dto.setEmployeeEmail("updated@mail.com");
        mockMvc.perform(put("/task/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Task when Task exist")
    public void givenTaskId_whenDeleteTask_thenReturnNothing() throws Exception {
        var inDb = repository.save(TaskMapper.mapToTask(dto));
        mockMvc.perform(delete("/task/{id}", inDb.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Task deleted successfully");
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Task when Task not exist")
    public void givenTaskId_whenDeleteTask_thenReturnException() throws Exception {
        mockMvc.perform(delete("/task/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}