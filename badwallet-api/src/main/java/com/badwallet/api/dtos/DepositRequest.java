package com.badwallet.api.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositRequest {
    private Double amount;
    private String paymentMethod;
}