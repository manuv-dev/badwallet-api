package com.badwallet.api.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawRequest {
    private String phoneNumber;
    private Double amount;
}