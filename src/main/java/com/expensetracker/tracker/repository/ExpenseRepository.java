package com.expensetracker.tracker.repository;

import com.expensetracker.tracker.dto.ExpenseChartData;
import com.expensetracker.tracker.model.Expense;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<Expense> findByCategory(String category);
	List<Expense> findByAmountBetween(Double min, Double max);
	List<Expense> findByDateBetween(LocalDate from, LocalDate to);
	
	@Query("SELECT new com.expensetracker.tracker.dto.ExpenseChartData(e.category, SUM(e.amount)) " +
		       "FROM Expense e WHERE e.date BETWEEN :start AND :end " +
		       "GROUP BY e.category")
		List<ExpenseChartData> getTotalByCategoryBetween(LocalDate start, LocalDate end);

}
