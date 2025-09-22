package com.expense.ExpenseTrackerApplication.Service;

import com.expense.ExpenseTrackerApplication.DTO.ExpenseRequestDTO;
import com.expense.ExpenseTrackerApplication.DTO.ExpenseResponseDTO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface ExpenseService {

    ExpenseResponseDTO create(Long userId, ExpenseRequestDTO dto);
    ExpenseResponseDTO update(Long userId, Long expenseId, ExpenseRequestDTO dto);
    void delete(Long userId, Long expenseId);
    ExpenseResponseDTO getOne(Long userId, Long expenseId);

    // basic lists without pagination
    List<ExpenseResponseDTO> listAll(Long userId);
    List<ExpenseResponseDTO> listByDateRange(Long userId, LocalDate from, LocalDate to);
    List<ExpenseResponseDTO> listByCategory(Long userId, String category);
    List<ExpenseResponseDTO> listByCategoryAndDateRange(Long userId, String category, LocalDate from, LocalDate to);

    // summaries (for budget notifications later)
    Double monthlyTotal(Long userId, YearMonth month);
    Map<String, Double> monthlyCategoryTotals(Long userId, YearMonth month);
}
