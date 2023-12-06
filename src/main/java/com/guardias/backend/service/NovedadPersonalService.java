package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.repository.NovedadPersonalRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NovedadPersonalService {

    @Autowired
    NovedadPersonalRepository novedadPersonalRepository;

    /*
     * Optional<NovedadPersonal> findByPersona(Long idPersona);
     * 
     * boolean existsByPersona(Long idPersona);
     */

    public List<NovedadPersonal> list() {
        return novedadPersonalRepository.findAll();
    }

    public Optional<NovedadPersonal> getById(Long id) {
        return novedadPersonalRepository.findById(id);
    }

    public Optional<NovedadPersonal> findById(Long id) {
        return novedadPersonalRepository.findById(id);
    }

    public List<NovedadPersonal> findByPersona(Long idPersona) {
        return novedadPersonalRepository.findByPersona(idPersona);
    }

    public boolean existsByPersona(Long idPersona) {
        return novedadPersonalRepository.existsByPersona(idPersona);
    }

    public boolean existsById(Long id) {
        return novedadPersonalRepository.existsById(id);
    }

    public void save(NovedadPersonal novedadPersonal) {
        novedadPersonalRepository.save(novedadPersonal);

    }

    public void delete(Long id) {
        novedadPersonalRepository.deleteById((Long) id);
    }

}
