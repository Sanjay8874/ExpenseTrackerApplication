package com.expense.ExpenseTrackerApplication.Exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        String timestamp,      // ISO-8601
        int status,            // 400/401/403/404/409/422/500
        String error,          // Bad Request / Unauthorized / ...
        String message,        // Human-friendly detail
        String path,           // /api/expenses/123
        String code,           // Optional app-specific code (e.g., EXPENSE_NOT_FOUND)
        Map<String, Object> details // Field errors or context
) {
    public static ApiError of(int status, String error, String message, String path, String code, Map<String, Object> details) {
        return new ApiError(OffsetDateTime.now().toString(), status, error, message, path, code, details);
    }
}

