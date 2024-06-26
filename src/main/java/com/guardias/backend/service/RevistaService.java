package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Revista;
import com.guardias.backend.repository.RevistaRepository;

@Service
@Transactional
public class RevistaService {

    @Autowired
    RevistaRepository revistaRepository;

    @Autowired
    LegajoService legajoService;

    public Revista findRevista(Long idRevista) {
        return revistaRepository.findById(idRevista).orElse(null);
    }

    public List<Revista> findByActivoTrue() {
        return revistaRepository.findByActivoTrue();
    }

    public List<Revista> findAll() {
        return revistaRepository.findAll();
    }

    public Optional<Revista> findById(Long id) {
        return revistaRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return revistaRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (revistaRepository.existsById(id) && revistaRepository.findById(id).get().isActivo());
    }

    public void save(Revista revista) {
        revistaRepository.save(revista);
    }

    public void deleteById(Long id) {
        revistaRepository.deleteById(id);
    }

}