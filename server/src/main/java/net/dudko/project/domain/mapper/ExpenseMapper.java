package net.dudko.project.domain.mapper;

import net.dudko.project.domain.entity.Category;
import net.dudko.project.domain.entity.Expense;
import net.dudko.project.model.dto.CategoryDto;
import net.dudko.project.model.dto.ExpenseDto;

public class ExpenseMapper {

    public static ExpenseDto maptoExpenseDto(Expense expense) {
        return new ExpenseDto(
                expense.getId(),
                expense.getAmount(),
                expense.getExpenseDate(),
                new CategoryDto(
                        expense.getCategory().getId(),
                        expense.getCategory().getName()
                )
        );
    }

    public static Expense mapToExpense(ExpenseDto expenseDto) {
        return Expense.builder()
                .id(expenseDto.id())
                .amount(expenseDto.amount())
                .expenseDate(expenseDto.expenseDate())
                .category(Category.builder()
                        .id(expenseDto.categoryDto().id())
                        .build())
                .build();
    }

}
