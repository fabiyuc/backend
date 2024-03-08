package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guardias.backend.entity.Servicio;
import com.guardias.backend.repository.ServicioRepository;

@Service
@Transactional
public class ServiceService {

    @Autowired
    ServicioRepository servicioRepositorio;

    public List<Servicio> findByActivo() {
        return servicioRepositorio.findByActivoTrue();
    }

    public List<Servicio> findAll() {
        return servicioRepositorio.findAll();
    }

    public Optional<Servicio> findById(Long id) {
        return servicioRepositorio.findById(id);
    }

    public Optional<Servicio> findByDescripcion(String descripcion) {
        return servicioRepositorio.findByDescripcion(descripcion);
    }

    public List<Servicio> findByNivel(int nivel) {
        return servicioRepositorio.findByNivel(nivel);
    }

    public void save(Servicio servicio) {
        servicioRepositorio.save(servicio);
    }

    public void deleteById(Long id) {
        servicioRepositorio.deleteById(id);
    }

    public boolean existsById(Long id) {
        return servicioRepositorio.existsById(id);
    }

    public boolean existsByDescripcion(String descripcion) {
        return servicioRepositorio.existsByDescripcion(descripcion);
    }

    public boolean existsByNivel(int nivel) {
        return servicioRepositorio.existsByNivel(nivel);

    }

}
