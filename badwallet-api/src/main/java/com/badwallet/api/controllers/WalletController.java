package com.badwallet.api.controllers;

import com.badwallet.api.dtos.DepositRequest;
import com.badwallet.api.dtos.PaymentRequest;
import com.badwallet.api.dtos.TransferRequest;
import com.badwallet.api.dtos.WalletCreationRequest;
import com.badwallet.api.dtos.WalletDTO;
import com.badwallet.api.dtos.WithdrawRequest;
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
    @GetMapping("/{phoneNumber}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String phoneNumber) {
        Map<String, Object> balanceInfo = walletService.getWalletBalance(phoneNumber);
        return ResponseEntity.ok(balanceInfo);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<WalletDTO> makeDeposit(
            @PathVariable Long id, 
            @RequestBody DepositRequest request) {
        WalletDTO updatedWallet = walletService.deposit(id, request);
        return ResponseEntity.ok(updatedWallet);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WalletDTO> makeWithdraw(@RequestBody WithdrawRequest request) {
        WalletDTO updatedWallet = walletService.withdraw(request);
        return ResponseEntity.ok(updatedWallet);
    }
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> makeTransfer(@RequestBody TransferRequest request) {
        walletService.transfer(request);
        return ResponseEntity.ok(Map.of("message", "Transfert effectué avec succès !"));
    }

    @PostMapping("/pay")
    public ResponseEntity<WalletDTO> payBill(@RequestBody PaymentRequest request) {
        WalletDTO updatedWallet = walletService.payBill(request);
        return ResponseEntity.ok(updatedWallet);
    }
}