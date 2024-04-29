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

    public Optional<List<Departamento>> findByActivoTrue() {
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

    public Optional<Departamento> getByCodigoPostal(String codigoPostal) {
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

    public boolean activo(Long id) {
        return (departamentoRepository.existsById(id) && departamentoRepository.findById(id).get().isActivo());
    }

    public boolean existsByNombre(String nombre) {
        return departamentoRepository.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return (departamentoRepository.existsByNombre(nombre)
                && departamentoRepository.findByNombre(nombre).get().isActivo());
    }

    public boolean existsByCodigoPostal(String codigoPostal) {
        return departamentoRepository.existsByCodigoPostal(codigoPostal);
    }
}