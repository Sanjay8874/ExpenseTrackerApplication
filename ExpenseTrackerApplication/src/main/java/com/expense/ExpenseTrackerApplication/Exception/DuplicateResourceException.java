package com.expense.ExpenseTrackerApplication.Exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String msg) { super(msg); }
}