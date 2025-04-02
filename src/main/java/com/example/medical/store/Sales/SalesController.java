package com.example.medical.store.Sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/medical-store/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @GetMapping("/daily")
    public ResponseEntity<?> getDailyTotalSales(@RequestParam Long storeId, @RequestParam String date) {
        double totalSales = salesService.getDailyTotalSales(storeId, LocalDate.parse(date));
        return ResponseEntity.ok(Map.of("storeId", storeId, "totalSales", totalSales));
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyTotalSales(@RequestParam Long storeId, @RequestParam int year, @RequestParam int month) {
        double totalSales = salesService.getMonthlyTotalSales(storeId, year, month);
        return ResponseEntity.ok(Map.of("storeId", storeId, "totalSales", totalSales));
    }

    @GetMapping("/expenses/daily")
    public ResponseEntity<?> getDailyTotalExpenses(@RequestParam Long storeId, @RequestParam String date) {
        double totalExpenses = salesService.getDailyTotalExpenses(storeId, LocalDate.parse(date));
        return ResponseEntity.ok(Map.of("storeId", storeId, "totalExpenses", totalExpenses));
    }

    @GetMapping("/expenses/monthly")
    public ResponseEntity<?> getMonthlyTotalExpenses(@RequestParam Long storeId, @RequestParam int year, @RequestParam int month) {
        double totalExpenses = salesService.getMonthlyTotalExpenses(storeId, year, month);
        return ResponseEntity.ok(Map.of("storeId", storeId, "totalExpenses", totalExpenses));
    }
}
