package com.example.employee_manager.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee_manager.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    // Task 11 (Challenge)
    List<Employee> findByFullNameContainingIgnoreCase(String keyword);
}
