package com.transaction.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.transaction.book.entities.Customer;

public interface CustomerRepo extends JpaRepository<Customer,Long>{
    Customer findByMobileNo(String mobileNo);

    @Query("SELECT c FROM Customer c ORDER BY c.updateDate DESC")
    List<Customer> findAllCustomers();

    @Query("SELECT SUM(c.amount) FROM Customer c WHERE c.amount<0")
    Double getTotalGetAmount();

    @Query("SELECT SUM(c.amount) FROM Customer c WHERE c.amount>0")
    Double getTotalGaveAmount();

    
}
