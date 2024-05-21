package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.PendientesSemanal;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.PendientesSemanalRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PendientesSemanalService {
    @Autowired
    PendientesSemanalRepository pendientesSemanalRepository;

    public List<PendientesSemanal> findByActivo() {
        return pendientesSemanalRepository.findByActivoTrue();
    }

    public List<PendientesSemanal> findAll() {
        return pendientesSemanalRepository.findAll();
    }

    public Optional<PendientesSemanal> findById(Long id) {
        return pendientesSemanalRepository.findById(id);
    }

    public void save(PendientesSemanal pendientesSemanal) {
        pendientesSemanalRepository.save(pendientesSemanal);
    }

    public void deleteById(Long id) {
        pendientesSemanalRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return pendientesSemanalRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (pendientesSemanalRepository.existsById(id)
                && pendientesSemanalRepository.findById(id).get().isActivo());
    }

    public List<PendientesSemanal> findByAnioAndMesAndEfectorId(int anio, MesesEnum mes, Long idEfector) {
        return pendientesSemanalRepository.findByAnioAndMesAndEfectorId(anio, mes, idEfector);
    }
}
