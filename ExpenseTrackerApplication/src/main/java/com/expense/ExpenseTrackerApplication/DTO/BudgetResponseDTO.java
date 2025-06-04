package com.expense.ExpenseTrackerApplication.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetResponseDTO {
    private Long id;
    private Integer month;
    private Integer year;
    private Double amount;
    private String userEmail;

    // Getters and Setters
}
