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

    public List<Profesion> findByActivo(boolean activo) {
        return profesionRepository.findByActivo(activo);
    }

    public List<Profesion> findAll() {
        return profesionRepository.findAll();
    }

    public List<Profesion> findByAsistencialTrue() {
        return profesionRepository.findByAsistencialTrue();
    }

    public List<Profesion> findByAsistencialFalse() {
        return profesionRepository.findByAsistencialFalse();
    }

    public Optional<Profesion> findById(Long id) {
        return profesionRepository.findById(id);
    }

    public Optional<Profesion> findByNombre(String nombre) {
        return profesionRepository.findByNombre(nombre);
    }

    public void save(Profesion profesion) {
        profesionRepository.save(profesion);
    }

    public void deleteById(Long id) {
        profesionRepository.deleteById(id);
    }

    public Boolean existsById(Long id) {
        return profesionRepository.existsById(id);
    }

    public Boolean existsByNombre(String nombre) {
        return profesionRepository.existsByNombre(nombre);
    }

}
