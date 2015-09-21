package com.bordisoft.client;

import java.io.Serializable;

public class MyException extends Exception implements Serializable{

    private String symbol;

    public MyException() {}

    public MyException(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

}
