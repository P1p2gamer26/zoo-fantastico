package com.Javeriana.zoo_fantastico.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando un recurso solicitado no existe en la base de datos.
 *
 * <p>Al estar anotada con {@code @ResponseStatus(HttpStatus.NOT_FOUND)},
 * Spring MVC convierte automáticamente esta excepción en una respuesta
 * HTTP 404 cuando es propagada desde un controlador REST.</p>
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construye la excepción con un mensaje descriptivo del recurso no encontrado.
     *
     * @param message descripción del recurso que no pudo ser localizado.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
