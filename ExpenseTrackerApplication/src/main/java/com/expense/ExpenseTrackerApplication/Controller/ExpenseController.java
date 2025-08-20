package com.expense.ExpenseTrackerApplication.Controller;

import com.expense.ExpenseTrackerApplication.DTO.ExpenseRequestDTO;
import com.expense.ExpenseTrackerApplication.DTO.ExpenseResponseDTO;
import com.expense.ExpenseTrackerApplication.Repository.UserRepository;
import com.expense.ExpenseTrackerApplication.ServiceImpl.ExpenseServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final UserRepository userRepository;
    private final ExpenseServiceImpl expenseService;

    public ExpenseController(UserRepository userRepository, ExpenseServiceImpl expenseService) {
        this.userRepository = userRepository;
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseResponseDTO addExpense(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ExpenseRequestDTO dto) {
        Long userId = userRepository.findByEmail(userDetails.getUsername()).get().getId();
        return expenseService.addExpense(userId, dto);
    }

    @GetMapping("/getAllExpense")
    public List<ExpenseResponseDTO> getAllExpense() {
        return expenseService.getAllExpenseDetails();
    }
}
