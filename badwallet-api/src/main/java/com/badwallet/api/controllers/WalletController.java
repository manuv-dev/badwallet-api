package com.badwallet.api.controllers;

import com.badwallet.api.dtos.WalletCreationRequest;
import com.badwallet.api.dtos.WalletDTO;
import com.badwallet.api.services.WalletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    @PostMapping("")
    public ResponseEntity<WalletDTO> createNewWallet(@RequestBody WalletCreationRequest request) {
        WalletDTO createdWallet = walletService.createWallet(request);
        return new ResponseEntity<>(createdWallet, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<Page<WalletDTO>> listWallets(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<WalletDTO> wallets = walletService.getAllWallets(pageable);
        return ResponseEntity.ok(wallets);
    }
    @GetMapping("/{phoneNumber}")
    public ResponseEntity<WalletDTO> getWalletByPhone(@PathVariable String phoneNumber) {
        WalletDTO wallet = walletService.getWalletByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(wallet);
    }
}