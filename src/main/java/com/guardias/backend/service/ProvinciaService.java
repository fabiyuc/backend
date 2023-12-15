package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Provincia;
import com.guardias.backend.repository.ProvinciaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProvinciaService {

    @Autowired
    ProvinciaRepository ProvinciaRepository;

    public List<Provincia> list() {
        return ProvinciaRepository.findAll();
    }

    public Optional<Provincia> getById(Long id) {
        return ProvinciaRepository.findById(id);
    }

    public Optional<Provincia> getByNombre(String nombre) {
        return ProvinciaRepository.findByNombre(nombre);
    }

    public void save(Provincia provincia) {
        ProvinciaRepository.save(provincia);
    }

    public void deleteById(Long id) {
        ProvinciaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return ProvinciaRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return ProvinciaRepository.existsByNombre(nombre);
    }

}