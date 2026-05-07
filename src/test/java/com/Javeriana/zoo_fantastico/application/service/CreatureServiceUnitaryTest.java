package com.Javeriana.zoo_fantastico.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;

import com.Javeriana.zoo_fantastico.domain.entity.Creature;
import java.util.Optional;
import com.Javeriana.zoo_fantastico.domain.repository.CreatureRepository;

@ExtendWith(MockitoExtension.class) // habilitamos moks
public class CreatureServiceUnitaryTest {

    @Mock // creamos una instancia simulada del repositorio
    private CreatureRepository creatureRepository;

    @InjectMocks // inyectamos dependencias en el servicio
    private CreatureService creatureService;

    @Test // anota que vamos a hacer una prueba unitaria
    void testCreateCreature_ShouldReturnSavedCreature() {
        // ARRANGE
        // Preparamos los datos que vamos a usar
        Creature creature = new Creature();
        creature.setName("Fiesto");

        // Creamos el comportamiento del repositorio para cuando el servicio llame al
        // método del repositorio mock que inyectamos dentro del repositorio
        when(creatureRepository.save(any(Creature.class))).thenReturn(creature);

        // ACT aca probamos el método que queremos testear, aca el método createCreature
        // , llama el método mock que creamos previamente
        Creature savedCreature = creatureService.createCreature(creature);

        // ASSERT
        // Nos aseguramos de que halla creado una creatura guardad, y que el nombre sea
        // igual al nombre que creamos
        assertNotNull(savedCreature);
        assertEquals("Fiesto", savedCreature.getName());
    }

    @Test
    void testGetCreatureById_ShouldReturnCreature() {
        // ARRANGE
        // Creamos una creatura
        Creature creature = new Creature();
        long id = 1L;
        creature.setId(id);
        creature.setName("pepe");

        // Simulamos el comportamiento que queremos del repositorio
        when(creatureRepository.findById(id)).thenReturn(Optional.of(creature));

        // ACT
        // Probamos el método que queremos testear trayendo un objeto con un id
        // especifico
        Creature creaturabyid = creatureService.getCreatureById(id);

        // ASSERT
        // Verificamos que los datos de la creatura traida por id sean correctos
        assertNotNull(creaturabyid);
        assertEquals(1L, creaturabyid.getId());
        assertEquals("pepe", creaturabyid.getName());
    }

    @Test
    void testUpdateCreature_ShouldUpdateAndReturnCreature() {
        // ARRANGE
        // Creamos la creatura existente y le asignamos un id
        Long id = 1L;

        Creature existing = new Creature();
        existing.setId(id);
        existing.setName("Viejo");

        Creature data = new Creature();
        data.setName("Nuevo");
        data.setSpecies("Fenix"); // Faltaba setear la especie aquí para que el Assert de abajo funcione

        // traemos una creatura por su id
        when(creatureRepository.findById(id)).thenReturn(Optional.of(existing));
        // guardamos la creatura actualzidaa y devuelve la creatura actualziada
        when(creatureRepository.save(any(Creature.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // ACT
        Creature result = creatureService.updateCreature(id, data);

        // ASSERT
        assertNotNull(result);
        assertEquals("Nuevo", result.getName());
        assertEquals("Fenix", result.getSpecies());
    }

    @Test
    void testDeleteCreature_ShouldDeleteSuccessfully() {
        // ARRANGE
        Long id = 2L;
        Creature existing = new Creature();
        existing.setId(id);
        existing.setHealthStatus("Saludable"); // No es crítico

        // Simulamos que al buscar la criatura (paso previo a borrar), sí la encuentra
        when(creatureRepository.findById(id)).thenReturn(Optional.of(existing));

        // eliminamos la creatura (no debe retornar nada)
        doNothing().when(creatureRepository).deleteById(id);

        // ACT
        creatureService.deleteCreature(id);

        // ASSERT
        // Verificamos matemáticamente que el mock de repositorio SÍ ejecutó su método
        // deleteById
        verify(creatureRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteCreature_WhenHealthStatusIsCritical_ShouldThrowException() {

        // --- ARRANGE ---
        Long id = 1L;

        Creature creature = new Creature();
        creature.setId(id);
        creature.setHealthStatus("Crítico");

        when(creatureRepository.findById(id))
                .thenReturn(Optional.of(creature));

        // --- ACT ---
        // “ejecuta este método y verifica que lance ESTA excepción específica
        Exception exception = assertThrows(IllegalStateException.class, () -> creatureService.deleteCreature(id));

        // --- ASSERT ---
        assertNotNull(exception);

        // verificamos que el metodo deleteById se ejecuto 0 veces en creatureRepository
        verify(creatureRepository, times(0)).deleteById(id);
    }

    // === PRUEBAS DE VALIDACIÓN DEL MODELO ===

    private Validator validator;

    // Esto se ejecuta antes de cada test para preparar el validador
    @BeforeEach
    void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreatureValidation_WhenNameIsBlank_ShouldFail() {
        // --- ARRANGE ---
        Creature invalidCreature = new Creature();
        invalidCreature.setName(""); // Nombre inválido (vacío)
        invalidCreature.setDangerLevel(5); // Peligro válido

        // --- ACT ---
        // Le pasamos la criatura al validador para que revise si rompe alguna regla
        Set<ConstraintViolation<Creature>> violations = validator.validate(invalidCreature);

        // --- ASSERT ---
        // Si hay violaciones, significa que la validación funcionó y bloqueó el objeto.
        // Esperamos que la lista NO esté vacía.
        assertEquals(1, violations.size());
    }

    @Test
    void testCreatureValidation_WhenDangerLevelIsOutOfRange_ShouldFail() {
        // --- ARRANGE ---
        Creature invalidCreature = new Creature();
        invalidCreature.setName("Dragón"); // Nombre válido
        invalidCreature.setDangerLevel(15); // Peligro INVÁLIDO (máximo es 10)

        // --- ACT ---
        Set<ConstraintViolation<Creature>> violations = validator.validate(invalidCreature);

        // --- ASSERT ---
        assertEquals(1, violations.size());

    }

}
