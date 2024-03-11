package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.DistribucionOtra;
import com.guardias.backend.repository.DistribucionOtraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionOtraService {

    @Autowired
    DistribucionOtraRepository distribucionOtraRepository;

    public Optional<List<DistribucionOtra>> findByActivoTrue() {
        return distribucionOtraRepository.findByActivoTrue();
    }

    public List<DistribucionOtra> findAll() {
        return distribucionOtraRepository.findAll();
    }

    public Optional<DistribucionOtra> findById(Long id) {
        return distribucionOtraRepository.findById(id);
    }

    public Optional<List<DistribucionOtra>> findByPersonaId(Long personaId) {
        return distribucionOtraRepository.findByPersonaId(personaId);
    }

    public List<DistribucionOtra> findByFechaInicio(LocalDate fechaInicio) {
        return distribucionOtraRepository.findByFechaInicio(fechaInicio);
    }

    public Optional<List<DistribucionOtra>> findByEfectorId(Long efectorId) {
        return distribucionOtraRepository.findByEfectorId(efectorId);
    }

    public boolean existsById(Long id) {
        return distribucionOtraRepository.existsById(id);
    }

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionOtraRepository.existsByEfectorId(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionOtraRepository.existsByPersonaId(personaId);
    }

    public void save(DistribucionOtra distribucionOtra) {
        distribucionOtraRepository.save(distribucionOtra);
    }

    public void deleteById(Long id) {
        distribucionOtraRepository.deleteById(id);
    }

}