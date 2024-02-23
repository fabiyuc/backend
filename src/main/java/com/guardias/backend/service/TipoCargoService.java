package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guardias.backend.entity.TipoCargo;
import com.guardias.backend.repository.TipoCargoRepository;

@Service
@Transactional
public class TipoCargoService {

    @Autowired
    TipoCargoRepository tipoCargoRepository;

    public List<TipoCargo> list() {
        return tipoCargoRepository.findAll();
    }

    public Optional<TipoCargo> findById(Long id) {
        return tipoCargoRepository.findById(id);
    }

    public Optional<TipoCargo> findByNombre(String nombre) {
        return tipoCargoRepository.findByNombre(nombre);
    }

    public void save(TipoCargo tipoCargo) {
        tipoCargoRepository.save(tipoCargo);

    }

    public void delete(Long id) {
        tipoCargoRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return tipoCargoRepository.existsById((Long) id);
    }

    public boolean existsByNombre(String nombre) {
        return tipoCargoRepository.existsByNombre(nombre);
    }

}