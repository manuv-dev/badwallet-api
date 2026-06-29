package com.badwallet.api.services;

import com.badwallet.api.dtos.WalletCreationRequest;
import com.badwallet.api.dtos.WalletDTO;

public interface WalletService {
    void seedDatabase(int numWallets, int eventsPerWallet);
    WalletDTO createWallet(WalletCreationRequest request);
}