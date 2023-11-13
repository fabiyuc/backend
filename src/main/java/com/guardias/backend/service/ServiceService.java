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

    public List<Servicio> list() {
        return servicioRepositorio.findAll();
    }

    public Optional<Servicio> getOne(int id) {
        return servicioRepositorio.findById(id);
    }

    public Optional<Servicio> getByDescripcion(String descripcion) {
        return servicioRepositorio.findByDescripcion(descripcion);
    }

    public void save(Servicio servicio) {
        servicioRepositorio.save(servicio);
    }

    public void delete(int id) {
        servicioRepositorio.deleteById(id);
    }

    public boolean existsById(int id) {
        return servicioRepositorio.existsById(id);
    }

    public boolean existsByDescripcion(String descripcion) {
        return servicioRepositorio.existsByDescripcion(descripcion);
    }

}
