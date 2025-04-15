package com.expensetracker.tracker.dto;

public class ExpenseChartData {
    private String label;
    private Double total;

    public ExpenseChartData(String label, Double total) {
        this.label = label;
        this.total = total;
    }

    public String getLabel() {
        return label;
    }

    public Double getTotal() {
        return total;
    }
}
