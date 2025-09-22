package com.expense.ExpenseTrackerApplication.ServiceImpl;

import com.expense.ExpenseTrackerApplication.DTO.ExpenseRequestDTO;
import com.expense.ExpenseTrackerApplication.DTO.ExpenseResponseDTO;
import com.expense.ExpenseTrackerApplication.Entity.Expense;
import com.expense.ExpenseTrackerApplication.Entity.User;
import com.expense.ExpenseTrackerApplication.Exception.BadRequestException;
import com.expense.ExpenseTrackerApplication.Exception.ResourceNotFoundException;
import com.expense.ExpenseTrackerApplication.Repository.ExpenseRepository;
import com.expense.ExpenseTrackerApplication.Repository.UserRepository;
import com.expense.ExpenseTrackerApplication.Service.ExpenseService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ExpenseResponseDTO create(Long userId, ExpenseRequestDTO dto) {
        User user = getUser(userId);
        validate(dto);

        Expense e = new Expense();
        copy(dto, e);
        e.setUser(user);

        return toDto(expenseRepository.save(e));
    }

    @Override
    public ExpenseResponseDTO update(Long userId, Long expenseId, ExpenseRequestDTO dto) {
        User user = getUser(userId);
        Expense e = expenseRepository.findByIdAndUser(expenseId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        // allow partial update; validation where logical
        if (dto.getAmount() != null && dto.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }
        if (dto.getDate() != null && dto.getDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Date cannot be in the future");
        }

        copy(dto, e);
        return toDto(expenseRepository.save(e));
    }

    @Override
    public void delete(Long userId, Long expenseId) {
        User user = getUser(userId);
        Expense e = expenseRepository.findByIdAndUser(expenseId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        expenseRepository.delete(e);
    }

    @Override
    public ExpenseResponseDTO getOne(Long userId, Long expenseId) {
        User user = getUser(userId);
        Expense e = expenseRepository.findByIdAndUser(expenseId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        return toDto(e);
    }

    @Override
    public List<ExpenseResponseDTO> listAll(Long userId) {
        User user = getUser(userId);
        return expenseRepository.findAllByUser(user).stream().map(this::toDto).toList();
    }

    @Override
    public List<ExpenseResponseDTO> listByDateRange(Long userId, LocalDate from, LocalDate to) {
        User user = getUser(userId);
        LocalDate f = from != null ? from : LocalDate.MIN;
        LocalDate t = to != null ? to : LocalDate.MAX;
        return expenseRepository.findAllByUserAndDateBetween(user, f, t).stream().map(this::toDto).toList();
    }

    @Override
    public List<ExpenseResponseDTO> listByCategory(Long userId, String category) {
        if (category == null || category.isBlank()) return listAll(userId);
        User user = getUser(userId);
        return expenseRepository.findAllByUserAndCategoryIgnoreCase(user, category.trim())
                .stream().map(this::toDto).toList();
    }

    @Override
    public List<ExpenseResponseDTO> listByCategoryAndDateRange(Long userId, String category, LocalDate from, LocalDate to) {
        User user = getUser(userId);
        LocalDate f = from != null ? from : LocalDate.MIN;
        LocalDate t = to != null ? to : LocalDate.MAX;
        if (category == null || category.isBlank()) {
            return expenseRepository.findAllByUserAndDateBetween(user, f, t).stream().map(this::toDto).toList();
        }
        return expenseRepository.findAllByUserAndCategoryIgnoreCaseAndDateBetween(user, category.trim(), f, t)
                .stream().map(this::toDto).toList();
    }

    @Override
    public Double monthlyTotal(Long userId, YearMonth month) {
        User user = getUser(userId);
        var from = month.atDay(1);
        var to = month.atEndOfMonth();
        return expenseRepository.sumAmountByUserAndDateBetween(user, from, to);
    }

    @Override
    public Map<String, Double> monthlyCategoryTotals(Long userId, YearMonth month) {
        User user = getUser(userId);
        var from = month.atDay(1);
        var to = month.atEndOfMonth();
        var rows = expenseRepository.sumByCategory(user, from, to);
        return rows.stream().collect(Collectors.toMap(
                r -> Objects.toString(r[0], "UNKNOWN"),
                r -> ((Number) r[1]).doubleValue()
        ));
    }

    // ===== helpers =====
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

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

    private void copy(ExpenseRequestDTO s, Expense t) {
        if (s.getTitle() != null) t.setTitle(s.getTitle().trim());
        if (s.getAmount() != null) t.setAmount(s.getAmount());
        if (s.getDate() != null) t.setDate(s.getDate());
        if (s.getCategory() != null) t.setCategory(s.getCategory().trim());
        // optional
        if (s.getReceiptUrl() != null) t.setReceiptUrl(s.getReceiptUrl());
    }

    private ExpenseResponseDTO toDto(Expense e) {
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setAmount(e.getAmount());
        dto.setDate(e.getDate());
        dto.setCategory(e.getCategory());
        //dto.setReceiptUrl(e.getReceiptUrl());
        //dto.setUserId(e.getUser() != null ? e.getUser().getId() : null);
        return dto;
    }
}
