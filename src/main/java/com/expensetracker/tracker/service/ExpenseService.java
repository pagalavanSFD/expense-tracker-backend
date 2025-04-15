package com.expensetracker.tracker.service;

import com.expensetracker.tracker.dto.ExpenseChartData;
import com.expensetracker.tracker.model.Expense;
import com.expensetracker.tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public List<Expense> findByCategory(String category) {
        return expenseRepository.findByCategory(category);
    }

    public List<Expense> findByAmountBetween(Double min, Double max) {
        return expenseRepository.findByAmountBetween(min, max);
    }

    public List<Expense> findByDateBetween(LocalDate from, LocalDate to) {
        return expenseRepository.findByDateBetween(from, to);
    }

    public Expense updateExpense(Long id, Expense newExpense) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        existing.setTitle(newExpense.getTitle());
        existing.setAmount(newExpense.getAmount());
        existing.setCategory(newExpense.getCategory());
        existing.setDate(newExpense.getDate());

        return expenseRepository.save(existing);
    }
    
    public List<ExpenseChartData> getCategoryTotalsBetween(LocalDate start, LocalDate end) {
        return expenseRepository.getTotalByCategoryBetween(start, end);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
