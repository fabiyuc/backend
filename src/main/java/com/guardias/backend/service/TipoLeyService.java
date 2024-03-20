package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.entity.TipoLey;
import com.guardias.backend.repository.TipoLeyRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TipoLeyService {

    @Autowired
    TipoLeyRepository tipoLeyRepository;

    public List<TipoLey> findByActivo() {
        return tipoLeyRepository.findByActivoTrue();
    }

    public List<TipoLey> findAll() {
        return tipoLeyRepository.findAll();
    }

    public Optional<TipoLey> findById(Long id) {
        return tipoLeyRepository.findById(id);
    }

    public Optional<TipoLey> findByDescripcion(String descripcion) {
        return tipoLeyRepository.findByDescripcion(descripcion);
    }

    public boolean existsById(Long id) {
        return tipoLeyRepository.existsById(id);
    }

    public boolean existsByDescripcion(String descripcion) {
        return tipoLeyRepository.existsByDescripcion(descripcion);
    }

    public void save(TipoLey tipoLey) {
        tipoLeyRepository.save(tipoLey);
    }

    public void deleteById(Long id) {
        tipoLeyRepository.deleteById(id);
    }
}
