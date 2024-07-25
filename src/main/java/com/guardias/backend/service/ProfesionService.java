package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.ProfesionLegajoDto;
import com.guardias.backend.entity.Profesion;
import com.guardias.backend.repository.ProfesionRepository;

@Service
@Transactional
public class ProfesionService {

    @Autowired
    ProfesionRepository profesionRepository;

    public Optional<List<Profesion>> findByActivoTrue() {
        return profesionRepository.findByActivoTrue();
    }

    public List<Profesion> findAll() {
        return profesionRepository.findAll();
    }

    public List<Profesion> findByAsistencialTrue() {
        return profesionRepository.findByAsistencialTrue();
    }

    public List<Profesion> findByAsistencialFalse() {
        return profesionRepository.findByAsistencialFalse();
    }

    public Optional<Profesion> findById(Long id) {
        return profesionRepository.findById((Long) id);
    }

    public Optional<Profesion> findByNombre(String nombre) {
        return profesionRepository.findByNombre(nombre);
    }

    public void save(Profesion profesion) {
        profesionRepository.save(profesion);
    }

    public void deleteById(Long id) {
        profesionRepository.deleteById((Long) id);
    }

    public boolean existsById(Long id) {
        return profesionRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return profesionRepository.existsByNombre(nombre);
    }

    public boolean activo(Long id) {
        return (profesionRepository.existsById(id)
                && profesionRepository.findById(id).get().isActivo());
    }

    public boolean activoByNombre(String nombre) {
        return (profesionRepository.existsByNombre(nombre)
                && profesionRepository.findByNombre(nombre).get().isActivo());
    }

    public List<ProfesionLegajoDto> findAllProfesiones() {
        return profesionRepository.findAll().stream()
                .map(profesion -> new ProfesionLegajoDto(profesion.getNombre()))
                .collect(Collectors.toList());
    }
}
