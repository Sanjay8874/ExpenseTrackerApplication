package com.expense.ExpenseTrackerApplication.Repository;

import com.expense.ExpenseTrackerApplication.Entity.Income;
import com.expense.ExpenseTrackerApplication.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}
