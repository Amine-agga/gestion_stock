package com.example.ms_stock.exception;

public class ProduitAlReadyExistException extends RuntimeException {
    public ProduitAlReadyExistException(String message) {
        super(message);
    }
}
