package com.guardias.backend.service;

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

    public List<DistribucionGira> list() {
        return distribucionGiraRepository.findAll();
    }

    public Optional<DistribucionGira> findById(Long id) {
        return distribucionGiraRepository.findById(id);
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

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionGiraRepository.existsByEfectorId(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionGiraRepository.existsByPersonaId(personaId);
    }

    public void save(DistribucionGira distribucionGira) {
        distribucionGiraRepository.save(distribucionGira);
    }

    public void delete(Long id) {
        distribucionGiraRepository.deleteById(id);
    }

}