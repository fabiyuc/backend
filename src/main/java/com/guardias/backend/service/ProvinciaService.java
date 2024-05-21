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

    public Optional<List<Provincia>> findByActivoTrue() {
        return ProvinciaRepository.findByActivoTrue();
    }

    public List<Provincia> findAll() {
        return ProvinciaRepository.findAll();
    }

    public Optional<Provincia> findById(Long id) {
        return ProvinciaRepository.findById((Long) id);
    }

    public Optional<Provincia> findByNombre(String nombre) {
        return ProvinciaRepository.findByNombre(nombre);
    }

    public void save(Provincia provincia) {
        ProvinciaRepository.save(provincia);
    }

    public void deleteById(Long id) {
        ProvinciaRepository.deleteById((Long) id);
    }

    public boolean existsById(Long id) {
        return ProvinciaRepository.existsById((Long) id);
    }

    public boolean activo(Long id) {
        return (ProvinciaRepository.existsById(id) && ProvinciaRepository.findById(id).get().isActivo());
    }

    public boolean existsByNombre(String nombre) {
        return ProvinciaRepository.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return (ProvinciaRepository.existsByNombre(nombre)
                && ProvinciaRepository.findByNombre(nombre).get().isActivo());
    }

}