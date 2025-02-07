package com.transaction.book.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transaction.book.entities.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.customer.id=:id  ORDER BY t.date DESC")
    List<Transaction> findByCustomerId(@Param("id") long id);

    @Query("SELECT t FROM Transaction t WHERE t.customer.id=:customerId AND t.date>:date ORDER BY t.date")
    List<Transaction> findAfterTransactions(@Param("customerId") long customerId ,@Param("date") String date);

    @Query(value = "SELECT * FROM transaction WHERE customer_id = :customerId AND date < :date ORDER BY date DESC LIMIT 1", nativeQuery = true)
    Transaction findPreviousTransaction(@Param("customerId") long customerId, @Param("date") String date);

}
