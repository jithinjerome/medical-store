package com.example.medical.store.Sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SalesRepo extends JpaRepository<Sales, Long> {

    @Query("SELECT COALESCE(SUM(s.totalSales), 0) FROM Sales s WHERE s.medicalStore.id = :storeId AND s.saleDate = :date")
    double getDailySales(@Param("storeId") Long storeId, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(s.totalSales), 0) FROM Sales s WHERE s.medicalStore.id = :storeId AND YEAR(s.saleDate) = :year AND MONTH(s.saleDate) = :month")
    double getMonthlySales(@Param("storeId") Long storeId, @Param("year") int year, @Param("month") int month);
}
