package com.example.employee_manager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public List<Employee> search(String keyword) {
        return repository.findByFullNameContainingIgnoreCase(keyword);
    }

    public Employee getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
    }

    public boolean emailExists(String email) {
        return repository.existsByEmail(email);
    }

    public boolean emailUsedByOther(String email, Long id) {
        return repository.existsByEmailAndIdNot(email, id);
    }

    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}