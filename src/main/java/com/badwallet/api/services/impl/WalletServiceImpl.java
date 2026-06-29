package com.badwallet.api.services.impl;

import com.badwallet.api.entities.Wallet;
import com.badwallet.api.repositories.WalletRepository;
import com.badwallet.api.services.WalletService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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
}