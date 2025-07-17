package com.example.wallet.web.controller;

import com.example.wallet.datasource.entityModel.WalletEntity;
import com.example.wallet.exception.WalletNotFoundException;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/createWallet")
    public ResponseEntity<Map<String, String>> createWallet(@RequestParam(required = false, defaultValue = "0.00") BigDecimal initialBalance) {
        WalletEntity newWallet = walletService.createWallet(initialBalance);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("walletUuid", newWallet.getWalletId().toString()));
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<Map<String, String>> getBalance(@PathVariable UUID walletId) {
        try {
            BigDecimal balance = walletService.getBalance(walletId);
            return ResponseEntity.ok(Map.of(walletId.toString(), balance.toString()));
        } catch (WalletNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(walletId.toString(), e.getMessage()));
        }
    }
}


