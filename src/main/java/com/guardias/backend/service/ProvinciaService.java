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

    public Optional<Provincia> getById(int id) {
        return ProvinciaRepository.findById(id);
    }

    public Optional<Provincia> getByNombre(String nombre) {
        return ProvinciaRepository.findByNombre(nombre);
    }

    public void save(Provincia provincia) {
        ProvinciaRepository.save(provincia);
    }

    public void delete(int id) {
        ProvinciaRepository.deleteById(id);
    }

    public boolean existById(int id) {
        return ProvinciaRepository.existsById(id);
    }

    public boolean existByNombre(String nombre) {
        return ProvinciaRepository.existsByNombre(nombre);
    }
}
