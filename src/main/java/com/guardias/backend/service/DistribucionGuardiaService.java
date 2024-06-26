package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.DistribucionGuardia;
import com.guardias.backend.repository.DistribucionGuardiaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionGuardiaService {

    @Autowired
    DistribucionGuardiaRepository distribucionGuardiaRepository;

    @Autowired
    EfectorService efectorService;

    @Autowired
    PersonService personService;

    public Optional<List<DistribucionGuardia>> findByActivoTrue() {
        return distribucionGuardiaRepository.findByActivoTrue();
    }

    public List<DistribucionGuardia> findAll() {
        return distribucionGuardiaRepository.findAll();
    }

    public Optional<DistribucionGuardia> findById(Long id) {
        return distribucionGuardiaRepository.findById(id);
    }

    public Optional<List<DistribucionGuardia>> findByPersonaId(Long personaId) {
        return distribucionGuardiaRepository.findByPersonaId(personaId);
    }

    public List<DistribucionGuardia> findByFechaInicio(LocalDate fechaInicio) {
        return distribucionGuardiaRepository.findByFechaInicio(fechaInicio);
    }

    public Optional<List<DistribucionGuardia>> findByEfectorId(Long efectorId) {
        return distribucionGuardiaRepository.findByEfectorId(efectorId);
    }

    public boolean existsById(Long id) {
        return distribucionGuardiaRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (distribucionGuardiaRepository.existsById(id)
                && distribucionGuardiaRepository.findById(id).get().isActivo());
    }

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionGuardiaRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionGuardiaRepository.existsByPersonaId(personaId) && personService.activoById(personaId);
    }

    public void save(DistribucionGuardia distribucionGuardia) {
        distribucionGuardiaRepository.save(distribucionGuardia);
    }

    public void deleteById(Long id) {
        distribucionGuardiaRepository.deleteById(id);
    }

}