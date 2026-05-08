package com.Javeriana.zoo_fantastico.infrastructure.api.rest;

import com.Javeriana.zoo_fantastico.domain.entity.Creature;
import com.Javeriana.zoo_fantastico.application.service.CreatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador REST que expone los endpoints de la API para la entidad {@link Creature}.
 *
 * <p>Base URL: {@code /api/creatures}</p>
 *
 * <ul>
 *   <li>{@code GET    /api/creatures}      → listar todas las criaturas</li>
 *   <li>{@code GET    /api/creatures/{id}} → obtener una criatura por ID</li>
 *   <li>{@code POST   /api/creatures}      → crear una nueva criatura</li>
 *   <li>{@code PUT    /api/creatures/{id}} → actualizar una criatura existente</li>
 *   <li>{@code DELETE /api/creatures/{id}} → eliminar una criatura</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/creatures")
public class CreatureController {

    private final CreatureService creatureService;

    @Autowired
    public CreatureController(CreatureService creatureService) {
        this.creatureService = creatureService;
    }

    /**
     * Crea una nueva criatura.
     *
     * @param creature datos de la criatura a crear (cuerpo JSON).
     * @return {@code 201 Created} con la criatura persistida.
     */
    @PostMapping
    public ResponseEntity<Creature> createCreature(@Valid @RequestBody Creature creature) {
        Creature newCreature = creatureService.createCreature(creature);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCreature);
    }

    /**
     * Obtiene la lista completa de criaturas.
     *
     * @return {@code 200 OK} con la lista de criaturas.
     */
    @GetMapping
    public List<Creature> getAllCreatures() {
        return creatureService.getAllCreatures();
    }

    /**
     * Obtiene una criatura específica por su ID.
     *
     * @param id identificador de la criatura.
     * @return {@code 200 OK} con la criatura, o {@code 404 Not Found}.
     */
    @GetMapping("/{id}")
    public Creature getCreatureById(@PathVariable Long id) {
        return creatureService.getCreatureById(id);
    }

    /**
     * Actualiza los datos de una criatura existente.
     *
     * @param id             identificador de la criatura a actualizar.
     * @param updatedCreature datos actualizados (cuerpo JSON).
     * @return {@code 200 OK} con la criatura actualizada.
     */
    @PutMapping("/{id}")
    public Creature updateCreature(@PathVariable Long id,
                                   @Valid @RequestBody Creature updatedCreature) {
        return creatureService.updateCreature(id, updatedCreature);
    }

    /**
     * Elimina una criatura por su ID.
     *
     * @param id identificador de la criatura a eliminar.
     * @return {@code 204 No Content}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreature(@PathVariable Long id) {
        creatureService.deleteCreature(id);
        return ResponseEntity.noContent().build();
    }
}
