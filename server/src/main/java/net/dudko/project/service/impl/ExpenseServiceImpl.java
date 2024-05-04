package net.dudko.project.service.impl;

import lombok.AllArgsConstructor;
import net.dudko.project.domain.repository.ExpenseRepository;
import net.dudko.project.service.ExpenseService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    //TODO: Create implements

}
