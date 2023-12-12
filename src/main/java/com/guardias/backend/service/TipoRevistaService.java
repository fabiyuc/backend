package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guardias.backend.entity.TipoRevista;
import com.guardias.backend.repository.TipoRevistaRepository;

@Service
@Transactional
public class TipoRevistaService {

    @Autowired
    TipoRevistaRepository tipoRevistaRepository;

    public List<TipoRevista> list() {
        return tipoRevistaRepository.findAll();
    }

    public Optional<TipoRevista> getOne(Long id) {
        return tipoRevistaRepository.findById(id);
    }

    public Optional<TipoRevista> getByNombre(String nombre) {
        return tipoRevistaRepository.findByNombre(nombre);
    }

    public boolean existsByNombre(String nombre) {
        return tipoRevistaRepository.existsByNombre(nombre);
    }

    public void save(TipoRevista tipoRevista) {
        tipoRevistaRepository.save(tipoRevista);
    }

    public void delete(Long id) {
        tipoRevistaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return tipoRevistaRepository.existsById(id);
    }

    
}
