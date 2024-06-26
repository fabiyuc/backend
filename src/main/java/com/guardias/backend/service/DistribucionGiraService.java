package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.DistribucionGira;
import com.guardias.backend.repository.DistribucionGiraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionGiraService {

    @Autowired
    DistribucionGiraRepository distribucionGiraRepository;

    @Autowired
    EfectorService efectorService;

    @Autowired
    PersonService personService;

    public Optional<List<DistribucionGira>> findByActivoTrue() {
        return distribucionGiraRepository.findByActivoTrue();
    }

    public List<DistribucionGira> findAll() {
        return distribucionGiraRepository.findAll();
    }

    public Optional<DistribucionGira> findById(Long id) {
        return distribucionGiraRepository.findById(id);
    }

    public List<DistribucionGira> findByFechaInicio(LocalDate fechaInicio) {
        return distribucionGiraRepository.findByFechaInicio(fechaInicio);
    }

    public Optional<List<DistribucionGira>> findByPersonaId(Long personaId) {
        return distribucionGiraRepository.findByPersonaId(personaId);
    }

    public Optional<List<DistribucionGira>> findByEfectorId(Long efectorId) {
        return distribucionGiraRepository.findByEfectorId(efectorId);
    }

    public boolean existsById(Long id) {
        return distribucionGiraRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (distribucionGiraRepository.existsById(id)
                && distribucionGiraRepository.findById(id).get().isActivo());
    }

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionGiraRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionGiraRepository.existsByPersonaId(personaId) && personService.activoById(personaId);
    }

    public void save(DistribucionGira distribucionGira) {
        distribucionGiraRepository.save(distribucionGira);
    }

    public void deleteById(Long id) {
        distribucionGiraRepository.deleteById(id);
    }

}