package com.badwallet.api.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletDTO {
    private Long id;
    private String phoneNumber;
    private String email;
    private Double balance;
    private String code;
    private String currency;
}