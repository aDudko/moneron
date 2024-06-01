package net.dudko.microservice;

import net.dudko.microservice.domain.entity.Expense;
import net.dudko.microservice.domain.mapper.ExpenseMapper;
import net.dudko.microservice.model.dto.ExpenseDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestUtil {

    public static final String MS_NAME = "EXPENSE-TRACKER-MICROSERVICE: ";
    private static final LocalDate date = LocalDate.now();

    public static Expense getValidEntity() {
        return Expense.builder()
                .id(1L)
                .amount(new BigDecimal("100.0"))
                .expenseDate(date)
                .categoryName("Test Category Name")
                .build();
    }

    public static ExpenseDto getValidDto() {
        return ExpenseMapper.maptoExpenseDto(getValidEntity());
    }

}
