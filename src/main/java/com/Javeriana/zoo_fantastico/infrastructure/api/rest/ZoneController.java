package com.Javeriana.zoo_fantastico.infrastructure.api.rest;

import com.Javeriana.zoo_fantastico.domain.entity.Zone;
import com.Javeriana.zoo_fantastico.application.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints de la API para la entidad {@link Zone}.
 *
 * <p>Base URL: {@code /api/zonas}</p>
 *
 * <ul>
 *   <li>{@code GET    /api/zonas}      → listar todas las zonas</li>
 *   <li>{@code GET    /api/zonas/{id}} → obtener una zona por ID</li>
 *   <li>{@code POST   /api/zonas}      → crear una nueva zona</li>
 *   <li>{@code PUT    /api/zonas/{id}} → actualizar una zona existente</li>
 *   <li>{@code DELETE /api/zonas/{id}} → eliminar una zona</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/zonas")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    /**
     * Obtiene la lista completa de zonas.
     *
     * @return {@code 200 OK} con la lista de zonas.
     */
    @GetMapping
    public List<Zone> getAllZones() {
        return zoneService.getAllZones();
    }

    /**
     * Obtiene una zona específica por su ID.
     *
     * @param id identificador de la zona.
     * @return {@code 200 OK} con la zona, o {@code 404 Not Found}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZoneById(@PathVariable Long id) {
        return zoneService.getZoneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva zona.
     *
     * @param zone datos de la zona a crear (cuerpo JSON).
     * @return {@code 200 OK} con la zona persistida.
     */
    @PostMapping
    public Zone createZone(@RequestBody Zone zone) {
        return zoneService.createZone(zone);
    }

    /**
     * Actualiza los datos de una zona existente.
     *
     * @param id          identificador de la zona a actualizar.
     * @param zoneDetails datos actualizados (cuerpo JSON).
     * @return {@code 200 OK} con la zona actualizada, o {@code 404 Not Found}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Zone> updateZone(@PathVariable Long id,
                                           @RequestBody Zone zoneDetails) {
        Zone updatedZone = zoneService.updateZone(id, zoneDetails);
        if (updatedZone != null) {
            return ResponseEntity.ok(updatedZone);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Elimina una zona por su ID.
     *
     * <p>Retorna {@code 400 Bad Request} si la zona tiene criaturas asignadas.</p>
     *
     * @param id identificador de la zona a eliminar.
     * @return {@code 204 No Content}, {@code 404 Not Found} o {@code 400 Bad Request}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteZone(@PathVariable Long id) {
        try {
            if (zoneService.deleteZone(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
