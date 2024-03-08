package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Articulo;
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.repository.ArticuloRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArticuloService {

    @Autowired
    ArticuloRepository articuloRepository;

    @Autowired
    IncisoService incisoService;

    public List<Articulo> findByActivo(boolean activo) {
        return articuloRepository.findByActivo(activo);
    }

    public List<Articulo> findAll() {
        return articuloRepository.findAll();
    }

    public List<Articulo> findAllActivos() {
        return articuloRepository.findAllActivos();
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

    public boolean activo(Long id) {
        return (articuloRepository.existsById(id) && articuloRepository.findById(id).get().isActivo());
    }

    public void save(Articulo articulo) {
        articuloRepository.save(articulo);
    }

    public void deleteById(Long id) {
        articuloRepository.deleteById(id);
    }

    public void agregarInciso(Long idArticulo, Long idInciso) {
        Articulo articulo = articuloRepository.findById(idArticulo).orElseThrow(
                () -> new EntityNotFoundException("No se encontró el adicional con el ID: " + idArticulo));

        Inciso inciso = incisoService.findById(idInciso).get();
        inciso.setArticulo(articulo);
        incisoService.save(inciso);
        articulo.getIncisos().add(inciso);
        articuloRepository.save(articulo);
    }

    public void agregarSubArticulo(Long idArticulo, Long idSubArticulo) {
        Articulo articulo = articuloRepository.findById(idArticulo).orElseThrow(
                () -> new EntityNotFoundException("No se encontró el adicional con el ID: " + idArticulo));

        Articulo subArticulo = articuloRepository.findById(idSubArticulo).get();
        subArticulo.setArticulo(articulo);
        articuloRepository.save(subArticulo);
        articulo.getSubArticulos().add(subArticulo);
        articuloRepository.save(articulo);
    }

}
