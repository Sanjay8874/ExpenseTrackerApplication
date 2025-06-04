package com.expense.ExpenseTrackerApplication.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomeResponseDTO {
    private Long id;
    private String source;
    private Double amount;
    private LocalDate date;
    private String userEmail;

    // Getters and Setters
}
