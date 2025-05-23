package com.kuyco.api.service.impl;

import com.kuyco.api.model.Customer;
import com.kuyco.api.model.Item;
import com.kuyco.api.model.Transaction;
import com.kuyco.api.producer.TransactionProducer;
import com.kuyco.api.repository.CustomerRepository;
import com.kuyco.api.repository.ItemRepository;
import com.kuyco.api.repository.TransactionRepository;
import com.kuyco.api.request.TransactionRequest;
import com.kuyco.api.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final TransactionProducer transactionProducer;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  ItemRepository itemRepository,
                                  CustomerRepository customerRepository, TransactionProducer transactionProducer) {
        this.transactionRepository = transactionRepository;
        this.itemRepository = itemRepository;
        this.customerRepository = customerRepository;
        this.transactionProducer = transactionProducer;
    }

    @Override
    public Transaction create(TransactionRequest request) {
        try {
            Transaction entity = new Transaction();
            if (request != null) {
                Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                        () -> new RuntimeException("Customer not found.")
                );
                if (customer != null) entity.setCustomer(customer);
                Item item = itemRepository.findById(request.getItemId()).orElseThrow(
                        () -> new RuntimeException("Item not found.")
                );

                entity.setQuantity(request.getQuantity());

                if (item != null) {
                    entity.setItem(item);
                    entity.setTotalPrice(request.getQuantity() * item.getPrice());
                }

            }
            entity = transactionRepository.save(entity);
            transactionProducer.sendTransactionCreated(entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Transaction> getTransactionHistory(Long customerId, LocalDateTime startDate, LocalDateTime endDate, String itemCategory) {
        return transactionRepository.findTransactionHistory(customerId, startDate, endDate, itemCategory);
    }
}
