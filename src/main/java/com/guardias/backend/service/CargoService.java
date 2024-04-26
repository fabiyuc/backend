package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Cargo;
import com.guardias.backend.enums.AgrupacionEnum;
import com.guardias.backend.repository.CargoRepository;

@Service
@Transactional
public class CargoService {

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    LegajoService legajoService;

    public List<Cargo> findByActivoTrue() {
        return cargoRepository.findByActivoTrue();
    }

    public List<Cargo> findAll() {
        return cargoRepository.findAll();
    }

    public Optional<Cargo> findById(Long id) {
        return cargoRepository.findById(id);
    }

    public Optional<Cargo> findByNombre(String nombre) {
        return cargoRepository.findByNombre(nombre);
    }

    public Optional<Cargo> findByAgrupacion(AgrupacionEnum agrupacion) {
        return cargoRepository.findByAgrupacion(agrupacion);
    }

    public List<Cargo> findByFechaInicio(LocalDate fechaInicio) {
        return cargoRepository.findByFechaInicio(fechaInicio);
    }

    public boolean existsById(Long id) {
        return cargoRepository.existsById(id);
    }

    public boolean activoByNombre(String nombre) {
        return (cargoRepository.existsByNombre(nombre) && cargoRepository.findByNombre(nombre).get().isActivo());
    }

    public boolean existsByNombre(String nombre) {
        return cargoRepository.existsByNombre(nombre);
    }

    public boolean activo(Long id) {
        return (cargoRepository.existsById(id) && cargoRepository.findById(id).get().isActivo());

    }

    public boolean existsByAgrupacion(AgrupacionEnum agrupacion) {
        return (cargoRepository.existsByAgrupacion(agrupacion)
                && cargoRepository.findByAgrupacion(agrupacion).get().isActivo());
    }

    public void save(Cargo cargo) {
        cargoRepository.save(cargo);
    }

    public void deleteById(Long id) {
        cargoRepository.deleteById(id);
    }
}
