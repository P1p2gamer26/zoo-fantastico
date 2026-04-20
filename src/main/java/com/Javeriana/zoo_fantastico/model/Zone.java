package com.Javeriana.zoo_fantastico.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "zona")
    @JsonIgnore // Evita bucle infinito en la serialización JSON (StackOverflow)
    private List<Creature> criaturas;
}
