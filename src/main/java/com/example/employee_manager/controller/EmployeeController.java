package com.example.employee_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/employees";
    }

    // READ: form + table (+ optional search)
    @GetMapping("/employees")
    public String list(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("employees", service.search(keyword));
        } else {
            model.addAttribute("employees", service.getAll());
        }
        model.addAttribute("keyword", keyword);
        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new Employee());
        }
        return "employees";
    }

    // CREATE
    @PostMapping("/employees")
    public String create(@Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result, Model model,
                         RedirectAttributes redirect) {
        if (service.emailExists(employee.getEmail())) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }
        if (result.hasErrors()) {
            model.addAttribute("employees", service.getAll());
            return "employees";
        }
        service.save(employee);
        redirect.addFlashAttribute("success", "Employee saved successfully!");
        return "redirect:/employees";
    }

    // Show edit form
    @GetMapping("/employees/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", service.getById(id));
        return "editemployee";
    }

    // UPDATE
    @PostMapping("/employees/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result, RedirectAttributes redirect) {
        if (service.emailUsedByOther(employee.getEmail(), id)) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }
        if (result.hasErrors()) {
            employee.setId(id);
            return "editemployee";
        }
        employee.setId(id);
        service.save(employee);
        redirect.addFlashAttribute("success", "Employee updated successfully!");
        return "redirect:/employees";
    }

    // DELETE
    @PostMapping("/employees/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        service.delete(id);
        redirect.addFlashAttribute("success", "Employee deleted.");
        return "redirect:/employees";
    }
}
