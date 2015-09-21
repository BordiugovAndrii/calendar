package com.bordisoft.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Date;

public class MyGwtClass implements EntryPoint {
    private static final int REFRESH_INTERVAL = 5000;
    private static final float MAX_PRICE = 100.00F;
    private static final float MAX_CHANGE = 0.08F;
    private VerticalPanel mainPanel = new VerticalPanel();
    private FlexTable stocksFlexTable = new FlexTable();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private TextBox newSymbolTextBox = new TextBox();
    private Button addStockButton = new Button("Click");
    private Label lastUpdatedLabel = new Label();
    private ArrayList<String> stocks = new ArrayList<String>();
    private StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);
    private Label errorMsgLabel = new Label();

    public void onModuleLoad() {
        stocksFlexTable.setText(0,0, "Symbol");
        stocksFlexTable.setText(0,1, "Price");
        stocksFlexTable.setText(0, 2, "Change");
        stocksFlexTable.setText(0, 3, "Remove");

        stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
        stocksFlexTable.addStyleName("watchList");
        stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");
        stocksFlexTable.setCellPadding(5);

        addPanel.add(newSymbolTextBox);
        addPanel.add(addStockButton);
        addPanel.addStyleName("addPanel");

        errorMsgLabel.setStyleName("errorMessage");
        errorMsgLabel.setVisible(false);

        mainPanel.add(stocksFlexTable);
        mainPanel.add(addPanel);
        mainPanel.add(lastUpdatedLabel);

        RootPanel.get("container").add(mainPanel);

        newSymbolTextBox.setFocus(true);

        addStockButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addStockEvent();
            }
        });

        newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
               if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
                   addStockEvent();
            }
        });

        Timer refreshTimer = new Timer() {
            @Override
            public void run(){
                refreshWatchList();
            }
        };

        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
    }

    private void addStockEvent() {
        int row = stocksFlexTable.getRowCount();
        final String text = newSymbolTextBox.getText().trim();
        final String stock = text.substring(0,1).toUpperCase() + text.substring(1, text.length());

        newSymbolTextBox.setFocus(true);
        newSymbolTextBox.setText("");

        if("" == stock || !stock.matches("^[0-9A-Za-z\\s\\.]{1,100}$")) {
            Window.alert("\"" + text + "\" is an invalid stock");
            return;
        }

        if(stocks.contains(stock))
            return;

        stocks.add(stock);
        stocksFlexTable.setText(row, 0, stock);
        stocksFlexTable.setWidget(row, 2, new Label());
        stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");

        Button deleteStockButton = new Button("x");
        deleteStockButton.addStyleDependentName("remove");
        deleteStockButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int removeIndex = stocks.indexOf(stock);
                stocks.remove(removeIndex);
                stocksFlexTable.removeRow(removeIndex + 1);
            }
        });

        stocksFlexTable.setWidget(row, 3, deleteStockButton);
        stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");

        refreshWatchList();
    }

    private void refreshWatchList() {
       // Initialize the service proxy.
        if (stockPriceSvc == null) {
            stockPriceSvc = GWT.create(StockPriceService.class);
        }

        // Set up the callback object.
        AsyncCallback<StockPrice[]> callback = new AsyncCallback<StockPrice[]>() {
            public void onFailure(Throwable caught) {
                String details = caught.getMessage();
                if (caught instanceof MyException) {
                    details = "'" + ((MyException)caught).getSymbol() + "' has been delisted";
                }
                errorMsgLabel.setText("Error: " + details);
                errorMsgLabel.setVisible(true);
            }

            public void onSuccess(StockPrice[] result) {
                updatePrices(result);
            }
        };

        // Make the call to the stock price service.
        stockPriceSvc.getPrices(stocks.toArray(new String[0]), callback);
    }

    private void updatePrices(StockPrice[] list) {
        if(list.length == 0)
            return;

        for(StockPrice price : list)
            updatePrice(price);

        DateTimeFormat dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
        lastUpdatedLabel.setText("Last update: " + dateFormat.format(new Date()));
        errorMsgLabel.setVisible(false);
    }

    private void updatePrice(StockPrice price) {
        if(!stocks.contains(price.getStock()))
            return;

        int row = stocks.indexOf(price.getStock()) + 1;
        String priceText = NumberFormat.getFormat("#,##0.00").format(price.getPrice());
        String changeText = NumberFormat.getFormat("+#,##0.00;-#,##0.00").format(price.getChange());
        String percentText = NumberFormat.getFormat("+#,##0.00;-#,##0.00").format(price.getChangePercent());

        stocksFlexTable.setText(row, 1, priceText);
        Label changeWidget = (Label)stocksFlexTable.getWidget(row, 2);
        changeWidget.setText(changeText + " (" + percentText + "%)");

        String changeStyleName = "noChange";
        if (price.getChangePercent() < -0.1f)
            changeStyleName = "negativeChange";
        else if (price.getChangePercent() > 0.1f)
            changeStyleName = "positiveChange";
        changeWidget.setStyleName(changeStyleName);
    }
}
