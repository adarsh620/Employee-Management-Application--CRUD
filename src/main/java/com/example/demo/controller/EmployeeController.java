package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeController {

    // display the list of employees

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return findPaginated(1, "firstName", "asc", model);
    }

    @GetMapping("/addNewEmployeeForm")
    public String saveEmployeeForm(Model model){
        Employee employee = new Employee();
        model.addAttribute("employee", employee); // create model attribute to bind form data.
        return "add_employee";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) { // @ModelAttribute is a Spring annotation used to bind form data directly to a model attribute.
        // Save employee to the database
        employeeService.saveEmployee(employee);
        return "redirect:/";
    }

    @GetMapping("/editEmployee/{id}")
    public String editEmployee(@PathVariable long id, Model model){
      // get employees from the service.
        Employee employee = employeeService.getEmployeeById(id);
      //  set employee as the model attribute to pre-populate the form data.
        model.addAttribute("employee", employee);
        return "edit_employee";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable long id){
        this.employeeService.deleteEmployee(id);
        return "redirect:/";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 5;

        Page < Employee > page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List < Employee > listEmployees = page.getContent();
        // paginnation
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        // sorting
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listEmployees", listEmployees);
        return "index";
    }

}

