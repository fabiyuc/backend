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

    @Autowired
    EfectorService efectorService;

    @Autowired
    PersonService personService;

    public Optional<List<DistribucionConsultorio>> findByActivoTrue() {
        return distribucionConsultorioRepository.findByActivoTrue();
    }

    public List<DistribucionConsultorio> findAll() {
        return distribucionConsultorioRepository.findAll();
    }

    public Optional<DistribucionConsultorio> findById(Long id) {
        return distribucionConsultorioRepository.findById(id);
    }

    public boolean activo(Long id) {
        return (distribucionConsultorioRepository.existsById(id)
                && distribucionConsultorioRepository.findById(id).get().isActivo());
    }

    public List<DistribucionConsultorio> findByFechaInicio(LocalDate fechaInicio) {
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
        return distribucionConsultorioRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionConsultorioRepository.existsByPersonaId(personaId) && personService.activoById(personaId);
    }

    public void save(DistribucionConsultorio distribucionConsultorio) {
        distribucionConsultorioRepository.save(distribucionConsultorio);
    }

    public void deleteById(Long id) {
        distribucionConsultorioRepository.deleteById(id);
    }

}
