package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.efector.EfectorCapsDto;
import com.guardias.backend.dto.efector.EfectorSummaryDto;
import com.guardias.backend.entity.Caps;
import com.guardias.backend.repository.CapsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CapsService {

    @Autowired
    CapsRepository capsRepository;

    public Optional<List<Caps>> findByActivoTrue() {
        return capsRepository.findByActivoTrue();
    }

    public List<Caps> findAll() {
        return capsRepository.findAll();
    }

    public Optional<Caps> findById(Long id) {
        return capsRepository.findById((Long) id);
    }

    public Optional<Caps> findByNombre(String nombre) {
        return capsRepository.findByNombre(nombre);
    }

    public void save(Caps caps) {
        capsRepository.save(caps);
    }

    public void deleteById(Long id) {
        capsRepository.deleteById((Long) id);
    }

    public boolean existsById(Long id) {
        return capsRepository.existsById((Long) id);
    }

    public boolean existsByNombre(String nombre) {
        return capsRepository.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return (capsRepository.existsByNombre(nombre)
                && capsRepository.findByNombre(nombre).get().isActivo());
    }

    public boolean activo(Long id) {
        return (capsRepository.existsById(id) && capsRepository.findById(id).get().isActivo());
    }

    public boolean isCaps(Long id) {
        Optional<Caps> caps = capsRepository.findById(id);
        return caps.isPresent() && caps.get().isActivo();
    }

    public List<EfectorSummaryDto> findActiveEfectors() {
        return capsRepository.findByActivoTrue()
                .orElse(new ArrayList<>())
                .stream()
                .map(caps -> new EfectorSummaryDto(caps.getId(), caps.getNombre()))
                .collect(Collectors.toList());
    }

    public Optional<EfectorCapsDto> findByIdNombre(Long id) {
        Optional<Caps> caps = capsRepository.findById(id);
        if (caps.isPresent()) {
            return Optional.of(new EfectorCapsDto(
                    caps.get().getId(),
                    caps.get().getNombre()));
        }
        return Optional.empty();
    }
}
