package com.guardias.backend.service;

import java.time.LocalDate;
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

    public List<NovedadPersonal> list() {
        return novedadPersonalRepository.findAll();
    }

    public Optional<NovedadPersonal> getById(Long id) {
        return novedadPersonalRepository.findById(id);
    }

    public Optional<List<NovedadPersonal>> getByPersona(Long idPersona) {
        return novedadPersonalRepository.findByPersona(idPersona);
    }

    public Optional<List<NovedadPersonal>> getByFecha(LocalDate fecha) {
        return novedadPersonalRepository.findByFecha(fecha);
    }

    public boolean existsByPersona(Long idPersona) {
        return novedadPersonalRepository.existsByPersona(idPersona);
    }

    public boolean existsByFecha(LocalDate fecha) {
        return novedadPersonalRepository.existsByFecha(fecha);
    }

    public void save(NovedadPersonal novedadPersonal) {
        novedadPersonalRepository.save(novedadPersonal);
    }

    public void deleteById(Long id) {
        novedadPersonalRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return novedadPersonalRepository.existsById(id);
    }

}
