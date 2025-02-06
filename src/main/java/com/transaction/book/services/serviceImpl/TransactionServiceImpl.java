package com.transaction.book.services.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transaction.book.entities.Transaction;
import com.transaction.book.repository.TransactionRepo;
import com.transaction.book.services.serviceInterface.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepo transactionRepo;

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return this.transactionRepo.save(transaction);
    }

    @Override
    public void deleteTransaction(long id) {
        this.transactionRepo.deleteById(id);
        return;
    }

    @Override
    public Transaction getTransactionById(long id) {
        return this.transactionRepo.findById(id).get();
    }
    
}
