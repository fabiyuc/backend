package com.guardias.backend.service;

import java.io.InputStream;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.LegajoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.legajo.LegajoActualDto;
import com.guardias.backend.dto.legajo.LegajoBajaDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Especialidad;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.LocationEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.AutoridadRepository;
import com.guardias.backend.repository.LegajoRepository;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@Service
@Transactional
public class LegajoService {

    @Autowired
    LegajoRepository legajoRepository;

    @Autowired
    AutoridadRepository autoridadRepository;
    @Autowired
    AsistencialRepository asistencialRepository;
    @Autowired
    TipoGuardiaService tipoGuardiaService;
    @Autowired
    PersonService personService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    RevistaService revistaService;
    @Autowired
    ProfesionService profesionService;
    @Autowired
    CargoService cargoService;
    @Autowired
    SuspencionService suspencionService;
    @Autowired
    EspecialidadService especialidadService;
    @Autowired
    RegionService regionService;
    @Autowired
    MinisterioService ministerioService;
    @Autowired
    CapsService capsService;
    @Autowired
    HospitalService hospitalService;

    public List<Legajo> findByActivoTrue() {
        return legajoRepository.findByActivoTrue();
    }

    public List<Legajo> findAllByActivoFalseByPersonAndEfector(Long idPersona, Long idEfector) {
        if (idPersona == null) {
            throw new IllegalArgumentException("El idPersona no puede ser nulo");
        }
        if (idEfector == null) {
            throw new IllegalArgumentException("El idEfector no puede ser nulo");
        }
        return legajoRepository.findByActivoFalseByPersonAndEfector(idPersona, idEfector);
    }

    public List<Legajo> findAll() {
        return legajoRepository.findAll();
    }

    public Optional<Legajo> findById(Long id) {
        return legajoRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return legajoRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (legajoRepository.existsById(id) && legajoRepository.findById(id).get().isActivo());
    }

    public void save(Legajo legajo) {
        legajoRepository.save(legajo);
    }

    public void deleteById(Long id) {
        legajoRepository.deleteById(id);
    }

    public Optional<Legajo> findLegajoAutoridadByPersonaId(Long personaId) {
        return legajoRepository.findLegajoAutoridadByPersonaId(personaId);
    }

