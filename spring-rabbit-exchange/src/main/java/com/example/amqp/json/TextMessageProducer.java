package com.example.amqp.json;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TextMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(
            "applicationExchange",
            "text-routing-key",
            message
        );
        System.out.println("Text Message Sent: " + message);
    }
} 