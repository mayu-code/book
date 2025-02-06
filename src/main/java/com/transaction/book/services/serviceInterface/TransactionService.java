package com.transaction.book.services.serviceInterface;

import com.transaction.book.entities.Transaction;

public interface TransactionService {
    Transaction addTransaction(Transaction transaction);
    void deleteTransaction(long id);
    Transaction getTransactionById(long id);

}
