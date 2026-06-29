package com.badwallet.api.services;

import com.badwallet.api.dtos.DepositRequest;
import com.badwallet.api.dtos.PaymentRequest;
import com.badwallet.api.dtos.SpecificPaymentRequest;
import com.badwallet.api.dtos.TransferRequest;
import com.badwallet.api.dtos.WalletCreationRequest;
import com.badwallet.api.dtos.WalletDTO;
import com.badwallet.api.dtos.WithdrawRequest;

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
    WalletDTO withdraw(WithdrawRequest request);
    void transfer(TransferRequest request);
    WalletDTO payBill(PaymentRequest request);
    WalletDTO paySpecificBills(SpecificPaymentRequest request);
}
