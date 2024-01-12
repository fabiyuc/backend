package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.repository.DistribucionHorariaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionHorariaService {

    @Autowired
    DistribucionHorariaRepository distribucionHorariaRepository;

    public List<DistribucionHoraria> list() {
        return distribucionHorariaRepository.findAll();
    }

    public Optional<DistribucionHoraria> findById(Long id) {
        return distribucionHorariaRepository.findById(id);
    }

    /*
     * public Optional<List<DistribucionHoraria>> findByPersonaId(Long personaId) {
     * return distribucionHorariaRepository.findByPersonaId(personaId);
     * }
     * 
     * public Optional<List<DistribucionHoraria>> findByEfectorId(Long efectorId) {
     * return distribucionHorariaRepository.findByEfectorId(efectorId);
     * }
     */

    public boolean existsById(Long id) {
        return distribucionHorariaRepository.existsById(id);
    }

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionHorariaRepository.existsByEfectorId(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionHorariaRepository.existsByPersonaId(personaId);
    }

    public void save(DistribucionHoraria distribucionHoraria) {
        distribucionHorariaRepository.save(distribucionHoraria);
    }

    public void delete(Long id) {
        distribucionHorariaRepository.deleteById(id);
    }
}
