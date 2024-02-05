package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Hospital;
import com.guardias.backend.repository.HospitalRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HospitalService {

    @Autowired
    HospitalRepository hospitalRepository;

    public List<Hospital> list() {
        return hospitalRepository.findAll();
    }

    public List<Hospital> findByPasiva() {
        return hospitalRepository.findByPasiva();
    }

    public Optional<Hospital> getById(Long id) {
        return hospitalRepository.findById(id);
    }

    public Optional<Hospital> getHospitalByNombre(String nombre) {
        return hospitalRepository.findByNombre(nombre);
    }

    public void save(Hospital hospital) {
        hospitalRepository.save(hospital);
    }

    public void deleteById(Long id) {
        hospitalRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return hospitalRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return hospitalRepository.existsByNombre(nombre);
    }
}
