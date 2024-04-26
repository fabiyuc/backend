package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Feriado;
import com.guardias.backend.repository.FeriadoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FeriadoService {
    @Autowired
    FeriadoRepository feriadoRepository;

    public Optional<List<Feriado>> findByActivoTrue() {
        return feriadoRepository.findByActivoTrue();
    }

    public List<Feriado> findAll() {
        return feriadoRepository.findAll();
    }

    public Optional<Feriado> findById(Long id) {
        return feriadoRepository.findById(id);
    }

    public Optional<Feriado> getByMotivo(String motivo) {
        return feriadoRepository.findByMotivo(motivo);
    }

    public Optional<Feriado> getByFecha(LocalDate fecha) {
        return feriadoRepository.findByFecha(fecha);
    }

    public void save(Feriado feriado) {
        feriadoRepository.save(feriado);
    }

    public void deleteById(Long id) {
        feriadoRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return feriadoRepository.existsById(id);
    }

    public boolean existsByMotivo(String motivo) {
        return feriadoRepository.existsByMotivo(motivo);
    }

    public boolean existsByFecha(LocalDate fecha) {
        return feriadoRepository.existsByFecha(fecha);
    }

    public boolean activo(Long id) {
        return (feriadoRepository.existsById(id) && feriadoRepository.findById(id).get().isActivo());
    }

}
