package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Efector;
import com.guardias.backend.repository.EfectorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EfectorService {
    @Autowired
    EfectorRepository efectorRepository;

    public List<Efector> list() {
        return efectorRepository.findAll();
    }

    public Optional<Efector> getById(Long id) {
        return efectorRepository.findById(id);
    }

    public Optional<Efector> getEfectorByNombre(String nombre) {
        return efectorRepository.findByNombre(nombre);
    }

    public void save(Efector efector) {
        efectorRepository.save(efector);
    }

    public void deleteById(Long id) {
        efectorRepository.deleteById(id);
    }

    public boolean existById(Long id) {
        return efectorRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return efectorRepository.existsByNombre(nombre);
    }
}
