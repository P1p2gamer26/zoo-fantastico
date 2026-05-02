package com.Javeriana.zoo_fantastico.domain.repository;

import com.Javeriana.zoo_fantastico.domain.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link Zone}.
 *
 * <p>Extiende {@link JpaRepository} para heredar automáticamente las
 * operaciones CRUD estándar sin necesidad de implementación manual.
 * Spring Data genera en tiempo de ejecución una implementación concreta
 * de esta interfaz.</p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
}
