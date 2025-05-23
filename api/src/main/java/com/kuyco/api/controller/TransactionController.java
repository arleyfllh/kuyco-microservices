package com.kuyco.api.controller;

import com.kuyco.api.model.Transaction;
import com.kuyco.api.request.TransactionRequest;
import com.kuyco.api.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.create(request));
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(
            @PathVariable Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String itemCategory
            ) {
        if (startDate == null && endDate == null && itemCategory == null) {
            endDate = LocalDateTime.now();
            startDate = endDate.minusDays(30);
        }

        List<Transaction> result = transactionService.getTransactionHistory(customerId, startDate, endDate, itemCategory);
        return ResponseEntity.ok(result);
    }

}
