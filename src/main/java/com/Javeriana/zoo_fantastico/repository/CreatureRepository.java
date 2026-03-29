package com.Javeriana.zoo_fantastico.repository;

import com.Javeriana.zoo_fantastico.model.Creature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio general para la entidad Creature.
 * Extiende JpaRepository para proporcionar funcionalidades integradas de manipulación
 * de bases de datos mediante Spring Data automatizando el CRUD y la paginación.
 */
@Repository
public interface CreatureRepository extends JpaRepository<Creature, Long> {
}
