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
import static org.junit.jupiter.api.Assertions.assertEquals;


import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;

import com.Javeriana.zoo_fantastico.domain.entity.Zone;
import java.util.Optional;
import com.Javeriana.zoo_fantastico.domain.repository.ZoneRepository;
import com.Javeriana.zoo_fantastico.domain.entity.Creature;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CreatureZoneUnitaryTest {
    
    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneService zoneService;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreateZoe_ShouldReturnCreatedZone(){
          
        //ARRANGE
        Long id = 1L;
        Zone zone = new Zone();
        zone.setId(id);
        zone.setNombre("Zona Ártica");


        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);

        //ACT
        Zone createdZone = zoneService.createZone(zone);
        
        //ASSERT
        assertNotNull((createdZone));
        assertEquals(id,createdZone.getId());

    }

    @Test
    void testGetZoneById_ShouldReturnZone(){

        //ARRANGE
        Long id = 1L;
        Zone zone = new Zone();
        zone.setId(id);

        when(zoneRepository.findById(id)).thenReturn(Optional.of(zone));

        //ACT
        Optional<Zone> foundZoneById = zoneService.getZoneById(id);

        //ASSERT
        assertNotNull(foundZoneById);
        assertEquals(id, foundZoneById.get().getId());
    }

    @Test
    void testUpdateZone_ShouldReturnUpdatedZone(){

        //ARRANGE
        Long id = 1L;
        Zone existingZone = new Zone();
        existingZone.setId(id);
        existingZone.setNombre("Zona Ártica");

        Zone dataZone = new Zone();
        dataZone.setNombre("Zona Tropical");

        when(zoneRepository.findById(id)).thenReturn(Optional.of(existingZone));
        when(zoneRepository.save(any(Zone.class))).thenReturn(existingZone);

        //ACT
        Zone updated = zoneService.updateZone(id, dataZone);

        //ASSERT
        assertNotNull(updated);
        assertEquals("Zona Tropical", updated.getNombre());
    }

    @Test
    void testDeleteZone_ShouldDelete(){
        //ARRANGE
        Long id = 1L;
        Zone existingZone = new Zone();
        existingZone.setId(id);

        when(zoneRepository.findById(id)).thenReturn(Optional.of(existingZone));
        doNothing().when(zoneRepository).delete(existingZone);

        //ACT
        zoneService.deleteZone(id);

        //ASSERT
        verify(zoneRepository, times(1)).delete(existingZone);
    }

    @Test
    void testDeleteZone_WithCreatures_ShouldThrowException() {
        // ARRANGE
        Long id = 1L;
        Zone existingZone = new Zone();
        existingZone.setId(id);
        
        // Simular que la zona tiene criaturas
        List<Creature> creatures = new ArrayList<>();
        creatures.add(new Creature());
        existingZone.setCriaturas(creatures);

        when(zoneRepository.findById(id)).thenReturn(Optional.of(existingZone));

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            zoneService.deleteZone(id);
        });

        assertEquals("No se puede eliminar la zona porque tiene criaturas asignadas.", exception.getMessage());
    }
}
