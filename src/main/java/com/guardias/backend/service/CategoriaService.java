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

    public List<Categoria> findByActivo() {
        return categoriaRepository.findByActivoTrue();
    }

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById((Long) id);
    }

    public Optional<Categoria> getByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public void save(Categoria adicional) {
        categoriaRepository.save(adicional);
    }

    public void deleteById(Long id) {
        categoriaRepository.deleteById((Long) id);
    }

    public boolean existsById(Long id) {
        return categoriaRepository.existsById((Long) id);
    }

    public boolean existsByNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

}