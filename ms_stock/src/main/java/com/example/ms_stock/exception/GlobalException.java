package com.example.ms_stock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(ProduitNotFoundException.class)
    public ResponseEntity<String> ProduitNotFound(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ProduitAlReadyExistException.class)
    public ResponseEntity<String> ProduitAllReadyExist(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(QuantiteInssufiantException.class)
    public ResponseEntity<String> QuantiteInssufiant(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<String> StockNotFound(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(StockAlReadyExistException.class)
    public ResponseEntity<String> StockAlReadyExist(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(StockItemsNotFoundException.class)
    public ResponseEntity<String> StockItemsNotFound(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(TypeMouvementDidntExistsException.class)
    public ResponseEntity<String> TypeMouvementDidntExists(Exception e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> Global(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
