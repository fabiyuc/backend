package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.Categoria;
import com.guardias.backend.repository.CategoriaRepository;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    RevistaService revistaService;

    public List<Categoria> findByActivoTrue() {
        return categoriaRepository.findByActivoTrue();
    }

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    public Optional<Categoria> findByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public boolean existsById(Long id) {
        return categoriaRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre)
                && categoriaRepository.findByNombre(nombre).get().isActivo();
    }

    public boolean activo(Long id) {
        return categoriaRepository.existsById(id) && categoriaRepository.findById(id).get().isActivo();
    }

    public void save(Categoria adicional) {
        categoriaRepository.save(adicional);
    }

    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }

    public boolean activoByNombre(String nombre) {
        return (categoriaRepository.existsByNombre(nombre)
                && categoriaRepository.findByNombre(nombre).get().isActivo());
    }

}