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
}
