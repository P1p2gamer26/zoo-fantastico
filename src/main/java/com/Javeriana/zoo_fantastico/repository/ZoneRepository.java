package com.Javeriana.zoo_fantastico.repository;

import com.Javeriana.zoo_fantastico.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
}
