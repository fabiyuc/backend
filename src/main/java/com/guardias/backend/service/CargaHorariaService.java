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

    public List<CargaHoraria> findByActivo(boolean activo) {
        return cargaHorariaRepository.findByActivo(activo);
    }

    public List<CargaHoraria> findAll() {
        return cargaHorariaRepository.findAll();
    }

    public Optional<CargaHoraria> findById(Long id) {
        return cargaHorariaRepository.findById(id);
    }

    public void save(CargaHoraria cargaHoraria) {
        cargaHorariaRepository.save(cargaHoraria);
    }

    public void deleteById(Long id) {
        cargaHorariaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return cargaHorariaRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (cargaHorariaRepository.existsById(id) && cargaHorariaRepository.findById(id).get().isActivo());
    }

    public Optional<CargaHoraria> getByCantidad(int cantidad) {
        return cargaHorariaRepository.findByCantidad(cantidad);
    }

    public boolean existsByCantidad(int cantidad) {
        return cargaHorariaRepository.existsByCantidad(cantidad);
    }

}
