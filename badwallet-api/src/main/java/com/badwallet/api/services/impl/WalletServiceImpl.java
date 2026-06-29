package com.badwallet.api.services.impl;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.badwallet.api.dtos.DepositRequest;
import com.badwallet.api.dtos.PaymentRequest;
import com.badwallet.api.dtos.SpecificPaymentRequest;
import com.badwallet.api.dtos.TransferRequest;
import com.badwallet.api.dtos.WalletCreationRequest;
import com.badwallet.api.dtos.WalletDTO;
import com.badwallet.api.dtos.WithdrawRequest;
import com.badwallet.api.entities.Wallet;
import com.badwallet.api.mappers.WalletMapper;
import com.badwallet.api.repositories.WalletRepository;
import com.badwallet.api.services.WalletService;

import jakarta.transaction.Transactional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper; // Ajout du mapper
    private final RestTemplate restTemplate;

    // 2. Modifie ton constructeur pour inclure RestTemplate
    public WalletServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper, RestTemplate restTemplate) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.restTemplate = restTemplate; // Ajouté ici
    }

    @Override
    @Async 
    public void seedDatabase(int numWallets, int eventsPerWallet) {
        
        for (int i = 1; i <= numWallets; i++) {
            String phoneNumber = "+22177" + String.format("%07d", i); 
            String email = "client" + i + "@gmail.com";
            String code = "WLT-" + String.format("%07d", i);

            Wallet wallet = Wallet.builder()
                    .phoneNumber(phoneNumber)
                    .email(email)
                    .balance(new Double("25000.00")) 
                    .code(code)
                    .currency("XOF") 
                    .build();

            walletRepository.save(wallet);
        }
        
    }
    @Override
    public WalletDTO createWallet(WalletCreationRequest request) {
        Wallet walletEntity = walletMapper.toEntity(request);
        
        Wallet savedWallet = walletRepository.save(walletEntity);
        
        return walletMapper.toDto(savedWallet);
    }

    @Override
    public Page<WalletDTO> getAllWallets(Pageable pageable) {
        return walletRepository.findAll(pageable)
                .map(walletMapper::toDto);
    }
    @Override
    public WalletDTO getWalletByPhoneNumber(String phoneNumber) {
        return walletRepository.findByPhoneNumber(phoneNumber)
                .map(walletMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, 
                        "Portefeuille introuvable pour ce numéro de téléphone : " + phoneNumber
                ));
    }
    @Override
    public Map<String, Object> getWalletBalance(String phoneNumber) {
        Wallet wallet = walletRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, 
                        "Portefeuille introuvable pour ce numéro"
                ));
        
        // Retourne une structure JSON propre {"phoneNumber": "...", "balance": ...}
        return Map.of(
            "phoneNumber", wallet.getPhoneNumber(),
            "balance", wallet.getBalance()
        );
    }

    @Override
    @Transactional
    public WalletDTO deposit(Long id, DepositRequest request) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, 
                        "Portefeuille introuvable avec l'ID : " + id
                ));
        
        wallet.setBalance(wallet.getBalance() + request.getAmount());
        
        Wallet updatedWallet = walletRepository.save(wallet);
        
        return walletMapper.toDto(updatedWallet);
    }
    @Override
    @Transactional
    public WalletDTO withdraw(WithdrawRequest request) {
        Wallet wallet = walletRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, 
                        "Portefeuille introuvable pour ce numéro"
                ));

        double fees = request.getAmount() * 0.01;
        
        if (fees > 5000.0) {
            fees = 5000.0;
        }

        double totalDeduction = request.getAmount() + fees;

        if (wallet.getBalance() < totalDeduction) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "Solde insuffisant pour effectuer le retrait (Montant + Frais = " + totalDeduction + " XOF)"
            );
        }

        wallet.setBalance(wallet.getBalance() - totalDeduction);
        Wallet updatedWallet = walletRepository.save(wallet);

        return walletMapper.toDto(updatedWallet);
    }
    @Override
    @Transactional 
    public void transfer(TransferRequest request) {
        if (request.getSenderPhone().equals(request.getReceiverPhone())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "Le numéro de l'expéditeur et du destinataire doit être différent."
            );
        }

        Wallet sender = walletRepository.findByPhoneNumber(request.getSenderPhone())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, 
                        "Portefeuille expéditeur introuvable."
                ));

        Wallet receiver = walletRepository.findByPhoneNumber(request.getReceiverPhone())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, 
                        "Portefeuille destinataire introuvable."
                ));

        if (sender.getBalance() < request.getAmount()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "Solde insuffisant pour effectuer le transfert."
            );
        }

        sender.setBalance(sender.getBalance() - request.getAmount());
        receiver.setBalance(receiver.getBalance() + request.getAmount());

        walletRepository.save(sender);
        walletRepository.save(receiver);
    }
    @Override
    @Transactional
    public WalletDTO payBill(PaymentRequest request) {
        Wallet wallet = walletRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, 
                        "Portefeuille introuvable."
                ));

        if (wallet.getBalance() < request.getAmount()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "Solde insuffisant pour régler cette facture."
            );
        }

        String paymentServiceUrl = "http://localhost:8081/api/pay-factures"; 
        
        ResponseEntity<Void> response = restTemplate.postForEntity(paymentServiceUrl, request, Void.class);
        
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, 
                    "Le service externe de paiement a refusé la transaction."
            );
        }

        wallet.setBalance(wallet.getBalance() - request.getAmount());
        Wallet updatedWallet = walletRepository.save(wallet);

        return walletMapper.toDto(updatedWallet);
    }
    @Override
    @Transactional
    public WalletDTO paySpecificBills(SpecificPaymentRequest request) {
        Wallet wallet = walletRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Portefeuille introuvable."));

        double totalAmount = request.getFactureReferences().size() * 10000.0;

        if (wallet.getBalance() < totalAmount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solde insuffisant pour payer ces factures (" + totalAmount + " XOF requis).");
        }

        String paymentServiceUrl = "http://localhost:8081/api/pay-factures-specifiques";
        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(paymentServiceUrl, request, Void.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Le service externe a refusé le règlement.");
            }
        } catch (Exception e) {
            System.out.println("[Simulation] Service distant injoignable, débit forcé en local.");
        }

        wallet.setBalance(wallet.getBalance() - totalAmount);
        return walletMapper.toDto(walletRepository.save(wallet));
    }
}