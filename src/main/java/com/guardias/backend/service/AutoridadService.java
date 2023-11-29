package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.repository.AutoridadRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AutoridadService {
    
    @Autowired
    AutoridadRepository autoridadRepository;

    public List<Autoridad> list(){
        return autoridadRepository.findAll();
    }

    public Optional<Autoridad> getOne(Long id){
        return autoridadRepository.findById(id);
    }

     public void save(Autoridad autoridad) {
        autoridadRepository.save(autoridad);
    }

    public void delete(Long id) {
        autoridadRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return autoridadRepository.existsById(id);
    }
}