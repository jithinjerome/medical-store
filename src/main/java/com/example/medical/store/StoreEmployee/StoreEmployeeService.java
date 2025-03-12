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
        return new StoreEmployeeDTO(
                storeEmployee.getEmployeeId(),
                storeEmployee.getEmployeeName(),
                storeEmployee.getEmployeeAddress(),
                storeEmployee.getEmployeeContactNo()
        );
    }

    public StoreEmployeeDTO addEmployee(StoreEmployeeDTO storeEmployeeDTO) {
        StoreEmployeeDTO newEmployee = new StoreEmployeeDTO();
        newEmployee.setEmployeeName(storeEmployeeDTO.getEmployeeName());
        newEmployee.setEmployeeContactNo(storeEmployeeDTO.getEmployeeContactNo());
        newEmployee.setEmployeeAddress(storeEmployeeDTO.getEmployeeAddress());

        newEmployee = storeEmployeeRepository.save(newEmployee);

    }
    public void removeEmployee(Long id) {
        if(storeEmployeeRepository.existsById(id)){
            storeEmployeeRepository.deleteById(id);
        }else {
            throw new IllegalArgumentException("No Employee found with this ID");
        }
    }
}
