package com.Javeriana.zoo_fantastico.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidad JPA que representa una Criatura del zoológico fantástico.
 * Cada criatura pertenece a una Zona.
 */
@Entity
@Data
public class Creature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre propio de la criatura (ej. "Dragón de Fuego"). */
    @NotBlank(message = "El nombre de la criatura es obligatorio")
    private String name;

    /** Especie taxonómica o fantástica de la criatura. */
    private String species;

    /** Tamaño de la criatura expresado en metros. */
    private double size;

    /** Nivel de peligrosidad de 1 (inofensiva) a 10 (extremadamente peligrosa). */
    @Min(value = 1, message = "El nivel de peligro mínimo es 1")
    @Max(value = 10, message = "El nivel de peligro máximo es 10")
    private int dangerLevel;

    /** Estado de salud actual de la criatura (ej. "Saludable", "En tratamiento"). */
    private String healthStatus;

    /**
     * Zona a la que pertenece esta criatura.
     * Relación Many-to-One: muchas criaturas pueden estar en una sola zona.
     */
    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zona;
}
