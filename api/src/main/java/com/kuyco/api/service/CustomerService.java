package com.kuyco.api.service;

import com.kuyco.api.dto.CustomerDTO;
import com.kuyco.api.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(CustomerDTO customer);

    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    Customer updateCustomer(Long id, CustomerDTO entity);

    void deleteCustomer(Long id);
}
