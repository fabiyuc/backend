package com.guardias.backend.service;

import java.sql.Date;
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

    public List<Suspencion> list() {
        return suspencionRepository.findAll();
    }

    public Optional<Suspencion> getOne(int id) {
        return suspencionRepository.findById(id);
    }

    public Optional<Suspencion> getByFechaInicio(Date fechaInicio) {
        return suspencionRepository.findByFechaInicio(fechaInicio);
    }

    public Optional<Suspencion> getByFechaFin(Date fechaFin) {
        return suspencionRepository.findByFechaFin(fechaFin);
    }

    public void save(Suspencion suspencion) {
        suspencionRepository.save(suspencion);
    }

    public void delete(int id) {
        suspencionRepository.deleteById(id);
    }

    public boolean existsById(int id) {
        return suspencionRepository.existsById(id);
    }

    public boolean existsByFechaInicio(Date fechaInicio) {
        return suspencionRepository.existsByFechaInicio(fechaInicio);
    }
    
    public boolean existsByFechaFin(Date fechaFin) {
        return suspencionRepository.existsByFechaFin(fechaFin);
    }
    
}
