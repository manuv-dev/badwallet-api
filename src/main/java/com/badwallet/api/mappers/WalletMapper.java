package com.badwallet.api.mappers;

import com.badwallet.api.dtos.WalletDTO;
import com.badwallet.api.entities.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletDTO toDto(Wallet wallet);
    Wallet toEntity(WalletDTO walletDTO);
}