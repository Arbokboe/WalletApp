package com.example.wallet;

import com.example.wallet.datasource.entityModel.TransactionType;
import com.example.wallet.datasource.entityModel.WalletEntity;
import com.example.wallet.service.WalletService;
import com.example.wallet.web.controller.TransactionController;
import com.example.wallet.web.controller.WalletController;
import com.example.wallet.web.dto.TransactionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({WalletController.class, TransactionController.class})
class WalletAppTest {

    @Autowired
    private MockMvc mocMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WalletService walletService;

    WalletEntity walletEntity;

    BigDecimal decimal;

    TransactionRequest transactionRequest;

    String requestBody;

    private static final Logger log = LoggerFactory.getLogger(WalletAppTest.class);

    @BeforeEach
    void SetUp() {
        this.objectMapper = new ObjectMapper();
        this.walletEntity = new WalletEntity();
        this.walletEntity.setWalletId(UUID.randomUUID());
        this.walletEntity.setBalance(BigDecimal.ZERO);
        this.decimal = new BigDecimal("155");
        this.transactionRequest = new TransactionRequest();
        this.transactionRequest.setWalletId(UUID.randomUUID());
        this.transactionRequest.setAmount(decimal);
        this.transactionRequest.setType(TransactionType.DEPOSITE);
        try {
            this.requestBody = objectMapper.writeValueAsString(transactionRequest);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    void testCreateWallet() throws Exception {

        when(walletService.createWallet(BigDecimal.ZERO)).thenReturn(walletEntity);

        mocMvc.perform(post("/api/v1/wallets/createWallet")
                        .param("initialBalance", BigDecimal.ZERO.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walletUuid").exists());
    }

    @Test
    void testGetBalance() throws Exception {

        when(walletService.getBalance(walletEntity.getWalletId())).thenReturn(decimal);

        mocMvc.perform(get("/api/v1/wallets/{walletId}", walletEntity.getWalletId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.%s", walletEntity.getWalletId()).value(decimal.toString()))
                .andExpect(status().isOk());

        verify(walletService).getBalance(walletEntity.getWalletId());
    }

    @Test
    void testPerformTransaction() throws Exception {

        mocMvc.perform(post("/api/v1/wallet/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isAccepted());
    }
}
