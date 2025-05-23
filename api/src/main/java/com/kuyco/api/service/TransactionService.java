package com.kuyco.api.service;

import com.kuyco.api.model.Transaction;
import com.kuyco.api.request.TransactionRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Transaction create(TransactionRequest request);

    List<Transaction> getTransactionHistory(Long customerId,
                                            LocalDateTime startDate,
                                            LocalDateTime endDate,
                                            String itemCategory);
}
