package com.badwallet.api.services.impl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import com.badwallet.api.dtos.WalletCreationRequest;
import com.badwallet.api.dtos.WalletDTO;
import com.badwallet.api.entities.Wallet;
import com.badwallet.api.mappers.WalletMapper;
import com.badwallet.api.repositories.WalletRepository;
import com.badwallet.api.services.WalletService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper; // Ajout du mapper

    public WalletServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
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
}