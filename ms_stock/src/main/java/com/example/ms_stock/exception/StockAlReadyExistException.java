package com.example.ms_stock.exception;

public class StockAlReadyExistException extends RuntimeException {
    public StockAlReadyExistException(String message) {
        super(message);
    }
}
