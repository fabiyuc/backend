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

    // metodos por descripcion
    public Optional<TipoGuardia> getByDescripcion(String descripcion) {
        return tipoGuardiaRepositorio.findByDescripcion(descripcion);
    }

    public boolean existsByDescripcion(String descripcion) {
        return tipoGuardiaRepositorio.existsByDescripcion(descripcion);
    }
    // fin descripcion

    // metodos por nombre
    public Optional<TipoGuardia> getByNombre(String nombre) {
        return tipoGuardiaRepositorio.findByNombre(nombre);
    }

    public boolean existsByNombre(String nombre) {
        return tipoGuardiaRepositorio.existsByNombre(nombre);
    }
    // fin nombre

    public List<TipoGuardia> list() {
        return tipoGuardiaRepositorio.findAll();
    }

    public Optional<TipoGuardia> getOne(Long id) {
        return tipoGuardiaRepositorio.findById(id);
    }

    public void save(TipoGuardia tipoGuardia) {
        tipoGuardiaRepositorio.save(tipoGuardia);
    }

    public void delete(Long id) {
        tipoGuardiaRepositorio.deleteById(id);
    }

    public boolean existsById(Long id) {
        return tipoGuardiaRepositorio.existsById(id);
    }

}
