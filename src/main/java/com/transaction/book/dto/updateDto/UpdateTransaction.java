package com.transaction.book.dto.updateDto;

import lombok.Data;

@Data
public class UpdateTransaction {
    private long id;
    private double gaveAmount;
    private double gotAmount;
    private String date;
    private String detail;
}
