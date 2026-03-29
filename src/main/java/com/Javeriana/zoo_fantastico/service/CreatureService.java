package com.Javeriana.zoo_fantastico.service;

import com.Javeriana.zoo_fantastico.model.Creature;
import com.Javeriana.zoo_fantastico.repository.CreatureRepository;
import com.Javeriana.zoo_fantastico.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio que contiene la lógica de negocio para gestionar las operaciones sobre criaturas.
 * Actúa como intermediario entre el controlador principal y el repositorio de acceso a bases de datos.
 */
@Service
public class CreatureService {
    private final CreatureRepository creatureRepository;

    /**
     * Constructor para inyección de dependencias.
     * @param creatureRepository Repositorio de persistencia a utilizar por el servicio.
     */
    @Autowired
    public CreatureService(CreatureRepository creatureRepository) {
        this.creatureRepository = creatureRepository;
    }

    /**
     * Valida y crea una nueva criatura guardándola de persistente en la base de datos.
     * @param creature Instancia de criatura a crear conformada por un cuerpo.
     * @return La criatura registrada junto a su ID auto-generado.
     */
    public Creature createCreature(Creature creature) {
        validateCreature(creature);
        return creatureRepository.save(creature);
    }

    /**
     * Extrae un listado general de todas las criaturas registradas en el sistema.
     * @return Lista general compuesta por entidades de Creature.
     */
    public List<Creature> getAllCreatures() {
        return creatureRepository.findAll();
    }

    /**
     * Obtiene una criatura particular mediante su identificador.
     * @param id Número identificador de tipo Long.
     * @return La estructura completa en base de datos.
     * @throws ResourceNotFoundException si la entidad con el ID no logra encontrarse en la DB.
     */
    public Creature getCreatureById(Long id) {
        return creatureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Creature not found"));
    }

    /**
     * Sobrescribe y actualiza la información parcial y metadatos de una criatura registrada.
     * @param id Identificador en DB correspondiente a actualizarse.
     * @param updatedCreature Instancia de criatura que trae las nuevas actualizaciones.
     * @return Entidad ya persistida junto al guardado actualizado.
     */
    public Creature updateCreature(Long id, Creature updatedCreature) {
        validateCreature(updatedCreature);
        Creature creature = getCreatureById(id);
        creature.setName(updatedCreature.getName());
        creature.setSpecies(updatedCreature.getSpecies());
        creature.setSize(updatedCreature.getSize());
        creature.setDangerLevel(updatedCreature.getDangerLevel());
        creature.setHealthStatus(updatedCreature.getHealthStatus());
        return creatureRepository.save(creature);
    }

    /**
     * Elimina una porción específica de criatura basándose en reglas lógicas.
     * Protege el borrado mientras el estado de salud permanezca crítico.
     * @param id Correlativo único de identificación para ser suprimido.
     * @throws IllegalStateException en el caso de tratarse un estado crítico no permitido para eliminación.
     */
    public void deleteCreature(Long id) {
        Creature creature = getCreatureById(id);
        if (!"critical".equals(creature.getHealthStatus())) {
            creatureRepository.delete(creature);
        } else {
            throw new IllegalStateException("Cannot delete a creature in critical health");
        }
    }

    /**
     * Validaciones fundamentales de lógicas empresariales de criaturas.
     * @param creature Estructura objetivo a someter por validación.
     * @throws IllegalArgumentException Para anomalías de atributos, previniendo así un guardado inseguro.
     */
    private void validateCreature(Creature creature) {
        if (creature.getSize() < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        if (creature.getDangerLevel() < 1 || creature.getDangerLevel() > 10) {
            throw new IllegalArgumentException("Danger level must be between 1 and 10");
        }
    }
}
