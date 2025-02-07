package com.transaction.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transaction.book.entities.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction,Long>{
    
    @Query("SELECT t FROM Transaction t WHERE t.customer.id=:id  ORDER BY t.date DESC")
    List<Transaction> findByCustomerId(@Param("id") long id);

    @Query("SELECT t FROM Transaction t WHERE t.customer.id=:customerId AND t.date>:date ORDER BY t.date")
    List<Transaction> findAfterTransactions(@Param("customerId") long customerId ,@Param("date") String date);
}
