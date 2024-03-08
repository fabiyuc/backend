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

    public List<Departamento> findByActivo() {
        return departamentoRepository.findByActivoTrue();
    }

    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }

    public Optional<Departamento> findById(Long id) {
        return departamentoRepository.findById(id);
    }

    public Optional<Departamento> getByNombre(String nombre) {
        return departamentoRepository.findByNombre(nombre);
    }

    public Optional<Departamento>getByCodigoPostal(String codigoPostal){
        return departamentoRepository.findByCodigoPostal(codigoPostal);
    }

    public void save(Departamento departamento) {
        departamentoRepository.save(departamento);
    }

    public void deleteById(Long id) {
        departamentoRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return departamentoRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return departamentoRepository.existsByNombre(nombre);
    }

    public boolean existsByCodigoPostal(String codigoPostal){
        return departamentoRepository.existsByCodigoPostal(codigoPostal);
    }
}