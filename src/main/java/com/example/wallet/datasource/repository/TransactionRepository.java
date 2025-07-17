package com.example.wallet.datasource.repository;

import com.example.wallet.datasource.entityModel.TransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {

    Optional<TransactionEntity> findByTransactionId(UUID transactionId);
}