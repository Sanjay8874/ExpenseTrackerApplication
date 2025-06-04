package com.expense.ExpenseTrackerApplication.Repository;

import com.expense.ExpenseTrackerApplication.Entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
