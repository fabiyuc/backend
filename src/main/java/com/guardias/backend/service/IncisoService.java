package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Inciso;
import com.guardias.backend.repository.IncisoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class IncisoService {
    @Autowired
    IncisoRepository incisoRepository;

    public List<Inciso> list() {
        return incisoRepository.findAll();
    }

    public Optional<Inciso> findById(Long id) {
        return incisoRepository.findById(id);
    }

    public Optional<Inciso> findByNumero(String numero) {
        return incisoRepository.findByNumero(numero);
    }

    public Optional<Inciso> findByDenominacion(String denominacion) {
        return incisoRepository.findByDenominacion(denominacion);
    }

    public boolean existsById(Long id) {
        return incisoRepository.existsById(id);
    }

    public boolean existsByNumero(String numero) {
        return incisoRepository.existsByNumero(numero);
    }

    public boolean existsByDenominacion(String denominacion) {
        return incisoRepository.existsByDenominacion(denominacion);
    }

    public void save(Inciso inciso) {
        incisoRepository.save(inciso);
    }

    public void deleteById(Long id) {
        incisoRepository.deleteById(id);
    }
}
