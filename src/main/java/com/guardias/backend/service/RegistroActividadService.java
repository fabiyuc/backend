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

    public List<RegistroActividad> findByActivo(boolean activo) {
        return registroActividadRepositorio.findByActivo(activo);
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
}
