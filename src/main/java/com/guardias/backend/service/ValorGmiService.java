package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.ValorGmi;
import com.guardias.backend.repository.ValorGmiRepository;

@Service
@Transactional
public class ValorGmiService {
    @Autowired
    ValorGmiRepository valorGmiRepository;

    public boolean existsById(Long id) {
        return valorGmiRepository.existsById(id);
    }

    public Optional<ValorGmi> findById(Long id) {
        return valorGmiRepository.findById(id);
    }

    public List<ValorGmi> findAll() {
        return valorGmiRepository.findAll();
    }

    public List<ValorGmi> findByActivoTrue() {
        return valorGmiRepository.findByActivoTrue();
    }

    public boolean activo(Long id) {
        return valorGmiRepository.existsById(id) && valorGmiRepository.findById(id).get().isActivo();
    }

    public void save(ValorGmi valorGmi) {
        valorGmiRepository.save(valorGmi);
    }

    public void deleteById(Long id) {
        valorGmiRepository.deleteById(id);
    }
}
