package com.expensetracker.tracker.controller;

import com.expensetracker.tracker.dto.ExpenseChartData;
import com.expensetracker.tracker.model.Expense;
import com.expensetracker.tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;
    
    @PostMapping
    public Expense create(@RequestBody Expense expense) {
    	return expenseService.saveExpense(expense);
    }
    
    @PostMapping("/ai")
    public Expense createWithAi(@RequestBody Expense expense) {
    	return expenseService.saveExpenseWithAi(expense);
    }


    @GetMapping
    public List<Expense> getFilteredExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        if (category != null) {
            return expenseService.findByCategory(category);
        } else if (min != null && max != null) {
            return expenseService.findByAmountBetween(min, max);
        } else if (from != null && to != null) {
            return expenseService.findByDateBetween(from, to);
        } else {
            return expenseService.findAll(); // default
        }
    }
    
    @GetMapping("/summary/category")
    public List<ExpenseChartData> getCategoryTotals(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
    	System.out.println("From: " + from + ", To: " + to);
        return expenseService.getCategoryTotalsBetween(from, to);
        
    }

    @PutMapping("/{id}")
    public Expense update(@PathVariable Long id, @RequestBody Expense expense) {
        return expenseService.updateExpense(id, expense);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "Expense deleted successfully!";
    }
}
