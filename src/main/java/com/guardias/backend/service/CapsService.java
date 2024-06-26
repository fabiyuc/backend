package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Caps;
import com.guardias.backend.repository.CapsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CapsService {

    @Autowired
    CapsRepository capsRepository;

    public List<Caps> findByActivoTrue() {
        return capsRepository.findByActivoTrue();
    }

    public List<Caps> findAll() {
        return capsRepository.findAll();
    }

    public Optional<Caps> findById(Long id) {
        return capsRepository.findById(id);
    }

    public Optional<Caps> findByNombre(String nombre) {
        return capsRepository.findByNombre(nombre);
    }

    public void save(Caps caps) {
        capsRepository.save(caps);
    }

    public void deleteById(Long id) {
        capsRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return capsRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return capsRepository.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return (capsRepository.existsByNombre(nombre) && capsRepository.findByNombre(nombre).get().isActivo());
    }

    public boolean activo(Long id) {
        return (capsRepository.existsById(id) && capsRepository.findById(id).get().isActivo());
    }
}
