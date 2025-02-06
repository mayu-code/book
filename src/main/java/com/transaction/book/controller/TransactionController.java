package com.transaction.book.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.book.dto.requestDTO.NewTransactionRequest;
import com.transaction.book.dto.responseObjects.SuccessResponse;
import com.transaction.book.dto.updateDto.UpdateTransaction;
import com.transaction.book.entities.Customer;
import com.transaction.book.entities.Transaction;
import com.transaction.book.services.serviceImpl.CustomerServiceImpl;
import com.transaction.book.services.serviceImpl.TransactionServiceImpl;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class TransactionController {
    
    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    @PostMapping("/addTransaction")
    public ResponseEntity<SuccessResponse> addTransaction(@RequestBody NewTransactionRequest request){
        SuccessResponse response = new SuccessResponse();
        Customer customer = this.customerServiceImpl.getCustomerById(request.getCustomerId());
        if(customer==null){
            response.setMessage("customer not present !");
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setStatusCode(200);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if((request.isGave()&&request.isGot())||(!request.isGave()&&!request.isGot())){
            response.setMessage("please set amount is gave or got ! you can set only one at a time");
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setStatusCode(200);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    
        try{
            Transaction transaction = new Transaction();
            transaction.setDate(request.getDate());
            transaction.setDetail(request.getDetail());

            if(request.isGave()){
                customer.setAmount(customer.getAmount()+(request.getAmount()*(-1)));
                transaction.setAmount(request.getAmount()*(-1));
            }
            else{
                customer.setAmount(customer.getAmount()+request.getAmount());
                transaction.setAmount(request.getAmount());
            }
            
            customer= this.customerServiceImpl.addCustomer(customer);
            transaction.setBalanceAmount(customer.getAmount());
            transaction.setCustomer(customer);
            this.transactionServiceImpl.addTransaction(transaction);

            response.setMessage("Add Transaction Successfully !");
            response.setHttpStatus(HttpStatus.OK);
            response.setStatusCode(200);
            return ResponseEntity.of(Optional.of(response));
        }catch(Exception e){
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/updateTransaction")
    public ResponseEntity<SuccessResponse> updateTransaction(@RequestBody UpdateTransaction request){
        SuccessResponse response = new SuccessResponse();
        try{

            response.setMessage("transaction update successfully !");
            response.setHttpStatus(HttpStatus.OK);
            response.setStatusCode(200);
            return ResponseEntity.of(Optional.of(response));
        }
        catch(Exception e){
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/deleteTransaction/{id}")
    public ResponseEntity<SuccessResponse> deleteTransaction(@PathVariable("id") long id){
        SuccessResponse response = new SuccessResponse();
        Transaction transaction = this.transactionServiceImpl.getTransactionById(id);
        Customer customer = transaction.getCustomer();
        try{
            customer.setAmount(customer.getAmount()-transaction.getAmount());
            this.customerServiceImpl.addCustomer(customer);
            this.transactionServiceImpl.deleteTransaction(id);

            response.setMessage("transaction deleted successfully !");
            response.setHttpStatus(HttpStatus.OK);
            response.setStatusCode(200);
            return ResponseEntity.of(Optional.of(response));
        }
        catch(Exception e){
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
