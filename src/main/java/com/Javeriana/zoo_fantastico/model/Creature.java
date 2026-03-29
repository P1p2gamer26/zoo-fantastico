package com.Javeriana.zoo_fantastico.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

/**
 * Representa una Criatura (Creature) dentro del zoológico fantástico.
 * Contiene la información básica y el estado de salud de la criatura.
 */
@Entity
@Data
@NoArgsConstructor
public class Creature {

    /**
     * Identificador único de la criatura. Generado automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la criatura.
     */
    private String name;

    /**
     * Especie a la cual pertenece estar criatura.
     */
    private String species;

    /**
     * Tamaño físico de la criatura (debe ser un valor positivo).
     */
    private double size;

    /**
     * Nivel de peligrosidad de la criatura, calificado en un rango de 1 al 10.
     */
    private int dangerLevel;

    /**
     * Estado de salud actual (ej. "healthy", "critical").
     */
    private String healthStatus;
    
    // WITHOUT Zone
}
