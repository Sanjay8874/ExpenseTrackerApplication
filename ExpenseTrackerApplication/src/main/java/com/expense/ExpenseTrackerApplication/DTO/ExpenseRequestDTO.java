package com.expense.ExpenseTrackerApplication.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpenseRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @Positive(message = "Amount must be > 0")
    private Double amount;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private java.time.LocalDate date;

    @NotBlank(message = "Category is required")
    private String category;

    private String receiptUrl;
}

