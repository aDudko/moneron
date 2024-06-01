package net.dudko.microservice.service;

import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Office;
import net.dudko.microservice.domain.repository.OfficeRepository;
import net.dudko.microservice.model.dto.OfficeDto;
import net.dudko.microservice.model.exception.ResourceDuplicatedException;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.impl.OfficeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OfficeServiceTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "OfficeService: ";

    @Mock
    private OfficeRepository repository;

    @InjectMocks
    private OfficeServiceImpl service;

    private Office entity;
    private OfficeDto dto;

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
    @DisplayName(testNamePrefix + "Test for create Office when not exist duplicates")
    public void givenOfficeDto_whenCreateOffice_thenReturnCreatedOfficeDto() {
        given(repository.existsByCode(entity.getCode())).willReturn(Boolean.FALSE);
        given(repository.save(entity)).willReturn(entity);
        var inDb = service.create(dto);
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(entity.getName());
        assertThat(inDb.getDescription()).isEqualTo(entity.getDescription());
        assertThat(inDb.getCode()).isEqualTo(entity.getCode());
        assertThat(inDb.getCreated()).isNotNull();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Office when exist duplicates")
    public void givenOfficeDto_whenCreateOffice_thenReturnException() {
        given(repository.existsByCode(entity.getCode())).willReturn(Boolean.TRUE);
        var message = assertThrows(ResourceDuplicatedException.class, () -> {
            service.create(dto);
        }).getMessage();
        assertThat(message).isEqualTo("Code of office already exists");
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Office by ID when Office exist")
    public void givenOfficeId_whenGetOfficeById_thenReturnOfficeDto() {
        given(repository.findById(id)).willReturn(Optional.of(entity));
        var inDb = service.getById(id);
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(dto.getName());
        assertThat(inDb.getDescription()).isEqualTo(dto.getDescription());
        assertThat(inDb.getCode()).isEqualTo(dto.getCode());
        assertThat(inDb.getCreated()).isNotNull();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Office by ID when Office not exist")
    public void givenOfficeId_whenGetOfficeById_thenReturnException() {
        given(repository.findById(id)).willReturn(Optional.empty());
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(id);
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Office with id: %d not found", id));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Office by code when Office exist")
    public void givenOfficeCode_whenGetOfficeByCode_thenReturnOfficeDto() {
        given(repository.existsByCode(entity.getCode())).willReturn(Boolean.TRUE);
        given(repository.findOfficeByCode(entity.getCode())).willReturn(entity);
        var inDb = service.getByCode(dto.getCode());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getName()).isEqualTo(dto.getName());
        assertThat(inDb.getDescription()).isEqualTo(dto.getDescription());
        assertThat(inDb.getCode()).isEqualTo(dto.getCode());
        assertThat(inDb.getCreated()).isNotNull();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Office by code when Office not exist")
    public void givenOfficeCode_whenGetOfficeByCode_thenReturnException() {
        given(repository.existsByCode(entity.getCode())).willReturn(Boolean.FALSE);
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getByCode(dto.getCode());
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Office with code: %s not found!", entity.getCode()));
    }

}