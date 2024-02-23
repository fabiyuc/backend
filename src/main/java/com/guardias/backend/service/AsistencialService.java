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

    public Optional<Asistencial> findById(Long id) {
        return asistencialRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return asistencialRepository.existsById(id);
    }

    public boolean existsByDni(int dni) {
        return asistencialRepository.existsByDni(dni);
    }

    public List<Asistencial> findByEstado(boolean estado) {
        return asistencialRepository.findByEstado(estado);
    }

    public Optional<Asistencial> findByDni(int dni) {
        return asistencialRepository.findByDni(dni);
    }

    public void save(Asistencial asistencial) {
        asistencialRepository.save(asistencial);
    }

    public void deleteById(Long id) {
        asistencialRepository.deleteById(id);
    }

}
