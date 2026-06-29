package com.badwallet.api.services;

public interface WalletService {
    void seedDatabase(int numWallets, int eventsPerWallet);
}