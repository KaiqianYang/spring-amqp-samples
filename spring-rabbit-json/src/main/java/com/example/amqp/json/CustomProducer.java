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
public class CustomProducer {

    private static final Logger logger = LoggerFactory.getLogger(CustomProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private String json = "{\"bar\" : \"value\" }";

    private Message jsonMessage = MessageBuilder.withBody(json.getBytes())
                                    .andProperties(MessagePropertiesBuilder.newInstance().setContentType("application/json").build())
                                    .build();   

    public void sendJsonMessageToBar1() {
        jsonMessage.getMessageProperties().setHeader("__TypeId__", "bar_1");
        rabbitTemplate.send(Application.BAR_QUEUE, jsonMessage);
        logger.info("Sent JSON message to queue {}: {}", Application.BAR_QUEUE, jsonMessage);
    }

    public void sendJsonMessageToBar2() {
        jsonMessage.getMessageProperties().setHeader("__TypeId__", "bar_2");
        rabbitTemplate.send(Application.BAR_QUEUE, jsonMessage);
        logger.info("Sent JSON message to queue {}: {}", Application.BAR_QUEUE, jsonMessage);
    }

    
}
