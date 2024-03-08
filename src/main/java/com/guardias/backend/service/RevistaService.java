package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.repository.RevistaRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RevistaService {

    @Autowired
    RevistaRepository revistaRepository;

    public List<Revista> findByActivo() {
        return revistaRepository.findByActivoTrue();
    }

    public List<Revista> findAll() {
        return revistaRepository.findAll();
    }

    public Optional<Revista> findById(Long id) {
        return revistaRepository.findById(id);
    }

    public void save(Revista revista) {
        revistaRepository.save(revista);
    }

    public void deleteById(Long id) {
        revistaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return revistaRepository.existsById(id);
    }

}
