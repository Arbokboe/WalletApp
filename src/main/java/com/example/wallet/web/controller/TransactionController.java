package com.example.wallet.web.controller;

import com.example.wallet.service.Wallet;
import com.example.wallet.web.dto.Transaction;
import com.example.wallet.web.dto.TransactionRequest;
import com.example.wallet.exception.WalletNotFoundException;
import com.example.wallet.datasource.mapper.TransactionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class TransactionController {

    private final Wallet walletService;

    @PostMapping("/transaction")
    public ResponseEntity<String> performTransaction(@Valid @RequestBody TransactionRequest request) {
        try {
            Transaction transaction = TransactionMapper.RequestToTransaction(request);
            walletService.processTransaction(transaction);

            return ResponseEntity.accepted().body("id транзакции: " + transaction.getTransactionId().toString() + " принят в обработку");
        } catch (WalletNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Неизвестная ошибка сервера");
        }
    }
}