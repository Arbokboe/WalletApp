package com.example.wallet.messaging;

import com.example.wallet.datasource.entityModel.TransactionEntity;
import com.example.wallet.datasource.entityModel.TransactionStatus;
import com.example.wallet.datasource.entityModel.TransactionType;
import com.example.wallet.datasource.entityModel.WalletEntity;
import com.example.wallet.datasource.mapper.TransactionMapper;
import com.example.wallet.datasource.service.TransactionRepositoryService;
import com.example.wallet.datasource.service.WalletRepositoryService;
import com.example.wallet.web.dto.Transaction;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.wallet.messaging.TransactionMessageProducer.TRANSACTION_QUEUE;

@Data
@Component
@RequiredArgsConstructor
public class TransactionMessageConsumer {

    private final WalletRepositoryService walletRepositoryService;
    private final TransactionRepositoryService transactionRepositoryService;

    @RabbitListener(queues = TRANSACTION_QUEUE, concurrency = "10")
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void handleTransactionRequest(Transaction request) {
        Long startTime = System.nanoTime();
        Optional<TransactionEntity> existingTransaction = transactionRepositoryService.findByTransactionId(request.getTransactionId());
        if (existingTransaction.isPresent()) {
            if (existingTransaction.get().getStatus() == TransactionStatus.COMPLETED || existingTransaction.get().getStatus() == TransactionStatus.PENDING) {
                updateTransactionStatus(existingTransaction.get(), TransactionStatus.DUPLICATE, null);
                return;
            }
        }
        TransactionEntity newTransaction = TransactionMapper.TransactionToTransactionEntity(request, TransactionStatus.PENDING);
        transactionRepositoryService.saveTransaction(newTransaction);
        Optional<WalletEntity> wallet = walletRepositoryService.findByWalletIdWithLock(request.getWalletId());

        try {
            if (request.getType() == TransactionType.WITHDRAW) {
                wallet.get().withdraw(request.getAmount());
            } else {
                wallet.get().deposite(request.getAmount());
            }
            walletRepositoryService.saveWallet(wallet.get());
            updateTransactionStatus(newTransaction, TransactionStatus.COMPLETED, null);
        } catch (IllegalArgumentException e) {
            updateTransactionStatus(newTransaction, TransactionStatus.FAILED, e.getMessage());
        } catch (Exception e) {
            updateTransactionStatus(newTransaction, TransactionStatus.FAILED, "Неизвестная ошибка на сервере: " + e.getMessage());
        }
        Long endTime = System.nanoTime();
        System.out.println(endTime - startTime);
    }

    private void updateTransactionStatus(TransactionEntity transaction, TransactionStatus status, String errorMessage) {
        transaction.setStatus(status);
        transaction.setErrorMessage(errorMessage);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepositoryService.saveTransaction(transaction);
    }
}