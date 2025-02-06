package com.transaction.book.dto.requestDTO;

import lombok.Data;

@Data
public class CustomerRequestDto {
    private String name;
    private String mobileNo;
    private String gstinNo;
    private AddressRequest address;
}
