package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Adicional;
import com.guardias.backend.repository.AdicionalRepository;

@Service
@Transactional
public class AdicionalService {

    @Autowired
    AdicionalRepository adicionalRepository;

    public List<Adicional> list() {
        return adicionalRepository.findAll();
    }

    public Optional<Adicional> getOne(int id) {
        return adicionalRepository.findById(id);
    }

    public Optional<Adicional> getByNombre(String nombre) {
        return adicionalRepository.findByNombre(nombre);
    }

    public void save(Adicional adicional) {
        adicionalRepository.save(adicional);
    }

    public void delete(int id) {
        adicionalRepository.deleteById(id);
    }

    public boolean existsById(int id) {
        return adicionalRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return adicionalRepository.existsByNombre(nombre);
    }

}
