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
    @Autowired
    PersonService personaService;

    public List<NovedadPersonal> findByActivo() {
        return novedadPersonalRepository.findByActivoTrue();
    }

    public List<NovedadPersonal> findAll() {
        return novedadPersonalRepository.findAll();
    }

    public Optional<NovedadPersonal> getById(Long id) {
        return novedadPersonalRepository.findById(id);
    }

    public Optional<List<NovedadPersonal>> findByPersona(Long idPersona) {
        return novedadPersonalRepository.findByPersona(idPersona);
    }

    public boolean activoByPersona(Long idPersona) {
        return novedadPersonalRepository.existsByPersona(idPersona)
                && personaService.activoById(idPersona);
    }

    public Optional<List<NovedadPersonal>> findByFechaInicio(LocalDate fecha) {
        return novedadPersonalRepository.findByFechaInicio(fecha);
    }

    public Optional<NovedadPersonal> findById(Long id) {
        return novedadPersonalRepository.findById(id);
    }

    public boolean existsByFechaInicio(LocalDate fechaInicio) {
        return novedadPersonalRepository.existsByFechaInicio(fechaInicio);
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