package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.CargaHoraria;
import com.guardias.backend.repository.CargaHorariaRepository;

@Service
@Transactional
public class CargaHorariaService {

    @Autowired
    CargaHorariaRepository cargaHorariaRepository;
    @Autowired
    RevistaService revistaService;

    public Optional<List<CargaHoraria>> findByActivoTrue() {
        return cargaHorariaRepository.findByActivoTrue();
    }

    public List<CargaHoraria> findAll() {
        return cargaHorariaRepository.findAll();
    }

    public Optional<CargaHoraria> findById(Long id) {
        return cargaHorariaRepository.findById((Long) id);
    }

    public Optional<CargaHoraria> findByCantidad(int cantidad) {
        return cargaHorariaRepository.findByCantidad(cantidad);
    }

    public boolean existsById(Long id) {
        return cargaHorariaRepository.existsById((Long) id);
    }

    public boolean activoByCantidad(int cantidad) {
        return (cargaHorariaRepository.existsByCantidad(cantidad)
                && cargaHorariaRepository.findByCantidad(cantidad).get().isActivo());
    }

    public boolean existsByCantidad(int cantidad) {
        return cargaHorariaRepository.existsByCantidad(cantidad);
    }

    public boolean activo(Long id) {
        Optional<CargaHoraria> cargaHorariaOptional = cargaHorariaRepository.findById(id);
        return cargaHorariaOptional.isPresent() && cargaHorariaOptional.get().isActivo();
    }

    public void save(CargaHoraria cargaHoraria) {
        cargaHorariaRepository.save(cargaHoraria);
    }

    public void deleteById(Long id) {
        cargaHorariaRepository.deleteById((Long) id);
    }

}
