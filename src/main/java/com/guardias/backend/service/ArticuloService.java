package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Articulo;
import com.guardias.backend.repository.ArticuloRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArticuloService {

    @Autowired
    ArticuloRepository articuloRepository;

    public List<Articulo> list() {
        return articuloRepository.findAll();
    }

    public Optional<Articulo> findById(Long id) {
        return articuloRepository.findById(id);
    }

    public Optional<Articulo> findByNumero(String numero) {
        return articuloRepository.findByNumero(numero);
    }

    Optional<Articulo> findByDenominacion(String denominacion) {
        return articuloRepository.findByDenominacion(denominacion);
    }

    public boolean existsById(Long id) {
        return articuloRepository.existsById(id);
    }

    public boolean existsByNumero(String numero) {
        return articuloRepository.existsByNumero(numero);
    }

    public boolean existsByDenominacion(String denominacion) {
        return articuloRepository.existsByDenominacion(denominacion);
    }

    public void save(Articulo articulo) {
        articuloRepository.save(articulo);
    }

    public void deleteById(Long id) {
        articuloRepository.deleteById(id);
    }

}
