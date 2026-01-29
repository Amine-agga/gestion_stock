package com.example.ms_commande.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExcpetion {
    @ExceptionHandler(CommandeNotFoundException.class)
    public ResponseEntity<String> CommandeNotFound(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CommandeStatusException.class)
    public ResponseEntity<String> CommandeStatus(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(FournisseurNotFoundException.class)
    public ResponseEntity<String> FournisseurNotFound(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ImpossibleCrediterException.class)
    public ResponseEntity<String> ImpossibleCrediter(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ImpossibleSupprimerCommandeException.class)
    public ResponseEntity<String> ImpossibleSupprimerCommande(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ImpossibleUpdateException.class)
    public ResponseEntity<String> ImpossibleUpdate(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NotAssocietedProductException.class)
    public ResponseEntity<String> NotAssocietedProduct(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ProduitNotFoundException.class)
    public ResponseEntity<String> ProduitNotFound(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<String> StockNotFound(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> TokenExpired(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> globalException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
