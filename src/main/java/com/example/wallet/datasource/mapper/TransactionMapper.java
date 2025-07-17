package com.example.wallet.datasource.mapper;

import com.example.wallet.datasource.entityModel.TransactionEntity;
import com.example.wallet.datasource.entityModel.TransactionStatus;
import com.example.wallet.web.dto.Transaction;
import com.example.wallet.web.dto.TransactionRequest;

import java.time.LocalDateTime;

public class TransactionMapper {


    public static Transaction RequestToTransaction(TransactionRequest request) {
        return new Transaction(request.getType(), request.getAmount(), request.getWalletId());
    }

    public static TransactionEntity TransactionToTransactionEntity(Transaction transaction, TransactionStatus status) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setType(transaction.getType());
        transactionEntity.setAmount(transaction.getAmount());
        transactionEntity.setTransactionId(transaction.getTransactionId());
        transactionEntity.setTimestamp(LocalDateTime.now());
        transactionEntity.setWalletUuid(transaction.getWalletId());
        transactionEntity.setStatus(status);
        return transactionEntity;
    }
}
