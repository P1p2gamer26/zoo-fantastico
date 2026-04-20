package com.Javeriana.zoo_fantastico.service;

import com.Javeriana.zoo_fantastico.model.Creature;
import com.Javeriana.zoo_fantastico.repository.CreatureRepository;
import com.Javeriana.zoo_fantastico.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CreatureService {
    
    @Autowired 
    private CreatureRepository repo;

    public List<Creature> getAllCreatures() { 
        return repo.findAll(); 
    }
    
    public Creature getCreatureById(Long id) { 
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe")); 
    }
    
    public Creature createCreature(Creature c) { 
        return repo.save(c); 
    }
    
    @Transactional
    public Creature updateCreature(Long id, Creature data) {
        Creature e = getCreatureById(id);
        e.setName(data.getName()); 
        e.setSpecies(data.getSpecies());
        e.setSize(data.getSize()); 
        e.setDangerLevel(data.getDangerLevel());
        e.setHealthStatus(data.getHealthStatus());
        if (data.getZona() != null) e.setZona(data.getZona());
        return repo.save(e);
    }
    
    public void deleteCreature(Long id) { 
        repo.deleteById(id); 
    }
}
