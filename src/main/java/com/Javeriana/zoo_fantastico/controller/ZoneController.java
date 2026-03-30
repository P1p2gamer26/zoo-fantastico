package com.Javeriana.zoo_fantastico.controller;

import com.Javeriana.zoo_fantastico.model.Zone;
import com.Javeriana.zoo_fantastico.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zonas")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @GetMapping
    public List<Zone> getAllZones() {
        return zoneService.getAllZones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZoneById(@PathVariable Long id) {
        return zoneService.getZoneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Zone createZone(@RequestBody Zone zone) {
        return zoneService.createZone(zone);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Zone> updateZone(@PathVariable Long id, @RequestBody Zone zoneDetails) {
        Zone updatedZone = zoneService.updateZone(id, zoneDetails);
        if (updatedZone != null) {
            return ResponseEntity.ok(updatedZone);
        }
        return ResponseEntity.notFound().build();
    }

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
