package com.example.medical.store.EmployeeWage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeWageDTO {
    private Long employeeId;
    private Long medicalStoreId;
    private LocalDate paymentDate;
    private double salary;
}
