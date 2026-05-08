package com.Javeriana.zoo_fantastico.infrastructure.api.rest;

import com.Javeriana.zoo_fantastico.application.service.ZoneService;
import com.Javeriana.zoo_fantastico.domain.entity.Zone;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para {@link ZoneController}.
 * 
 * Verifica que los endpoints de zonas reciben las peticiones correctamente
 * y devuelven las respuestas esperadas.
 */
@WebMvcTest(ZoneController.class)
public class ZoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ZoneService zoneService;

    @Autowired
    private ObjectMapper objectMapper;

    private Zone testZone;

    @BeforeEach
    void setUp() {
        testZone = new Zone();
        testZone.setId(1L);
        testZone.setNombre("Zona Ártica");
    }

    @Test
    void testGetAllZones_ShouldReturn200() throws Exception {
        // ARRANGE
        Zone zone2 = new Zone();
        zone2.setId(2L);
        zone2.setNombre("Zona Tropical");

        when(zoneService.getAllZones()).thenReturn(List.of(testZone, zone2));

        // ACT & ASSERT
        mockMvc.perform(get("/api/zonas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Zona Ártica"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre").value("Zona Tropical"));
    }

    @Test
    void testGetAllZones_ShouldReturnEmptyList() throws Exception {
        // ARRANGE
        when(zoneService.getAllZones()).thenReturn(List.of());

        // ACT & ASSERT
        mockMvc.perform(get("/api/zonas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetZoneById_ShouldReturn200() throws Exception {
        // ARRANGE
        when(zoneService.getZoneById(1L)).thenReturn(Optional.of(testZone));

        // ACT & ASSERT
        mockMvc.perform(get("/api/zonas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Zona Ártica"));
    }

    @Test
    void testGetZoneById_ShouldReturn404() throws Exception {
        // ARRANGE
        when(zoneService.getZoneById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        mockMvc.perform(get("/api/zonas/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateZone_ShouldReturn200() throws Exception {
        // ARRANGE
        Zone newZone = new Zone();
        newZone.setNombre("Zona Desértica");

        Zone savedZone = new Zone();
        savedZone.setId(1L);
        savedZone.setNombre("Zona Desértica");

        when(zoneService.createZone(any(Zone.class))).thenReturn(savedZone);

        // ACT & ASSERT
        mockMvc.perform(post("/api/zonas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newZone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Zona Desértica"));
    }

    @Test
    void testUpdateZone_ShouldReturn200() throws Exception {
        // ARRANGE
        Zone updateData = new Zone();
        updateData.setNombre("Zona Ártica Actualizada");

        Zone updatedZone = new Zone();
        updatedZone.setId(1L);
        updatedZone.setNombre("Zona Ártica Actualizada");

        when(zoneService.updateZone(eq(1L), any(Zone.class))).thenReturn(updatedZone);

        // ACT & ASSERT
        mockMvc.perform(put("/api/zonas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Zona Ártica Actualizada"));
    }

    @Test
    void testUpdateZone_ShouldReturn404() throws Exception {
        // ARRANGE
        Zone updateData = new Zone();
        updateData.setNombre("Zona Inexistente");

        when(zoneService.updateZone(eq(999L), any(Zone.class))).thenReturn(null);

        // ACT & ASSERT
        mockMvc.perform(put("/api/zonas/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteZone_ShouldReturn204() throws Exception {
        // ARRANGE
        when(zoneService.deleteZone(1L)).thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/zonas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteZone_ShouldReturn404() throws Exception {
        // ARRANGE
        when(zoneService.deleteZone(999L)).thenReturn(false);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/zonas/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteZone_ShouldReturn400_WhenZoneHasCreatures() throws Exception {
        // ARRANGE
        when(zoneService.deleteZone(1L))
                .thenThrow(new RuntimeException("La zona tiene criaturas asignadas"));

        // ACT & ASSERT
        mockMvc.perform(delete("/api/zonas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("La zona tiene criaturas asignadas"));
    }
}
