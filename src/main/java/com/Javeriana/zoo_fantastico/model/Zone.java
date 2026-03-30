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

    @OneToMany(mappedBy = "zona") // Coincide con el nombre arriba
    @JsonIgnore
    private List<Creature> criaturas;
}
