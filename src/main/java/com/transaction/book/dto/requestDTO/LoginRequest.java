package com.transaction.book.dto.requestDTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String mobileNo;
    private String password;
}
