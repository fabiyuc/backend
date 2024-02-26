package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Region;
import com.guardias.backend.repository.RegionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {
    @Autowired
    RegionRepository regionRepository;

    public List<Region> list() {
        return regionRepository.findAll();
    }

    public Optional<Region> findById(Long id) {
        return regionRepository.findById(id);
    }

    public Optional<Region> findByNombre(String nombre) {
        return regionRepository.findByNombre(nombre);
    }

    public void save(Region region) {
        regionRepository.save(region);
    }

    public void deleteById(Long id) {
        regionRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return regionRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return regionRepository.existsByNombre(nombre);
    }
}
