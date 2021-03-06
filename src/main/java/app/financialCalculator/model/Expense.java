package app.financialCalculator.model;

import java.time.LocalDate;

public class Expense {

    private String expenseName;
    private String expenseDescription;
    private double expenseValue;
    private LocalDate expenseDate;
    private long expenseUserId;
    private long expenseTypeId;

    public String getExpenseName() {
        return expenseName;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public double getExpenseValue() {
        return expenseValue;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public long getExpenseUserId() {
        return expenseUserId;
    }

    public long getExpenseTypeId() {
        return expenseTypeId;
    }

}
