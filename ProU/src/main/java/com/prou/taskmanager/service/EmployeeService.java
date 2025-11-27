package com.prou.taskmanager.service;

import com.prou.taskmanager.exception.ResourceNotFoundException;
import com.prou.taskmanager.model.Employee;
import com.prou.taskmanager.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee createEmployee(Employee employee) {
        log.info("Creating new employee with email: {}", employee.getEmail());
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            log.error("Email already in use: {}", employee.getEmail());
            throw new IllegalArgumentException("Email already in use");
        }
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
    }
}
