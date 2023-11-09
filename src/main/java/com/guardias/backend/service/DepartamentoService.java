package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Departamento;
import com.guardias.backend.repository.DepartamentoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DepartamentoService {

    @Autowired
    DepartamentoRepository departamentoRepository;

    public List<Departamento> list() {
        return departamentoRepository.findAll();
    }

    public Optional<Departamento> getById(int id) {
        return departamentoRepository.findById(id);
    }

    public Optional<Departamento> getByNombre(String nombre) {
        return departamentoRepository.findByNombre(nombre);
    }

    public void save(Departamento departamento) {
        departamentoRepository.save(departamento);
    }

    public void deleteById(int id) {
        departamentoRepository.deleteById(id);
    }

    public boolean existById(int id) {
        return departamentoRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return departamentoRepository.existsByNombre(nombre);
    }
}