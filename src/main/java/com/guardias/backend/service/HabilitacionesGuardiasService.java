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
import com.guardias.backend.entity.HabilitacionesGuardia;
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
    @Autowired
    HabilitacionesGuardiasRepository habilitacionesGuardiasRepository;

    public Optional<List<HabilitacionesGuardia>> findByActivoTrue() {
        return habilitacionesGuardiasRepository.findByActivoTrue();
    }

    public List<HabilitacionesGuardia> findAll() {
        return habilitacionesGuardiasRepository.findAll();
    }

    public boolean activo(Long id) {
        return (habilitacionesGuardiasRepository.existsById(id)
                && habilitacionesGuardiasRepository.findById(id).get().isActivo());
    }

    public Optional<HabilitacionesGuardia> findById(Long id) {
        return habilitacionesGuardiasRepository.findById(id);
    }

    public boolean activoByPersona(Long idPersona) {
        return (habilitacionesGuardiasRepository.existsByPersonaId(idPersona)
                && habilitacionesGuardiasRepository.findByPersonaId(idPersona).get().isActivo());
    }

    public Optional<HabilitacionesGuardia> findByPersona(Long idPersona) {
        return habilitacionesGuardiasRepository.findByPersonaId(idPersona);
    }

    public ResponseEntity<?> validations(HabilitacionesGuardiasDto permisosDto) {
        if (permisosDto.getIdPersona() == null)
            return new ResponseEntity(new Mensaje("el id de la persona es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public HabilitacionesGuardia createUpdate(HabilitacionesGuardia habilitacionesGuardias,
            HabilitacionesGuardiasDto permisosDto) {

        if (habilitacionesGuardias.getPersona() == null
                || !Objects.equals(habilitacionesGuardias.getPersona().getId(), permisosDto.getIdPersona()))
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

    public void save(HabilitacionesGuardia permisos) {
        habilitacionesGuardiasRepository.save(permisos);
    }

    public boolean existsById(Long id) {
        return habilitacionesGuardiasRepository.existsById(id);
    }

    public void deleteById(Long id) {
        habilitacionesGuardiasRepository.deleteById(id);
    }

    public boolean tieneHabilitacionesGuardias(Long idPersona, Long idEfector) {

        if (!personService.activoById(idPersona)) {
            throw new EntityNotFoundException("El asistencial con ID " + idPersona + " no existe.");
        }

        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con ID " + idEfector + " no existe.");
        }

        Optional<HabilitacionesGuardia> optionalHabilitacion = habilitacionesGuardiasRepository
                .findByPersonaIdAndActivoTrue(idPersona);

        if (optionalHabilitacion.isPresent()) {
            
            HabilitacionesGuardia habilitacion = optionalHabilitacion.get();
            // Verificar si la lista de efectores contiene el idEfector
            return habilitacion.getEfectores() != null &&
                    habilitacion.getEfectores().stream()
                            .anyMatch(efector -> efector.getId().equals(idEfector));
        }
        return false; // Retorna false si no hay una habilitacion de guardia activa o no se encuentra el idEfector

    }

    public List<HabilitacionesGuardia> getHabilitacionesGuardiasByEfectorAndAsistencial(Long idEfector) {
        return habilitacionesGuardiasRepository.findHabilitacionesGuardiasByEfectorAndAsistencial(idEfector);
    }
}
