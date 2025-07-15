package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.HabilitacionesGeneralesDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.HabilitacionesGenerales;
import com.guardias.backend.entity.Region;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.CapsRepository;
import com.guardias.backend.repository.HabilitacionesGeneralesRepository;
import com.guardias.backend.repository.HospitalRepository;
import com.guardias.backend.repository.MinisterioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class HabilitacionesGeneralesService {

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
    HabilitacionesGeneralesRepository habilitacionesGeneralesRepository;
    @Autowired
    CapsService capsService;
    @Autowired
    HospitalService hospitalService;

    @Autowired
    RegionService regionService;
    /*
     * @Autowired
     * AutoridadService autoridadService;
     */

    public Optional<List<HabilitacionesGenerales>> findByActivoTrue() {
        return habilitacionesGeneralesRepository.findByActivoTrue();
    }

    public List<HabilitacionesGenerales> findAll() {
        return habilitacionesGeneralesRepository.findAll();
    }

    public boolean activo(Long id) {
        return (habilitacionesGeneralesRepository.existsById(id)
                && habilitacionesGeneralesRepository.findById(id).get().isActivo());
    }

    public Optional<HabilitacionesGenerales> findById(Long id) {
        return habilitacionesGeneralesRepository.findById(id);
    }

    public boolean activoByPersona(Long idPersona) {
        return (habilitacionesGeneralesRepository.existsByPersonaId(idPersona)
                && habilitacionesGeneralesRepository.findByPersonaId(idPersona).get().isActivo());
    }

    public Optional<HabilitacionesGenerales> findByPersona(Long idPersona) {
        return habilitacionesGeneralesRepository.findByPersonaId(idPersona);
    }

    public Optional<HabilitacionesGenerales> findByPersonaAndActivoTrue(Long idPersona) {
        return habilitacionesGeneralesRepository.findByPersonaIdAndActivoTrue(idPersona);
    }

    public ResponseEntity<?> validations(HabilitacionesGeneralesDto habilitacionesGeneralesDto) {
        if (habilitacionesGeneralesDto.getIdPersona() == null)
            return new ResponseEntity(new Mensaje("el id de la persona es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        /*
         * // Nueva validación para tipoEfector y idEfectores
         * try {
         * if (habilitacionesGeneralesDto.getTipoEfectorEx() != null) {
         * LocationEnum tipoEfectorEx = LocationEnum
         * .valueOf(habilitacionesGeneralesDto.getTipoEfectorEx().toString());
         * 
         * if (LocationEnum.CAPS == tipoEfectorEx) {
         * if (habilitacionesGeneralesDto.getIdEfectores() == null ||
         * habilitacionesGeneralesDto.getIdEfectores().isEmpty()) {
         * return new ResponseEntity<>(
         * new Mensaje("El idEfectores es obligatorio para el tipo CAPS"),
         * HttpStatus.BAD_REQUEST);
         * }
         * boolean isCapsValid =
         * capsService.existsById(habilitacionesGeneralesDto.getIdEfectores().get(0));
         * if (!isCapsValid) {
         * return new ResponseEntity<>(new
         * Mensaje("El idEfectores no corresponde a un CAPS válido"),
         * HttpStatus.BAD_REQUEST);
         * }
         * }
         * 
         * if (LocationEnum.HOSPITAL == tipoEfectorEx) {
         * if (habilitacionesGeneralesDto.getIdEfectores() == null ||
         * habilitacionesGeneralesDto.getIdEfectores().isEmpty()) {
         * return new ResponseEntity<>(new
         * Mensaje("El idEfectores es obligatorio para el tipo HOSPITAL"),
         * HttpStatus.BAD_REQUEST);
         * }
         * boolean isHospitalValid = hospitalService
         * .existsById(habilitacionesGeneralesDto.getIdEfectores().get(0));
         * if (!isHospitalValid) {
         * return new ResponseEntity<>(new
         * Mensaje("El idEfectores no corresponde a un HOSPITAL válido"),
         * HttpStatus.BAD_REQUEST);
         * }
         * }
         * }
         * } catch (IllegalArgumentException e) {
         * return new ResponseEntity<>(new Mensaje("El tipoEfector no es válido"),
         * HttpStatus.BAD_REQUEST);
         * }
         */

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public HabilitacionesGenerales createUpdate(HabilitacionesGenerales habilitacionesGenerales,
            HabilitacionesGeneralesDto habilitacionesGeneralesDto) {

        if (habilitacionesGenerales.getPersona() == null || !Objects
                .equals(habilitacionesGenerales.getPersona().getId(), habilitacionesGeneralesDto.getIdPersona()))
            habilitacionesGenerales.setPersona(personService.findById(habilitacionesGeneralesDto.getIdPersona()));

        if (habilitacionesGeneralesDto.getIdEfectores() != null) {

            if (habilitacionesGenerales.getEfectores() == null) {
                habilitacionesGenerales.setEfectores(new ArrayList<>());
            }

            // Crea una nueva lista para almacenar los efectores actualizados
            List<Efector> efectoresActualizados = new ArrayList<>();
            for (Efector efector : habilitacionesGenerales.getEfectores()) {
                if (habilitacionesGeneralesDto.getIdEfectores().contains(efector.getId())) {
                    efectoresActualizados.add(efector);
                } else {
                    // Remover el legajo de los efectores que se eliminarán
                    efector.getHabilitacionesGenerales().remove(habilitacionesGenerales);
                }
            }
            habilitacionesGenerales.setEfectores(efectoresActualizados);

            // agrega nuevos efectores si no estan presentes
            for (Long id : habilitacionesGeneralesDto.getIdEfectores()) {
                boolean found = false;
                for (Efector efector : habilitacionesGenerales.getEfectores()) {
                    if (efector.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Efector efectorToAdd = efectorService.findById(id);
                    if (efectorToAdd != null) {
                        habilitacionesGenerales.getEfectores().add(efectorToAdd);
                        efectorToAdd.getHabilitacionesGenerales().add(habilitacionesGenerales);
                    } else {
                        throw new RuntimeException("No se encontró el efector con ID: " + id);
                    }
                }
            }
        }
        /*
         * if (habilitacionesGeneralesDto.getTipoEfectorEx() != null) {
         * habilitacionesGenerales.setTipoEfectorEx(habilitacionesGeneralesDto.
         * getTipoEfectorEx());
         * System.out.println("tipoEfector: " +
         * habilitacionesGeneralesDto.getTipoEfectorEx()); // Verificar el valor
         * 
         * }
         */

        // Manejar el caso cuando activo es null, por defecto establecer como true
        Boolean activo = habilitacionesGeneralesDto.getActivo();
        habilitacionesGenerales.setActivo(activo != null ? activo : true);
        return habilitacionesGenerales;
    }

    public void save(HabilitacionesGenerales habilitacionesGenerales) {
        habilitacionesGeneralesRepository.save(habilitacionesGenerales);
    }

    public boolean existsById(Long id) {
        return habilitacionesGeneralesRepository.existsById(id);
    }

    public void deleteById(Long id) {
        habilitacionesGeneralesRepository.deleteById(id);
    }

    public boolean tieneHabilitacionesGenerales(Long idPersona, Long idEfector) {

        if (!personService.activoById(idPersona)) {
            throw new EntityNotFoundException("El asistencial con ID " + idPersona + " no existe.");
        }

        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con ID " + idEfector + " no existe.");
        }

        Optional<HabilitacionesGenerales> optionalHabilitacion = habilitacionesGeneralesRepository
                .findByPersonaIdAndActivoTrue(idPersona);

        if (optionalHabilitacion.isPresent()) {
            HabilitacionesGenerales habilitacion = optionalHabilitacion.get();
            // Verificar si la lista de efectores contiene el idEfector
            return habilitacion.getEfectores() != null &&
                    habilitacion.getEfectores().stream()
                            .anyMatch(efector -> efector.getId().equals(idEfector));
        }

        return false;
    }

    public List<HabilitacionesGenerales> getHabilitacionesGeneralesByEfectorAndAsistencial(Long idEfector) {
        return habilitacionesGeneralesRepository.findHabilitacionesGeneralesByEfectorAndAsistencial(idEfector);
    }

    public ResponseEntity<?> validarHabilitacion(Long idPersona, Long idRegion) {

        if (personService.findById(idPersona) == null) {
            throw new EntityNotFoundException("No se encontró la persona con id: " + idPersona);
        }

        // Obtener la región
        Region region = regionService.findById(idRegion).get();
        if (region == null) {
            throw new EntityNotFoundException("Region no encontrada");
        }

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public HabilitacionesGenerales addHabilitaciones(Long idPersona, Long idRegion) {

        HabilitacionesGenerales habilitacionExistente = findByPersonaAndActivoTrue(idPersona).orElse(null);
        Region region = regionService.findById(idRegion).get();

        if (habilitacionExistente == null) {

            List<Long> idsEfectores = region.getEfectores().stream()
                    .map(Efector::getId)
                    .collect(Collectors.toList());

            HabilitacionesGeneralesDto habilitacionDto = new HabilitacionesGeneralesDto();
            habilitacionDto.setIdPersona(idPersona);
            habilitacionDto.setIdEfectores(idsEfectores);

            HabilitacionesGenerales habilitacionesGenerales = createUpdate(new HabilitacionesGenerales(),
                    habilitacionDto);

            return habilitacionesGenerales;

        } else {
            for (Efector efectorDeRegion : region.getEfectores()) {
                boolean found = false;
                for (Efector efectorExistente : habilitacionExistente.getEfectores()) {
                    if (efectorExistente.equals(efectorDeRegion)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    habilitacionExistente.getEfectores().add(efectorDeRegion);
                    efectorDeRegion.getHabilitacionesGenerales().add(habilitacionExistente);
                }
            }
        }
        return habilitacionExistente;
    }

}
