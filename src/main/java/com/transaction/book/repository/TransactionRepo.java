package com.transaction.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transaction.book.entities.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction,Long>{
    
}
