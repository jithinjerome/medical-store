package com.example.medical.store.Sales;

import com.example.medical.store.EmployeeWage.EmployeeWageRepo;
import com.example.medical.store.Expense.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SalesService {

    @Autowired
    private SalesRepo salesRepo;

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private EmployeeWageRepo employeeWageRepo;

    public double getDailyTotalSales(Long medicalStoreId, LocalDate date) {
        return salesRepo.getDailySales(medicalStoreId, date);
    }

    public double getMonthlyTotalSales(Long medicalStoreId, int year, int month) {
        return salesRepo.getMonthlySales(medicalStoreId, year, month);
    }

    public double getDailyTotalExpenses(Long medicalStoreId, LocalDate date) {
        double dailyExpenses = expenseRepo.getTotalExpensesForStore(medicalStoreId, date);
        double dailyWages = employeeWageRepo.getDailyWages(medicalStoreId, date);
        return dailyExpenses + dailyWages;
    }

    public double getMonthlyTotalExpenses(Long medicalStoreId, int year, int month) {
        double monthlyExpenses = expenseRepo.getMonthlyExpensesForStore(medicalStoreId, year, month);
        double monthlyWages = employeeWageRepo.getMonthlyWages(medicalStoreId, year, month);
        return monthlyExpenses + monthlyWages;
    }
}
