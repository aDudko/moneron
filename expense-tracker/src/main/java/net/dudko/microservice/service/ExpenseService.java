package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.ExpenseDto;

import java.util.List;

public interface ExpenseService {

    ExpenseDto createExpense(ExpenseDto expenseDto);

    ExpenseDto getExpenseById(Long id);

    List<ExpenseDto> getExpenses();

    ExpenseDto updateExpense(Long id, ExpenseDto expenseDto);

    void deleteExpense(Long id);

}
