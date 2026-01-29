package com.example.ms_stock.exception;

public class StockItemsNotFoundException extends RuntimeException {
    public StockItemsNotFoundException(String message) {
        super(message);
    }
}
