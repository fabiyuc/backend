package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Legajo;
import com.guardias.backend.repository.LegajoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LegajoService {

    @Autowired
    LegajoRepository legajoRepository;

    // @Autowired
    // EfectorService efectorService;

    public Optional<List<Legajo>> findByActivoTrue() {
        return legajoRepository.findByActivoTrue();
    }

    public List<Legajo> findAll() {
        return legajoRepository.findAll();
    }

    public Optional<Legajo> findById(Long id) {
        return legajoRepository.findById((Long) id);
    }

    public boolean existsById(Long id) {
        return legajoRepository.existsById((Long) id);
    }

    public boolean activo(Long id) {
        return (legajoRepository.existsById(id) && legajoRepository.findById(id).get().isActivo());
    }

    public void save(Legajo legajo) {
        legajoRepository.save(legajo);
    }

    public void deleteById(Long id) {
        legajoRepository.deleteById((Long) id);
    }

    public void save(Legajo legajo) {
        legajoRepository.save(legajo);
    }

    public void deleteById(Long id) {
        legajoRepository.deleteById(id);
    }

    // @Transactional
    // public void agregarEfector(Long idLegajo, Long idEfector) {

    // Legajo legajo = findById(idLegajo).get();
    // Efector efector = efectorService.findEfector(idEfector);
    // legajo.getEfectores().add(efector);
    // efector.getLegajos().add(legajo);
    // save(legajo);
    // }

}
