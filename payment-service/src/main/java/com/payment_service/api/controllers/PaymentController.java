package com.payment_service.api.controllers;

import com.payment_service.api.dtos.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @PostMapping("/pay-factures")
    public ResponseEntity<Map<String, Object>> processFacturePayment(@RequestBody PaymentRequest request) {
        // Ces lignes s'afficheront dans ta console pour prouver que l'appel passe bien d'un projet à l'air !
        System.out.println("[Payment-Service] Facture reçue pour : " + request.getPhoneNumber());
        System.out.println("[Payment-Service] Service : " + request.getServiceName() + " | Montant : " + request.getAmount());

        // On renvoie un statut de succès simulé
        return ResponseEntity.ok(Map.of(
            "status", "SUCCESS",
            "transactionId", UUID.randomUUID().toString(),
            "message", "Facture validée par l'opérateur externe."
        ));
    }
}