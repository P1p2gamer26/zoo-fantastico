package com.Javeriana.zoo_fantastico.domain.repository;

import com.Javeriana.zoo_fantastico.domain.entity.Creature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link Creature}.
 *
 * <p>Extiende {@link JpaRepository} para heredar automáticamente las
 * operaciones CRUD estándar (findAll, findById, save, deleteById, etc.)
 * sin necesidad de implementación manual.</p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface CreatureRepository extends JpaRepository<Creature, Long> {
}
