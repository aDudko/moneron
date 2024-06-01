package net.dudko.microservice.service;

import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.entity.Expense;
import net.dudko.microservice.domain.mapper.ExpenseMapper;
import net.dudko.microservice.domain.repository.ExpenseRepository;
import net.dudko.microservice.model.dto.CategoryDto;
import net.dudko.microservice.model.dto.ExpenseDto;
import net.dudko.microservice.model.exception.ResourceDuplicatedException;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ExpenseServiceTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "ExpenseService: ";

    @Mock
    private ExpenseRepository repository;

    @Mock
    private CategoryServiceApiClient categoryServiceApiClient;

    @InjectMocks
    private ExpenseServiceImpl service;

    private Expense entity;
    private ExpenseDto dto;

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
    @DisplayName(testNamePrefix + "Test for create Expense")
    public void givenExpenseDto_whenCreateExpense_thenReturnCreatedExpenseDto() {
        given(repository.save(entity)).willReturn(entity);
        var inDb = service.create(dto);
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getAmount()).isEqualTo(entity.getAmount());
        assertThat(inDb.getExpenseDate()).isEqualTo(entity.getExpenseDate());
        assertThat(inDb.getCategoryName()).isEqualTo(entity.getCategoryName());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Expense by ID when Expense exist")
    public void givenExpenseId_whenGetExpenseById_thenReturnExpenseDto() {
        given(repository.findById(entity.getId())).willReturn(Optional.of(entity));
        var inDb = service.getById(entity.getId());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getExpenseDto()).isNotNull();
        assertThat(inDb.getExpenseDto().getId()).isNotNull();
        assertThat(inDb.getExpenseDto().getAmount()).isEqualTo(dto.getAmount());
        assertThat(inDb.getExpenseDto().getExpenseDate()).isEqualTo(dto.getExpenseDate());
        assertThat(inDb.getExpenseDto().getCategoryName()).isEqualTo(dto.getCategoryName());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Expense by ID when Expense not exist")
    public void givenExpenseId_whenGetExpenseById_thenReturnException() {
        given(repository.findById(entity.getId())).willReturn(Optional.empty());
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(entity.getId());
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Expense with id: %d not found", entity.getId()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Expenses when Expenses exist")
    public void givenExpenses_whenGetAllExpenses_thenReturnListOfExpenseDto() {
        given(repository.findAll()).willReturn(List.of(entity));
        var inDb = service.getAll();
        assertThat(inDb).isNotNull();
        assertThat(inDb).isNotEmpty();
        assertThat(inDb.size()).isEqualTo(1);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Expenses when Expenses not exist")
    public void givenExpenses_whenGetAllExpenses_thenReturnEmptyList() {
        given(repository.findAll()).willReturn(Collections.emptyList());
        var inDb = service.getAll();
        assertThat(inDb).isNotNull();
        assertThat(inDb).isEmpty();
        assertThat(inDb.size()).isEqualTo(0);
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Expense when Expense exist")
    public void givenExpenseDto_whenUpdateExpense_thenReturnUpdatedExpenseDto() {
        given(repository.save(entity)).willReturn(entity);
        given(repository.findById(entity.getId())).willReturn(Optional.of(entity));
        entity.setAmount(new BigDecimal("77.7"));
        entity.setCategoryName("Updated Name");
        var inDb = service.update(entity.getId(), ExpenseMapper.maptoExpenseDto(entity));
        assertThat(inDb).isNotNull();
        assertThat(inDb.getId()).isNotNull();
        assertThat(inDb.getAmount()).isEqualTo(entity.getAmount());
        assertThat(inDb.getExpenseDate()).isEqualTo(entity.getExpenseDate());
        assertThat(inDb.getCategoryName()).isEqualTo(entity.getCategoryName());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Expense when Expense not exist")
    public void givenExpenseDto_whenUpdateExpense_thenReturnException() {
        given(repository.findById(entity.getId())).willReturn(Optional.empty());
        entity.setAmount(new BigDecimal("77.7"));
        entity.setCategoryName("Not Exist");
        var message = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(entity.getId(), ExpenseMapper.maptoExpenseDto(entity));
        }).getMessage();
        assertThat(message).isEqualTo(String.format("Expense with id: %d not found", entity.getId()));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Expense when Expense exist")
    public void givenExpenseId_whenDeleteExpense_thenReturnNothing() {
        given(repository.findById(entity.getId())).willReturn(Optional.of(entity));
        service.delete(entity.getId());
        verify(repository, times(0)).deleteById(entity.getId());
    }

}