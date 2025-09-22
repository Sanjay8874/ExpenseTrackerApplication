package com.expense.ExpenseTrackerApplication.Controller;

import com.expense.ExpenseTrackerApplication.DTO.ExpenseRequestDTO;
import com.expense.ExpenseTrackerApplication.DTO.ExpenseResponseDTO;
import com.expense.ExpenseTrackerApplication.Repository.UserRepository;
import com.expense.ExpenseTrackerApplication.ServiceImpl.ExpenseServiceImpl1;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private final UserRepository userRepository;
    private final ExpenseServiceImpl1 expenseService;

    public ExpenseController(UserRepository userRepository, ExpenseServiceImpl1 expenseService) {
        this.userRepository = userRepository;
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> addExpense(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ExpenseRequestDTO dto) {
        Long userId = userRepository.findByEmail(userDetails.getUsername()).get().getId();
        logger.info("Ready to Response");
        return ResponseEntity.status(201).body(expenseService.addExpense(userId, dto));
    }

    @GetMapping("/getAllExpense")
    public List<ExpenseResponseDTO> getAllExpense() {
        return expenseService.getAllExpenseDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ExpenseResponseDTO>> getById(@PathVariable Long id){
        logger.info("Start finding the Expense for userId {}",id);
        return ResponseEntity.status(201).body(expenseService.getById(id));
    }
}
