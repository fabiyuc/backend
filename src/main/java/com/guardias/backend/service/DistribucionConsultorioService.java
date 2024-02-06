package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.DistribucionConsultorio;
import com.guardias.backend.repository.DistribucionConsultorioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionConsultorioService {

    @Autowired
    DistribucionConsultorioRepository distribucionConsultorioRepository;

    public List<DistribucionConsultorio> list() {
        return distribucionConsultorioRepository.findAll();
    }

    public Optional<DistribucionConsultorio> findById(Long id) {
        return distribucionConsultorioRepository.findById(id);
    }

    public Optional<List<DistribucionConsultorio>> findByFechaInicio(LocalDate fechaInicio) {
        return distribucionConsultorioRepository.findByFechaInicio(fechaInicio);
    }

    public Optional<List<DistribucionConsultorio>> findByPersonaId(Long personaId) {
        return distribucionConsultorioRepository.findByPersonaId(personaId);
    }

    public Optional<List<DistribucionConsultorio>> findByEfectorId(Long efectorId) {
        return distribucionConsultorioRepository.findByEfectorId(efectorId);
    }

    public boolean existsById(Long id) {
        return distribucionConsultorioRepository.existsById(id);
    }

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionConsultorioRepository.existsByEfectorId(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionConsultorioRepository.existsByPersonaId(personaId);
    }

    public void save(DistribucionConsultorio distribucionConsultorio) {
        distribucionConsultorioRepository.save(distribucionConsultorio);
    }

    public void delete(Long id) {
        distribucionConsultorioRepository.deleteById(id);
    }

}
