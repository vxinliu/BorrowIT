package com.example.borrowit.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDuplicateKeyException(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest().body("Cet email est déjà utilisé.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        // Tu peux ici récupérer le message de l'exception
        String detailedMessage = "Erreur : " + ex.getClass().getSimpleName() + " - " + ex.getMessage();
        return ResponseEntity.internalServerError().body(detailedMessage);
    }
}