    public ResponseEntity<?> validations(LegajoDto legajoDto, Long id) {
        if (legajoDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("La fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        if (legajoDto.getEsAutoridad() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar si es autoridad o no"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getIdPersona() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la persona"),
                    HttpStatus.BAD_REQUEST);

        // Nueva validación para tipoUdo y idUdo
        try {
            if (legajoDto.getTipoUdo() != null) {
                LocationEnum tipoUdo = LocationEnum.valueOf(legajoDto.getTipoUdo().toString());

                if (LocationEnum.MINISTERIO == tipoUdo) {
                    if (legajoDto.getIdUdo() == null) {
                        return new ResponseEntity<>(new Mensaje("El idUdo es obligatorio para el tipo Ministerio"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isMinisterioValid = ministerioService.existsById(legajoDto.getIdUdo());
                    if (!isMinisterioValid) {
                        return new ResponseEntity<>(new Mensaje("El idUdo no corresponde a un Ministerio válido"),
                                HttpStatus.BAD_REQUEST);
                    }
                }

                if (LocationEnum.CAPS == tipoUdo) {
                    if (legajoDto.getIdUdo() == null) {
                        return new ResponseEntity<>(new Mensaje("El idUdo es obligatorio para el tipo CAPS"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isCapsValid = capsService.existsById(legajoDto.getIdUdo());
                    if (!isCapsValid) {
                        return new ResponseEntity<>(new Mensaje("El idUdo no corresponde a un CAPS válido"),
                                HttpStatus.BAD_REQUEST);
                    }
                }

                if (LocationEnum.HOSPITAL == tipoUdo) {
                    if (legajoDto.getIdUdo() == null) {
                        return new ResponseEntity<>(new Mensaje("El idUdo es obligatorio para el tipo HOSPITAL"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isHospitalValid = hospitalService.existsById(legajoDto.getIdUdo());
                    if (!isHospitalValid) {
                        return new ResponseEntity<>(new Mensaje("El idUdo no corresponde a un HOSPITAL válido"),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje("El tipoUdo no es válido"),
                    HttpStatus.BAD_REQUEST);
        }

        // Nueva validación para tipoEfector y idEfectores

        try {
            if (legajoDto.getTipoEfector() != null) {
                LocationEnum tipoEfector = LocationEnum.valueOf(legajoDto.getTipoEfector().toString());

                if (LocationEnum.MINISTERIO == tipoEfector) {
                    if (legajoDto.getIdEfectores() == null || legajoDto.getIdEfectores().isEmpty()) {
                        return new ResponseEntity<>(
                                new Mensaje("El idEfectores es obligatorio para el tipo Ministerio"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isMinisterioValid = ministerioService.existsById(legajoDto.getIdEfectores().get(0));
                    if (!isMinisterioValid) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores no corresponde a un Ministerio válido"),
                                HttpStatus.BAD_REQUEST);
                    }
                }

                if (LocationEnum.CAPS == tipoEfector) {
                    if (legajoDto.getIdEfectores() == null || legajoDto.getIdEfectores().isEmpty()) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores es obligatorio para el tipo CAPS"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isCapsValid = capsService.existsById(legajoDto.getIdEfectores().get(0));
                    if (!isCapsValid) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores no corresponde a un CAPS válido"),
                                HttpStatus.BAD_REQUEST);
                    }
                }

                if (LocationEnum.HOSPITAL == tipoEfector) {
                    if (legajoDto.getIdEfectores() == null || legajoDto.getIdEfectores().isEmpty()) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores es obligatorio para el tipo HOSPITAL"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isHospitalValid = hospitalService.existsById(legajoDto.getIdEfectores().get(0));
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

        // Nueva validación para tipoEfectorCargo y idEfectores
        try {
            if (legajoDto.getTipoEfectorCargo() != null) {
                LocationEnum tipoEfectorCargo = LocationEnum.valueOf(legajoDto.getTipoEfectorCargo().toString());

                if (LocationEnum.MINISTERIO == tipoEfectorCargo) {
                    if (legajoDto.getIdEfectores() == null || legajoDto.getIdEfectores().isEmpty()) {
                        return new ResponseEntity<>(
                                new Mensaje("El idEfectores es obligatorio para el tipo Ministerio"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isMinisterioValid = ministerioService.existsById(legajoDto.getIdEfectores().get(0));
                    if (!isMinisterioValid) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores no corresponde a un Ministerio válido"),
                                HttpStatus.BAD_REQUEST);
                    }
                }

                if (LocationEnum.CAPS == tipoEfectorCargo) {
                    if (legajoDto.getIdEfectores() == null || legajoDto.getIdEfectores().isEmpty()) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores es obligatorio para el tipo CAPS"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isCapsValid = capsService.existsById(legajoDto.getIdEfectores().get(0));
                    if (!isCapsValid) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores no corresponde a un CAPS válido"),
                                HttpStatus.BAD_REQUEST);
                    }
                }

                if (LocationEnum.HOSPITAL == tipoEfectorCargo) {
                    if (legajoDto.getIdEfectores() == null || legajoDto.getIdEfectores().isEmpty()) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores es obligatorio para el tipo HOSPITAL"),
                                HttpStatus.BAD_REQUEST);
                    }
                    boolean isHospitalValid = hospitalService.existsById(legajoDto.getIdEfectores().get(0));
                    if (!isHospitalValid) {
                        return new ResponseEntity<>(new Mensaje("El idEfectores no corresponde a un HOSPITAL válido"),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje("El tipoEfectorCargo no es válido"),
                    HttpStatus.BAD_REQUEST);
        }

        boolean esAsistencial = personService.activoById(legajoDto.getIdPersona())
                && asistencialService.existsById(legajoDto.getIdPersona());

        boolean esContraFactura = legajoDto.getIdTipoGuardias() != null && legajoDto.getIdTipoGuardias().contains(4L);

        boolean esCargoOAgrupacion = legajoDto.getIdTipoGuardias() != null
                && (legajoDto.getIdTipoGuardias().contains(1L) || legajoDto.getIdTipoGuardias().contains(2L));

        if ((esAsistencial && !esContraFactura) && legajoDto.getEsAutoridad() == false) {
            if (legajoDto.getIdRevista() == null) {
                return new ResponseEntity<>(new Mensaje("Indicar la situación de revista para Asistenciales"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        if ((!esContraFactura && (legajoDto.getEsAutoridad() != null && legajoDto.getEsAutoridad() == false))) {

            if (legajoDto.getIdUdo() == null) {
                return new ResponseEntity<>(new Mensaje("indicar la UdO"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        if ((esAsistencial && esCargoOAgrupacion) || ((!esAsistencial && legajoDto.getEsAutoridad() == false)
                || (legajoDto.getEsAutoridad() == true && legajoDto.getEsRegional() == false))) {

            if (legajoDto.getIdEfectores() == null) {
                return new ResponseEntity<>(new Mensaje("indicar efector"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        if (esAsistencial && legajoDto.getEsAutoridad() == false) {

            if (legajoDto.getMatriculaProvincial() == null)
                return new ResponseEntity<Mensaje>(new Mensaje("la matricula provincial es obligatoria"),
                        HttpStatus.BAD_REQUEST);

            if (legajoRepository.existsByMatriculaProvincialAndActivoTrue(legajoDto.getMatriculaProvincial())) {
                Legajo existingLegajo = legajoRepository
                        .findByMatriculaProvincialAndActivoTrue(legajoDto.getMatriculaProvincial()).get();
                if (!existingLegajo.getId().equals(id)) {
                    return new ResponseEntity<>(new Mensaje("La matricula provincial ya existe"),
                            HttpStatus.BAD_REQUEST);
                }
            }

            if (legajoDto.getIdProfesion() == null)
                return new ResponseEntity<Mensaje>(new Mensaje("indicar la profesion"),
                        HttpStatus.BAD_REQUEST);

            if (legajoDto.getIdTipoGuardias() == null) {
                return new ResponseEntity<>(new Mensaje("Indicar los tipos de guardia para Asistenciales"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        if (legajoDto.getEsAutoridad() == true) {
            if (legajoDto.getEsRegional() == null) {
                return new ResponseEntity<>(new Mensaje("Indicar si es regional para autoridades"),
                        HttpStatus.BAD_REQUEST);
            }

            if (legajoDto.getIdCargo() == null) {
                return new ResponseEntity<>(new Mensaje("Indicar el cargo para autoridades"), HttpStatus.BAD_REQUEST);
            }

            if (legajoDto.getEsRegional() == true) {
                if (legajoDto.getIdRegion() == null) {
                    return new ResponseEntity<>(new Mensaje("Indicar la region para autoridad regional"),
                            HttpStatus.BAD_REQUEST);
                }
            }

            if (StringUtils.isBlank(legajoDto.getNroResolucion()))
                return new ResponseEntity<>(new Mensaje("El numero de resolucion es obligatorio"),
                        HttpStatus.BAD_REQUEST);

            if (legajoDto.getFechaResolucion() == null)
                return new ResponseEntity(new Mensaje("La fecha de resolucion es obligatoria"), HttpStatus.BAD_REQUEST);

            if (StringUtils.isBlank(legajoDto.getNroDecreto()))
                return new ResponseEntity<>(new Mensaje("El numero de decreto es obligatorio"), HttpStatus.BAD_REQUEST);

        }

        // Si es una actualización (id != 0), impedir la modificación de campos
        // inmutables
        if (id != null && id != 0L && this.existsById(id)) {
            Optional<Legajo> optLegajo = this.findById(id);
            if (optLegajo.isPresent()) {
                Legajo existente = optLegajo.get();

                // persona
                if (legajoDto.getIdPersona() != null && existente.getPersona() != null
                        && !Objects.equals(existente.getPersona().getId(), legajoDto.getIdPersona())) {
                    return new ResponseEntity<>(new Mensaje("La persona no puede modificarse en una actualización"),
                            HttpStatus.BAD_REQUEST);
                }

                // profesion
                Long dtoProf = legajoDto.getIdProfesion();
                Long existProf = existente.getProfesion() != null ? existente.getProfesion().getId() : null;
                if (dtoProf != null && existProf != null && !Objects.equals(dtoProf, existProf)) {
                    return new ResponseEntity<>(new Mensaje("La profesión no puede modificarse en una actualización"),
                            HttpStatus.BAD_REQUEST);
                }
                if (dtoProf != null && existProf == null) {
                    return new ResponseEntity<>(new Mensaje("La profesión no puede agregarse en una actualización"),
                            HttpStatus.BAD_REQUEST);
                }

                // matriculas
                if (legajoDto.getMatriculaNacional() != null && existente.getMatriculaNacional() != null
                        && !Objects.equals(legajoDto.getMatriculaNacional(), existente.getMatriculaNacional())) {
                    return new ResponseEntity<>(
                            new Mensaje("La matrícula nacional no puede modificarse en una actualización"),
                            HttpStatus.BAD_REQUEST);
                }
                if (legajoDto.getMatriculaProvincial() != null && existente.getMatriculaProvincial() != null
                        && !Objects.equals(legajoDto.getMatriculaProvincial(), existente.getMatriculaProvincial())) {
                    return new ResponseEntity<>(
                            new Mensaje("La matrícula provincial no puede modificarse en una actualización"),
                            HttpStatus.BAD_REQUEST);
                }

                // especialidades (comparar sets de ids)
                Set<Long> existEspIds = new HashSet<>();
                if (existente.getEspecialidades() != null) {
                    existente.getEspecialidades().forEach(e -> existEspIds.add(e.getId()));
                }
                Set<Long> dtoEspIds = new HashSet<>();
                if (legajoDto.getIdEspecialidades() != null) {
                    dtoEspIds.addAll(legajoDto.getIdEspecialidades());
                }
                if (!existEspIds.isEmpty() && !dtoEspIds.equals(existEspIds)) {
                    return new ResponseEntity<>(
                            new Mensaje("Las especialidades no pueden modificarse en una actualización"),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Legajo createUpdate(Legajo legajo, LegajoDto legajoDto) {
        boolean isNew = (legajo.getId() == null);

        if (legajo.getFechaInicio() != legajoDto.getFechaInicio())
            legajo.setFechaInicio(legajoDto.getFechaInicio());

        if (legajoDto.getFechaFinal() != null && legajo.getFechaFinal() != legajoDto.getFechaFinal())
            legajo.setFechaFinal(legajoDto.getFechaFinal());

        if (legajoDto.getMatriculaNacional() != null
                && legajo.getMatriculaNacional() != legajoDto.getMatriculaNacional())
            legajo.setMatriculaNacional(legajoDto.getMatriculaNacional());

        if (legajo.getMatriculaProvincial() != legajoDto.getMatriculaProvincial())
            legajo.setMatriculaProvincial(legajoDto.getMatriculaProvincial());

        // persona sólo se asigna en creación
        if (isNew) {
            if (legajo.getPersona() == null
                    || !Objects.equals(legajo.getPersona() != null ? legajo.getPersona().getId() : null,
                            legajoDto.getIdPersona())) {
                legajo.setPersona(personService.findById(legajoDto.getIdPersona()));
            }
        }

        if (legajoDto.getMotivoBaja() != null && legajo.getMotivoBaja() != legajoDto.getMotivoBaja())
            legajo.setMotivoBaja(legajoDto.getMotivoBaja());

        if (legajoDto.getMotivoModificacion() != null
                && legajo.getMotivoModificacion() != legajoDto.getMotivoModificacion())
            legajo.setMotivoModificacion(legajoDto.getMotivoModificacion());

        boolean esAsistencial = personService.activoById(legajoDto.getIdPersona())
                && asistencialService.existsById(legajoDto.getIdPersona());

        boolean esContraFactura = legajoDto.getIdTipoGuardias() != null && legajoDto.getIdTipoGuardias().contains(4L);

        boolean esCargoOAgrupacion = legajoDto.getIdTipoGuardias() != null
                && (legajoDto.getIdTipoGuardias().contains(1L) || legajoDto.getIdTipoGuardias().contains(2L));

        boolean esExtra = legajoDto.getIdTipoGuardias() != null
                && legajoDto.getIdTipoGuardias().contains(3L);

        if (esExtra) {
            if (legajo.getEfectores() == null || legajo.getEfectores().isEmpty()) {
                legajo.setEfectores(new ArrayList<>());

            }

            List<Efector> efectoresActualizados = new ArrayList<>();
            for (Efector efector : legajo.getEfectores()) {
                if (legajoDto.getIdEfectores().contains(efector.getId())) {
                    efectoresActualizados.add(efector);
                } else {
                    efector.getLegajos().remove(legajo);
                }
            }
            legajo.setEfectores(efectoresActualizados);

            for (Long id : legajoDto.getIdEfectores()) {
                boolean found = false;
                for (Efector efector : legajo.getEfectores()) {
                    if (efector.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Efector efectorToAdd = efectorService.findById(id);
                    if (efectorToAdd != null) {
                        legajo.getEfectores().add(efectorToAdd);
                        efectorToAdd.getLegajos().add(legajo);
                    } else {
                        throw new RuntimeException("No se encontró el efector con ID: " + id);
                    }
                }
            }
        }

        if ((esAsistencial && !esContraFactura) && legajoDto.getEsAutoridad() == false) {

            if (legajo.getRevista() == null || !Objects.equals(legajo.getRevista().getId(), legajoDto.getIdRevista())) {
                legajo.setRevista(revistaService.findById(legajoDto.getIdRevista()).get());
            }
        }

        if ((!esContraFactura && (legajoDto.getEsAutoridad() != null && legajoDto.getEsAutoridad() == false))) {
            if (legajo.getUdo() == null || !Objects.equals(legajo.getUdo().getId(), legajoDto.getIdUdo())) {
                legajo.setUdo(efectorService.findById(legajoDto.getIdUdo()));
            }
        }

        if ((esAsistencial && esCargoOAgrupacion) || ((!esAsistencial && legajoDto.getEsAutoridad() == false)
                || (legajoDto.getEsAutoridad() == true && legajoDto.getEsRegional() == false))) {
            if (legajo.getEfectores() == null) {
                legajo.setEfectores(new ArrayList<>());
            }

            // Crea una nueva lista para almacenar los efectores actualizados
            List<Efector> efectoresActualizados = new ArrayList<>();
            for (Efector efector : legajo.getEfectores()) {
                if (legajoDto.getIdEfectores().contains(efector.getId())) {
                    efectoresActualizados.add(efector);
                } else {
                    // Remover el legajo de los efectores que se eliminarán
                    efector.getLegajos().remove(legajo);
                }
            }
            legajo.setEfectores(efectoresActualizados);

            // agrega nuevos efectores si no estan presentes
            for (Long id : legajoDto.getIdEfectores()) {
                boolean found = false;
                for (Efector efector : legajo.getEfectores()) {
                    if (efector.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Efector efectorToAdd = efectorService.findById(id);
                    if (efectorToAdd != null) {
                        legajo.getEfectores().add(efectorToAdd);
                        efectorToAdd.getLegajos().add(legajo);
                    } else {
                        throw new RuntimeException("No se encontró el efector con ID: " + id);
                    }
                }
            }
        }
        // revisar validacion para despues
        /* if (esAsistencial && legajoDto.getEsAutoridad() == false) { */

        if (legajo.getProfesion() == null ||
                (legajoDto.getIdProfesion() != null &&
                        !Objects.equals(legajo.getProfesion().getId(), legajoDto.getIdProfesion()))) {

            // Asignar la profesión si `idProfesion` no es null
            if (legajoDto.getIdProfesion() != null) {
                legajo.setProfesion(profesionService.findById(legajoDto.getIdProfesion()).orElse(null));
            } else {
                // Permitir guardar con profesión null
                legajo.setProfesion(null);
            }
        }
        // Validar si idTipoGuardias no es null
        if (legajoDto.getIdTipoGuardias() != null) {
            // Si no es null, procesar los tipos de guardia
            for (Long idTipoGuardia : legajoDto.getIdTipoGuardias()) {
                // Lógica para procesar cada idTipoGuardia
                TipoGuardia tipoGuardia = tipoGuardiaService.findById(idTipoGuardia).orElse(null);
                if (tipoGuardia != null && !legajo.getTipoGuardias().contains(tipoGuardia)) {
                    legajo.getTipoGuardias().add(tipoGuardia);
                    tipoGuardia.getLegajos().add(legajo);
                }
            }
        }

        if (legajoDto.getEsAutoridad() == true) {

            if (legajoDto.getEsRegional() != null) {
                legajo.setEsRegional(legajoDto.getEsRegional());
            }

            if (legajo.getCargo() == null || !Objects.equals(legajo.getCargo().getId(), legajoDto.getIdCargo())) {
                legajo.setCargo(cargoService.findById(legajoDto.getIdCargo()).get());
            }

            if (legajoDto.getEsRegional() == true) {
                if (legajo.getRegion() == null || !Objects.equals(legajo.getRegion().getId(),
                        legajoDto.getIdRegion())) {
                    legajo.setRegion(regionService.findById(legajoDto.getIdRegion()).get());
                }
            }

            if (legajoDto.getNroResolucion() != null && !legajoDto.getNroResolucion().equals(legajo.getNroresolucion())
                    && !legajoDto.getNroResolucion().isEmpty())
                legajo.setNroresolucion(legajoDto.getNroResolucion());

            if (legajo.getFechaResolucion() != legajoDto.getFechaResolucion())
                legajo.setFechaResolucion(legajoDto.getFechaResolucion());

            if (legajoDto.getNroDecreto() != null && !legajoDto.getNroDecreto().equals(legajo.getNrodecreto())
                    && !legajoDto.getNroDecreto().isEmpty())
                legajo.setNrodecreto(legajoDto.getNroDecreto());
        }

        if (legajoDto.getIdSuspencion() != null) {
            if (legajo.getSuspencion() == null
                    || !Objects.equals(legajo.getSuspencion().getId(), legajoDto.getIdSuspencion())) {
                legajo.setSuspencion(suspencionService.findById(legajoDto.getIdSuspencion()).get());
            }
        }

        if (legajoDto.getIdEspecialidades() != null) {
            if (legajo.getEspecialidades() == null) {
                legajo.setEspecialidades(new ArrayList<>());
            }

            List<Especialidad> especialidadesParaEliminar = new ArrayList<>();
            for (Especialidad especialidad : legajo.getEspecialidades()) {
                if (!legajoDto.getIdEspecialidades().contains(especialidad.getId())) {
                    especialidadesParaEliminar.add(especialidad);
                }
            }

            for (Especialidad especialidad : especialidadesParaEliminar) {
                legajo.getEspecialidades().remove(especialidad);
                especialidad.getLegajos().remove(legajo);
            }

            // Agrego nuevas especialidades al legajo si no están presentes
            for (Long id : legajoDto.getIdEspecialidades()) {
                boolean found = false;
                for (Especialidad especialidad : legajo.getEspecialidades()) {
                    if (especialidad.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Especialidad especialidadToAdd = especialidadService.findById(id).orElse(null);
                    if (especialidadToAdd != null) {
                        legajo.getEspecialidades().add(especialidadToAdd);
                        especialidadToAdd.getLegajos().add(legajo);
                    }
                }
            }
        }

        if (legajoDto.getTipoEfector() != null) {
            legajo.setTipoEfector(legajoDto.getTipoEfector());
        }

        if (legajoDto.getTipoEfectorCargo() != null) {
            legajo.setTipoEfectorCargo(legajoDto.getTipoEfectorCargo());
        }

        if (legajoDto.getTipoUdo() != null) {
            legajo.setTipoUdo(legajoDto.getTipoUdo());
        }

        legajo.setEsAutoridad(legajoDto.getEsAutoridad());

        legajo.setActivo(legajoDto.getActivo());

        if (legajo.getUrl() == null || (legajoDto.getUrl() != null
                && !Objects.equals(legajo.getUrl(), legajoDto.getUrl())))
            legajo.setUrl(legajoDto.getUrl());

        return legajo;
    }

    // Método para verificar si una persona es contrafactura
    public boolean esContraFactura(Long idPersona) {

        Optional<Asistencial> asistencialOpt = asistencialRepository.findById(idPersona);
        if (asistencialOpt.isPresent()) {
            Asistencial persona = asistencialOpt.get();

            // Si la persona no tiene legajos, retorna false para permitir la creación
            if (persona.getLegajos() == null || persona.getLegajos().isEmpty()) {
                return true;
            }
            // Recorre los legajos activos del asistencial y verifica si alguno tiene un
            // TipoGuardia de CONTRAFACTURA
            return persona.getLegajos().stream()
                    .filter(legajo -> legajo.getFechaFinal() == null) // modificar esto, debe validar que activo = true
                    .flatMap(legajo -> legajo.getTipoGuardias().stream()) // Obtener los tipos de guardias de
                                                                          // cada legajo activo
                    .anyMatch(tipoGuardia -> tipoGuardia.getNombre() == TipoGuardiaEnum.CONTRAFACTURA);
        }
        // Si no existe la persona, retorna false
        return false;
    }

    public void updateTipoGuardias(Legajo legajo, LegajoDto legajoDto) {
        if (legajo.getTipoGuardias() == null) {
            legajo.setTipoGuardias(new ArrayList<>());
        }

        // crea una nueva lista para almacenar los tipos de guardias actualizados
        List<TipoGuardia> tipoGuardiasActualizados = new ArrayList<>();
        for (TipoGuardia tipoGuardia : legajo.getTipoGuardias()) {
            if (legajoDto.getIdTipoGuardias().contains(tipoGuardia.getId())) {
                tipoGuardiasActualizados.add(tipoGuardia);
            } else {
                // Remover el legajo de los tipos de guardias que se eliminarán
                tipoGuardia.getLegajos().remove(legajo);
            }
        }
        legajo.setTipoGuardias(tipoGuardiasActualizados);

        // agregar nuevos tipos de guardia si no estan presentes
        for (Long id : legajoDto.getIdTipoGuardias()) {
            boolean found = false;
            for (TipoGuardia tipoGuardia : legajo.getTipoGuardias()) {
                if (tipoGuardia.getId().equals(id)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                TipoGuardia tipoGuardiaToAdd = tipoGuardiaService.findById(id).get();
                if (tipoGuardiaToAdd != null) {
                    legajo.getTipoGuardias().add(tipoGuardiaToAdd);
                    tipoGuardiaToAdd.getLegajos().add(legajo);
                } else {
                    throw new RuntimeException("No se encontró el tipo de guardia con ID: " + id);
                }
            }
        }
    }

    public boolean tieneTipoGuardiaPermitido(Long idPersona) {

        if (idPersona == null) {
            throw new IllegalArgumentException("el id de la persona no pueden ser nulo.");
        }

        if (!personService.activoById(idPersona)) {
            throw new EntityNotFoundException("La persona con ID " + idPersona + " no existe.");
        }

        Legajo legajoActivo = legajoRepository.findByPersonaIdAndActivoTrue(idPersona).get();
        if (legajoActivo == null) {
            throw new EntityNotFoundException("No se encontró un legajo activo para la persona con ID " + idPersona);
        }

        // Validar si el legajo tiene un tipo de guardia permitido
        List<TipoGuardia> tipoGuardias = legajoActivo.getTipoGuardias();
        if (tipoGuardias == null || tipoGuardias.isEmpty()) {
            return false;
        }

        return tipoGuardias.stream()
                .map(TipoGuardia::getNombre)
                .anyMatch(nombre -> nombre == TipoGuardiaEnum.CONTRAFACTURA ||
                        nombre == TipoGuardiaEnum.EXTRA ||
                        nombre == TipoGuardiaEnum.PASIVA);
    }

    public void logicDelete(Long id, LegajoBajaDto legajoBajaDto) {

        Legajo legajo = legajoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el legajo con el ID: " + id));

        // Validar que los campos requeridos no sean nulos
        if (legajoBajaDto.getMotivoBaja() == null || legajoBajaDto.getMotivoBaja().isBlank()) {
            throw new ValidationException("El motivo de la baja es obligatorio");
        }
        if (legajoBajaDto.getFechaFinal() == null) {
            throw new ValidationException("La fecha final es obligatoria");
        }

        // fechaFinal debe ser posterior a fechaInicio
        if (legajo.getFechaInicio() != null
                && legajoBajaDto.getFechaFinal().isBefore(legajo.getFechaInicio().plusDays(1))) {

            throw new ValidationException("La fecha de finalización debe ser posterior a la fecha de inicio");
        }

        // Actualiza el legajo
        legajo.setActivo(false);
        legajo.setMotivoBaja(legajoBajaDto.getMotivoBaja());
        legajo.setFechaFinal(legajoBajaDto.getFechaFinal());
        legajo.setFechaBajaSistema(LocalDate.now());

        legajoRepository.save(legajo);
    }

    public boolean tieneLegajoActivo(Long personaId) {
        return legajoRepository.existsByPersonaIdAndActivoTrue(personaId);
    }

    public List<LegajoActualDto> getLegajosByAsistencial(Long idPerson) {
        List<Legajo> legajos = legajoRepository.findLegajosByPersonaId(idPerson);

        return legajos.stream()
                .map(legajo -> {
                    if (legajo.getRevista() == null) {
                        return new LegajoActualDto("Sin revista", "Sin revista", "Sin revista");
                    }

                    String nombreAdicional = legajo.getRevista().getAdicional() != null
                            ? legajo.getRevista().getAdicional().getNombre()
                            : "Sin adicional";

                    String nombreCategoria = legajo.getRevista().getCategoria() != null
                            ? legajo.getRevista().getCategoria().getNombre()
                            : "Sin categoría";

                    String nombreTipoRevista = legajo.getRevista().getTipoRevista() != null
                            ? legajo.getRevista().getTipoRevista().getNombre()
                            : "Sin tipo de revista";

                    return new LegajoActualDto(nombreAdicional, nombreCategoria, nombreTipoRevista);
                })
                .toList();
    }

    public String calculateMD5(InputStream inputStream) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            md.update(buffer, 0, bytesRead);
        }

        byte[] hashBytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public List<Legajo> findAllByModificacion(Long idPersona) {
        if (idPersona == null) {
            throw new IllegalArgumentException("El idPersona no puede ser nulo");
        }
        return legajoRepository.findAllByPersonaIdWithMotivoModificacion(idPersona);
    }

    public List<Legajo> findAllByBaja(Long idPersona) {
        if (idPersona == null) {
            throw new IllegalArgumentException("El idPersona no puede ser nulo");
        }
        return legajoRepository.findAllByPersonaIdWithMotivoBaja(idPersona);
    }

    public List<Legajo> findAllByPersonaIdAndActivo(Long idPersona, boolean activo) {
        if (idPersona == null) {
            throw new IllegalArgumentException("El idPersona no puede ser nulo");
        }
        return legajoRepository.findAllByPersonaIdAndActivo(idPersona, activo);
    }

    public List<Legajo> findAllByPersonaId(Long idPersona) {
        if (idPersona == null) {
            throw new IllegalArgumentException("El idPersona no puede ser nulo");
        }
        return legajoRepository.findAllByPersonaId(idPersona);
    }

    /*
     * public List<Legajo> findInactivosBypersonaAndEfector(Long idPersona, Long
     * idEfector) {
     * if (idPersona == null) {
     * throw new IllegalArgumentException("El idPersona no puede ser nulo");
     * }
     * if (idEfector == null) {
     * throw new IllegalArgumentException("El idEfector no puede ser nulo");
     * }
     * return legajoRepository.findInactivosBypersonaAndEfector(idPersona,
     * idEfector);
     * }
     */
}
