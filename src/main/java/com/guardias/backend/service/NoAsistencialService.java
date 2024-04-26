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

    public Optional<List<NoAsistencial>> findByActivoTrue() {
        return noAsistencialRepository.findByActivoTrue();
    }

    public List<NoAsistencial> findAll() {
        return noAsistencialRepository.findAll();
    }

    public Optional<NoAsistencial> findById(Long id) {
        return noAsistencialRepository.findById((Long) id);
    }

    public boolean existsById(Long id) {
        return noAsistencialRepository.existsById(id);
    }

    public boolean existsByDni(int dni) {
        return noAsistencialRepository.existsByDni(dni);
    }

    public boolean existsByCuil(String cuil) {
        return noAsistencialRepository.existsByCuil(cuil);
    }

    public Optional<NoAsistencial> findByDni(int dni) {
        return noAsistencialRepository.findByDni(dni);
    }

    public void save(NoAsistencial noAsistencial) {
        noAsistencialRepository.save(noAsistencial);
    }

    public void deleteById(Long id) {
        noAsistencialRepository.deleteById(id);
    }

    public boolean activo(Long id) {
        return (noAsistencialRepository.existsById(id)
                && noAsistencialRepository.findById(id).get().isActivo());
    }

    public boolean activoDni(int dni) {
        return (noAsistencialRepository.existsByDni(dni) && noAsistencialRepository.findByDni(dni).get().isActivo());
    }
}
