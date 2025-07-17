package com.example.wallet.messaging;

import com.example.wallet.web.dto.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TransactionMessageProducer {

    private final RabbitTemplate rabbitTemplate;
    public static final String TRANSACTION_QUEUE = "wallet.transaction.queue";

    public void sendTransactionRequest(Transaction transaction) {
        rabbitTemplate.convertAndSend("", TRANSACTION_QUEUE, transaction);
    }
}