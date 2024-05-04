package net.dudko.project.service.impl;

import lombok.AllArgsConstructor;
import net.dudko.project.domain.mapper.ExpenseMapper;
import net.dudko.project.domain.repository.CategoryRepository;
import net.dudko.project.domain.repository.ExpenseRepository;
import net.dudko.project.model.dto.ExpenseDto;
import net.dudko.project.model.exceprion.ResourceNotFoundException;
import net.dudko.project.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ExpenseDto createExpense(ExpenseDto expenseDto) {
        var expense = expenseRepository.save(ExpenseMapper.mapToExpense(expenseDto));
        return ExpenseMapper.maptoExpenseDto(expense);
    }

    @Override
    public ExpenseDto getExpenseById(Long id) {
        var expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        return ExpenseMapper.maptoExpenseDto(expense);
    }

    @Override
    public List<ExpenseDto> getExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(ExpenseMapper::maptoExpenseDto)
                .toList();
    }

    @Override
    public ExpenseDto updateExpense(Long id, ExpenseDto expenseDto) {
        var expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        expense.setAmount(expenseDto.amount());
        expense.setExpenseDate(expenseDto.expenseDate());
        if (expenseDto.categoryDto() != null) {
            var category = categoryRepository.findById(expenseDto.categoryDto().id())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + expenseDto.categoryDto().id()));
            expense.setCategory(category);
        }
        expense = expenseRepository.save(expense);
        return ExpenseMapper.maptoExpenseDto(expense);
    }

    @Override
    public void deleteExpense(Long id) {
        var expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        expenseRepository.delete(expense);
    }

}
