/*
 * Copyright 2002-2010 the original author or authors.
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

package com.example.rabbit.stocks.ui;

import com.example.rabbit.stocks.domain.Quote;
import com.example.rabbit.stocks.domain.TradeRequest;
import com.example.rabbit.stocks.domain.TradeResponse;
import com.example.rabbit.stocks.gateway.StockServiceGateway;

/**
 * Basic controller for the UI.
 * 
 * @author Mark Pollack
 * @author Mark Fisher
 */
public class StockController {

	private StockPanel stockPanel;

	private StockServiceGateway stockServiceGateway;


	public StockPanel getStockPanel() {
		return stockPanel;
	}

	public void setStockPanel(StockPanel stockPanel) {
		this.stockPanel = stockPanel;
	}

	public StockServiceGateway getStockServiceGateway() {
		return stockServiceGateway;
	}

	public void setStockServiceGateway(StockServiceGateway stockServiceGateway) {
		this.stockServiceGateway = stockServiceGateway;
	}

	// "Actions"

	public void sendTradeRequest(String text) {
		String[] tokens = text.split("\\s");
		String quantityString = tokens[0];
		String ticker = tokens[1];
		int quantity = Integer.parseInt(quantityString);
		TradeRequest tr = new TradeRequest();
		tr.setAccountName("ACCT-123");
		tr.setBuyRequest(true);
		tr.setOrderType("MARKET");
		tr.setTicker(ticker);
		tr.setQuantity(quantity);
		tr.setRequestId("REQ-1");
		tr.setUserName("Joe Trader");
		tr.setUserName("Joe");
		stockServiceGateway.send(tr);		
	}

    public void displayQuote(Quote quote) {
    	if (stockPanel != null) {
    		stockPanel.displayQuote(quote);
    	}
    }

    public void updateTrade(TradeResponse tradeResponse) {
    	stockPanel.update(tradeResponse);
    }

}
