package com.example.wallet.datasource.service;

import com.example.wallet.datasource.entityModel.TransactionEntity;
import com.example.wallet.datasource.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionRepositoryService {

   private final TransactionRepository transactionRepository;

    public Optional<TransactionEntity> findByTransactionId(UUID transactionId) {
        return transactionRepository.findByTransactionId(transactionId);
    }

    public TransactionEntity saveTransaction(TransactionEntity transactionEntity) {
        return transactionRepository.save(transactionEntity);
    }
}
