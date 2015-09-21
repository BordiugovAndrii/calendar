package com.bordisoft.client;

import java.io.Serializable;

public class StockPrice implements Serializable{
    private float price;
    private float change;
    private String stock;

    private StockPrice() {}

    public StockPrice(float price, float change, String stock) {
        this.price = price;
        this.change = change;
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockPrice that = (StockPrice) o;

        if (Float.compare(that.change, change) != 0) return false;
        if (Float.compare(that.price, price) != 0) return false;
        if (stock != null ? !stock.equals(that.stock) : that.stock != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + (change != +0.0f ? Float.floatToIntBits(change) : 0);
        result = 31 * result + (stock != null ? stock.hashCode() : 0);
        return result;
    }

    public float getPrice() {
        return price;
    }

    public double getChangePercent() {
        return this.change / this.price * 100;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
