package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Ministerio;
import com.guardias.backend.repository.MinisterioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MinisterioService {

    @Autowired
    MinisterioRepository ministerioRepository;

    public List<Ministerio> findByActivoTrue() {
        return ministerioRepository.findByActivoTrue();
    }

    public List<Ministerio> findAll() {
        return ministerioRepository.findAll();
    }

    public Optional<Ministerio> findById(Long id) {
        return ministerioRepository.findById(id);
    }

    public Optional<Ministerio> findByNombre(String nombre) {
        return ministerioRepository.findByNombre(nombre);
    }

    public void save(Ministerio ministerio) {
        ministerioRepository.save(ministerio);
    }

    public void deleteById(Long id) {
        ministerioRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return ministerioRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return ministerioRepository.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return (ministerioRepository.existsByNombre(nombre)
                && ministerioRepository.findByNombre(nombre).get().isActivo());
    }

    public boolean activo(Long id) {
        return (ministerioRepository.existsById(id) && ministerioRepository.findById(id).get().isActivo());
    }
}
