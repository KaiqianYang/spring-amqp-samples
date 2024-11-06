package com.example.amqp.json;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static final String FOO_QUEUE = "sample.inferred.foo";
	public static final String BAR_QUEUE = "sample.inferred.bar";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(jsonConverter());
		return template;
	}

	@Bean
	public MessageConverter jsonConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public Queue foo() {
		return new Queue(FOO_QUEUE);
	}

	@Bean
	public Queue bar() {
		return new Queue(BAR_QUEUE);
	}
}
	