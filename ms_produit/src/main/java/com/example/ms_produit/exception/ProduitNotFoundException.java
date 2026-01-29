package com.example.ms_produit.exception;

public class ProduitNotFoundException extends RuntimeException{
    public ProduitNotFoundException(String message){
        super(message);
    }
}
