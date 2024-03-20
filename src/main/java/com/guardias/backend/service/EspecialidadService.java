package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.entity.Especialidad;
import com.guardias.backend.repository.EspecialidadRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EspecialidadService {

    @Autowired
    EspecialidadRepository especialidadRepository;

    public List<Especialidad> findByActivo() {
        return especialidadRepository.findByActivoTrue();
    }

    public List<Especialidad> findAll() {
        return especialidadRepository.findAll();
    }

    public Optional<Especialidad> findById(Long id) {
        return especialidadRepository.findById(id);
    }

    public Optional<Especialidad> findByNombre(String nombre) {
        return especialidadRepository.findByNombre(nombre);
    }

    public void save(Especialidad especialidad) {
        especialidadRepository.save(especialidad);
    }

    public void deleteById(Long id) {
        especialidadRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return especialidadRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return especialidadRepository.existsByNombre(nombre);
    }
}
