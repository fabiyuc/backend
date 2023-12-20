package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.repository.AsistencialRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AsistencialService {

    @Autowired
    AsistencialRepository asistencialRepository;

    public List<Asistencial> list() {
        return asistencialRepository.findAll();
    }

    public Optional<Asistencial> getone(Long id) {
        return asistencialRepository.findById(id);
    }

    public void save(Asistencial asistencial) {
        asistencialRepository.save(asistencial);
    }

    public void delete(Long id) {
        asistencialRepository.deleteById(id);
    }

    public Boolean existsById(Long id) {
        return asistencialRepository.existsById(id);
    }

    public Boolean existsByDni(String dni) {
        return asistencialRepository.existsByDni(dni);
    }

    public List<Asistencial> findByEstado(Boolean estado) {
        return asistencialRepository.findByEstado(estado);
    }

    public Optional<Asistencial> getByDni(String dni) {
        return asistencialRepository.findByDni(dni);
    }

}
