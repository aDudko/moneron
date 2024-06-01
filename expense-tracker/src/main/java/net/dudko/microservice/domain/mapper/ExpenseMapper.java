package net.dudko.microservice.domain.mapper;

import net.dudko.microservice.domain.entity.Expense;
import net.dudko.microservice.model.dto.ExpenseDto;

public class ExpenseMapper {

    public static Expense mapToExpense(ExpenseDto expenseDto) {
        return Expense.builder()
                .id(expenseDto.getId())
                .amount(expenseDto.getAmount())
                .expenseDate(expenseDto.getExpenseDate())
                .categoryName(expenseDto.getCategoryName())
                .build();
    }

    public static ExpenseDto maptoExpenseDto(Expense expense) {
        return ExpenseDto.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .expenseDate(expense.getExpenseDate())
                .categoryName(expense.getCategoryName())
                .build();
    }

}
