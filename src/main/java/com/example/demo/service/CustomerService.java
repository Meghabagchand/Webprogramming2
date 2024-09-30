package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Customer;
import com.example.demo.repo.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public CustomerService() {
		logger.info("UserServiceImpl created");
	}
    @Autowired
    private CustomerRepository customerRepository;  // Injecting the CustomerRepository for database operations

    // Retrieve all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();  // Return all customers from the database
    }

    // Save a new customer, with password encryption
    public void saveCustomer(Customer customer) {
        // Encrypt the password before saving
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        customerRepository.save(customer);  // Save the customer with the encoded password
    }

    // Retrieve a customer by their ID
    public Customer getCustomerById(int id) {
        return customerRepository.findById(id).orElse(null);  // Return customer by ID or null if not found
    }

    // Update an existing customer's details
    public void updateCustomer(int id, String name, String email, String phone, String password) {
        Customer customer = getCustomerById(id);
        if (customer != null) {
            customer.setName(name);
            customer.setEmail(email);
            if (!password.isEmpty()) {
                customer.setPassword(passwordEncoder.encode(password));  // Encrypt new password if provided
            }
            customerRepository.save(customer);  // Save updated customer details
        }
    }

    // Delete a customer by their ID
    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);  // Delete the customer by their ID
    }
}