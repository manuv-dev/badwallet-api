package com.badwallet.api.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
    private String senderPhone;
    private String receiverPhone;
    private Double amount;
}