package com.kuyco.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {
    private Long customerId;
    private Long itemId;
    private Integer quantity;
}
