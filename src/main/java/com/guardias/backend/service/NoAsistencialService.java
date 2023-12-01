package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.repository.NoAsistencialRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NoAsistencialService {

    @Autowired
    NoAsistencialRepository noAsistencialRepository;

    public List<NoAsistencial> list() {
        return noAsistencialRepository.findAll();
    }

    public Optional<NoAsistencial> getById(Long id) {
        return noAsistencialRepository.findById(id);
    }

    public void save(NoAsistencial noAsistencial) {
        noAsistencialRepository.save(noAsistencial);
    }

    public void deleteById(Long id) {
        noAsistencialRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return noAsistencialRepository.existsById(id);
    }
}
