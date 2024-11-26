package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.PermisosDto;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Permisos;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.CapsRepository;
import com.guardias.backend.repository.HospitalRepository;
import com.guardias.backend.repository.MinisterioRepository;
import com.guardias.backend.repository.PermisosRepository;

import jakarta.persistence.EntityNotFoundException;
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
    @Autowired
    AsistencialRepository asistencialRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    PersonService personService;

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

    public boolean activoByPersona(Long idPersona) {
        return (permisosRepository.existsByPersonaId(idPersona)
                && permisosRepository.findByPersonaId(idPersona).get().isActivo());
    }

    public Optional<Permisos> findByPersona(Long idPersona) {
        return permisosRepository.findByPersonaId(idPersona);
    }

    public ResponseEntity<?> validations(PermisosDto permisosDto) {
        if (permisosDto.getIdPersona() == null)
            return new ResponseEntity(new Mensaje("el id de la persona es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Permisos createUpdate(Permisos permisos, PermisosDto permisosDto) {

        if (permisos.getPersona() == null || !Objects.equals(permisos.getPersona().getId(), permisosDto.getIdPersona()))
            permisos.setPersona(personService.findById(permisosDto.getIdPersona()));

        if (permisosDto.getIdEfectores() != null) {
           
            if (permisos.getEfectores() == null) {
                permisos.setEfectores(new ArrayList<>());
            }

            // Crea una nueva lista para almacenar los efectores actualizados
            List<Efector> efectoresActualizados = new ArrayList<>();
            for (Efector efector : permisos.getEfectores()) {
                if (permisosDto.getIdEfectores().contains(efector.getId())) {
                    efectoresActualizados.add(efector);
                } else {
                    // Remover el legajo de los efectores que se eliminarán
                    efector.getPermisos().remove(permisos);
                }
            }
            permisos.setEfectores(efectoresActualizados);

            // agrega nuevos efectores si no estan presentes
            for (Long id : permisosDto.getIdEfectores()) {
                boolean found = false;
                for (Efector efector : permisos.getEfectores()) {
                    if (efector.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Efector efectorToAdd = efectorService.findById(id);
                    if (efectorToAdd != null) {
                        permisos.getEfectores().add(efectorToAdd);
                        efectorToAdd.getPermisos().add(permisos);
                    } else {
                        throw new RuntimeException("No se encontró el efector con ID: " + id);
                    }
                }
            }
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

    public boolean tienePermisos(Long idPersona, Long idEfector) {

        if (!personService.activoById(idPersona)) {
            throw new EntityNotFoundException("El asistencial con ID " + idPersona + " no existe.");
        }

        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con ID " + idEfector + " no existe.");
        }

        Optional<Permisos> optionalPermiso = permisosRepository.findByPersonaIdAndActivoTrue(idPersona);

        if (optionalPermiso.isPresent()) {
            Permisos permiso = optionalPermiso.get();
            // Verificar si la lista de efectores contiene el idEfector
            return permiso.getEfectores() != null && permiso.getEfectores().contains(idEfector);
        }
        
        return false; // Retorna false si no hay un permiso activo o no se encuentra el idEfector
    
    }

    public List<Permisos> getPermisosByEfectorAndAsistencial(Long idEfector) {
        return permisosRepository.findPermisosByEfectorAndAsistencial(idEfector);
    }
}
