package com.badwallet.api.services;

import com.badwallet.api.dtos.DepositRequest;
import com.badwallet.api.dtos.WalletCreationRequest;
import com.badwallet.api.dtos.WalletDTO;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WalletService {
    void seedDatabase(int numWallets, int eventsPerWallet);
    WalletDTO createWallet(WalletCreationRequest request);
    Page<WalletDTO> getAllWallets(Pageable pageable);
    WalletDTO getWalletByPhoneNumber(String phoneNumber);
    Map<String, Object> getWalletBalance(String phoneNumber);
    WalletDTO deposit(Long id, DepositRequest request);
}
