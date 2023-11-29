package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.repository.LegajoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LegajoService {

    @Autowired
    LegajoRepository legajoRepository;

    public List<Legajo> list(){
        return legajoRepository.findAll();
    }

    public Optional<Legajo> getOne(Long id){
        return legajoRepository.findById(id);
    }

     public void save(Legajo legajo) {
        legajoRepository.save(legajo);
    }

    public void delete(Long id) {
        legajoRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return legajoRepository.existsById(id);
    }
    
}