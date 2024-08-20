package com.example.REST_API.demo.controller;

import com.example.REST_API.demo.model.Employee;
import com.example.REST_API.demo.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class RESTController {

    private final EmployeeRepository employeeRepository;

    public RESTController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Get all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get an employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
            .map(employee -> ResponseEntity.ok().body(employee))
            .orElse(ResponseEntity.notFound().build());
    }

    // Create a new employee
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // Update an existing employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        return employeeRepository.findById(id)
            .map(employee -> {
                employee.setFirstName(updatedEmployee.getFirstName());
                employee.setLastName(updatedEmployee.getLastName());
                employee.setRole(updatedEmployee.getRole());
                Employee savedEmployee = employeeRepository.save(employee);
                return ResponseEntity.ok().body(savedEmployee);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Delete an employee by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id)
            .map(employee -> {
                employeeRepository.delete(employee);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}

