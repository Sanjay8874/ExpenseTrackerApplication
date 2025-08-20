package com.expense.ExpenseTrackerApplication.ServiceImpl;

import com.expense.ExpenseTrackerApplication.DTO.ExpenseRequestDTO;
import com.expense.ExpenseTrackerApplication.DTO.ExpenseResponseDTO;
import com.expense.ExpenseTrackerApplication.Entity.Expense;
import com.expense.ExpenseTrackerApplication.Entity.User;
import com.expense.ExpenseTrackerApplication.Repository.ExpenseRepository;
import com.expense.ExpenseTrackerApplication.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExpenseServiceImpl {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public ExpenseResponseDTO addExpense(Long userId, ExpenseRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = new Expense();
        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setCategory(dto.getCategory());
        //expense.setReceiptUrl(dto.get);
        expense.setUser(user);
        Expense savedExpense = expenseRepository.save(expense);

       /* ExpenseResponseDTO expenseResponseDTO = new ExpenseResponseDTO();
        expenseResponseDTO.setAmount(savedExpense.getAmount());*/
        // Map Entity → ResponseDTO
        return new ExpenseResponseDTO(
                savedExpense.getId(),
                savedExpense.getTitle(),
                savedExpense.getAmount(),
                savedExpense.getDate(),
                savedExpense.getCategory(),
                savedExpense.getReceiptUrl()
        );
    }

    public List<ExpenseResponseDTO> getAllExpenseDetails() {
        List<Expense> expenseList = expenseRepository.findAll();
        return expenseList.stream().map(this::mapToDto).toList();
    }

    public ExpenseResponseDTO mapToDto(Expense expense){
        return new ExpenseResponseDTO(expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getDate(),
                expense.getCategory(),
                expense.getReceiptUrl());
    }
// use Bean Util to copy properties from Expense to ExpenseResponse
    public List<ExpenseResponseDTO> getAllExpenseDetails1() {
        List<Expense> expenseList = expenseRepository.findAll();
        List<ExpenseResponseDTO> expenseList1 = new ArrayList<>();
        for (Expense expense:expenseList){
            ExpenseResponseDTO expenseResponseDTO = new ExpenseResponseDTO();
            BeanUtils.copyProperties(expense,expenseResponseDTO);
            expenseList1.add(expenseResponseDTO);
        }
        return expenseList1;
    }
}
