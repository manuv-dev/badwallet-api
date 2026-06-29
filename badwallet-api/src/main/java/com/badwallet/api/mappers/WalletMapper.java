package com.badwallet.api.mappers;

import com.badwallet.api.dtos.WalletDTO;
import com.badwallet.api.dtos.WalletCreationRequest;
import com.badwallet.api.entities.Wallet;
import org.springframework.stereotype.Component;

@Component 
public class WalletMapper {

    public WalletDTO toDto(Wallet wallet) {
        if (wallet == null) {
            return null;
        }
        return WalletDTO.builder()
                .id(wallet.getId())
                .phoneNumber(wallet.getPhoneNumber())
                .email(wallet.getEmail())
                .balance(wallet.getBalance())
                .code(wallet.getCode())
                .currency(wallet.getCurrency())
                .build();
    }

    public Wallet toEntity(WalletCreationRequest request) {
        if (request == null) {
            return null;
        }
        return Wallet.builder()
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .balance(request.getInitialBalance()) // Fait le lien entre initialBalance et balance
                .code(request.getCode())
                .currency(request.getCurrency())
                .build();
    }
}