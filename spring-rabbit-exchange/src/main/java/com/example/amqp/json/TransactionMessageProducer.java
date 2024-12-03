package com.example.amqp.json;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TransactionMessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionMessageProducer.class);
    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("10000.00");
    private static final String HIGH_PRIORITY = "high";
    private static final String NORMAL_PRIORITY = "normal";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendTransaction(Transaction transaction) {
        try {
            // Validate and enrich transaction data
            validateAndEnrichTransaction(transaction);

            // Determine message priority based on amount
            String priority = determineTransactionPriority(transaction);

            rabbitTemplate.send(
                "applicationExchange",
                "transaction-routing-key",
                MessageBuilder.withBody(transaction.toString().getBytes())
                    .andProperties(MessagePropertiesBuilder.newInstance().setContentType("application/json").setHeader("priority", priority).build())
                    .build()
            );

        } catch (IllegalArgumentException e) {
            logger.error("Validation failed for transaction: {}", transaction, e);
            throw e;
        } catch (Exception e) {
            logger.error("Failed to process transaction: {}", transaction, e);
            throw new RuntimeException("Transaction processing failed", e);
        }
    }

    private void validateAndEnrichTransaction(Transaction transaction) {
        // Basic validation
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }

        if (transaction.getAccountNumber() == null || transaction.getAccountNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }

        // Enrich transaction with additional data
        if (transaction.getTransactionId() == null) {
            transaction.setTransactionId(UUID.randomUUID().toString());
        }
        
        if (transaction.getTimestamp() == null) {
            transaction.setTimestamp(LocalDateTime.now());
        }

        if (transaction.getStatus() == null) {
            transaction.setStatus("PENDING");
        }
    }

    private String determineTransactionPriority(Transaction transaction) {
        if (transaction.getAmount().compareTo(HIGH_VALUE_THRESHOLD) >= 0) {
            logger.info("High-value transaction detected: {}", transaction.getTransactionId());
            return HIGH_PRIORITY;
        }
        return NORMAL_PRIORITY;
    }
}
