package net.dudko.microservice.service;

import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Task;
import net.dudko.microservice.domain.mapper.TaskMapper;
import net.dudko.microservice.domain.repository.TaskRepository;
import net.dudko.microservice.model.dto.StaffDto;
import net.dudko.microservice.model.dto.TaskDto;
import net.dudko.microservice.model.dto.TaskStatus;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TaskServiceTest {

    private static final String testNamePrefix = "TASK-MICROSERVICE: TASK-SERVICE: ";

    @Mock
    private TaskRepository repository;

    @Mock
    private DepartmentServiceApiClient departmentServiceApiClient;

    @Mock
    private OfficeServiceApiClient officeServiceApiClient;

    @Mock
    private StaffServiceApiClient staffServiceApiClient;

    @InjectMocks
    private TaskServiceImpl service;

    private Task entity;
    private TaskDto dto;

    private final Long id = 1L;

    @BeforeEach
    public void setup() {
        entity = TestUtil.getValidEntity();
        dto = TestUtil.getValidDto();
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Task")
    public void givenTaskDto_whenCreateTask_thenReturnCreatedTaskDto() {
        given(repository.save(entity)).willReturn(entity);
        var inDb = service.create(dto);
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getTitle()).isEqualTo(dto.getTitle());
        assertThat(inDb.getDescription()).isEqualTo(dto.getDescription());
        assertThat(inDb.getStatus()).isEqualTo(dto.getStatus());
        assertThat(inDb.getDepartmentCode()).isEqualTo(entity.getDepartmentCode());
        assertThat(inDb.getOfficeCode()).isEqualTo(entity.getOfficeCode());
        assertThat(inDb.getEmployeeEmail()).isEqualTo(entity.getEmployeeEmail());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Task by ID when Task exist")
    public void givenTaskId_whenGetTaskById_thenReturnTaskDto() {
        given(repository.findById(id)).willReturn(Optional.of(entity));
        var inDb = service.getById(id);
        assertThat(inDb).isNotNull();
        assertThat(inDb.getTaskDto().getId()).isNotNull();
        assertThat(inDb.getTaskDto().getId()).isNotNull();
        assertThat(inDb.getTaskDto().getTitle()).isEqualTo(dto.getTitle());
        assertThat(inDb.getTaskDto().getDescription()).isEqualTo(dto.getDescription());
        assertThat(inDb.getTaskDto().getStatus()).isEqualTo(dto.getStatus());
        assertThat(inDb.getTaskDto().getDepartmentCode()).isEqualTo(entity.getDepartmentCode());
        assertThat(inDb.getTaskDto().getOfficeCode()).isEqualTo(entity.getOfficeCode());
        assertThat(inDb.getTaskDto().getEmployeeEmail()).isEqualTo(entity.getEmployeeEmail());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Task by ID when Task not exist")
    public void givenTaskId_whenGetTaskById_thenReturnException() {
        given(repository.findById(id)).willReturn(Optional.empty());
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(id);
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Task with id '%d' not found", id));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Task when Tasks exist")
    public void givenTask_whenGetAllTasksByEmployeeEmail_thenReturnListOfTasks() {
        given(repository.findAllByEmployeeEmail(entity.getEmployeeEmail())).willReturn(List.of(entity));
        var inDb = service.getAllByEmployeeEmail(entity.getEmployeeEmail());
        assertThat(inDb).isNotNull();
        assertThat(inDb).isNotEmpty();
        assertThat(inDb.size()).isEqualTo(1);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Task when Tasks not exist")
    public void givenTask_whenGetAllTasksByEmployeeEmail_thenReturnEmptyList() {
        given(repository.findAllByEmployeeEmail(entity.getEmployeeEmail())).willReturn(Collections.emptyList());
        var inDb = service.getAllByEmployeeEmail(entity.getEmployeeEmail());
        assertThat(inDb).isNotNull();
        assertThat(inDb).isEmpty();
        assertThat(inDb.size()).isEqualTo(0);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Task when Task exist")
    public void givenTaskDto_whenUpdateTask_thenReturnUpdatedTaskDto() {
        given(repository.save(entity)).willReturn(entity);
        given(repository.findById(id)).willReturn(Optional.of(entity));
        entity.setTitle("Updated Title");
        entity.setDescription("Updated Description");
        entity.setStatus(TaskStatus.DONE);
        entity.setDepartmentCode("UpdatedDepartmentCode");
        entity.setOfficeCode("UpdatedOfficeCode");
        entity.setEmployeeEmail("updated@mail.com");
        var inDb = service.update(id, TaskMapper.mapToTaskDto(entity));
        assertThat(inDb).isNotNull();
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getTitle()).isEqualTo(entity.getTitle());
        assertThat(inDb.getDescription()).isEqualTo(entity.getDescription());
        assertThat(inDb.getStatus()).isEqualTo(entity.getStatus());
        assertThat(inDb.getDepartmentCode()).isEqualTo(entity.getDepartmentCode());
        assertThat(inDb.getOfficeCode()).isEqualTo(entity.getOfficeCode());
        assertThat(inDb.getEmployeeEmail()).isEqualTo(entity.getEmployeeEmail());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Task when Task not exist")
    public void givenTaskDto_whenUpdateTask_thenReturnException() {
        given(repository.findById(id)).willReturn(Optional.empty());
        entity.setTitle("Updated Title");
        entity.setDescription("Updated Description");
        entity.setStatus(TaskStatus.DONE);
        entity.setDepartmentCode("UpdatedDepartmentCode");
        entity.setOfficeCode("UpdatedOfficeCode");
        entity.setEmployeeEmail("updated@mail.com");
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(id, TaskMapper.mapToTaskDto(entity));
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Task with id '%d' not found", id));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Task when Task exist")
    public void givenTaskId_whenDeleteTask_thenReturnNothing() {
        willDoNothing().given(repository).deleteById(id);
        repository.deleteById(id);
        verify(repository, times(1)).deleteById(id);
    }

}