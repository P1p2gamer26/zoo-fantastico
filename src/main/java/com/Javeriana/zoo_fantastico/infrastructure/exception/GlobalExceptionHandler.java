package com.Javeriana.zoo_fantastico.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para los controladores REST.
 *
 * <p>Centraliza el mapeo de excepciones a respuestas HTTP, garantizando
 * respuestas consistentes en toda la API.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja {@link IllegalStateException} lanzada cuando una regla de negocio
     * impide ejecutar la operación (ej. eliminar criatura en estado crítico).
     *
     * @param ex la excepción capturada.
     * @return {@code 400 Bad Request} con el mensaje de error.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
