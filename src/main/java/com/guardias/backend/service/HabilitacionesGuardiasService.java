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
import com.guardias.backend.dto.HabilitacionesGuardiasDto;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.HabilitacionesGuardias;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.CapsRepository;
import com.guardias.backend.repository.HospitalRepository;
import com.guardias.backend.repository.MinisterioRepository;
import com.guardias.backend.repository.HabilitacionesGuardiasRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class HabilitacionesGuardiasService {

    @Autowired
    HabilitacionesGuardiasRepository permisosRepository;
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

    public Optional<List<HabilitacionesGuardias>> findByActivoTrue() {
        return permisosRepository.findByActivoTrue();
    }

    public List<HabilitacionesGuardias> findAll() {
        return permisosRepository.findAll();
    }

    public boolean activo(Long id) {
        return (permisosRepository.existsById(id) && permisosRepository.findById(id).get().isActivo());
    }

    public Optional<HabilitacionesGuardias> findById(Long id) {
        return permisosRepository.findById(id);
    }

    public boolean activoByPersona(Long idPersona) {
        return (permisosRepository.existsByPersonaId(idPersona)
                && permisosRepository.findByPersonaId(idPersona).get().isActivo());
    }

    public Optional<HabilitacionesGuardias> findByPersona(Long idPersona) {
        return permisosRepository.findByPersonaId(idPersona);
    }

    public ResponseEntity<?> validations(HabilitacionesGuardiasDto permisosDto) {
        if (permisosDto.getIdPersona() == null)
            return new ResponseEntity(new Mensaje("el id de la persona es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public HabilitacionesGuardias createUpdate(HabilitacionesGuardias habilitacionesGuardias, HabilitacionesGuardiasDto permisosDto) {

        if (habilitacionesGuardias.getPersona() == null || !Objects.equals(habilitacionesGuardias.getPersona().getId(), permisosDto.getIdPersona()))
        habilitacionesGuardias.setPersona(personService.findById(permisosDto.getIdPersona()));

        if (permisosDto.getIdEfectores() != null) {
           
            if (habilitacionesGuardias.getEfectores() == null) {
                habilitacionesGuardias.setEfectores(new ArrayList<>());
            }

            // Crea una nueva lista para almacenar los efectores actualizados
            List<Efector> efectoresActualizados = new ArrayList<>();
            for (Efector efector : habilitacionesGuardias.getEfectores()) {
                if (permisosDto.getIdEfectores().contains(efector.getId())) {
                    efectoresActualizados.add(efector);
                } else {
                    // Remover el legajo de los efectores que se eliminarán
                    efector.getHabilitacionesGuardias().remove(habilitacionesGuardias);
                }
            }
            habilitacionesGuardias.setEfectores(efectoresActualizados);

            // agrega nuevos efectores si no estan presentes
            for (Long id : permisosDto.getIdEfectores()) {
                boolean found = false;
                for (Efector efector : habilitacionesGuardias.getEfectores()) {
                    if (efector.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Efector efectorToAdd = efectorService.findById(id);
                    if (efectorToAdd != null) {
                        habilitacionesGuardias.getEfectores().add(efectorToAdd);
                        efectorToAdd.getHabilitacionesGuardias().add(habilitacionesGuardias);
                    } else {
                        throw new RuntimeException("No se encontró el efector con ID: " + id);
                    }
                }
            }
        }

        habilitacionesGuardias.setActivo(true);
        return habilitacionesGuardias;
    }

    public void save(HabilitacionesGuardias permisos) {
        permisosRepository.save(permisos);
    }

    public boolean existsById(Long id) {
        return permisosRepository.existsById(id);
    }

    public void deleteById(Long id) {
        permisosRepository.deleteById(id);
    }

    public boolean tieneHabilitacionesGuardias(Long idPersona, Long idEfector) {

        if (!personService.activoById(idPersona)) {
            throw new EntityNotFoundException("El asistencial con ID " + idPersona + " no existe.");
        }

        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con ID " + idEfector + " no existe.");
        }

        Optional<HabilitacionesGuardias> optionalPermiso = permisosRepository.findByPersonaIdAndActivoTrue(idPersona);

        if (optionalPermiso.isPresent()) {
            HabilitacionesGuardias permiso = optionalPermiso.get();
            // Verificar si la lista de efectores contiene el idEfector
            return permiso.getEfectores() != null && permiso.getEfectores().contains(idEfector);
        }
        
        return false; // Retorna false si no hay un permiso activo o no se encuentra el idEfector
    
    }

    public List<HabilitacionesGuardias> getHabilitacionesGuardiasByEfectorAndAsistencial(Long idEfector) {
        return permisosRepository.findHabilitacionesGuardiasByEfectorAndAsistencial(idEfector);
    }
}
