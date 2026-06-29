package com.payment_service.api.controllers;

import com.payment_service.api.dtos.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;
import com.payment_service.api.dtos.SpecificPaymentRequest; 

@RestController
@RequestMapping("/api")
public class PaymentController {

    @PostMapping("/pay-factures")
    public ResponseEntity<Map<String, Object>> processFacturePayment(@RequestBody PaymentRequest request) {
        System.out.println("[Payment-Service] Facture reçue pour : " + request.getPhoneNumber());
        System.out.println("[Payment-Service] Service : " + request.getServiceName() + " | Montant : " + request.getAmount());

        return ResponseEntity.ok(Map.of(
            "status", "SUCCESS",
            "transactionId", UUID.randomUUID().toString(),
            "message", "Facture validée par l'opérateur externe."
        ));
    }

    @PostMapping("/pay-factures-specifiques")
    public ResponseEntity<Map<String, Object>> processSpecificBills(@RequestBody com.payment_service.api.dtos.SpecificPaymentRequest request) {
        System.out.println("[Payment-Service] Traitement de " + request.getFactureReferences().size() + " factures pour le service " + request.getServiceName());
        
        return ResponseEntity.ok(Map.of(
            "status", "SUCCESS",
            "message", "Les factures " + request.getFactureReferences() + " ont été archivées et payées."
        ));
    }
}