package com.example.medical.store.EmployeeWage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EmployeeWageRepo extends JpaRepository<EmployeeWage, Long> {

    @Query("SELECT COALESCE(SUM(e.salary), 0) FROM EmployeeWage e WHERE e.medicalStore.id = :storeId AND e.paymentDate = :date")
    double getDailyWages(@Param("storeId") Long storeId, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(e.salary), 0) FROM EmployeeWage e WHERE e.medicalStore.id = :storeId AND YEAR(e.paymentDate) = :year AND MONTH(e.paymentDate) = :month")
    double getMonthlyWages(@Param("storeId") Long storeId, @Param("year") int year, @Param("month") int month);
}