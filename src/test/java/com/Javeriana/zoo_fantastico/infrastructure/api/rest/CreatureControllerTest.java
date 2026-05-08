package com.Javeriana.zoo_fantastico.infrastructure.api.rest;

import com.Javeriana.zoo_fantastico.application.service.CreatureService;
import com.Javeriana.zoo_fantastico.domain.entity.Creature;
import com.Javeriana.zoo_fantastico.infrastructure.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para {@link CreatureController}.
 *
 * <p>Verifica que los endpoints de criaturas reciben las peticiones correctamente
 * y devuelven las respuestas esperadas, siguiendo el patrón Arrange-Act-Assert.</p>
 */
@WebMvcTest(CreatureController.class)
public class CreatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreatureService creatureService;

    @Autowired
    private ObjectMapper objectMapper;

    private Creature testCreature;

    @BeforeEach
    void setUp() {
        testCreature = new Creature();
        testCreature.setId(1L);
        testCreature.setName("León");
        testCreature.setSpecies("Panthera leo");
        testCreature.setSize(1.8);
        testCreature.setDangerLevel(7);
        testCreature.setHealthStatus("Saludable");
    }

    // ─────────────────────────────────────────────────────────────
    // GET /api/creatures
    // ─────────────────────────────────────────────────────────────

    @Test
    void testGetAllCreatures_ShouldReturn200() throws Exception {
        // ARRANGE
        Creature creature2 = new Creature();
        creature2.setId(2L);
        creature2.setName("Tigre");
        creature2.setSpecies("Panthera tigris");
        creature2.setSize(2.1);
        creature2.setDangerLevel(8);
        creature2.setHealthStatus("En tratamiento");

        when(creatureService.getAllCreatures()).thenReturn(List.of(testCreature, creature2));

        // ACT & ASSERT
        mockMvc.perform(get("/api/creatures")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("León"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Tigre"));
    }

    @Test
    void testGetAllCreatures_ShouldReturnEmptyList() throws Exception {
        // ARRANGE
        when(creatureService.getAllCreatures()).thenReturn(List.of());

        // ACT & ASSERT
        mockMvc.perform(get("/api/creatures")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ─────────────────────────────────────────────────────────────
    // GET /api/creatures/{id}
    // ─────────────────────────────────────────────────────────────

    @Test
    void testGetCreatureById_ShouldReturn200() throws Exception {
        // ARRANGE
        when(creatureService.getCreatureById(1L)).thenReturn(testCreature);

        // ACT & ASSERT
        mockMvc.perform(get("/api/creatures/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("León"));
    }

    @Test
    void testGetCreatureById_ShouldReturn404_WhenNotFound() throws Exception {
        // ARRANGE
        when(creatureService.getCreatureById(999L))
                .thenThrow(new ResourceNotFoundException("Criatura no encontrada con ID: 999"));

        // ACT & ASSERT
        mockMvc.perform(get("/api/creatures/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────────────────────
    // POST /api/creatures
    // ─────────────────────────────────────────────────────────────

    @Test
    void testCreateCreature_ShouldReturn201() throws Exception {
        // ARRANGE
        Creature newCreature = new Creature();
        newCreature.setName("Oso Polar");
        newCreature.setSpecies("Ursus maritimus");
        newCreature.setSize(2.5);
        newCreature.setDangerLevel(6);
        newCreature.setHealthStatus("Saludable");

        Creature savedCreature = new Creature();
        savedCreature.setId(3L);
        savedCreature.setName("Oso Polar");
        savedCreature.setSpecies("Ursus maritimus");
        savedCreature.setSize(2.5);
        savedCreature.setDangerLevel(6);
        savedCreature.setHealthStatus("Saludable");

        when(creatureService.createCreature(any(Creature.class))).thenReturn(savedCreature);

        // ACT & ASSERT
        mockMvc.perform(post("/api/creatures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCreature)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Oso Polar"));
    }

    // ─────────────────────────────────────────────────────────────
    // PUT /api/creatures/{id}
    // ─────────────────────────────────────────────────────────────

    @Test
    void testUpdateCreature_ShouldReturn200() throws Exception {
        // ARRANGE
        Creature updateData = new Creature();
        updateData.setName("León Africano");
        updateData.setSpecies("Panthera leo melanochaita");
        updateData.setSize(2.0);
        updateData.setDangerLevel(8);
        updateData.setHealthStatus("Saludable");

        Creature updatedCreature = new Creature();
        updatedCreature.setId(1L);
        updatedCreature.setName("León Africano");
        updatedCreature.setSpecies("Panthera leo melanochaita");
        updatedCreature.setSize(2.0);
        updatedCreature.setDangerLevel(8);
        updatedCreature.setHealthStatus("Saludable");

        when(creatureService.updateCreature(eq(1L), any(Creature.class)))
                .thenReturn(updatedCreature);

        // ACT & ASSERT
        mockMvc.perform(put("/api/creatures/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("León Africano"));
    }

    @Test
    void testUpdateCreature_ShouldReturn404_WhenNotFound() throws Exception {
        // ARRANGE
        Creature updateData = new Creature();
        updateData.setName("Criatura Inexistente");

        when(creatureService.updateCreature(eq(999L), any(Creature.class)))
                .thenThrow(new ResourceNotFoundException("Criatura no encontrada con ID: 999"));

        // ACT & ASSERT
        mockMvc.perform(put("/api/creatures/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE /api/creatures/{id}
    // ─────────────────────────────────────────────────────────────

    @Test
    void testDeleteCreature_ShouldReturn204() throws Exception {
        // ARRANGE
        doNothing().when(creatureService).deleteCreature(1L);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/creatures/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCreature_ShouldReturn404_WhenNotFound() throws Exception {
        // ARRANGE
        doThrow(new ResourceNotFoundException("Criatura no encontrada con ID: 999"))
                .when(creatureService).deleteCreature(999L);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/creatures/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCreature_ShouldReturn400_WhenCriticalHealth() throws Exception {
        // ARRANGE
        doThrow(new IllegalStateException("No se puede eliminar una criatura en estado crítico."))
                .when(creatureService).deleteCreature(1L);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/creatures/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
