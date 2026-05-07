package com.Javeriana.zoo_fantastico.application.service;

import com.Javeriana.zoo_fantastico.domain.entity.Creature;
import com.Javeriana.zoo_fantastico.domain.repository.CreatureRepository;
import com.Javeriana.zoo_fantastico.infrastructure.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de la capa de aplicación que encapsula la lógica de negocio
 * relacionada con la entidad {@link Creature}.
 *
 * <p>
 * Actúa como intermediario entre los controladores REST (infraestructura)
 * y los repositorios de dominio, garantizando que las reglas de negocio
 * se apliquen de forma centralizada.
 * </p>
 */
@Service
public class CreatureService {

    @Autowired
    private CreatureRepository repo;

    /**
     * Retorna todas las criaturas registradas en el sistema.
     *
     * @return lista de {@link Creature}; nunca {@code null}.
     */
    public List<Creature> getAllCreatures() {
        return repo.findAll();
    }

    /**
     * Busca una criatura por su identificador único.
     *
     * @param id identificador de la criatura.
     * @return la criatura encontrada.
     * @throws ResourceNotFoundException si no existe ninguna criatura con ese ID.
     */
    public Creature getCreatureById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Criatura no encontrada con ID: " + id));
    }

    /**
     * Persiste una nueva criatura en la base de datos.
     *
     * @param c objeto {@link Creature} a guardar.
     * @return la criatura persistida con su ID generado.
     */
    public Creature createCreature(Creature c) {
        return repo.save(c);
    }

    /**
     * Actualiza los datos de una criatura existente.
     *
     * @param id   identificador de la criatura a actualizar.
     * @param data objeto con los nuevos datos.
     * @return la criatura actualizada y persistida.
     * @throws ResourceNotFoundException si no existe ninguna criatura con ese ID.
     */
    @Transactional
    public Creature updateCreature(Long id, Creature data) {
        Creature existing = getCreatureById(id);
        existing.setName(data.getName());
        existing.setSpecies(data.getSpecies());
        existing.setSize(data.getSize());
        existing.setDangerLevel(data.getDangerLevel());
        existing.setHealthStatus(data.getHealthStatus());
        if (data.getZona() != null) {
            existing.setZona(data.getZona());
        }
        return repo.save(existing);
    }

    /**
     * Elimina una criatura por su identificador.
     *
     * @param id identificador de la criatura a eliminar.
     */
    public void deleteCreature(Long id) {
        Creature creature = getCreatureById(id);
        if ("Crítico".equalsIgnoreCase(creature.getHealthStatus())) {
            throw new IllegalStateException("No se puede eliminar una criatura en estado crítico.");
        }
        repo.deleteById(id);
    }
}
