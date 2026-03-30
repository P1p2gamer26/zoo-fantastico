package com.Javeriana.zoo_fantastico.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Creature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private double size;
    private int dangerLevel;
    private String healthStatus;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zona; // Hibernate busca este nombre exacto
}
