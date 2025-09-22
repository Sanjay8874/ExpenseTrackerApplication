package com.expense.ExpenseTrackerApplication.Repository;

import com.expense.ExpenseTrackerApplication.DTO.ExpenseResponseDTO;
import com.expense.ExpenseTrackerApplication.Entity.Expense;
import com.expense.ExpenseTrackerApplication.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);
    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
    List<Expense> findByUserIdAndCategory(Long userId, String category);


    /*=========================================================================*/

    // strict ownership checks
    Optional<Expense> findByIdAndUser(Long id, User user);
    List<Expense> findAllByUser(User user);

    // optional filters
    List<Expense> findAllByUserAndDateBetween(User user, LocalDate from, LocalDate to);
    List<Expense> findAllByUserAndCategoryIgnoreCase(User user, String category);
    List<Expense> findAllByUserAndCategoryIgnoreCaseAndDateBetween(User user, String category, LocalDate from, LocalDate to);

    // summaries for future notifications/analytics
    @Query("select coalesce(sum(e.amount),0) from Expense e where e.user = :user and e.date between :from and :to")
    Double sumAmountByUserAndDateBetween(User user, LocalDate from, LocalDate to);

    @Query("select e.category, coalesce(sum(e.amount),0) from Expense e where e.user = :user and e.date between :from and :to group by e.category")
    List<Object[]> sumByCategory(User user, LocalDate from, LocalDate to);
}
