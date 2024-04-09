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
    TipoGuardiaRepository tipoGuardiaRepository;

    public List<TipoGuardia> findByActivo(){
        return tipoGuardiaRepository.findByActivoTrue();
    }

    public List<TipoGuardia> findAll(){
        return tipoGuardiaRepository.findAll();
    }

    public Optional<TipoGuardia> findByDescripcion(String descripcion) {
        return tipoGuardiaRepository.findByDescripcion(descripcion);
    }

    public boolean existsByDescripcion(String descripcion) {
        return tipoGuardiaRepository.existsByDescripcion(descripcion);
    }

    public Optional<TipoGuardia> findByNombre(String nombre) {
        return tipoGuardiaRepository.findByNombre(nombre);
    }

    public boolean existsByNombre(String nombre) {
        return tipoGuardiaRepository.existsByNombre(nombre);
    }

    public Optional<TipoGuardia> findById(Long id) {
        return tipoGuardiaRepository.findById(id);
    }

    public void save(TipoGuardia tipoGuardia) {
        tipoGuardiaRepository.save(tipoGuardia);
    }

    public void deleteById(Long id) {
        tipoGuardiaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return tipoGuardiaRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (tipoGuardiaRepository.existsById(id) && tipoGuardiaRepository.findById(id).get().isActivo());
    }

}
