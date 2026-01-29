package com.example.ms_produit.exception;

public class DuplicateProduitException extends RuntimeException{
    public DuplicateProduitException(String message){
        super(message);
    }
}
