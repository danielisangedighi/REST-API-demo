package com.example.REST_API.demo.controller;

import com.example.REST_API.demo.model.Employee;
import com.example.REST_API.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class ViewController {

    private final EmployeeRepository employeeRepository;

    public ViewController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Display all employees
    @GetMapping
    public String getAllEmployees(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employee-list"; // This refers to employee-list.html
    }

    // Display an employee
    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable("id") Long id, Model model) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "employee-details"; // This refers to employee-details.html
        } else {
            // Handle the case where the employee is not found
            return "redirect:/employees"; // Redirect to the employee list page
        }
    }


    // Display a form to create a new employee
    @GetMapping("/new")
    public String showCreateEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form"; // This refers to employee-form.html
    }

    // Handle the submission of the new employee form
    @PostMapping
    public String createEmployee(@ModelAttribute("employee") Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    // Display a form to update an existing employee
    @GetMapping("/edit/{id}")
    public String showUpdateEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        model.addAttribute("employee", employee);
        return "employee-form"; // Reuses the form for creating and updating
    }

    // Handle the submission of the updated employee form
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable("id") Long id, @ModelAttribute("employee") Employee updatedEmployee) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            // Update fields
            existingEmployee.setFirstName(updatedEmployee.getFirstName());
            existingEmployee.setLastName(updatedEmployee.getLastName());
            existingEmployee.setRole(updatedEmployee.getRole());
            // Save updated employee
            employeeRepository.save(existingEmployee);
            return "redirect:/employees";
        } else {
            return "redirect:/employees"; // Redirect to the employee list page if not found
        }
    }

    // Delete an employee by ID
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        employeeRepository.delete(employee);
        return "redirect:/employees";
    }
}
