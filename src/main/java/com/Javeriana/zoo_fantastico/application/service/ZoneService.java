package com.Javeriana.zoo_fantastico.application.service;

import com.Javeriana.zoo_fantastico.domain.entity.Zone;
import com.Javeriana.zoo_fantastico.domain.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de la capa de aplicación que encapsula la lógica de negocio
 * relacionada con la entidad {@link Zone}.
 *
 * <p>Centraliza las validaciones de negocio, como impedir la eliminación
 * de una zona que todavía contiene criaturas asignadas.</p>
 */
@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    /**
     * Retorna todas las zonas registradas en el sistema.
     *
     * @return lista de {@link Zone}; nunca {@code null}.
     */
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    /**
     * Busca una zona por su identificador único.
     *
     * @param id identificador de la zona.
     * @return un {@link Optional} con la zona si existe, vacío si no.
     */
    public Optional<Zone> getZoneById(Long id) {
        return zoneRepository.findById(id);
    }

    /**
     * Persiste una nueva zona en la base de datos.
     *
     * @param zone objeto {@link Zone} a guardar.
     * @return la zona persistida con su ID generado.
     */
    public Zone createZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    /**
     * Actualiza los datos de una zona existente.
     *
     * @param id          identificador de la zona a actualizar.
     * @param zoneDetails objeto con los nuevos datos.
     * @return la zona actualizada, o {@code null} si no existe.
     */
    public Zone updateZone(Long id, Zone zoneDetails) {
        return zoneRepository.findById(id).map(zone -> {
            zone.setNombre(zoneDetails.getNombre());
            zone.setDescripcion(zoneDetails.getDescripcion());
            return zoneRepository.save(zone);
        }).orElse(null);
    }

    /**
     * Elimina una zona por su identificador.
     *
     * <p>Lanza una excepción de negocio si la zona aún tiene criaturas
     * asignadas, garantizando integridad referencial a nivel de dominio.</p>
     *
     * @param id identificador de la zona a eliminar.
     * @return {@code true} si se eliminó, {@code false} si no existía.
     * @throws RuntimeException si la zona contiene criaturas asignadas.
     */
    public boolean deleteZone(Long id) {
        return zoneRepository.findById(id).map(zone -> {
            if (zone.getCriaturas() != null && !zone.getCriaturas().isEmpty()) {
                throw new RuntimeException(
                        "No se puede eliminar la zona porque tiene criaturas asignadas.");
            }
            zoneRepository.delete(zone);
            return true;
        }).orElse(false);
    }
}
