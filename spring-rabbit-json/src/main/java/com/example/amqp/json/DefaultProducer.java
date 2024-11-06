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
public class DefaultProducer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String queueName, String message) {
        try {
            rabbitTemplate.convertAndSend(queueName, message);
            logger.info("Sent message to queue {}: {}", queueName, message);
        } catch (Exception e) {
            logger.error("Failed to send message to queue {}: {}", queueName, message, e);
        }
    }

    public void sendJsonMessageToFoo() {
        String json = "{\"foo\" : \"value\" }";
        try {
            Message jsonMessage = MessageBuilder.withBody(json.getBytes())
                    .andProperties(MessagePropertiesBuilder.newInstance().setContentType("application/json").build())
                    .build();   
            jsonMessage.getMessageProperties().setHeader("__TypeId__", "foo");
            rabbitTemplate.send(Application.FOO_QUEUE, jsonMessage);
            logger.info("Sent JSON message to queue {}: {}", Application.FOO_QUEUE, json);
        } catch (Exception e) {
            logger.error("Failed to send JSON message to queue {}: {}", Application.FOO_QUEUE, json, e);
        }
    }

    
}
