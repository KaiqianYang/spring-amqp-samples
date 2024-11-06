package com.example.amqp.json;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;

@SpringBootApplication
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	private static final String INFERRED_ALPHA_QUEUE = "sample.inferred.alpha";

	private static final String INFERRED_BETA_QUEUE = "sample.inferred.beta";

	private static final String MAPPED_QUEUE = "sample.mapped";

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
		ctx.getBean(Application.class).runDemo(ctx.getBean("rabbitTemplate", RabbitTemplate.class),
				ctx.getBean("jsonRabbitTemplate", RabbitTemplate.class));
		ctx.close();
	}

	private volatile CountDownLatch latch = new CountDownLatch(2);

	public void runDemo(RabbitTemplate rabbitTemplate, RabbitTemplate jsonRabbitTemplate) throws Exception {
		String json = "{\"alpha\" : \"value\", \"beta\" : \"anotherValue\" }";
		Message jsonMessage = MessageBuilder.withBody(json.getBytes())
				.andProperties(MessagePropertiesBuilder.newInstance().setContentType("application/json")
				.build()).build();

		try {
			// inferred type
			rabbitTemplate.send(INFERRED_ALPHA_QUEUE, jsonMessage);
			rabbitTemplate.send(INFERRED_BETA_QUEUE, jsonMessage);
			this.latch.await(10, TimeUnit.SECONDS);


			// Mapped type information with legacy POJO listener
			this.latch = new CountDownLatch(2);
			jsonMessage.getMessageProperties().setHeader("__TypeId__", "alpha");
			rabbitTemplate.send(MAPPED_QUEUE, jsonMessage);
			jsonMessage.getMessageProperties().setHeader("__TypeId__", "beta");
			rabbitTemplate.send(MAPPED_QUEUE, jsonMessage);
			this.latch.await(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Error during runDemo", e);
		}
	}

	@RabbitListener(queues = INFERRED_ALPHA_QUEUE)
	public void listenForAAlpha(Alpha alpha) {
		logger.info("Expected an Alpha, got a {}", alpha);
		this.latch.countDown();
	}

	@RabbitListener(queues = INFERRED_BETA_QUEUE)
	public void listenForABeta(Beta beta) {
		logger.info("Expected a Beta, got a {}", beta);
		this.latch.countDown();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}

	public static class Alpha {

		private String alpha;
		private String beta;

		public Alpha() {
			super();
		}

		public Alpha(String alpha, String beta) {
			this.alpha = alpha;
			this.beta = beta;
		}

		public String getAlpha() {
			return this.alpha;
		}

		public void setAlpha(String alpha) {
			this.alpha = alpha;
		}

		public String getBeta() {
			return this.beta;
		}

		public void setBeta(String beta) {
			this.beta = beta;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + " [alpha=" + this.alpha + ", beta=" + this.beta + "]";
		}

	}

	public static class Beta extends Alpha {

		public Beta() {
			super();
		}

		public Beta(String alpha, String beta) {
			super(alpha, beta);
		}

	}

}