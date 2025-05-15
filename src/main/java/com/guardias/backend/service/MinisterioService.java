package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.efector.EfectorMinisterioDto;
import com.guardias.backend.dto.efector.EfectorSummaryDto;
import com.guardias.backend.entity.Ministerio;
import com.guardias.backend.repository.MinisterioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MinisterioService {

    @Autowired
    MinisterioRepository ministerioRepository;

    public Optional<List<Ministerio>> findByActivoTrue() {
        return ministerioRepository.findByActivoTrue();
    }

    public List<Ministerio> findAll() {
        return ministerioRepository.findAll();
    }

    public Optional<Ministerio> findById(Long id) {
        return ministerioRepository.findById((Long) id);
    }

    public Optional<Ministerio> findByNombre(String nombre) {
        return ministerioRepository.findByNombre((String) nombre);
    }

    public boolean existsById(Long id) {
        return ministerioRepository.existsById((Long) id);
    }

    public boolean existsByNombre(String nombre) {
        return ministerioRepository.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return (ministerioRepository.existsByNombre(nombre)
                && ministerioRepository.findByNombre(nombre).get().isActivo());
    }

    public boolean activo(Long id) {
        return (ministerioRepository.existsById(id)
                && ministerioRepository.findById(id).get().isActivo());
    }

    public void save(Ministerio ministerio) {
        ministerioRepository.save(ministerio);
    }

    public void deleteById(Long id) {
        ministerioRepository.deleteById((Long) id);
    }

    public boolean isMinisterio(Long id) {
        Optional<Ministerio> ministerio = ministerioRepository.findById(id);
        return ministerio.isPresent() && ministerio.get().isActivo();
    }

    public List<EfectorSummaryDto> findActiveEfectors() {
        return ministerioRepository.findByActivoTrue()
                .orElse(new ArrayList<>())
                .stream()
                .map(ministerio -> new EfectorSummaryDto(ministerio.getId(), ministerio.getNombre()))
                .collect(Collectors.toList());
    }

    public Optional<EfectorMinisterioDto> findByIdNombre(Long id) {
        Optional<Ministerio> ministerio = ministerioRepository.findById(id);
        if (ministerio.isPresent()) {
            return Optional.of(new EfectorMinisterioDto(
                    ministerio.get().getId(),
                    ministerio.get().getNombre()));
        }
        return Optional.empty();
    }
}
