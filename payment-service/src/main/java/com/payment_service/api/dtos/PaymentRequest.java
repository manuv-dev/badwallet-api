package com.payment_service.api.dtos;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private String phoneNumber;
    private String serviceName;
    private Double amount;
}