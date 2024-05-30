package net.dudko.microservice.service;

import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Department;
import net.dudko.microservice.domain.mapper.DepartmentMapper;
import net.dudko.microservice.domain.repository.DepartmentRepository;
import net.dudko.microservice.model.dto.DepartmentDto;
import net.dudko.microservice.model.exception.ResourceDuplicatedException;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.impl.DepartmentServiceImpl;
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

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class DepartmentServiceTest {

    private static final String testNamePrefix = "DEPARTMENT-MICROSERVICE: DEPARTMENT-SERVICE: ";

    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentServiceImpl service;

    private Department entity;
    private DepartmentDto dto;

    private final Long id = 1L;

    @BeforeEach
    public void setup() {
        entity = TestUtil.getValidDepartment();
        dto = TestUtil.getValidDepartmentDto();
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Department when not exist duplicates")
    public void givenDepartmentDto_whenCreateDepartment_thenReturnCreatedDepartmentDto() {
        given(repository.existsByCode(entity.getCode())).willReturn(Boolean.FALSE);
        given(repository.save(entity)).willReturn(entity);
        var inDb = service.create(dto);
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(entity.getName());
        assertThat(inDb.getDescription()).isEqualTo(entity.getDescription());
        assertThat(inDb.getCode()).isEqualTo(entity.getCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Department when exist duplicates")
    public void givenDepartmentDto_whenCreateDepartment_thenReturnException() {
        given(repository.existsByCode(entity.getCode())).willReturn(Boolean.TRUE);
        var message = assertThrows(ResourceDuplicatedException.class, () -> {
            service.create(dto);
        }).getMessage();
        assertThat(message).isEqualTo("Code of department already exists!");
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Department by ID when Department exist")
    public void givenDepartmentId_whenGetDepartmentById_thenReturnDepartmentDto() {
        given(repository.findById(id)).willReturn(Optional.of(entity));
        var inDb = service.getById(id);
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(dto.getName());
        assertThat(inDb.getDescription()).isEqualTo(dto.getDescription());
        assertThat(inDb.getCode()).isEqualTo(dto.getCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Department by ID when Department not exist")
    public void givenDepartmentId_whenGetDepartmentById_thenReturnException() {
        given(repository.findById(id)).willReturn(Optional.empty());
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(id);
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Department with id: %d not found", id));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Department by code when Department exist")
    public void givenDepartmentCode_whenGetDepartmentByCode_thenReturnDepartmentDto() {
        given(repository.existsByCode(entity.getCode())).willReturn(Boolean.TRUE);
        given(repository.findDepartmentByCode(entity.getCode())).willReturn(entity);
        var inDb = service.getByCode(dto.getCode());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(dto.getName());
        assertThat(inDb.getDescription()).isEqualTo(dto.getDescription());
        assertThat(inDb.getCode()).isEqualTo(dto.getCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Department by code when Department not exist")
    public void givenDepartmentCode_whenGetDepartmentByCode_thenReturnException() {
        given(repository.existsByCode(entity.getCode())).willReturn(Boolean.FALSE);
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getByCode(dto.getCode());
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Department with code: %s not found!", entity.getCode()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Departments when Departments exist")
    public void givenDepartments_whenGetAllDepartments_thenReturnListOfDepartmentDto() {
        given(repository.findAll()).willReturn(List.of(entity));
        var inDb = service.getAll();
        assertThat(inDb).isNotNull();
        assertThat(inDb).isNotEmpty();
        assertThat(inDb.size()).isEqualTo(1);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Departments when Departments not exist")
    public void givenDepartments_whenGetAllDepartments_thenReturnEmptyList() {
        given(repository.findAll()).willReturn(Collections.emptyList());
        var inDb = service.getAll();
        assertThat(inDb).isNotNull();
        assertThat(inDb).isEmpty();
        assertThat(inDb.size()).isEqualTo(0);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Department when Department exist")
    public void givenDepartmentDto_whenUpdateDepartment_thenReturnUpdatedDepartmentDto() {
        given(repository.save(entity)).willReturn(entity);
        given(repository.findById(id)).willReturn(Optional.of(entity));
        entity.setName("updated name");
        entity.setDescription("updated description");
        var inDb = service.update(id, DepartmentMapper.mapToDepartmentDto(entity));
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(entity.getName());
        assertThat(inDb.getDescription()).isEqualTo(entity.getDescription());
        assertThat(inDb.getCode()).isEqualTo(entity.getCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Department when Department not exist")
    public void givenDepartmentDto_whenUpdateDepartment_thenReturnException() {
        given(repository.findById(id)).willReturn(Optional.empty());
        entity.setName("updated name");
        entity.setDescription("updated description");
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(id, DepartmentMapper.mapToDepartmentDto(entity));
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Department with id: %d not found", id));
    }

}