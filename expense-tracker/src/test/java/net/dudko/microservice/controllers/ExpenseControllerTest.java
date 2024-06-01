package net.dudko.microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dudko.microservice.AbstractContainerBaseTest;
import net.dudko.microservice.TestUtil;
import net.dudko.microservice.domain.mapper.ExpenseMapper;
import net.dudko.microservice.domain.repository.ExpenseRepository;
import net.dudko.microservice.model.dto.ExpenseDto;
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

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExpenseControllerTest extends AbstractContainerBaseTest {

    private static final String testNamePrefix = TestUtil.MS_NAME + "ExpenseController: ";
    private static final String BASE_URL = "/expense";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExpenseRepository repository;

    private ExpenseDto dto;

    @BeforeEach
    public void setup() {
        dto = TestUtil.getValidDto();
    }

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName(testNamePrefix + "Test for create Expense")
    public void givenExpenseDto_whenCreateExpense_thenReturnCreatedExpenseDto() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.amount", is(dto.getAmount().doubleValue())))
                .andExpect(jsonPath("$.expenseDate", is(dto.getExpenseDate().toString())))
                .andExpect(jsonPath("$.categoryName", is(dto.getCategoryName())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Expense by ID when Expense exist")
    public void givenExpenseId_whenGetExpenseById_thenReturnExpenseDto() throws Exception {
        var inDb = repository.save(ExpenseMapper.mapToExpense(dto));
        mockMvc.perform(get(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expenseDto.id", notNullValue()))
                .andExpect(jsonPath("$.expenseDto.amount", is(dto.getAmount().doubleValue())))
                .andExpect(jsonPath("$.expenseDto.expenseDate", is(dto.getExpenseDate().toString())))
                .andExpect(jsonPath("$.expenseDto.categoryName", is(dto.getCategoryName())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get Expense by ID when Expense not exist")
    public void givenExpenseId_whenGetExpenseById_thenReturnException() throws Exception {
        mockMvc.perform(get(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Expenses when Expenses exist")
    public void givenExpenses_whenGetAllExpenses_thenReturnListOfExpenseDto() throws Exception {
        repository.save(ExpenseMapper.mapToExpense(dto));
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for get all Expenses when Expenses not exist")
    public void givenExpenses_whenGetAllExpenses_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Expense when Expense exist")
    public void givenExpenseDto_whenUpdateExpense_thenReturnUpdatedExpenseDto() throws Exception {
        var inDb = repository.save(ExpenseMapper.mapToExpense(dto));
        dto.setAmount(new BigDecimal("77.7"));
        dto.setCategoryName("Updated Name");
        mockMvc.perform(put(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.amount", is(dto.getAmount().doubleValue())))
                .andExpect(jsonPath("$.expenseDate", is(dto.getExpenseDate().toString())))
                .andExpect(jsonPath("$.categoryName", is(dto.getCategoryName())));
    }

    @Test
    @DisplayName(testNamePrefix + "Test for update Expense when Expense not exist")
    public void givenExpenseDto_whenUpdateExpense_thenReturnException() throws Exception {
        dto.setAmount(new BigDecimal("77.7"));
        dto.setCategoryName("Updated Name");
        mockMvc.perform(put(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Expense when Expense exist")
    public void givenExpenseId_whenDeleteExpense_thenReturnNothing() throws Exception {
        var inDb = repository.save(ExpenseMapper.mapToExpense(dto));
        mockMvc.perform(delete(BASE_URL.concat("/{id}"), inDb.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Expense was deleted successfully");
    }

    @Test
    @DisplayName(testNamePrefix + "Test for delete Expense when Expense not exist")
    public void givenExpenseId_whenDeleteExpense_thenReturnException() throws Exception {
        mockMvc.perform(delete(BASE_URL.concat("/{id}"), dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}