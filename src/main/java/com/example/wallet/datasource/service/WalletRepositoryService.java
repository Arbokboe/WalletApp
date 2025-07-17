package com.example.wallet.datasource.service;

import com.example.wallet.datasource.entityModel.WalletEntity;
import com.example.wallet.datasource.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletRepositoryService {

    private final WalletRepository walletRepository;

    public Optional<WalletEntity> findByWalletIdWithLock(UUID walletUuid) {
        return walletRepository.findByWalletIdWithLock(walletUuid);
    }

    public Optional<WalletEntity> findByWalletId(UUID walletUuid) {
        return walletRepository.findByWalletId(walletUuid);
    }

    public WalletEntity saveWallet(WalletEntity wallet) {
        return walletRepository.save(wallet);
    }

    public void deleteWallet(WalletEntity wallet) {
        walletRepository.delete(wallet);
    }
}
