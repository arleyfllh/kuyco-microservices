package com.kuyco.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
    private Long customerId;
    private Long itemId;
    private Integer quantity;
    private Double totalPrice;
}
