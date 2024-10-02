package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/") // Explicitly define GET method for the root path
    public String hello() {
        return "hello";
    }

    @GetMapping("/add") // GET method for showing the registration form
    public String showRegistrationForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "add";
    }

    @PostMapping("/add") // POST method for processing the registration form
    public String processRegistration(@ModelAttribute("customer") Customer customer, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "add";
        }
        customerService.saveCustomer(customer);
        model.addAttribute("message", "Registration successful!");
        return "success";
    }

    @GetMapping("/result") // GET method for showing the result page
    public String showResult(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "result";
    }

    // @GetMapping("/register") // GET method for showing the register page
    // public String registerPage() {
    // return "register";
    // }

    // @PostMapping("/register") // POST method for submitting registration data
    // public String resultData(@ModelAttribute Customer customer, Model model) {
    // customerService.saveCustomer(customer);
    // return "result";
    // }
}
