package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.TipoRevista;
import com.guardias.backend.repository.TipoRevistaRepository;

@Service
@Transactional
public class TipoRevistaService {

    @Autowired
    TipoRevistaRepository tipoRevistaRepository;
    @Autowired
    RevistaService revistaService;

    public List<TipoRevista> findByActivoTrue() {
        return tipoRevistaRepository.findByActivoTrue();
    }

    public List<TipoRevista> findAll() {
        return tipoRevistaRepository.findAll();
    }

    public Optional<TipoRevista> findById(Long id) {
        return tipoRevistaRepository.findById(id);
    }

    public Optional<TipoRevista> findByNombre(String nombre) {
        return tipoRevistaRepository.findByNombre(nombre);
    }

    public boolean existsById(Long id) {
        return tipoRevistaRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return tipoRevistaRepository.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return tipoRevistaRepository.existsByNombre(nombre)
                && tipoRevistaRepository.findByNombre(nombre).get().isActivo();
    }

    public boolean activo(Long id) {
        return tipoRevistaRepository.existsById(id) && tipoRevistaRepository.findById(id).get().isActivo();
    }

    public void save(TipoRevista tipoRevista) {
        tipoRevistaRepository.save(tipoRevista);
    }

    public void deleteById(Long id) {
        tipoRevistaRepository.deleteById(id);
    }

}