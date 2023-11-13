package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Profesion;
import com.guardias.backend.repository.ProfesionRepository;

@Service
@Transactional
public class ProfesionService {

    @Autowired
    ProfesionRepository profesionRepository;

    public List<Profesion> list() {
        return profesionRepository.findAll();
    }

    public Optional<Profesion> getOne(Long id) {
        return profesionRepository.findById(id);
    }

    public Optional<Profesion> getByNombre(String nombre) {
        return profesionRepository.findByNombre(nombre);
    }

    public void save(Profesion profesion) {
        profesionRepository.save(profesion);
    }

    public void delete(Long id) {
        profesionRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return profesionRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return profesionRepository.existsByNombre(nombre);
    }

}
