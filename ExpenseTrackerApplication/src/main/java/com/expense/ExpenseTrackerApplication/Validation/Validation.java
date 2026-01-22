package com.expense.ExpenseTrackerApplication.Validation;

import com.expense.ExpenseTrackerApplication.DTO.ExpenseRequestDTO;
import com.expense.ExpenseTrackerApplication.Exception.BadRequestException;

import java.time.LocalDate;

public class Validation {

    //Validate Expense.....
    private void validate(ExpenseRequestDTO dto) {
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new BadRequestException("Title is required");
        }
        if (dto.getCategory() == null || dto.getCategory().isBlank()) {
            throw new BadRequestException("Category is required");
        }
        if (dto.getDate() == null) {
            throw new BadRequestException("Date is required");
        }
        if (dto.getDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Date cannot be in the future");
        }
    }
}
