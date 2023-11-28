package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.modelo.Profesional;
import com.guardias.backend.repositorio.ProfesionalRepositorio;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProfesionalServicio {
    
   @Autowired
    ProfesionalRepositorio profesionalRepositorio;

    public List<Profesional> list() {
        return profesionalRepositorio.findAll();
    }

    public Optional<Profesional> getOne(Long id) {
        return profesionalRepositorio.findById(id);
    }

    public Optional<Profesional> getByDni(int dni) {
        return profesionalRepositorio.findByDni(dni);
    }

    public void save(Profesional profesional) {
        profesionalRepositorio.save(profesional);
    }

    public void delete(Long id) {
        profesionalRepositorio.deleteById(id);
    }

    public boolean existsById(Long id) {
        return profesionalRepositorio.existsById(id);
    }

    public boolean existsByDni(int dni) {
        return profesionalRepositorio.existsByDni(dni);
    } 
}
