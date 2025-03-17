package com.example.medical.store.StoreEmployee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreEmployeeService {

    @Autowired
    private StoreEmployeeRepository storeEmployeeRepository;

    public List<StoreEmployeeDTO> getAllEmployees() {
        List<StoreEmployee> storeEmployees = storeEmployeeRepository.findAll();
        return storeEmployees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StoreEmployeeDTO convertToDTO(StoreEmployee storeEmployee){
        StoreEmployeeDTO dto = new StoreEmployeeDTO();
        dto.setEmployeeId(storeEmployee.getEmployeeId());
        dto.setEmployeeName(storeEmployee.getEmployeeName());
        dto.setEmployeeAddress(storeEmployee.getEmployeeAddress());
        dto.setEmployeeContactNo(storeEmployee.getEmployeeContactNo());
        return dto;
    }

    public StoreEmployee addEmployee(StoreEmployeeDTO storeEmployeeDTO) {
        StoreEmployee model = new StoreEmployee();
        model.setEmployeeName(storeEmployeeDTO.getEmployeeName());
        model.setEmployeeContactNo(storeEmployeeDTO.getEmployeeContactNo());
        model.setEmployeeAddress(storeEmployeeDTO.getEmployeeAddress());
        model.setRole(storeEmployeeDTO.getRole());
        return storeEmployeeRepository.save(model);
    }

    public void removeEmployee(Long id) {
        if(storeEmployeeRepository.existsById(id)){
            storeEmployeeRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException("No Employee found with this ID");
        }
    }

    public StoreEmployeeDTO updateEmployee(Long id, StoreEmployeeDTO storeEmployeeDTO) {
        StoreEmployee existingEmployee = storeEmployeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Employee found with this ID"));

        // Update employee fields
        if (storeEmployeeDTO.getEmployeeName() != null) {
            existingEmployee.setEmployeeName(storeEmployeeDTO.getEmployeeName());
        }
        if (storeEmployeeDTO.getEmployeeAddress() != null) {
            existingEmployee.setEmployeeAddress(storeEmployeeDTO.getEmployeeAddress());
        }
        if (storeEmployeeDTO.getEmployeeContactNo() != null) {
            existingEmployee.setEmployeeContactNo(storeEmployeeDTO.getEmployeeContactNo());
        }
        if (storeEmployeeDTO.getRole() != null) {
            existingEmployee.setRole(storeEmployeeDTO.getRole());
        }

        // Save the updated employee to the database
        StoreEmployee updatedEmployee = storeEmployeeRepository.save(existingEmployee);

        return convertToDTO(updatedEmployee);
    }

}
