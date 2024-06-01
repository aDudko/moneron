package net.dudko.microservice.service;

import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Employee;
import net.dudko.microservice.domain.mapper.EmployeeMapper;
import net.dudko.microservice.domain.repository.StaffRepository;
import net.dudko.microservice.model.dto.EmployeeDto;
import net.dudko.microservice.model.exception.ResourceDuplicatedException;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.impl.StaffServiceImpl;
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
class StaffServiceTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "StaffService: ";

    @Mock
    private StaffRepository repository;

    @Mock
    private DepartmentServiceApiClient departmentServiceApiClient;

    @Mock
    private OfficeServiceApiClient officeServiceApiClient;

    @InjectMocks
    private StaffServiceImpl service;

    private Employee entity;
    private EmployeeDto dto;

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
    @DisplayName(testNamePrefix + "Test for create Employee when not exist duplicates")
    public void givenEmployeeDto_whenCreateEmployee_thenReturnCreatedEmployeeDto() {
        given(repository.existsByEmail(entity.getEmail())).willReturn(Boolean.FALSE);
        given(repository.save(entity)).willReturn(entity);
        var inDb = service.create(dto);
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(inDb.getLastName()).isEqualTo(entity.getLastName());
        assertThat(inDb.getEmail()).isEqualTo(entity.getEmail());
        assertThat(inDb.getStatus()).isEqualTo(entity.getStatus());
        assertThat(inDb.getDepartmentCode()).isEqualTo(entity.getDepartmentCode());
        assertThat(inDb.getOfficeCode()).isEqualTo(entity.getOfficeCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Employee when exist duplicates")
    public void givenEmployeeDto_whenCreateEmployee_thenReturnException() {
        given(repository.existsByEmail(entity.getEmail())).willReturn(Boolean.TRUE);
        var message = assertThrows(ResourceDuplicatedException.class, () -> {
            service.create(dto);
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Employee with email: %s already exists!", dto.getEmail()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Employee by ID when Employee exist")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeDto() {
        given(repository.findById(entity.getId())).willReturn(Optional.of(entity));
        var inDb = service.getById(dto.getId());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getEmployeeDto()).isNotNull();
        assertThat(inDb.getEmployeeDto().getId()).isNotNull();
        assertThat(inDb.getEmployeeDto().getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(inDb.getEmployeeDto().getLastName()).isEqualTo(entity.getLastName());
        assertThat(inDb.getEmployeeDto().getEmail()).isEqualTo(entity.getEmail());
        assertThat(inDb.getEmployeeDto().getStatus()).isEqualTo(entity.getStatus());
        assertThat(inDb.getEmployeeDto().getDepartmentCode()).isEqualTo(entity.getDepartmentCode());
        assertThat(inDb.getEmployeeDto().getOfficeCode()).isEqualTo(entity.getOfficeCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Employee by ID when Employee not exist")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnException() {
        given(repository.findById(entity.getId())).willReturn(Optional.empty());
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(dto.getId());
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Employee with id: %d not found", dto.getId()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Employee by email when Employee exist")
    public void givenEmployeeEmail_whenGetEmployeeByEmail_thenReturnEmployeeDto() {
        given(repository.existsByEmail(entity.getEmail())).willReturn(Boolean.TRUE);
        given(repository.findByEmail(entity.getEmail())).willReturn(entity);
        var inDb = service.getByEmail(dto.getEmail());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(inDb.getLastName()).isEqualTo(entity.getLastName());
        assertThat(inDb.getEmail()).isEqualTo(entity.getEmail());
        assertThat(inDb.getStatus()).isEqualTo(entity.getStatus());
        assertThat(inDb.getDepartmentCode()).isEqualTo(entity.getDepartmentCode());
        assertThat(inDb.getOfficeCode()).isEqualTo(entity.getOfficeCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Employee by email when Employee not exist")
    public void givenEmployeeEmail_whenGetEmployeeByEmail_thenReturnException() {
        given(repository.existsByEmail(entity.getEmail())).willReturn(Boolean.FALSE);
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getByEmail(dto.getEmail());
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Employee with email: %s not found", entity.getEmail()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Staff when Staff exist")
    public void givenStaff_whenGetStaff_thenReturnListOfEmployeeDto() {
        given(repository.findAll()).willReturn(List.of(entity));
        var inDb = service.getStaff();
        assertThat(inDb).isNotNull();
        assertThat(inDb).isNotEmpty();
        assertThat(inDb.size()).isEqualTo(1);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Staff when Staff not exist")
    public void givenStaff_whenGetStaff_thenReturnEmptyList() {
        given(repository.findAll()).willReturn(Collections.emptyList());
        var inDb = service.getStaff();
        assertThat(inDb).isNotNull();
        assertThat(inDb).isEmpty();
        assertThat(inDb.size()).isEqualTo(0);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Employee when Employee exist")
    public void givenEmployeeDto_whenUpdateEmployee_thenReturnUpdatedEmployeeDto() {
        given(repository.save(entity)).willReturn(entity);
        given(repository.findById(entity.getId())).willReturn(Optional.of(entity));
        entity.setFirstName("Updated FirstName");
        entity.setLastName("Updated LastName");
        entity.setEmail("updated@mail.com");
        entity.setDepartmentCode("Updated Department Code");
        entity.setOfficeCode("Updated Office Code");
        var inDb = service.update(dto.getId(), EmployeeMapper.mapToEmployeeDto(entity));
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(inDb.getLastName()).isEqualTo(entity.getLastName());
        assertThat(inDb.getEmail()).isEqualTo(entity.getEmail());
        assertThat(inDb.getStatus()).isEqualTo(entity.getStatus());
        assertThat(inDb.getDepartmentCode()).isEqualTo(entity.getDepartmentCode());
        assertThat(inDb.getOfficeCode()).isEqualTo(entity.getOfficeCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Employee when Employee not exist")
    public void givenEmployeeDto_whenUpdateEmployee_thenReturnException() {
        given(repository.findById(entity.getId())).willReturn(Optional.empty());
        entity.setFirstName("Updated FirstName");
        entity.setLastName("Updated LastName");
        entity.setEmail("updated@mail.com");
        entity.setDepartmentCode("Updated Department Code");
        entity.setOfficeCode("Updated Office Code");
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(dto.getId(), EmployeeMapper.mapToEmployeeDto(entity));
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Employee with id: %d not found", dto.getId()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Employee when Employee exist")
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        given(repository.findById(entity.getId())).willReturn(Optional.of(entity));
        service.delete(entity.getId());
        verify(repository, times(0)).deleteById(entity.getId());
    }

}