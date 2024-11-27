/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.rabbit.stocks.config.client;

import com.example.rabbit.stocks.config.AbstractStockAppRabbitConfiguration;
import com.example.rabbit.stocks.handler.ClientHandler;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitClientConfiguration extends AbstractStockAppRabbitConfiguration {

	private String marketDataRoutingKey = "app.stock.marketdata";

	@Bean
	public Queue marketDataQueue() {
		return new AnonymousQueue();
	}

	@Bean
	public Binding marketDataBinding(Queue marketDataQueue, DirectExchange marketDataExchange) {
		return BindingBuilder.bind(marketDataQueue)
				.to(marketDataExchange)
				.with(marketDataRoutingKey);
	}
}
