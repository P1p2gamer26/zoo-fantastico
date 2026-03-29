package com.Javeriana.zoo_fantastico.controller;

import com.Javeriana.zoo_fantastico.model.Creature;
import com.Javeriana.zoo_fantastico.service.CreatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de arquitectura REST que provee interfaces de endpoints web HTTP asociados 
 * al manejo global a peticiones y operaciones a la base de datos de criaturas del sistema.
 */
@RestController
@RequestMapping("/api/creatures")
public class CreatureController {
    private final CreatureService creatureService;

    /**
     * Constructor para inyectabilidad de módulos y servicios.
     * @param creatureService Proveedor intermedio de servicio con el repositorio respectivo.
     */
    @Autowired
    public CreatureController(CreatureService creatureService) {
        this.creatureService = creatureService;
    }

    /**
     * Creado a partir del método POST la información proveniente del frontend 
     * como serialización automática proveniente de web.
     * @param creature Cuerpo deserializado e inyectado.
     * @return Entidad completa bajo protocolo estandarizado de CREATED. 
     */
    @PostMapping
    public ResponseEntity<Creature> createCreature(@RequestBody Creature creature) {
        Creature newCreature = creatureService.createCreature(creature);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCreature);
    }

    /**
     * Punto de acceso principal GET listándolas en general.
     * @return Cúmulo iterativo del sistema en JSON para List de criaturas.
     */
    @GetMapping
    public List<Creature> getAllCreatures() {
        return creatureService.getAllCreatures();
    }

    /**
     * Petición y rastreo por el ID directo incrustado en la URI.
     * @param id Dato serializador a Long de ruta web.
     * @return Contenedor retornado si ocurre efectividad desde la ruta final a extraer.
     */
    @GetMapping("/{id}")
    public Creature getCreatureById(@PathVariable Long id) {
        return creatureService.getCreatureById(id);
    }

    /**
     * Procedimiento PUT sobre actualizador web reescribiendo y recargándolo.
     * @param id Marcador particular.
     * @param updatedCreature Instancia general encapsulada sobre HTTP Request Body.
     * @return Refresca y devulve lo obtenido como respuesta principal.
     */
    @PutMapping("/{id}")
    public Creature updateCreature(@PathVariable Long id, @RequestBody Creature updatedCreature) {
        return creatureService.updateCreature(id, updatedCreature);
    }

    /**
     * Metodología principal DELETE con confirmación de vacío de red exitoso.
     * @param id Componente borrado y evaluado si el estado biológico no es de carácter crítico.
     * @return Respuesta limpia general. 
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreature(@PathVariable Long id) {
        creatureService.deleteCreature(id);
        return ResponseEntity.noContent().build();
    }
}
