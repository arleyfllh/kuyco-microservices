package com.kuyco.api.repository;

import com.kuyco.api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t " +
            "WHERE t.customer.id = :customerId " +
//            "AND (:startDate IS NULL OR t.createdAt >= :startDate) " +
//            "AND (:endDate IS NULL OR t.createdAt <= :endDate) " +
            "AND t.createdAt BETWEEN CAST(:startDate AS TIMESTAMP) AND CAST(:endDate AS TIMESTAMP) " +
            "AND (:itemCategory IS NULL OR t.item.category.name = :itemCategory)")
    List<Transaction> findTransactionHistory(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("itemCategory") String itemCategory);
}
