package net.dudko.microservice.service;

import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Staff;
import net.dudko.microservice.domain.mapper.StaffMapper;
import net.dudko.microservice.domain.repository.StaffRepository;
import net.dudko.microservice.model.dto.StaffDto;
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

    private static final String testNamePrefix = "STAFF-MICROSERVICE: STAFF-SERVICE: ";

    @Mock
    private StaffRepository repository;

    @Mock
    private DepartmentServiceApiClient departmentServiceApiClient;

    @Mock
    private OfficeServiceApiClient officeServiceApiClient;

    @InjectMocks
    private StaffServiceImpl service;

    private Staff entity;
    private StaffDto dto;

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
    @DisplayName(testNamePrefix + "Test for create Staff when not exist duplicates")
    public void givenStaffDto_whenCreateStaff_thenReturnCreatedStaffDto() {
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
    @DisplayName(testNamePrefix + "Test for create Staff when exist duplicates")
    public void givenStaffDto_whenCreateStaff_thenReturnException() {
        given(repository.existsByEmail(entity.getEmail())).willReturn(Boolean.TRUE);
        var message = assertThrows(ResourceDuplicatedException.class, () -> {
            service.create(dto);
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Employee with email: %s already exists!", dto.getEmail()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Staff by ID when Staff exist")
    public void givenStaffId_whenGetStaffById_thenReturnStaffDto() {
        given(repository.findById(id)).willReturn(Optional.of(entity));
        var inDb = service.getById(id);
        assertThat(inDb).isNotNull();
        assertThat(inDb.getStaffDto()).isNotNull();
        assertThat(inDb.getStaffDto().getId()).isNotNull();
        assertThat(inDb.getStaffDto().getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(inDb.getStaffDto().getLastName()).isEqualTo(entity.getLastName());
        assertThat(inDb.getStaffDto().getEmail()).isEqualTo(entity.getEmail());
        assertThat(inDb.getStaffDto().getStatus()).isEqualTo(entity.getStatus());
        assertThat(inDb.getStaffDto().getDepartmentCode()).isEqualTo(entity.getDepartmentCode());
        assertThat(inDb.getStaffDto().getOfficeCode()).isEqualTo(entity.getOfficeCode());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Staff by ID when Staff not exist")
    public void givenStaffId_whenGetStaffById_thenReturnException() {
        given(repository.findById(id)).willReturn(Optional.empty());
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(id);
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Employee with id: %d not found", id));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Staff by email when Staff exist")
    public void givenStaffCode_whenGetStaffByCode_thenReturnStaffDto() {
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
    @DisplayName(testNamePrefix + "Test for get Staff by email when Staff not exist")
    public void givenStaffCode_whenGetStaffByCode_thenReturnException() {
        given(repository.existsByEmail(entity.getEmail())).willReturn(Boolean.FALSE);
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getByEmail(dto.getEmail());
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Employee with email: %s not found", entity.getEmail()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Staffs when Staffs exist")
    public void givenStaffs_whenGetAllStaffs_thenReturnListOfStaffDto() {
        given(repository.findAll()).willReturn(List.of(entity));
        var inDb = service.getAll();
        assertThat(inDb).isNotNull();
        assertThat(inDb).isNotEmpty();
        assertThat(inDb.size()).isEqualTo(1);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Staff when Staff not exist")
    public void givenStaff_whenGetAllStaffs_thenReturnEmptyList() {
        given(repository.findAll()).willReturn(Collections.emptyList());
        var inDb = service.getAll();
        assertThat(inDb).isNotNull();
        assertThat(inDb).isEmpty();
        assertThat(inDb.size()).isEqualTo(0);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Staff when Staff exist")
    public void givenStaffDto_whenUpdateStaff_thenReturnUpdatedStaffDto() {
        given(repository.save(entity)).willReturn(entity);
        given(repository.findById(id)).willReturn(Optional.of(entity));
        entity.setFirstName("Updated first name");
        entity.setLastName("Updated last name");
        entity.setEmail("updated@mail.com");
        entity.setDepartmentCode("UpdatedDepartmentCode");
        entity.setOfficeCode("UpdatedOfficeCode");
        var inDb = service.update(id, StaffMapper.mapToStaffDto(entity));
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
    @DisplayName(testNamePrefix + "Test for update Staff when Staff not exist")
    public void givenStaffDto_whenUpdateStaff_thenReturnException() {
        given(repository.findById(id)).willReturn(Optional.empty());
        entity.setFirstName("Updated first name");
        entity.setLastName("Updated last name");
        entity.setEmail("updated@mail.com");
        entity.setDepartmentCode("UpdatedDepartmentCode");
        entity.setOfficeCode("UpdatedOfficeCode");
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(id, StaffMapper.mapToStaffDto(entity));
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Employee with id: %d not found", id));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Staff when Staff exist")
    public void givenStaffId_whenDeleteStaff_thenReturnNothing() {
        willDoNothing().given(repository).deleteById(id);
        repository.deleteById(id);
        verify(repository, times(1)).deleteById(id);
    }

}