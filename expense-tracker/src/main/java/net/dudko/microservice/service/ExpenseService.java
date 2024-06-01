package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.ExpenseDto;

import java.util.List;

public interface ExpenseService {

    ExpenseDto create(ExpenseDto expenseDto);

    ApiResponseDto getById(Long id);

    List<ExpenseDto> getAll();

    ExpenseDto update(Long id, ExpenseDto expenseDto);

    void delete(Long id);

}
