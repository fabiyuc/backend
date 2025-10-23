package com.guardias.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
import com.guardias.backend.enums.QuincenaEnum;
import com.guardias.backend.repository.CronogramaDefinitivoRepository;
import com.guardias.backend.repository.DdjjRepository;

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

        // Verificar que las DDJJ coincidan en mes y año
        ResponseEntity<?> validacionDdjjs = validarCoincidenciaMesAnioDdjjs(
                cronogramaDefinitivoDto.getIdDdjjs(),
                cronogramaDefinitivoDto.getMes(),
                cronogramaDefinitivoDto.getAnio());

        if (validacionDdjjs.getStatusCode() != HttpStatus.OK) {
            return validacionDdjjs;
        }
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private ResponseEntity<?> validarCoincidenciaMesAnioDdjjs(List<Long> idDdjjs, MesesEnum mes, int anio) {
        if (idDdjjs == null || idDdjjs.isEmpty()) {
            return new ResponseEntity(new Mensaje("La lista de DDJJ está vacía"), HttpStatus.BAD_REQUEST);
        }

        for (Long idDdjj : idDdjjs) {
            Ddjj ddjj = ddjjRepository.findById(idDdjj).orElse(null);

            if (ddjj == null) {
                return new ResponseEntity(new Mensaje("DDJJ no encontrada con ID: " + idDdjj), HttpStatus.BAD_REQUEST);
            }

            if (ddjj.getMes() != mes) {
                return new ResponseEntity(
                        new Mensaje("La DDJJ con ID " + idDdjj + " es del mes " + ddjj.getMes() +
                                " pero se esperaba " + mes),
                        HttpStatus.BAD_REQUEST);
            }

            if (ddjj.getAnio() != anio) {
                return new ResponseEntity(
                        new Mensaje("La DDJJ con ID " + idDdjj + " es del año " + ddjj.getAnio() +
                                " pero se esperaba " + anio),
                        HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity(new Mensaje("DDJJ válidas"), HttpStatus.OK);
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

        if (cronogramaDefinitivoDto.getQuincena() != null) {
            cronogramaDefinitivo.setQuincena(cronogramaDefinitivoDto.getQuincena());
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

    public CronogramaDefinitivo createUpdateDefinitivo(CronogramaDefinitivoDto dto) {

        // según tipo de quincena
        if (dto.getQuincena() == QuincenaEnum.PRIMERA) {
            return processPrimeraQuincena(dto);
        } else if (dto.getQuincena() == QuincenaEnum.SEGUNDA || dto.getQuincena() == QuincenaEnum.FUERA_DE_TERMINO) {
            return processCompleto(dto);
        } else {
            throw new IllegalArgumentException("Tipo de quincena no válido: " + dto.getQuincena());
        }
    }

    private CronogramaDefinitivo processPrimeraQuincena(CronogramaDefinitivoDto dto) {
        // Verificar si ya existe primera quincena
        Optional<CronogramaDefinitivo> existente = cronogramaDefinitivoRepository
                .findByEfectorIdAndMesAndAnioAndQuincenaAndActivoTrue(
                        dto.getIdEfector(), dto.getMes(), dto.getAnio(), QuincenaEnum.PRIMERA);

        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un cronograma activo para la primera quincena");
        }

        // Crear nueva primera quincena
        return createNewCronograma(dto);
    }

    private CronogramaDefinitivo processCompleto(CronogramaDefinitivoDto dto) {
        // Buscar primera quincena existente
        Optional<CronogramaDefinitivo> primeraQuincenaOpt = cronogramaDefinitivoRepository
                .findByEfectorIdAndMesAndAnioAndQuincenaAndActivoTrue(
                        dto.getIdEfector(), dto.getMes(), dto.getAnio(), QuincenaEnum.PRIMERA);

        if (primeraQuincenaOpt.isPresent()) {
            // Fusionar en cronograma COMPLETO
            CronogramaDefinitivo cronogramaCompleto = createCronogramaCompleto(primeraQuincenaOpt.get(), dto);

            // Desactivar primera quincena
            primeraQuincenaOpt.get().setActivo(false);
            cronogramaDefinitivoRepository.save(primeraQuincenaOpt.get());

            return cronogramaCompleto;
        } else {
            // Crear con estado completo
            return createNewCronograma(dto);
        }
    }

    private CronogramaDefinitivo createCronogramaCompleto(CronogramaDefinitivo primeraQuincena,
            CronogramaDefinitivoDto completarDto) {
        CronogramaDefinitivo completo = new CronogramaDefinitivo();

        // Configurar datos base usando el método existente createUpdate
        completo = createUpdate(completo, completarDto );
        completo.setQuincena(QuincenaEnum.COMPLETO);

        // Combinar DDJJs de ambas quincenas
        Set<Ddjj> todasDdjjs = new HashSet<>(primeraQuincena.getDdjjs());

        for (Long idDdjj : completarDto .getIdDdjjs()) {
            Ddjj ddjj = ddjjRepository.findById(idDdjj)
                    .orElseThrow(() -> new IllegalArgumentException("DDJJ no encontrada: " + idDdjj));
            todasDdjjs.add(ddjj);
        }

        completo.setDdjjs(new ArrayList<>(todasDdjjs));
        return completo;
    }

    private CronogramaDefinitivo createNewCronograma(CronogramaDefinitivoDto dto) {
        CronogramaDefinitivo nuevo = new CronogramaDefinitivo();
        return createUpdate(nuevo, dto);
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
                    // Filtramos DDJJs por tipoGuardia (Cuando idTipoGuardia == 1, incluimos ambos
                    // tipos (1 y 2))
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

    public List<CronogramaDefinitivoListDto> findByAnioMesIdEfectorAndActivoTrueDto(int anio, MesesEnum mes,
            Long idEfector) {
        List<CronogramaDefinitivo> cronogramas = findByAnioAndMesAndIdEfectorAndActivoTrue(anio, mes, idEfector);
        return cronogramas.stream()
                .map(cronograma -> new CronogramaDefinitivoListDto(
                        cronograma.getId(),
                        cronograma.getMes(),
                        cronograma.getAnio(),
                        cronograma.getDdjjs().stream()
                                .map(ddjj -> new DdjjListDto(
                                        ddjj.getId(),
                                        ddjj.getMes(),
                                        ddjj.getAnio(),
                                        registroMensualService.mapToDtoList(ddjj.getRegistrosMensuales(),
                                                ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getId() : null),
                                        ddjj.getDirector() != null ? ddjj.getDirector().getId() : null,
                                        ddjj.getDirectorDPH() != null ? ddjj.getDirectorDPH().getId() : null,
                                        ddjj.getEstadoDdjjDirector(),
                                        ddjj.getEstadoDdjjDirectorDPH(),
                                        ddjj.getEnPosesionDirector(),
                                        ddjj.getEnPosesionDirectorDPH(),
                                        ddjj.getMotivoDirector(),
                                        ddjj.getMotivoDirectorDPH(),
                                        ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getId() : null))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

}
