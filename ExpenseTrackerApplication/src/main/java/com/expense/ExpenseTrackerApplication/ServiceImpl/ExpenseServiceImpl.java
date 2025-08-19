package com.expense.ExpenseTrackerApplication.ServiceImpl;

import com.expense.ExpenseTrackerApplication.DTO.ExpenseRequestDTO;
import com.expense.ExpenseTrackerApplication.DTO.ExpenseResponseDTO;
import com.expense.ExpenseTrackerApplication.Entity.Expense;
import com.expense.ExpenseTrackerApplication.Entity.User;
import com.expense.ExpenseTrackerApplication.Repository.ExpenseRepository;
import com.expense.ExpenseTrackerApplication.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
}
