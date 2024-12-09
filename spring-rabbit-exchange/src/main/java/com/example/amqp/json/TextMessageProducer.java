package com.example.amqp.json;

import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.messaging.support.MessageBuilder;

@Service
public class TextMessageProducer {

    @Autowired
    private ServiceBusTemplate serviceBusTemplate;

    @Autowired
    private MQConfig mqConfig;

public void sendTransactionSuccessMessage(String transactionId) {
    String message = "Transaction " + transactionId + " has been processed successfully";
    serviceBusTemplate.send(mqConfig.TEXT_QUEUE, MessageBuilder.withPayload(message).build());
    System.out.println("Transaction Success Message Sent: " + message);
}

public void sendTransactionFailureMessage(String transactionId, String reason) {
    String message = "Transaction " + transactionId + " failed due to: " + reason;
    serviceBusTemplate.send(mqConfig.TEXT_QUEUE, MessageBuilder.withPayload(message).build());
    System.out.println("Transaction Failure Message Sent: " + message);
}

}