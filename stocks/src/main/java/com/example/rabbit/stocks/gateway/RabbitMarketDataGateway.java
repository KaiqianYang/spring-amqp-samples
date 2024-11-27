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

package com.example.rabbit.stocks.gateway;

import com.example.rabbit.stocks.domain.Quote;
import com.example.rabbit.stocks.domain.Stock;
import com.example.rabbit.stocks.domain.StockExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class RabbitMarketDataGateway implements MarketDataGateway {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMarketDataGateway.class);
	private static final Random random = new Random();
	private final List<MockStock> stocks = new ArrayList<>();
	private final RabbitTemplate rabbitTemplate;
	private static final String MARKET_DATA_EXCHANGE_NAME = "app.stock.marketdata";

	public RabbitMarketDataGateway(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
		initializeStocks();
	}

	private void initializeStocks() {
		stocks.add(new MockStock("AAPL", StockExchange.nasdaq, 255));
		stocks.add(new MockStock("MSFT", StockExchange.nasdaq, 29));
		stocks.add(new MockStock("GOOG", StockExchange.nasdaq, 500));
		stocks.add(new MockStock("IBM", StockExchange.nyse, 130));
	}

	@Override
	public void sendMarketData() {
		Quote quote = generateFakeQuote();
		Stock stock = quote.getStock();
		
		logger.info("Sending market data for {}", stock.getTicker());
		rabbitTemplate.convertAndSend(MARKET_DATA_EXCHANGE_NAME, "app.stock.marketdata", quote);
	}

	private Quote generateFakeQuote() {
		MockStock stock = stocks.get(random.nextInt(stocks.size()));
		String price = stock.randomPrice();
		return new Quote(stock, price);
	}

	private static class MockStock extends Stock {
		private final int basePrice;
		private final DecimalFormat twoPlacesFormat = new DecimalFormat("0.00");

		private MockStock(String ticker, StockExchange stockExchange, int basePrice) {
			super(stockExchange, ticker);
			this.basePrice = basePrice;
		}

		private String randomPrice() {
			return twoPlacesFormat.format(basePrice + Math.abs(random.nextGaussian()));
		}
	}
}
