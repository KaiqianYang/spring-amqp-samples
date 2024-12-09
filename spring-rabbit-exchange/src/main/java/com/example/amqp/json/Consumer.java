package com.example.amqp.json;

import java.util.concurrent.CountDownLatch;

import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import com.azure.spring.messaging.implementation.annotation.EnableAzureMessaging;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@EnableAzureMessaging
public class Consumer {
	private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
	private volatile CountDownLatch latch = new CountDownLatch(2);
	

	@ServiceBusListener(destination = "#{@textQueue.name}")
	public void consumeTextMessage(String message) {
		logger.info("Received text message: {}", message);
		this.latch.countDown();
	}

	@ServiceBusListener(destination = "#{@transactionQueue.name}")
	public void consumeTransaction(Transaction transaction) {
		logger.info("Received transaction: ID={}, Amount={}, Account={}",
			transaction.getTransactionId(),
			transaction.getAmount(),
			transaction.getAccountNumber());
		this.latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}