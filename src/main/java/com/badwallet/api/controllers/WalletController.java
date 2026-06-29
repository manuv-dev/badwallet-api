package com.badwallet.api.controllers;

import com.badwallet.api.services.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/seed")
    public ResponseEntity<Map<String, String>> seedWallets(
            @RequestParam int numWallets,
            @RequestParam int eventsPerWallet) {
        
        walletService.seedDatabase(numWallets, eventsPerWallet);
        
        return ResponseEntity.ok(Map.of(
            "status", "Processing",
            "message", "Le seeding de la base de données a été lancé de manière asynchrone."
        ));
    }
}