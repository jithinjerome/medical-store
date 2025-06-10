package com.example.medical.store.Expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.medicalStore.id = :storeId AND e.expenseDate = :date")
    double getTotalExpensesForStore(@Param("storeId") Long storeId, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.medicalStore.id = :storeId AND YEAR(e.expenseDate) = :year AND MONTH(e.expenseDate) = :month")
    double getMonthlyExpensesForStore(@Param("storeId") Long storeId, @Param("year") int year, @Param("month") int month);
}
