package com.example.amqp.json;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String TEXT_QUEUE = "application.text.queue";
    public static final String TRANSACTION_QUEUE = "application.transaction.queue";

}