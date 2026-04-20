package com.Javeriana.zoo_fantastico.repository;

// Importa nuestro modelo Creature para que el repositorio sepa con qué estructura de datos va a trabajar.
import com.Javeriana.zoo_fantastico.model.Creature;
// Importa la interfaz madre JpaRepository de Spring Data. Al heredar de ella, obtenemos gratis los métodos CRUD.
import org.springframework.data.jpa.repository.JpaRepository;
// Anotación que marca esta interfaz como un componente de acceso a base de datos dentro del entorno de Spring.
import org.springframework.stereotype.Repository;

/**
 * Repositorio general para la entidad Creature.
 * Extiende JpaRepository para proporcionar funcionalidades integradas de manipulación
 * de bases de datos mediante Spring Data automatizando el CRUD y la paginación.
 */
// @Repository: Le dice a Spring Boot "Esta es una herramienta para la base de datos". Al saber esto, Spring te permitirá inyectarla luego usando @Autowired en los servicios.
@Repository
// extends JpaRepository<Creature, Long>: Le estamos dando superpoderes a nuestra interfaz al heredar de JPA. Indicando <Entidad a manejar, Tipo de Dato de su Primary Key (ID)>.
public interface CreatureRepository extends JpaRepository<Creature, Long> {
}
