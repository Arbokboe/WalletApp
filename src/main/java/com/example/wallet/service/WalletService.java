package com.example.wallet.service;

import com.example.wallet.datasource.entityModel.WalletEntity;
import com.example.wallet.datasource.repository.WalletRepository;
import com.example.wallet.web.dto.Transaction;
import com.example.wallet.exception.WalletNotFoundException;
import com.example.wallet.messaging.TransactionMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionMessageProducer transactionMessageProducer;

    @Transactional
    public WalletEntity createWallet(BigDecimal initialBalance) {
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Начальный баланс должен быть >= 0");
        }
        WalletEntity wallet = new WalletEntity();
        wallet.setWalletId(UUID.randomUUID());
        wallet.setBalance(initialBalance);
        wallet.setVersion(0L);
        return walletRepository.save(wallet);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(UUID walletUuid) {
        return walletRepository.findByWalletId(walletUuid)
                .map(WalletEntity::getBalance)
                .orElseThrow(() -> new WalletNotFoundException("Кошелек не найден"));
    }

    public void processTransaction(Transaction transaction) {
        walletRepository.findByWalletId(transaction.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException("Кошелек не найден"));

        transactionMessageProducer.sendTransactionRequest(transaction);
    }
}