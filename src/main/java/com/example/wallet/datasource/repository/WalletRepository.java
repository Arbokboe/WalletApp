package com.example.wallet.datasource.repository;

import com.example.wallet.datasource.entityModel.WalletEntity;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface WalletRepository extends CrudRepository<WalletEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WalletEntity w WHERE w.walletId = :walletUuid")
    Optional<WalletEntity> findByWalletIdWithLock(@Param("walletUuid") UUID walletUuid);

    Optional<WalletEntity> findByWalletId(UUID walletUuid);
}
