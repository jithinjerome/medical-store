package com.example.medical.store.StoreEmployee;

import com.example.medical.store.User.Role;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
public class StoreEmployeeDTO {
    private Long employeeId;
    private String employeeName;
    private String employeeAddress;
    private String employeeContactNo;
    private Role role;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeContactNo() {
        return employeeContactNo;
    }

    public void setEmployeeContactNo(String employeeContactNo) {
        this.employeeContactNo = employeeContactNo;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
