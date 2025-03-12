package com.example.medical.store.StoreEmployee;

import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
public class StoreEmployeeDTO {
    private Long employeeId;
    private String employeeName;
    private String employeeAddress;
    private String employeeContactNo;
    public StoreEmployeeDTO(Long employeeId, String employeeName, String employeeAddress, String employeeContactNo) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeAddress = employeeAddress;
        this.employeeContactNo = employeeContactNo;
    }

}
