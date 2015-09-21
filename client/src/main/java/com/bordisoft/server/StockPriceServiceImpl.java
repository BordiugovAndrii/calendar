package com.bordisoft.server;

import com.bordisoft.client.MyException;
import com.bordisoft.client.StockPrice;
import com.bordisoft.client.StockPriceService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Random;

public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {
    private static final double MAX_PRICE = 100.0;
    private static final double MAX_PRICE_CHANGE = 0.02;

    @Override
    public StockPrice[] getPrices(String[] symbols) throws MyException {
        Random rnd = new Random();
        StockPrice[] prices = new StockPrice[symbols.length];
        for (int i=0; i<symbols.length; i++) {
            float price = (float)(rnd.nextDouble() * MAX_PRICE);
            float change = (float)(price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f));

            prices[i] = new StockPrice(price, change, symbols[i]);
        }

        return prices;
    }
}
