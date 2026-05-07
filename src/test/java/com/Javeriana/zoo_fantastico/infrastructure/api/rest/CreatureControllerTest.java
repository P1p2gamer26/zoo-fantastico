package com.Javeriana.zoo_fantastico.infrastructure.api.rest;

import com.Javeriana.zoo_fantastico.application.service.CreatureService;
import com.Javeriana.zoo_fantastico.domain.entity.Creature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreatureController.class)
public class CreatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreatureService creatureService;

    @Autowired
    private ObjectMapper objectMapper;

    private Creature creature;

    @BeforeEach
    void setUp() {
        creature = new Creature();
        creature.setId(1L);
        creature.setName("Grifo");
        creature.setSpecies("Mitológica");
        creature.setDangerLevel(8);
        creature.setHealthStatus("Saludable");
    }

    @Test
    void testGetAllCreatures_ShouldReturnList() throws Exception {
        List<Creature> creatures = Arrays.asList(creature);
        when(creatureService.getAllCreatures()).thenReturn(creatures);

        mockMvc.perform(get("/api/creatures"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Grifo"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetCreatureById_ShouldReturnCreature() throws Exception {
        when(creatureService.getCreatureById(1L)).thenReturn(creature);

        mockMvc.perform(get("/api/creatures/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Grifo"));
    }

    @Test
    void testCreateCreature_ShouldReturnCreated() throws Exception {
        when(creatureService.createCreature(any(Creature.class))).thenReturn(creature);

        mockMvc.perform(post("/api/creatures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creature)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Grifo"));
    }

    @Test
    void testUpdateCreature_ShouldReturnUpdated() throws Exception {
        Creature updatedData = new Creature();
        updatedData.setName("Grifo Real");
        
        when(creatureService.updateCreature(eq(1L), any(Creature.class))).thenReturn(creature);
        creature.setName("Grifo Real");

        mockMvc.perform(put("/api/creatures/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Grifo Real"));
    }

    @Test
    void testDeleteCreature_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/creatures/1"))
                .andExpect(status().isNoContent());
    }
}
