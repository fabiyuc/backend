package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.repository.TipoGuardiaRepository;

@Service
@Transactional
public class TipoGuardiaService {

    @Autowired
    TipoGuardiaRepository tipoGuardiaRepositorio;

    public Optional<TipoGuardia> findByDescripcion(String descripcion) {
        return tipoGuardiaRepositorio.findByDescripcion(descripcion);
    }

    public boolean existsByDescripcion(String descripcion) {
        return tipoGuardiaRepositorio.existsByDescripcion(descripcion);
    }

    public boolean activoByDescripcion(String descripcion) {
        return (tipoGuardiaRepositorio.existsByDescripcion(descripcion)
                && tipoGuardiaRepositorio.findByDescripcion(descripcion).get().isActivo());
    }

    public Optional<TipoGuardia> findByNombre(String nombre) {
        return tipoGuardiaRepositorio.findByNombre(nombre);
    }

    public boolean existsByNombre(String nombre) {
        return tipoGuardiaRepositorio.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return (tipoGuardiaRepositorio.existsByNombre(nombre)
                && tipoGuardiaRepositorio.findByNombre(nombre).get().isActivo());
    }

    public List<TipoGuardia> list() {
        return tipoGuardiaRepositorio.findAll();
    }

    public Optional<TipoGuardia> findById(Long id) {
        return tipoGuardiaRepositorio.findById(id);
    }

    public void save(TipoGuardia tipoGuardia) {
        tipoGuardiaRepositorio.save(tipoGuardia);
    }

    public void deleteById(Long id) {
        tipoGuardiaRepositorio.deleteById(id);
    }

    public boolean existsById(Long id) {
        return tipoGuardiaRepositorio.existsById(id);
    }

    public boolean activo(Long id) {
        return (tipoGuardiaRepositorio.existsById(id) && tipoGuardiaRepositorio.findById(id).get().isActivo());
    }

}
