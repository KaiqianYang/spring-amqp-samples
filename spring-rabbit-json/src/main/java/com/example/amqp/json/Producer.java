package com.example.amqp.json;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendJsonMessage(String queueName, String json) {
        try {
            Message jsonMessage = MessageBuilder.withBody(json.getBytes())
                    .andProperties(MessagePropertiesBuilder.newInstance().setContentType("application/json").build())
                    .build();
            rabbitTemplate.send(queueName, jsonMessage);
            logger.info("Sent JSON message to queue {}: {}", queueName, json);
        } catch (Exception e) {
            logger.error("Failed to send JSON message to queue {}: {}", queueName, json, e);
        }
    }

    public void sendMessage(String queueName, String message) {
        try {
            rabbitTemplate.convertAndSend(queueName, message);
            logger.info("Sent message to queue {}: {}", queueName, message);
        } catch (Exception e) {
            logger.error("Failed to send message to queue {}: {}", queueName, message, e);
        }
    }

    public void sendDelayedMessage(String queueName, String message, int delay) {
        try {
            Message delayedMessage = MessageBuilder.withBody(message.getBytes())
                    .andProperties(MessagePropertiesBuilder.newInstance().setHeader("x-delay", delay).build())
                    .build();
            rabbitTemplate.send(queueName, delayedMessage);
            logger.info("Sent delayed message to queue {}: {} with delay {} ms", queueName, message, delay);
        } catch (Exception e) {
            logger.error("Failed to send delayed message to queue {}: {} with delay {} ms", queueName, message, delay, e);
        }
    }
}
