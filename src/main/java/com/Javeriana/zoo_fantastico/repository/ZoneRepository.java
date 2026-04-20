package com.Javeriana.zoo_fantastico.repository;

// Importa el modelo (entidad) Zone, definiendo qué información va a consultar el repositorio.
import com.Javeriana.zoo_fantastico.model.Zone;
// Importa JpaRepository que provee de forma automática las sentencias SQL (INSERT, SELECT, UPDATE, DELETE).
import org.springframework.data.jpa.repository.JpaRepository;
// Importa el decorador para señalarle al sistema qué papel juega este archivo.
import org.springframework.stereotype.Repository;

// @Repository: Indica que esta interfaz pertenece a la capa de persistencia/DB. En Spring esto la convierte en un "Bean" que puede ser inyectado donde lo necesites.
@Repository
// extends JpaRepository<Zone, Long>: Literalmente transfiere toda la inteligencia artificial de JPA a este archivo para operar sobre Zonas, sabiendo que su ID es Long. 
public interface ZoneRepository extends JpaRepository<Zone, Long> {
}
