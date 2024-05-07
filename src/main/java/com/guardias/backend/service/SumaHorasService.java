package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.repository.SumaHorasRepository;

@Service
@Transactional
public class SumaHorasService {
    @Autowired
    SumaHorasRepository sumaHorasRepository;

    public List<SumaHoras> findAll() {
        return sumaHorasRepository.findAll();
    }

    public List<SumaHoras> findByActivoTrue() {
        return sumaHorasRepository.findByActivoTrue();
    }

    public boolean existsById(Long id) {
        return sumaHorasRepository.existsById(id);
    }

    public Optional<SumaHoras> findById(Long id) {
        return sumaHorasRepository.findById(id);
    }

    public boolean existByRegistroMensual(Long idRegistroMensual) {
        return sumaHorasRepository.existByRegistroMensual(idRegistroMensual);
    }

    public List<SumaHoras> findByRegistroMensual(Long idRegistroMensual, Long idAsistencial) {
        return sumaHorasRepository.findByRegistroMensual(idRegistroMensual, idAsistencial);
    }

    public boolean activo(Long id) {
        return sumaHorasRepository.existsById(id) && sumaHorasRepository.findById(id).get().isActivo();
    }

    public void save(SumaHoras sumaHoras) {
        sumaHorasRepository.save(sumaHoras);
    }

    public void deleteById(Long id) {
        sumaHorasRepository.deleteById(id);
    }
}
