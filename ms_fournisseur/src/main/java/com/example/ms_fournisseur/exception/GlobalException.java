package com.example.ms_fournisseur.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(FournisseurNotFoundException.class)
    public ResponseEntity<String> FournisseurNotFound(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(FournisseurEmailAllReadyExistException.class)
    public ResponseEntity<String> FournisseurEmailAllReadyExist(Exception e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(FournisseurTelephoneAllReadyExistException.class)
    public ResponseEntity<String> FournisseurTelephoneAllReadyExist(Exception e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> globalException(Exception e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
