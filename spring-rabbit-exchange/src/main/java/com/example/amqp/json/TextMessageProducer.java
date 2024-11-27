package com.example.amqp.json;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

public void sendTransactionSuccessMessage(String transactionId) {
    String message = "Transaction " + transactionId + " has been processed successfully";
    rabbitTemplate.convertAndSend(
        "applicationExchange",
        "text-routing-key",
        message
    );
    System.out.println("Transaction Success Message Sent: " + message);
}

public void sendTransactionFailureMessage(String transactionId, String reason) {
    String message = "Transaction " + transactionId + " failed due to: " + reason;
    rabbitTemplate.convertAndSend(
        "applicationExchange",
        "text-routing-key",
        message
    );
    System.out.println("Transaction Failure Message Sent: " + message);
}

} 