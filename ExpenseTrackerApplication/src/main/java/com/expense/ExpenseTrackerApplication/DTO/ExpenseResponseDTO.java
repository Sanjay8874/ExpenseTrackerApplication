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
public class ExpenseResponseDTO {
    private Long id;
    private String title;
    private Double amount;
    private LocalDate date;
    private String category;
    private String userEmail;
}

