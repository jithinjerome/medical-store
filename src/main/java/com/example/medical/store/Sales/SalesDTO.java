package com.example.medical.store.Sales;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesDTO {
    private Long medicalStoreId;
    private LocalDate saleDate;
    private double totalSales;
}