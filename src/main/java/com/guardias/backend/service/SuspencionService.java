package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Suspencion;
import com.guardias.backend.repository.SuspencionRepository;

@Service
@Transactional
public class SuspencionService {

    @Autowired
    SuspencionRepository suspencionRepository;

    public List<Suspencion> findByActivo() {
        return suspencionRepository.findByActivoTrue();
    }

    public List<Suspencion> findAll() {
        return suspencionRepository.findAll();
    }

    public Optional<Suspencion> findById(Long id) {
        return suspencionRepository.findById(id);
    }

    public void save(Suspencion suspencion) {
        suspencionRepository.save(suspencion);
    }

    public void delete(Long id) {
        suspencionRepository.deleteById(id);
    }

    public Boolean existsById(Long id) {
        return suspencionRepository.existsById(id);
    }

    public Boolean activo(Long id) {
        return (suspencionRepository.existsById(id) && suspencionRepository.findById(id).get().isActivo());
    }

    public Optional<Suspencion> findByFechaInicio(LocalDate fechaInicio) {
        return suspencionRepository.findByFechaInicio(fechaInicio);
    }

    public Boolean activoByFechaInicio(LocalDate fechaInicio) {
        return (suspencionRepository.existsByFechaInicio(fechaInicio)
                && suspencionRepository.findByFechaInicio(fechaInicio).get().isActivo());
    }

    public Optional<Suspencion> findByFechaFin(LocalDate fechaFin) {
        return suspencionRepository.findByFechaFin(fechaFin);
    }

    public Boolean activoByFechaFin(LocalDate fechaFin) {
        return (suspencionRepository.existsByFechaFin(fechaFin)
                && suspencionRepository.findByFechaFin(fechaFin).get().isActivo());
    }

    public Boolean existsByFechaInicio(LocalDate fechaInicio) {
        return suspencionRepository.existsByFechaInicio(fechaInicio);
    }

    public Boolean existsByFechaFin(LocalDate fechaFin) {
        return suspencionRepository.existsByFechaFin(fechaFin);
    }

}
