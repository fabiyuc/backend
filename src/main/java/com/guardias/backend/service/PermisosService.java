package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.PermisosDto;
import com.guardias.backend.entity.Permisos;
import com.guardias.backend.repository.CapsRepository;
import com.guardias.backend.repository.HospitalRepository;
import com.guardias.backend.repository.MinisterioRepository;
import com.guardias.backend.repository.PermisosRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PermisosService {

    @Autowired
    PermisosRepository permisosRepository;
    @Autowired
    HospitalRepository hospitalRepository;
    @Autowired
    MinisterioRepository ministerioRepository;
    @Autowired
    CapsRepository capsRepository;

    public Optional<List<Permisos>> findByActivoTrue() {
        return permisosRepository.findByActivoTrue();
    }

    public List<Permisos> findAll() {
        return permisosRepository.findAll();
    }

    public boolean activo(Long id) {
        return (permisosRepository.existsById(id) && permisosRepository.findById(id).get().isActivo());
    }

    public Optional<Permisos> findById(Long id) {
        return permisosRepository.findById(id);
    }

    public boolean activoByAsistencial(Long idAsistencial) {
        return (permisosRepository.existsByIdAsistencial(idAsistencial)
                && permisosRepository.findByIdAsistencial(idAsistencial).get().isActivo());
    }

    public Optional<Permisos> findByIdAsistencial(Long idAsistencial) {
        return permisosRepository.findByIdAsistencial(idAsistencial);
    }

    public ResponseEntity<?> validations(PermisosDto permisosDto) {
        if (permisosDto.getIdAsistencial() == null)
            return new ResponseEntity(new Mensaje("el id del asistencial es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Permisos createUpdate(Permisos permisos, PermisosDto permisosDto) {

        if (!permisosDto.getIdAsistencial().equals(permisos.getIdAsistencial()))
            permisos.setIdAsistencial(permisosDto.getIdAsistencial());

        // Actualiza idAsistencial si es diferente
        if (!permisosDto.getIdAsistencial().equals(permisos.getIdAsistencial())) {
            permisos.setIdAsistencial(permisosDto.getIdAsistencial());
        }

        if (permisosDto.getIdEfectores() != null) {
            Set<Long> currentEfectorIds = new HashSet<>(permisos.getIdEfectores());
            List<Long> newIds = permisosDto.getIdEfectores().stream()
                    .filter(id -> !currentEfectorIds.contains(id))
                    .collect(Collectors.toList());
    
            // Valida solo los nuevos IDs
            List<Long> validNewIds = findValidIdsAcrossSubclasses(newIds);
            if (validNewIds.size() != newIds.size()) {
                throw new IllegalArgumentException("Algunos IDs de efectores no son válidos.");
            }
    
            permisos.setIdEfectores(new ArrayList<>(permisosDto.getIdEfectores()));
        }

        permisos.setActivo(true);
        return permisos;
    }

    public void save(Permisos permisos) {
        permisosRepository.save(permisos);
    }

    public boolean existsById(Long id) {
        return permisosRepository.existsById(id);
    }

    public void deleteById(Long id) {
        permisosRepository.deleteById(id);
    }

    public List<Long> findValidIdsAcrossSubclasses(List<Long> ids) {
        List<Long> validIds = new ArrayList<>();
        validIds.addAll(hospitalRepository.findValidIds(ids));
        validIds.addAll(capsRepository.findValidIds(ids));
        validIds.addAll(ministerioRepository.findValidIds(ids));
        return validIds;
    }

}
