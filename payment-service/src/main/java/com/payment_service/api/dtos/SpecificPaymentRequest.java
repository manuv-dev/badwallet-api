package com.payment_service.api.dtos;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificPaymentRequest {
    private String phoneNumber;
    private String serviceName;
    private List<String> factureReferences;
}