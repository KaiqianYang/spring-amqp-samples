package com.example.amqp.json;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String TEXT_QUEUE = "application.text.queue";
    public static final String TRANSACTION_QUEUE = "application.transaction.queue";

    @Bean
    public DirectExchange applicationExchange() {
        return new DirectExchange("applicationExchange");
    }

    @Bean
    public Queue textQueue() {
        return new Queue(TEXT_QUEUE);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(TRANSACTION_QUEUE);
    }


    @Bean
    public Binding textQueueBinding() {
        return BindingBuilder.bind(textQueue())
                .to(applicationExchange())
                .with("text-routing-key");
    }

    @Bean
    public Binding transactionQueueBinding() {
        return BindingBuilder.bind(transactionQueue())
                .to(applicationExchange())
                .with("transaction-routing-key");
    }

    @Bean
    @Autowired
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonConverter());
        return template;
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
