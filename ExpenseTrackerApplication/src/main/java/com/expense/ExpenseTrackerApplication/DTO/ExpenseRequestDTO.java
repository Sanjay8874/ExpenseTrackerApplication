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
public class ExpenseRequestDTO {
    private String title;
    private Double amount;
    private String category;
    private LocalDate date;
}

