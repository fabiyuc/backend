package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.TipoLicencia;
import com.guardias.backend.repository.TipoLicenciaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TipoLicenciaService {

    @Autowired
    TipoLicenciaRepository tipoLicenciaRepository;

    public List<TipoLicencia> list() {
        return tipoLicenciaRepository.findAll();
    }

    public Optional<TipoLicencia> findById(Long id) {
        return tipoLicenciaRepository.findById(id);
    }

    public Optional<TipoLicencia> findByNombre(String nombre) {
        return tipoLicenciaRepository.findByNombre(nombre);
    }

    public void save(TipoLicencia tipoLicencia) {
        tipoLicenciaRepository.save(tipoLicencia);
    }

    public void deleteById(Long id) {
        tipoLicenciaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return tipoLicenciaRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (tipoLicenciaRepository.existsById(id) && tipoLicenciaRepository.findById(id).get().isActivo());
    }

    public boolean existsByNombre(String nombre) {
        return tipoLicenciaRepository.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return (tipoLicenciaRepository.existsByNombre(nombre)
                && tipoLicenciaRepository.findByNombre(nombre).get().isActivo());
    }

}
