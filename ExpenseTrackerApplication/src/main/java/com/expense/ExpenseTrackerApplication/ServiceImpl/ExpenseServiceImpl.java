package com.expense.ExpenseTrackerApplication.ServiceImpl;

import com.expense.ExpenseTrackerApplication.DTO.ExpenseRequestDTO;
import com.expense.ExpenseTrackerApplication.DTO.ExpenseResponseDTO;
import com.expense.ExpenseTrackerApplication.Entity.Expense;
import com.expense.ExpenseTrackerApplication.Entity.User;
import com.expense.ExpenseTrackerApplication.Exception.ResourceNotFoundException;
import com.expense.ExpenseTrackerApplication.Repository.ExpenseRepository;
import com.expense.ExpenseTrackerApplication.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExpenseServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseServiceImpl.class);
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

    public ExpenseResponseDTO mapToDto(Expense expense) {
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
        for (Expense expense : expenseList) {
            ExpenseResponseDTO expenseResponseDTO = new ExpenseResponseDTO();
            BeanUtils.copyProperties(expense, expenseResponseDTO);
            expenseList1.add(expenseResponseDTO);
        }
        return expenseList1;
    }


    public List<ExpenseResponseDTO> getById(Long id) {
        List<Expense> expenseList = expenseRepository.findByUserId(id);
        logger.info("Size {}", expenseList.size());
        for (Expense ex1 : expenseList) {
            logger.info("expense {}", ex1.toString());
        }

        List<ExpenseResponseDTO> listOfExpense = new ArrayList<>();
        if (!expenseList.isEmpty()) {
            logger.info("in if condition");

            for (Expense expense : expenseList) {
                ExpenseResponseDTO expenseResponseDTO = new ExpenseResponseDTO();
                BeanUtils.copyProperties(expense, expenseResponseDTO);
                listOfExpense.add(expenseResponseDTO);
            }

            for (ExpenseResponseDTO ex1 : listOfExpense) {
                logger.info("expense11 {}", ex1.toString());
            }
        } else {
            throw new ResourceNotFoundException("Expense not available for UserId" + id);
        }

        /*Optional<List<Expense>> exO = Optional.ofNullable(expenseList);
        ExpenseResponseDTO expenseResponseDTO = new ExpenseResponseDTO();
        List<ExpenseResponseDTO> listOfExpense = new ArrayList<>();
        if (exO.isPresent()){
            for(Expense expense:expenseList){
                BeanUtils.copyProperties(expense, expenseResponseDTO);
                listOfExpense.add(expenseResponseDTO);
            }
        }
        else {
            throw new ResourceNotFoundException("Expense not available for UserId" +id);
        }
        return listOfExpense;*/
        return listOfExpense;
    }
}
