package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.CargaHoraria;
import com.guardias.backend.repository.CargaHorariaRepository;

@Service
@Transactional
public class CargaHorariaService {
    
    CargaHorariaRepository cargaHorariaRepository;

    public List<CargaHoraria> list() {
        return cargaHorariaRepository.findAll();
    }

    public Optional<CargaHoraria> getOne(int id) {
        return cargaHorariaRepository.findById(id);
    }

    public Optional<CargaHoraria> getByCantidad(int cantidad) {
        return cargaHorariaRepository.findByCantidad(cantidad);
    }

    public void save(CargaHoraria cargaHoraria) {
        cargaHorariaRepository.save(cargaHoraria);
    }

    public void delete(int id) {
        cargaHorariaRepository.deleteById(id);
    }

    public boolean existsById(int id) {
        return cargaHorariaRepository.existsById(id);
    }

    public boolean existsByCantidad(int cantidad) {
        return cargaHorariaRepository.existsByCantidad(cantidad);
    }
}
