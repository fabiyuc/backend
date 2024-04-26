package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Pais;
import com.guardias.backend.repository.PaisRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaisService {

    @Autowired
    PaisRepository paisRepository;

    public Optional<List<Pais>> findByActivoTrue() {
        return paisRepository.findByActivoTrue();
    }

    public List<Pais> findAll() {
        return paisRepository.findAll();
    }

    public Optional<Pais> findById(Long id) {
        return paisRepository.findById((Long) id);
    }

    public Optional<Pais> findByNombre(String nombre) {
        return paisRepository.findByNombre(nombre);
    }

    public Optional<Pais> findByNacionalidad(String nacionalidad) {
        return paisRepository.findByNacionalidad(nacionalidad);
    }

    public void save(Pais pais) {
        paisRepository.save(pais);
    }

    public void deleteById(Long id) {
        paisRepository.deleteById((Long) id);
    }

    public boolean existsById(Long id) {
        return paisRepository.existsById((Long) id);
    }

    public boolean existsByNombre(String nombre) {
        return paisRepository.existsByNombre(nombre);
    }

    public boolean existsByNacionalidad(String nacionalidad) {
        return paisRepository.existsByNacionalidad(nacionalidad);
    }

    public boolean activoByNombre(String nombre) {
        return (paisRepository.existsByNombre(nombre)
                && paisRepository.findByNombre(nombre).get().isActivo());
    }

    public boolean activo(Long id) {
        return (paisRepository.existsById(id)
                && paisRepository.findById(id).get().isActivo());
    }

}