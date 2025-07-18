package com.example.wallet.service;

import com.example.wallet.datasource.entityModel.WalletEntity;
import com.example.wallet.web.dto.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public interface Wallet {

    WalletEntity createWallet(BigDecimal initialBalance);

    BigDecimal getBalance(UUID walletUuid);

    void processTransaction(Transaction transaction);
}
