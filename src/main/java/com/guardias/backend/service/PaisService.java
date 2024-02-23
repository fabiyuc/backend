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

    public List<Pais> list() {
        return paisRepository.findAll();
    }

    public Optional<Pais> findById(Long id) {
        return paisRepository.findById(id);
    }

    public Optional<Pais> findByNombre(String nombre) {
        return paisRepository.findByNombre(nombre);
    }

    public void save(Pais pais) {
        paisRepository.save(pais);
    }

    public void deleteById(Long id) {
        paisRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return paisRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return paisRepository.existsByNombre(nombre);
    }

    public boolean existsByNacionalidad(String nacionalidad) {
        return paisRepository.existsByNacionalidad(nacionalidad);
    }

}