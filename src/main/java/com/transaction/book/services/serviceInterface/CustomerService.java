package com.transaction.book.services.serviceInterface;

import java.util.List;

import com.transaction.book.entities.Customer;

public interface CustomerService {
    Customer addCustomer(Customer customer);
    Customer getCustomerByMobileNo(String mobileNO);
    Customer getCustomerById(long id);
    List<Customer> getAllCustomers();
    void deleteCusotmer(long id);
    double getTotalGetAmount();
    double getToalGaveAmount();
}
