package com.Javeriana.zoo_fantastico.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entidad JPA que representa una Zona del zoológico fantástico.
 * Cada zona puede contener múltiples criaturas.
 */
@Entity
@Data
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre descriptivo de la zona (ej. "Zona Ártica"). */
    private String nombre;

    /** Descripción del ambiente o características de la zona. */
    private String descripcion;

    /**
     * Lista de criaturas asignadas a esta zona.
     * Se ignora en la serialización JSON para evitar ciclos infinitos.
     */
    @OneToMany(mappedBy = "zona")
    @JsonIgnore
    private List<Creature> criaturas;
}
