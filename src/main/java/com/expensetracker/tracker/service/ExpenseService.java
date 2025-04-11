package com.expensetracker.tracker.service;

import com.expensetracker.tracker.model.Expense;
import com.expensetracker.tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    //post
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }
    //get
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
    //update
    public Expense updateExpense(Long id, Expense newExpense) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        existing.setTitle(newExpense.getTitle());
        existing.setAmount(newExpense.getAmount());
        existing.setCategory(newExpense.getCategory());
        existing.setDate(newExpense.getDate());

        return expenseRepository.save(existing);
    }
    //delete
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

}
