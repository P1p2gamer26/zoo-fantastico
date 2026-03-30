package com.Javeriana.zoo_fantastico.service;

import com.Javeriana.zoo_fantastico.model.Zone;
import com.Javeriana.zoo_fantastico.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    public Optional<Zone> getZoneById(Long id) {
        return zoneRepository.findById(id);
    }

    public Zone createZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    public Zone updateZone(Long id, Zone zoneDetails) {
        return zoneRepository.findById(id).map(zone -> {
            zone.setNombre(zoneDetails.getNombre());
            zone.setDescripcion(zoneDetails.getDescripcion());
            return zoneRepository.save(zone);
        }).orElse(null);
    }

    public boolean deleteZone(Long id) {
        return zoneRepository.findById(id).map(zone -> {
            if (zone.getCriaturas() != null && !zone.getCriaturas().isEmpty()) {
                throw new RuntimeException("No se puede eliminar la zona porque tiene criaturas asignadas.");
            }
            zoneRepository.delete(zone);
            return true;
        }).orElse(false);
    }
}
