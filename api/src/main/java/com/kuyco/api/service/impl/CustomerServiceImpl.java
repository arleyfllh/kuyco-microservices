package com.kuyco.api.service.impl;

import com.kuyco.api.dto.CustomerDTO;
import com.kuyco.api.producer.CustomerProducer;
import com.kuyco.api.model.Customer;
import com.kuyco.api.repository.CustomerRepository;
import com.kuyco.api.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerProducer customerProducer;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerProducer customerProducer) {
        this.customerRepository = customerRepository;
        this.customerProducer = customerProducer;
    }

    @Override
    public Customer createCustomer(CustomerDTO customer) {
        Customer entity = new Customer();
        if (customer.getName() != null) entity.setName(customer.getName());
        if (customer.getEmail() != null) entity.setEmail(customer.getEmail());
        if (customer.getBalance() != null) entity.setBalance(customer.getBalance());

        entity = customerRepository.save(entity);
        customerProducer.sendCustomerCreated(entity);
        return entity;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer updateCustomer(Long id, CustomerDTO entity) {
        return customerRepository.findById(id).map(customer -> {
            if (entity.getName() != null) customer.setName(entity.getName());
            if (entity.getBalance() != null) customer.setBalance(entity.getBalance());
            if (entity.getEmail() != null) customer.setEmail(entity.getEmail());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found."));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
