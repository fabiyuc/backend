package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.HabilitacionesGuardiasDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.HabilitacionesGuardia;
import com.guardias.backend.enums.LocationEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.CapsRepository;
import com.guardias.backend.repository.HabilitacionesGuardiasRepository;
import com.guardias.backend.repository.HospitalRepository;
import com.guardias.backend.repository.MinisterioRepository;

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
    AsistencialService asistencialService;
    @Autowired
    HabilitacionesGuardiasRepository habilitacionesGuardiasRepository;
    @Autowired
    CapsService capsService;
    @Autowired
    HospitalService hospitalService;

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

    public boolean activoByAsistencial(Long idAsistencial) {
        return (habilitacionesGuardiasRepository.existsByAsistencialId(idAsistencial)
                && habilitacionesGuardiasRepository.findByAsistencialId(idAsistencial).get().isActivo());
    }

    public Optional<HabilitacionesGuardia> findByAsistencial(Long idAsistencial) {
        return habilitacionesGuardiasRepository.findByAsistencialId(idAsistencial);
    }

    public ResponseEntity<?> validations(HabilitacionesGuardiasDto permisosDto) {
        if (permisosDto.getIdAsistencial() == null)
            return new ResponseEntity(new Mensaje("el id del asistencial es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        // Nueva validación para tipoEfector y idEfectores
        try {
            if (permisosDto.getTipoEfector() != null) {
                LocationEnum tipoEfector = LocationEnum.valueOf(permisosDto.getTipoEfector().toString());

                if (LocationEnum.CAPS == tipoEfector) {
                    if (permisosDto.getIdEfectores() == null ||
                            permisosDto.getIdEfectores().isEmpty()) {
                        return new ResponseEntity<>(
                                new Mensaje("El idEfectores es obligatorio para el tipo CAPS"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isCapsValid = capsService.existsById(permisosDto.getIdEfectores().get(0));
                    if (!isCapsValid) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores no corresponde a un CAPS válido"),
                                HttpStatus.BAD_REQUEST);
                    }
                }

                if (LocationEnum.HOSPITAL == tipoEfector) {
                    if (permisosDto.getIdEfectores() == null ||
                            permisosDto.getIdEfectores().isEmpty()) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores es obligatorio para el tipo HOSPITAL"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isHospitalValid = hospitalService.existsById(permisosDto.getIdEfectores().get(0));
                    if (!isHospitalValid) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores no corresponde a un HOSPITAL válido"),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje("El tipoEfector no es válido"),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public HabilitacionesGuardia createUpdate(HabilitacionesGuardia habilitacionesGuardias,
            HabilitacionesGuardiasDto habilitacionesGuardiasDto) {

        if (habilitacionesGuardias.getAsistencial() == null
                || !Objects.equals(habilitacionesGuardias.getAsistencial().getId(),
                        habilitacionesGuardiasDto.getIdAsistencial()))
            habilitacionesGuardias
                    .setAsistencial(asistencialService.findById(habilitacionesGuardiasDto.getIdAsistencial()).get());

        if (habilitacionesGuardiasDto.getIdEfectores() != null) {

            if (habilitacionesGuardias.getEfectores() == null) {
                habilitacionesGuardias.setEfectores(new ArrayList<>());
            }

            // Crea una nueva lista para almacenar los efectores actualizados
            List<Efector> efectoresActualizados = new ArrayList<>();
            for (Efector efector : habilitacionesGuardias.getEfectores()) {
                if (habilitacionesGuardiasDto.getIdEfectores().contains(efector.getId())) {
                    efectoresActualizados.add(efector);
                } else {
                    // Remover el legajo de los efectores que se eliminarán
                    efector.getHabilitacionesGuardias().remove(habilitacionesGuardias);
                }
            }
            habilitacionesGuardias.setEfectores(efectoresActualizados);

            // agrega nuevos efectores si no estan presentes
            for (Long id : habilitacionesGuardiasDto.getIdEfectores()) {
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

        if (habilitacionesGuardiasDto.getTipoEfector() != null) {
            habilitacionesGuardias.setTipoEfector(habilitacionesGuardiasDto.getTipoEfector());
            System.out.println("tipoEfector: " + habilitacionesGuardiasDto.getTipoEfector()); // Verificar el valor

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

    public boolean tieneHabilitacionesGuardias(Long idAsistencial, Long idEfector) {

        if (!personService.activoById(idAsistencial)) {
            throw new EntityNotFoundException("El asistencial con ID " + idAsistencial + " no existe.");
        }

        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con ID " + idEfector + " no existe.");
        }

        Optional<HabilitacionesGuardia> optionalHabilitacion = habilitacionesGuardiasRepository
                .findByAsistencialIdAndActivoTrue(idAsistencial);

        if (optionalHabilitacion.isPresent()) {

            HabilitacionesGuardia habilitacion = optionalHabilitacion.get();
            // Verificar si la lista de efectores contiene el idEfector
            return habilitacion.getEfectores() != null &&
                    habilitacion.getEfectores().stream()
                            .anyMatch(efector -> efector.getId().equals(idEfector));
        }
        return false; // Retorna false si no hay una habilitacion de guardia activa o no se encuentra
                      // el idEfector

    }

    public List<HabilitacionesGuardia> getHabilitacionesGuardiasByEfectorAndAsistencial(Long idEfector) {
        if (idEfector == null || idEfector <= 0) {
            throw new IllegalArgumentException("El idEfector no es válido.");
        }
        return habilitacionesGuardiasRepository.findHabilitacionesGuardiasByEfectorAndAsistencial(idEfector);
    }

    public List<Asistencial> getAsistencialesByEfectorAndTG(Long idEfector, String tipoGuardia) {

        TipoGuardiaEnum tipoGuardiaEnum;

        if (idEfector == null || idEfector <= 0) {
            throw new IllegalArgumentException("El idEfector no es válido.");
        }
        try {
            tipoGuardiaEnum = TipoGuardiaEnum.valueOf(tipoGuardia.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El tipo de guardia proporcionado no es válido: " + tipoGuardia);
        }
        return habilitacionesGuardiasRepository.findByEfectorAndActivoTrueAndTG(idEfector, tipoGuardiaEnum);
    }
}
