package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.entity.Localidad;
import com.guardias.backend.repository.LocalidadRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LocalidadService {

    @Autowired
    LocalidadRepository localidadRepository;

    public List<Localidad> findByActivo(boolean activo) {
        return localidadRepository.findByActivo(activo);
    }

    public List<Localidad> findAll() {
        return localidadRepository.findAll();
    }

    public Optional<Localidad> findById(Long id) {
        return localidadRepository.findById(id);
    }

    public Optional<Localidad> findByNombre(String nombre) {
        return localidadRepository.findByNombre(nombre);
    }

    public void save(Localidad localidad) {
        localidadRepository.save(localidad);
    }

    public void deleteById(Long id) {
        localidadRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return localidadRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return localidadRepository.existsByNombre(nombre);
    }

}