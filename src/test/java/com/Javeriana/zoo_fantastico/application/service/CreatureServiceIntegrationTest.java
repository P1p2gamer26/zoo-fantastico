package com.Javeriana.zoo_fantastico.application.service;

import com.Javeriana.zoo_fantastico.domain.entity.Creature;
import com.Javeriana.zoo_fantastico.domain.repository.CreatureRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@SpringBootTest
// configuramos la base de datos h2 como base de datos
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreatureServiceIntegrationTest {

    @Autowired // inyectamos el servicio
    private CreatureService creatureService;

    @Autowired // inyectamos el repositorio
    private CreatureRepository creatureRepository;

    @Test
    void testCreateCreature_ShouldPersistInDatabase() {

        // creamos una creatura para probar la persistencia de la misma
        Creature creature = new Creature();
        creature.setName("Unicornio");
        creature.setDangerLevel(5);
        // probamos el metodo que hace que el servicio interactue con la abse de datos
        creatureService.createCreature(creature);
        Long id = creature.getId();
        assertNotNull(id); // verificamos que la creatura tenga un id
        Optional<Creature> foundCreature = creatureRepository.findById(id);
        // verificamos que la creatura exista
        assertTrue(foundCreature.isPresent());
        // verificamos que el nombre de la creatura sea el correcto
        assertEquals("Unicornio", foundCreature.get().getName());
    }

    @Test
    void testUpdateCreature_ShouldBeUpdatedInDB() {
        // Creamos la criatura para que la base de datos le asigne un ID real
        Creature existing = new Creature();
        existing.setName("Dragónviejo");
        existing.setDangerLevel(10);

        Creature data = new Creature();
        data.setName("Dragónnuevo");
        data.setDangerLevel(5);
        // Guardamos y capturamos la criatura persistida (con su ID)
        Creature existingFound = creatureService.createCreature(existing);
        Long id = existingFound.getId();

        assertNotNull(id); // Esto quita el aviso de Null Safety
        // actualizamos la criatura con los nuevos datos usando el ID real
        creatureService.updateCreature(id, data);

        // buscamos la criatura actualizada directamente desde el repositorio
        Optional<Creature> updatedCreature = creatureRepository.findById(id);
        // verificamos que se haya encontrado la creatura actualizada
        assertTrue(updatedCreature.isPresent());
        // verificamos que el nombre de la creatura actualizada sea el correcto
        assertEquals("Dragónnuevo", updatedCreature.get().getName());

    }

    @Test
    void testDeleteCreature_ShouldNotPersist() {
        // Creamos una criatura
        Creature existing = new Creature();
        existing.setName("Criatura ABorrar");
        existing.setDangerLevel(1);

        // guardamos la criatura y obtenemos su ID real
        existing = creatureService.createCreature(existing);
        Long id = existing.getId();
        assertNotNull(id);

        // eliminamos la criatura desde el metodo que interactua con la base de datos
        creatureService.deleteCreature(id);
        // verificamos desde el repositorio que la creatura haya sido borrada
        Optional<Creature> deletedCreature = creatureRepository.findById(id);

        // verificamos que el Optional esté vacío
        assertTrue(deletedCreature.isEmpty());
    }

}
