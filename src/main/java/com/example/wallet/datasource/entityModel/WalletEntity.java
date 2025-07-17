package com.example.wallet.datasource.entityModel;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Data
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false, name = "wallet_id")
    private UUID walletId;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;
    private Long version;

    public void withdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void deposite(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}
