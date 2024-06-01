package net.dudko.microservice.service.impl;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import net.dudko.microservice.domain.mapper.ExpenseMapper;
import net.dudko.microservice.domain.repository.ExpenseRepository;
import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.CategoryDto;
import net.dudko.microservice.model.dto.ExpenseDto;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.CategoryServiceApiClient;
import net.dudko.microservice.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final CategoryServiceApiClient categoryServiceApiClient;

    @Override
    public ExpenseDto create(ExpenseDto expenseDto) {
        var inDb = expenseRepository.save(ExpenseMapper.mapToExpense(expenseDto));
        return ExpenseMapper.maptoExpenseDto(inDb);
    }

    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultResponse")
    @Override
    public ApiResponseDto getById(Long id) {
        var inDb = expenseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Expense with id: %d not found", id)));
        var inCategoryService = categoryServiceApiClient.getCategoryByName(inDb.getCategoryName());
        return ApiResponseDto.builder()
                .expenseDto(ExpenseMapper.maptoExpenseDto(inDb))
                .categoryDto(inCategoryService)
                .build();
    }

    @Override
    public List<ExpenseDto> getAll() {
        return expenseRepository.findAll()
                .stream()
                .map(ExpenseMapper::maptoExpenseDto)
                .toList();
    }

    @Override
    public ExpenseDto update(Long id, ExpenseDto expenseDto) {
        expenseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Expense with id: %d not found", id)));
        var inDb = expenseRepository.save(ExpenseMapper.mapToExpense(expenseDto));
        return ExpenseMapper.maptoExpenseDto(inDb);
    }

    @Override
    public void delete(Long id) {
        var inDb = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Expense with id: %d not found", id)));
        expenseRepository.delete(inDb);
    }

    public ApiResponseDto getDefaultResponse(Long id, Exception exception) {
        var inDb = expenseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id: %d not found", id)));
        var inCategoryService = CategoryDto.builder()
                .name("Default Category")
                .build();
        return ApiResponseDto.builder()
                .expenseDto(ExpenseMapper.maptoExpenseDto(inDb))
                .categoryDto(inCategoryService)
                .build();
    }

}
