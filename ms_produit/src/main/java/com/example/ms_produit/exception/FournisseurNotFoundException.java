package com.example.ms_produit.exception;

public class FournisseurNotFoundException extends RuntimeException {
    public FournisseurNotFoundException(String message) {
        super(message);
    }
}
