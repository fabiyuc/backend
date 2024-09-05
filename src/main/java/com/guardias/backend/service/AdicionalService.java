package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Adicional;
import com.guardias.backend.repository.AdicionalRepository;

@Service
@Transactional
public class AdicionalService {

    @Autowired
    AdicionalRepository adicionalRepository;
    @Autowired
    RegionService regionService;

    public Optional<List<Adicional>> findByActivoTrue() {
        return adicionalRepository.findByActivoTrue();
    }

    public List<Adicional> findAll() {
        return adicionalRepository.findAll();
    }

    public Optional<Adicional> findById(Long id) {
        return adicionalRepository.findById(id);
    }

    public Optional<Adicional> findByNombre(String nombre) {
        return adicionalRepository.findByNombre(nombre);
    }

    public boolean existsById(Long id) {
        return adicionalRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return adicionalRepository.existsByNombre(nombre);
    }

    public boolean activo(Long id) {
        return adicionalRepository.existsById(id) && adicionalRepository.findById(id).get().isActivo();
    }

    public boolean activoByNombre(String nombre) {
        return (adicionalRepository.existsByNombre(nombre)
                && adicionalRepository.findByNombre(nombre).get().isActivo());
    }

    /*
     * public boolean existsByNombreAndIdNot(String nombre, Long id) {
     * return adicionalRepository.existsByNombreAndIdNot(nombre, id);
     * }
     */

    public void save(Adicional adicional) {
        adicionalRepository.save(adicional);
    }

    public void deleteById(Long id) {
        adicionalRepository.deleteById(id);
    }

}