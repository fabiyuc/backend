package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.repository.RegistroActividadRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistroActividadService {
    @Autowired
    RegistroActividadRepository registroActividadRepositorio;

    public List<RegistroActividad> findByActivo() {
        return registroActividadRepositorio.findByActivoTrue();
    }

    public List<RegistroActividad> findAll() {
        return registroActividadRepositorio.findAll();
    }

    public Optional<RegistroActividad> findById(Long id) {
        return registroActividadRepositorio.findById((Long) id);
    }

    public void save(RegistroActividad registroActividad) {
        registroActividadRepositorio.save(registroActividad);
    }

    public void deleteById(Long id) {
        registroActividadRepositorio.deleteById(id);
    }

    public boolean existsById(Long id) {
        return registroActividadRepositorio.existsById(id);
    }

    public boolean activo(Long id) {
        return (registroActividadRepositorio.existsById(id)
                && registroActividadRepositorio.findById(id).get().isActivo());
    }
}
