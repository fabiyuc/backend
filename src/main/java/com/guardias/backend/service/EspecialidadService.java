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

    public Optional<List<Especialidad>> findByActivoTrue() {
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

    public boolean activo(Long id) {
        return (especialidadRepository.existsById(id) && especialidadRepository.findById(id).get().isActivo());
    }

    public boolean existsByNombre(String nombre) {
        return especialidadRepository.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return (especialidadRepository.existsByNombre(nombre)
                && especialidadRepository.findByNombre(nombre).get().isActivo());
    }
}
