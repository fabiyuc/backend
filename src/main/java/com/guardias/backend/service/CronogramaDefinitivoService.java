package com.guardias.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.CronogramaDefinitivoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.cronogramaDefinitivo.CronogramaDefinitivoListDto;
import com.guardias.backend.dto.ddjj.DdjjListDto;
import com.guardias.backend.dto.registroMensual.RegistroMensualListDto;
import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.CronogramaDefinitivoRepository;
import com.guardias.backend.repository.DdjjRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class CronogramaDefinitivoService {

    @Autowired
    CronogramaDefinitivoRepository cronogramaDefinitivoRepository;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    DdjjRepository ddjjRepository;
    @Autowired
    RegistroActividadService registroActividadService;
    @Autowired
    RegistroMensualService registroMensualService;

    public Optional<List<CronogramaDefinitivo>> findByActivoTrue() {
        return cronogramaDefinitivoRepository.findByActivoTrue();
    }

    public List<CronogramaDefinitivo> findAll() {
        return cronogramaDefinitivoRepository.findAll();
    }

    public Optional<CronogramaDefinitivo> findById(Long id) {
        return cronogramaDefinitivoRepository.findById(id);
    }

    public List<CronogramaDefinitivo> findByAnioAndMesAndIdEfectorAndActivoTrue(int anio, MesesEnum mes,
            Long idEfector) {
        return cronogramaDefinitivoRepository.findByAnioAndMesAndEfectorIdAndActivoTrue(anio, mes, idEfector);
    }

    public boolean existsByAnioAndMes(int anio, MesesEnum mes) {
        return cronogramaDefinitivoRepository.existsByAnioAndMes(anio, mes);
    }

    public boolean existsById(Long id) {
        return cronogramaDefinitivoRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (cronogramaDefinitivoRepository.existsById(id)
                && cronogramaDefinitivoRepository.findById(id).get().isActivo());
    }

    public void save(CronogramaDefinitivo cronogramaDefinitivo) {
        cronogramaDefinitivoRepository.save(cronogramaDefinitivo);
    }

    public void deleteById(Long id) {
        cronogramaDefinitivoRepository.deleteById(id);
    }

    public ResponseEntity<?> validations(CronogramaDefinitivoDto cronogramaDefinitivoDto) {

        if (cronogramaDefinitivoDto.getMes() == null)
            return new ResponseEntity(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cronogramaDefinitivoDto.getAnio() < 1991)
            return new ResponseEntity(new Mensaje("El año es incorrecto"), HttpStatus.BAD_REQUEST);

        if (cronogramaDefinitivoDto.getIdDdjjs() == null)
            return new ResponseEntity(new Mensaje("la lista de ddjj no debe ser nula"), HttpStatus.BAD_REQUEST);

        boolean apto = registroActividadService.validarPrecondicionesCronograma(cronogramaDefinitivoDto.getIdEfector(),
                cronogramaDefinitivoDto.getMes().getNumeroMes(), cronogramaDefinitivoDto.getAnio());
        if (apto != true) {
            return new ResponseEntity(
                    new Mensaje("no cumple con las validaciones de la ddjj con estadoDirector aprobadas"),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public CronogramaDefinitivo createUpdate(CronogramaDefinitivo cronogramaDefinitivo,
            CronogramaDefinitivoDto cronogramaDefinitivoDto) {

        if (cronogramaDefinitivoDto.getMes() != null
                && !cronogramaDefinitivoDto.getMes().equals(cronogramaDefinitivo.getMes()))
            cronogramaDefinitivo.setMes(cronogramaDefinitivoDto.getMes());

        if (cronogramaDefinitivoDto.getAnio() != cronogramaDefinitivo.getAnio())
            cronogramaDefinitivo.setAnio(cronogramaDefinitivoDto.getAnio());

        if (cronogramaDefinitivoDto.getIdEfector() != null && (cronogramaDefinitivo.getEfector() == null
                || !Objects.equals(cronogramaDefinitivo.getEfector().getId(),
                        cronogramaDefinitivoDto.getIdEfector()))) {
            cronogramaDefinitivo.setEfector(efectorService.findById(cronogramaDefinitivoDto.getIdEfector()));
        }

        // Validar si idDdjjs no es null
        if (cronogramaDefinitivoDto.getIdDdjjs() != null) {
            // Si no es null, procesar las ddjj
            for (Long idDdjj : cronogramaDefinitivoDto.getIdDdjjs()) {
                // Lógica para procesar cada idDdjj
                Ddjj ddjj = ddjjRepository.findById(idDdjj).orElse(null);
                if (ddjj != null && !cronogramaDefinitivo.getDdjjs().contains(ddjj)) {
                    cronogramaDefinitivo.getDdjjs().add(ddjj);
                    ddjj.getCronogramasDefinitivos().add(cronogramaDefinitivo);
                }
            }
        }

        cronogramaDefinitivo.setActivo(true);
        return cronogramaDefinitivo;
    }

    public CronogramaDefinitivo createCronogramaDefinitivo(Long idAsistencial, Long idEfector, MesesEnum mesEnum,
            int anio) {

        CronogramaDefinitivo cronogramaDefinitivo = new CronogramaDefinitivo();
        cronogramaDefinitivo.setMes(mesEnum);
        cronogramaDefinitivo.setAnio(anio);
        cronogramaDefinitivo.setEfector(efectorService.findById(idEfector));
        cronogramaDefinitivo.setActivo(true);

        try {
            save(cronogramaDefinitivo);
            return cronogramaDefinitivo;
        } catch (Exception e) {
            System.out.println(
                    "error al crear el cronograma definitivo-  registroMensualService Ln196 -- " + e.getMessage());
            return null;
        }
    }

    public List<CronogramaDefinitivoListDto> findByAnioMesIdEfectorTipoGuardiaAndActivoTrue(
            int anio, MesesEnum mes, Long idEfector, Long idTipoGuardia) {

        List<CronogramaDefinitivo> cronogramas = cronogramaDefinitivoRepository
                .findByAnioAndMesAndEfectorIdAndActivoTrue(anio, mes, idEfector);

        return cronogramas.stream()
                .map(cronograma -> {
                    // Filtramos DDJJs por tipoGuardia (Cuando idTipoGuardia == 1, incluimos ambos tipos (1 y 2))
                    List<DdjjListDto> ddjjsFiltradas = cronograma.getDdjjs().stream()
                            .filter(ddjj -> idTipoGuardia == null ||
                                    (ddjj.getTipoGuardia() != null &&
                                    (ddjj.getTipoGuardia().getId().equals(idTipoGuardia) || 
                                         (idTipoGuardia == 1L && ddjj.getTipoGuardia().getId() == 2L))))
                            .map(ddjj -> {
                                // Aplicamos filtro adicional para tipoGuardia == 1
                                List<RegistroMensualListDto> registros = idTipoGuardia != null && idTipoGuardia == 1L
                                        ? filtrarRegistrosConNovedades(ddjj.getRegistrosMensuales())
                                        : registroMensualService.mapToDtoList(ddjj.getRegistrosMensuales(),
                                                idTipoGuardia);

                                return new DdjjListDto(
                                        ddjj.getId(),
                                        ddjj.getMes(),
                                        ddjj.getAnio(),
                                        registros,
                                        ddjj.getDirector() != null ? ddjj.getDirector().getId() : null,
                                        ddjj.getDirectorDPH() != null ? ddjj.getDirectorDPH().getId() : null,
                                        ddjj.getEstadoDdjjDirector(),
                                        ddjj.getEstadoDdjjDirectorDPH(),
                                        ddjj.getEnPosesionDirector(),
                                        ddjj.getEnPosesionDirectorDPH(),
                                        ddjj.getMotivoDirector(),
                                        ddjj.getMotivoDirectorDPH(),
                                        ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getId() : null);
                            })
                            .collect(Collectors.toList());

                    return new CronogramaDefinitivoListDto(
                            cronograma.getId(),
                            cronograma.getMes(),
                            cronograma.getAnio(),
                            ddjjsFiltradas);
                })
                .collect(Collectors.toList());
    }

    private List<RegistroMensualListDto> filtrarRegistrosConNovedades(List<RegistroMensual> registros) {
        return registros.stream()
                .filter(rm -> !tieneNovedadCompensatoriaOLAO(rm))
                .map(rm -> registroMensualService.convertirARegistroMensualCompletoDTO(
                        rm,
                        rm.getRegistroActividad().stream()
                                .filter(act -> act.getTipoGuardia() != null && act.getTipoGuardia().getId() == 1L)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    private boolean tieneNovedadCompensatoriaOLAO(RegistroMensual registro) {
        if (registro.getAsistencial() == null) {
            return false;
        }

        return registro.getAsistencial().getNovedadesPersonales().stream()
                .filter(n -> n.isActivo())
                .anyMatch(novedad -> {
                    String tipoLicencia = novedad.getTipoLicencia() != null
                            ? novedad.getTipoLicencia().getNombre()
                            : null;

                    boolean esLicenciaRelevante = "Compensatorio".equalsIgnoreCase(tipoLicencia)
                            || "LAO".equalsIgnoreCase(tipoLicencia);

                    if (!esLicenciaRelevante) {
                        return false;
                    }

                    return haySolapamiento(
                            registro.getAnio(), registro.getMes(),
                            novedad.getFechaInicio(), novedad.getHoraInicio(),
                            novedad.getFechaFinal(), novedad.getHoraFinal());
                });
    }

    private boolean haySolapamiento(
            int anioAct, MesesEnum mesAct,
            LocalDate fechaIniNov, LocalTime horaIniNov,
            LocalDate fechaFinNov, LocalTime horaFinNov) {

        // Asumimos que el registro mensual es para todo el mes
        LocalDateTime inicioMes = LocalDateTime.of(LocalDate.of(anioAct, mesAct.ordinal() + 1, 1), LocalTime.MIN);
        LocalDateTime finMes = inicioMes.plusMonths(1).minusSeconds(1);

        LocalDateTime inicioNov = LocalDateTime.of(fechaIniNov, horaIniNov != null ? horaIniNov : LocalTime.MIN);
        LocalDateTime finNov = LocalDateTime.of(fechaFinNov, horaFinNov != null ? horaFinNov : LocalTime.MAX);

        return inicioNov.isBefore(finMes) && finNov.isAfter(inicioMes);
    }

     public List<Long> getTiposGuardia(Long idCronograma) {
        return cronogramaDefinitivoRepository.findByIdAndActivoTrue(idCronograma)
                .map(cronograma -> cronograma.getDdjjs().stream()
                        .filter(ddjj -> ddjj.getTipoGuardia() != null)
                        .map(ddjj -> ddjj.getTipoGuardia().getId())
                        .distinct()
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList()); // Devuelve lista vacía si no existe
    }

}
