package com.example.wallet.web.dto;

import com.example.wallet.datasource.entityModel.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Transaction {

    private UUID walletId;

    private TransactionType type;

    private UUID transactionId;

    private BigDecimal amount;

    public Transaction(TransactionType type, BigDecimal amount, UUID walletId) {
        this.transactionId = UUID.randomUUID();
        this.type = type;
        this.amount = amount;
        this.walletId = walletId;
    }
}