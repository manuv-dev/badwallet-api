package com.badwallet.api.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletCreationRequest {
    private String phoneNumber;
    private String email;
    private Double initialBalance;
    private String code;
    private String currency;
}