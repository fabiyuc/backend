package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.entity.GiraMedica;
import com.guardias.backend.repository.GiraMedicaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class GiraMedicaService {

    @Autowired
    GiraMedicaRepository giraMedicaRepository;

    public List<GiraMedica> list() {
        return giraMedicaRepository.findAll();
    }

    public Optional<GiraMedica> findById(Long id) {
        return giraMedicaRepository.findById(id);
    }

    public Optional<List<GiraMedica>> findByFecha(LocalDate fecha) {
        return giraMedicaRepository.findByFecha(fecha);
    }

    public void save(GiraMedica giraMedica) {
        giraMedicaRepository.save(giraMedica);
    }

    public void deleteById(Long id) {
        giraMedicaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return giraMedicaRepository.existsById(id);
    }

    public boolean existsByFecha(LocalDate fecha) {
        return giraMedicaRepository.existsByFecha(fecha);
    }
}
