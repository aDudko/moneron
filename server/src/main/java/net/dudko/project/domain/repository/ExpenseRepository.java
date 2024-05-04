package net.dudko.project.domain.repository;

import net.dudko.project.domain.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}