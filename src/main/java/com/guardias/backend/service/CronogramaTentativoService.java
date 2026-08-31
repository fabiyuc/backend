package com.guardias.backend.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.CronogramaTentativoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asistencial.AsistencialDetailDto;
import com.guardias.backend.dto.cronogramaTentativo.AutorizadoUpdateDto;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoListAtorizadoDto;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoServicioDto;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoSummaryDto;
import com.guardias.backend.dto.cronogramaTentativo.TentativoIdsResponseDto;
import com.guardias.backend.dto.cronogramaTentativo.TentativoSearchRequestDto;
import com.guardias.backend.dto.cronogramaTentativo.TotalHorasDiaDto;
import com.guardias.backend.dto.cronogramaTentativo.TotalHorasDto;
import com.guardias.backend.dto.cronogramaTentativo.TotalHorasResponseDto;
import com.guardias.backend.dto.cronogramaTentativo.VerificacionTentativoResponseDto;
import com.guardias.backend.dto.registroActividad.RegActivRegIngresoDto;
import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.entity.CronogramaTentativo;
import com.guardias.backend.enums.AutorizadoTentativoEnum;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.AutoridadRepository;
import com.guardias.backend.repository.CronogramaTentativoRepository;
import com.guardias.backend.security.entity.Usuario;
import com.guardias.backend.security.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@Service
@Transactional
public class CronogramaTentativoService {

    @Autowired
    CronogramaTentativoRepository cronogramaTentativoRepository;
    @Autowired
    TipoGuardiaService tipoGuardiaService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    ServicioService servicioService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    AsistencialRepository asistencialRepository;
    @Autowired
    AutoridadService autoridadService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    AutoridadRepository autoridadRepository;

    public Optional<List<CronogramaTentativo>> findByActivoTrue() {
        return cronogramaTentativoRepository.findByActivoTrue();
    }

    public List<CronogramaTentativo> findAll() {
        return cronogramaTentativoRepository.findAll();
    }

    public boolean activo(Long id) {
        return (cronogramaTentativoRepository.existsById(id)
                && cronogramaTentativoRepository.findById(id).get().isActivo());
    }

    public boolean existsByEfectorId(Long efectorId) {
        return cronogramaTentativoRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public Optional<List<CronogramaTentativo>> findByEfectorId(Long efectorId) {
        return cronogramaTentativoRepository.findByEfectorId(efectorId);
    }

    public Optional<List<CronogramaTentativo>> findByEfectorAndServicio(Long efectorId, Long idServicio) {
        return cronogramaTentativoRepository.findByEfectorAndServicio(efectorId, idServicio);
    }

    public Optional<CronogramaTentativo> findById(Long id) {
        return cronogramaTentativoRepository.findById(id);
    }

    public Optional<List<CronogramaTentativo>> findByIdAsistencial(Long id) {
        return cronogramaTentativoRepository.findByIdAsistencial(id);
    }

    public ResponseEntity<?> validations(CronogramaTentativoDto cronogramaTentativoDto) {

        if (cronogramaTentativoDto.getFechaIngreso() == null)
            return new ResponseEntity(new Mensaje("la fecha de ingreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (cronogramaTentativoDto.getFechaEgreso() == null)
            return new ResponseEntity(new Mensaje("la fecha de egreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (cronogramaTentativoDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (cronogramaTentativoDto.getHoraEgreso() == null)
            return new ResponseEntity(new Mensaje("la hora de egreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (cronogramaTentativoDto.getIdTipoGuardia() == null)
            return new ResponseEntity<>(new Mensaje("Indicar el tipo de guardia"),
                    HttpStatus.BAD_REQUEST);

        if (cronogramaTentativoDto.getIdAsistencial() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar el asistencial"), HttpStatus.BAD_REQUEST);

        if (cronogramaTentativoDto.getIdServicio() == null)
            return new ResponseEntity<>(new Mensaje("indicar el servicio"), HttpStatus.BAD_REQUEST);

        if (cronogramaTentativoDto.getIdEfector() == null)
            return new ResponseEntity<>(new Mensaje("indicar el efector"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public CronogramaTentativo createUpdate(CronogramaTentativo cronogramaTentativo,
            CronogramaTentativoDto cronogramaTentativoDto) {

        if (cronogramaTentativo.getFechaIngreso() != cronogramaTentativoDto.getFechaIngreso() &&
                cronogramaTentativoDto.getFechaIngreso() != null)
            cronogramaTentativo.setFechaIngreso(cronogramaTentativoDto.getFechaIngreso());

        if (cronogramaTentativo.getFechaEgreso() != cronogramaTentativoDto.getFechaEgreso() &&
                cronogramaTentativoDto.getFechaEgreso() != null)
            cronogramaTentativo.setFechaEgreso(cronogramaTentativoDto.getFechaEgreso());

        if (cronogramaTentativo.getHoraIngreso() != cronogramaTentativoDto.getHoraIngreso() &&
                cronogramaTentativoDto.getHoraIngreso() != null)
            cronogramaTentativo.setHoraIngreso(cronogramaTentativoDto.getHoraIngreso());

        if (cronogramaTentativo.getHoraEgreso() != cronogramaTentativoDto.getHoraEgreso() &&
                cronogramaTentativoDto.getHoraEgreso() != null)
            cronogramaTentativo.setHoraEgreso(cronogramaTentativoDto.getHoraEgreso());

        if (cronogramaTentativo.getTipoGuardia() == null || (cronogramaTentativoDto.getIdTipoGuardia() != null
                && !Objects.equals(cronogramaTentativo.getTipoGuardia().getId(),
                        cronogramaTentativoDto.getIdTipoGuardia()))) {
            cronogramaTentativo
                    .setTipoGuardia(tipoGuardiaService.findById(cronogramaTentativoDto.getIdTipoGuardia()).get());
        }

        if (cronogramaTentativo.getAsistencial() == null ||
                (cronogramaTentativoDto.getIdAsistencial() != null &&
                        !Objects.equals(cronogramaTentativo.getAsistencial().getId(),
                                cronogramaTentativoDto.getIdAsistencial()))) {
            cronogramaTentativo
                    .setAsistencial(asistencialService.findById(cronogramaTentativoDto.getIdAsistencial()).get());
        }

        if (cronogramaTentativo.getServicio() == null ||
                (cronogramaTentativoDto.getIdServicio() != null &&
                        !Objects.equals(cronogramaTentativo.getServicio().getId(),
                                cronogramaTentativoDto.getIdServicio()))) {
            cronogramaTentativo.setServicio(servicioService.findById(cronogramaTentativoDto.getIdServicio()).get());
        }

        if (cronogramaTentativo.getEfector() == null ||
                (cronogramaTentativoDto.getIdEfector() != null &&
                        !Objects.equals(cronogramaTentativo.getEfector().getId(),
                                cronogramaTentativoDto.getIdEfector()))) {
            cronogramaTentativo.setEfector(efectorService.findById(cronogramaTentativoDto.getIdEfector()));
        }

        if (cronogramaTentativo.getObservacion() != cronogramaTentativoDto.getObservacion() &&
                cronogramaTentativoDto.getObservacion() != null)
            cronogramaTentativo.setObservacion(cronogramaTentativoDto.getObservacion());

        if (cronogramaTentativo.getMotivoAutorizacion() != cronogramaTentativoDto.getMotivoAutorizacion() &&
                cronogramaTentativoDto.getMotivoAutorizacion() != null)
            cronogramaTentativo.setMotivoAutorizacion(cronogramaTentativoDto.getMotivoAutorizacion());

        if (cronogramaTentativo.getMotivoPendiente() != cronogramaTentativoDto.getMotivoPendiente() &&
                cronogramaTentativoDto.getMotivoPendiente() != null)
            cronogramaTentativo.setMotivoPendiente(cronogramaTentativoDto.getMotivoPendiente());

        if (cronogramaTentativoDto.getIdAutoridad() != null) {
            if (cronogramaTentativo.getAutoridad() == null ||
                    !Objects.equals(cronogramaTentativo.getAutoridad().getId(),
                            cronogramaTentativoDto.getIdAutoridad())) {
                cronogramaTentativo
                        .setAutoridad(autoridadService.findById(cronogramaTentativoDto.getIdAutoridad()).orElse(null));
            }
        } else {
            cronogramaTentativo.setAutoridad(null);
        }

        cronogramaTentativo.setAceptado(cronogramaTentativoDto.isAceptado());
        cronogramaTentativo.setAutorizado(cronogramaTentativoDto.getAutorizado());
        cronogramaTentativo.setActivo(true);
        return cronogramaTentativo;
    }

    public void save(CronogramaTentativo cronogramaTentativo) {
        cronogramaTentativoRepository.save(cronogramaTentativo);
    }

    public ResponseEntity<?> update(Long id, CronogramaTentativoDto dto) {

        Optional<CronogramaTentativo> existenteOpt = cronogramaTentativoRepository.findById(id);
        if (existenteOpt.isEmpty())
            return new ResponseEntity<>(new Mensaje("No existe el cronograma tentativo indicado"),
                    HttpStatus.NOT_FOUND);

        CronogramaTentativo existente = existenteOpt.get();

        if (!existente.isActivo()) {
            return new ResponseEntity<>(new Mensaje("No se puede editar un cronograma tentativo inactivo"),
                    HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<?> respuestaValidaciones = validations(dto);
        if (respuestaValidaciones.getStatusCode() != HttpStatus.OK) {
            return respuestaValidaciones;
        }

        // Historizar: dar de baja el existente
        existente.setActivo(false);
        existente.setObservacion("Reemplazado por edición el " + LocalDate.now());
        cronogramaTentativoRepository.save(existente);

        // Crear el nuevo, con los datos actualizados del DTO
        CronogramaTentativo nuevo = createUpdate(new CronogramaTentativo(), dto);
        CronogramaTentativo guardado = cronogramaTentativoRepository.save(nuevo);

        return new ResponseEntity<>(guardado, HttpStatus.OK);
    }

    public void logicDelete(Long id, String observacion) {

        CronogramaTentativo cronogramaTentativo = cronogramaTentativoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el cronograma tentativo con el ID: " + id));

        // Validar que los campos requeridos no sean nulos
        if (observacion == null || observacion.isBlank()) {
            throw new ValidationException("Es obligatorio indicar observacion");
        }

        // Actualiza el legajo
        cronogramaTentativo.setActivo(false);
        cronogramaTentativo.setObservacion(observacion);

        cronogramaTentativoRepository.save(cronogramaTentativo);
    }

    public boolean existCronograma(CronogramaTentativoDto dto) {
        // Validar parámetros
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo.");
        }

        // Validar campos obligatorios
        if (dto.getFechaIngreso() == null || dto.getFechaEgreso() == null ||
                dto.getHoraIngreso() == null || dto.getHoraEgreso() == null ||
                dto.getIdAsistencial() == null || dto.getIdEfector() == null) {
            throw new IllegalArgumentException("Los campos obligatorios no pueden ser nulos.");
        }

        // Verificar si el asistencial existe
        if (!asistencialRepository.existsById(dto.getIdAsistencial())) {
            throw new EntityNotFoundException("El asistencial con ID " + dto.getIdAsistencial() + " no existe.");
        }

        // Verificar si el efector existe
        if (!efectorService.existsById(dto.getIdEfector())) {
            throw new EntityNotFoundException("El efector con ID " + dto.getIdEfector() + " no existe.");
        }

        // Verificar si existe un cronograma tentativo que coincida con los datos
        // proporcionados
        return cronogramaTentativoRepository.existsByCronogramaTentativo(
                dto.getFechaIngreso(), dto.getFechaEgreso(), dto.getHoraIngreso(), dto.getHoraEgreso(),
                dto.getIdAsistencial(), dto.getIdEfector()) == 1;
    }

    public List<Long> efectoresConCronogramaSuperpuesto(CronogramaTentativoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo.");
        }

        if (dto.getFechaIngreso() == null || dto.getFechaEgreso() == null ||
                dto.getHoraIngreso() == null || dto.getHoraEgreso() == null ||
                dto.getIdAsistencial() == null || dto.getIdEfector() == null) {
            throw new IllegalArgumentException("Los campos obligatorios no pueden ser nulos.");
        }

        return cronogramaTentativoRepository.findEfectoresConCronogramaSuperpuesto(
                dto.getFechaIngreso(), dto.getFechaEgreso(),
                dto.getHoraIngreso(), dto.getHoraEgreso(),
                dto.getIdAsistencial());
    }

    public void autorizar(Long id) {
        CronogramaTentativo cronogramaTentativo = cronogramaTentativoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el cronograma tentativo con el ID: " + id));

        // Actualiza el tentativo
        cronogramaTentativo.setAutorizado(AutorizadoTentativoEnum.CONFIRMADO);
        cronogramaTentativoRepository.save(cronogramaTentativo);
    }

    public void aceptar(Long id) {
        CronogramaTentativo cronogramaTentativo = cronogramaTentativoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el cronograma tentativo con el ID: " + id));

        // Actualiza el tentativo
        cronogramaTentativo.setAceptado(true);
        cronogramaTentativoRepository.save(cronogramaTentativo);
    }

    public void autorizarUpdate(Long id, AutorizadoTentativoEnum nuevoEstado, AutorizadoUpdateDto autorizadoUpdateDto) {
        CronogramaTentativo cronogramaTentativo = cronogramaTentativoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el cronograma tentativo con el ID: " + id));

        if (nuevoEstado == null) {
            throw new ValidationException("El estado autorizado no puede ser nulo.");
        }

        if (cronogramaTentativo.getMotivoAutorizacion() != autorizadoUpdateDto.getMotivoAutorizacion() &&
                autorizadoUpdateDto.getMotivoAutorizacion() != null)
            cronogramaTentativo.setMotivoAutorizacion(autorizadoUpdateDto.getMotivoAutorizacion());

        if (cronogramaTentativo.getMotivoPendiente() != autorizadoUpdateDto.getMotivoPendiente() &&
                autorizadoUpdateDto.getMotivoPendiente() != null)
            cronogramaTentativo.setMotivoPendiente(autorizadoUpdateDto.getMotivoPendiente());

        Long idNuevaAutoridad = autorizadoUpdateDto.getIdAutoridad();
        // Solo entramos si el ID autoridad del DTO no es nulo
        if (idNuevaAutoridad != null) {
            if (cronogramaTentativo.getAutoridad() == null ||
                    !Objects.equals(cronogramaTentativo.getAutoridad().getId(), idNuevaAutoridad)) {
                cronogramaTentativo.setAutoridad(autoridadService.findById(idNuevaAutoridad)
                        .orElseThrow(() -> new EntityNotFoundException("Autoridad no encontrada")));
            }
        }

        cronogramaTentativo.setAutorizado(nuevoEstado);
        cronogramaTentativoRepository.save(cronogramaTentativo);
    }

    public Optional<List<CronogramaTentativoSummaryDto>> findByEfectorIdAndActivoTrueAndAutorizadoFalse(
            Long idEfector) {

        List<CronogramaTentativo> tentativos = cronogramaTentativoRepository
                .findByEfectorIdAndActivoTrueAndAutorizadoFalse(idEfector)
                .orElse(Collections.emptyList());

        List<CronogramaTentativoSummaryDto> dtos = tentativos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return Optional.of(dtos);
    }

    public CronogramaTentativoSummaryDto convertToDto(CronogramaTentativo tentativo) {
        CronogramaTentativoSummaryDto dto = new CronogramaTentativoSummaryDto();

        dto.setId(tentativo.getId());
        dto.setFechaIngreso(tentativo.getFechaIngreso());
        dto.setFechaEgreso(tentativo.getFechaEgreso());
        dto.setHoraIngreso(tentativo.getHoraIngreso());
        dto.setHoraEgreso(tentativo.getHoraEgreso());
        dto.setActivo(tentativo.isActivo());
        dto.setAceptado(tentativo.isAceptado());
        dto.setAutorizado(tentativo.getAutorizado());
        dto.setIdTipoGuardia(tentativo.getTipoGuardia().getId());
        dto.setIdAsistencial(tentativo.getAsistencial().getId());
        dto.setIdServicio(tentativo.getServicio().getId());
        dto.setIdEfector(tentativo.getEfector().getId());
        dto.setObservacion(tentativo.getObservacion());

        return dto;
    }

    public VerificacionTentativoResponseDto verificarRegistroIngresoEnTentativo(RegActivRegIngresoDto dto) {
        List<CronogramaTentativo> cronogramas = cronogramaTentativoRepository.findCronogramaParaRegistroConRetraso(
                dto.getIdAsistencial(),
                dto.getIdEfector(),
                dto.getIdTipoGuardia(),
                dto.getIdServicio(),
                dto.getFechaIngreso(),
                dto.getHoraIngreso());

        if (cronogramas.isEmpty()) {
            return new VerificacionTentativoResponseDto(null, false);
        }

        return new VerificacionTentativoResponseDto(cronogramas.get(0).getId(), true);
    }

    public Optional<List<CronogramaTentativoListAtorizadoDto>> findByEfectorIdAndAutorizado(Long efectorId,
            AutorizadoTentativoEnum autorizado) {
        List<CronogramaTentativo> tentativos = cronogramaTentativoRepository
                .findByEfectorIdAndAutorizado(efectorId, autorizado).orElse(Collections.emptyList());

        List<CronogramaTentativoListAtorizadoDto> dtos = tentativos.stream()
                .map(this::convertToDtoAutorizados)
                .collect(Collectors.toList());

        return Optional.of(dtos);
    }

    public Optional<List<CronogramaTentativoListAtorizadoDto>> findByAsistencialIdAndAutorizado(Long asistencialId,
            AutorizadoTentativoEnum autorizado) {
        List<CronogramaTentativo> tentativos = cronogramaTentativoRepository
                .findByAsistencialIdAndAutorizado(asistencialId, autorizado).orElse(Collections.emptyList());

        List<CronogramaTentativoListAtorizadoDto> dtos = tentativos.stream()
                .map(this::convertToDtoAutorizados)
                .collect(Collectors.toList());

        return Optional.of(dtos);
    }

    public CronogramaTentativoListAtorizadoDto convertToDtoAutorizados(CronogramaTentativo tentativo) {
        CronogramaTentativoListAtorizadoDto dto = new CronogramaTentativoListAtorizadoDto();

        dto.setId(tentativo.getId());

        // Convertir Asistencial a AsistencialDetailDto
        AsistencialDetailDto asistencialDetail = new AsistencialDetailDto();
        asistencialDetail.setId(tentativo.getAsistencial().getId());
        asistencialDetail.setNombre(tentativo.getAsistencial().getNombre());
        asistencialDetail.setApellido(tentativo.getAsistencial().getApellido());
        asistencialDetail.setCuil(tentativo.getAsistencial().getCuil());
        dto.setAsistencial(asistencialDetail);

        dto.setIdEfector(tentativo.getEfector().getId());
        dto.setTipoGuardia(tentativo.getTipoGuardia().getNombre().name());
        dto.setFechaIngreso(tentativo.getFechaIngreso());
        dto.setFechaEgreso(tentativo.getFechaEgreso());
        dto.setHoraIngreso(tentativo.getHoraIngreso());
        dto.setHoraEgreso(tentativo.getHoraEgreso());
        dto.setAutorizado(tentativo.getAutorizado());
        dto.setIdServicio(tentativo.getServicio().getId());
        dto.setMotivoAutorizacion(tentativo.getMotivoAutorizacion());
        dto.setMotivoPendiente(tentativo.getMotivoPendiente());
        dto.setIdAutoridad(tentativo.getAutoridad() != null ? tentativo.getAutoridad().getId() : null);

        return dto;
    }

    public Long countPendientesByEfectorId(Long idEfector) {
        return cronogramaTentativoRepository.countByEfectorIdAndEstado(
                idEfector,
                AutorizadoTentativoEnum.PENDIENTE);
    }

    public Long countPendientesByAsistencialId(Long idAsistencial) {
        return cronogramaTentativoRepository.countByAsistencialIdAndEstado(
                idAsistencial,
                AutorizadoTentativoEnum.PENDIENTE);
    }

    public boolean existenCronogramasDesdeFecha(LocalDate fechaInicio, Long idAsistencial, Long idEfector) {
        LocalDate finDeMes = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());
        return cronogramaTentativoRepository.existsByFechaIngresoBetweenAndActivoTrue(fechaInicio, finDeMes,
                idAsistencial, idEfector);
    }

    public TotalHorasResponseDto calcularTotalHorasPorDia(LocalDate fecha, Long idEfector, List<Long> idsAsistencial,
            List<Long> idsServicio) {
        List<CronogramaTentativo> cronogramas = cronogramaTentativoRepository.findByFechaIngresoAndActivoTrue(fecha);

        // Filter by Efector (Mandatory)
        cronogramas = cronogramas.stream()
                .filter(c -> c.getEfector().getId().equals(idEfector))
                .collect(Collectors.toList());

        // Filter by Asistencial
        if (idsAsistencial != null && !idsAsistencial.isEmpty()) {
            cronogramas = cronogramas.stream()
                    .filter(c -> idsAsistencial.contains(c.getAsistencial().getId()))
                    .collect(Collectors.toList());
        }
        // Filter by Servicio
        if (idsServicio != null && !idsServicio.isEmpty()) {
            cronogramas = cronogramas.stream()
                    .filter(c -> idsServicio.contains(c.getServicio().getId()))
                    .collect(Collectors.toList());
        }

        Map<String, List<CronogramaTentativo>> grouped = cronogramas.stream()
                .collect(Collectors.groupingBy(c -> c.getAsistencial().getId() + "-" + c.getServicio().getId()));

        List<TotalHorasDto> detalles = new ArrayList<>();
        double totalGeneral = 0;

        for (List<CronogramaTentativo> list : grouped.values()) {
            double totalHoras = 0;
            for (CronogramaTentativo c : list) {
                LocalDateTime inicio = LocalDateTime.of(c.getFechaIngreso(), c.getHoraIngreso());
                LocalDateTime fin = LocalDateTime.of(c.getFechaEgreso(), c.getHoraEgreso());
                totalHoras += Duration.between(inicio, fin).toMinutes() / 60.0;
            }
            totalGeneral += totalHoras;

            CronogramaTentativo first = list.get(0);
            TotalHorasDto dto = new TotalHorasDto(
                    fecha,
                    first.getAsistencial().getId(),
                    first.getAsistencial().getNombre(),
                    first.getAsistencial().getApellido(),
                    first.getServicio().getId(),
                    first.getServicio().getDescripcion(),
                    totalHoras);
            detalles.add(dto);
        }
        return new TotalHorasResponseDto(detalles, totalGeneral);
    }

    public List<TotalHorasDiaDto> calcularTotalHorasPorMes(
            YearMonth mes,
            Long idEfector,
            List<Long> idsAsistencial,
            List<Long> idsServicio) {

        LocalDate fechaInicio = mes.atDay(1);
        LocalDate fechaEgreso = mes.atEndOfMonth();

        List<CronogramaTentativo> cronogramas = cronogramaTentativoRepository
                .findByFechaIngresoBetweenAndActivoTrue(
                        fechaInicio,
                        fechaEgreso);

        cronogramas = cronogramas.stream()
                .filter(c -> c.getEfector().getId().equals(idEfector))
                .filter(c -> idsAsistencial == null
                        || idsAsistencial.isEmpty()
                        || idsAsistencial.contains(c.getAsistencial().getId()))
                .filter(c -> idsServicio == null
                        || idsServicio.isEmpty()
                        || idsServicio.contains(c.getServicio().getId()))
                .collect(Collectors.toList());

        Map<LocalDate, Double> totales = new LinkedHashMap<>();

        for (LocalDate fecha = fechaInicio; !fecha.isAfter(fechaEgreso); fecha = fecha.plusDays(1)) {

            totales.put(fecha, 0.0);
        }

        for (CronogramaTentativo c : cronogramas) {

            LocalDateTime inicio = LocalDateTime.of(
                    c.getFechaIngreso(),
                    c.getHoraIngreso());

            LocalDateTime fin = LocalDateTime.of(
                    c.getFechaEgreso(),
                    c.getHoraEgreso());

            double horas = Duration
                    .between(inicio, fin)
                    .toMinutes() / 60.0;

            totales.merge(
                    c.getFechaIngreso(),
                    horas,
                    Double::sum);
        }

        return totales.entrySet()
                .stream()
                .map(e -> new TotalHorasDiaDto(
                        e.getKey(),
                        e.getValue()))
                .collect(Collectors.toList());
    }

    public boolean updateCronogramasDesdeFecha(LocalDate fechaInicio, Long idAsistencial, Long idEfector) {
        LocalDate finDeMes = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());

        // Buscar cronogramas activos en el rango de fechas
        List<CronogramaTentativo> cronogramas = cronogramaTentativoRepository
                .findByFechaIngresoBetweenAndActivoTrue(fechaInicio, finDeMes, idAsistencial, idEfector);

        if (cronogramas.isEmpty()) {
            return false; // No hay cronogramas para actualizar
        }

        // Actualizar el estado de cada cronograma
        for (CronogramaTentativo cronograma : cronogramas) {
            // cronograma.setActivo(false);
            cronograma.setAutorizado(AutorizadoTentativoEnum.ANULADO);
        }

        // Guardar los cambios
        cronogramaTentativoRepository.saveAll(cronogramas);
        return true; // Se actualizaron cronogramas
    }

    public Optional<List<CronogramaTentativo>> findAnuladosByEfectorId(Long idEfector) {
        return cronogramaTentativoRepository.findByEfectorIdAndAnulado(idEfector, AutorizadoTentativoEnum.ANULADO);
    }

    public Optional<List<CronogramaTentativo>> findAnuladosByEfectorAndAsistencial(LocalDate fechaInicio,
            Long idAsistencial, Long idEfector) {
        LocalDate finDeMes = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());

        return Optional.of(cronogramaTentativoRepository.findByFechaIngresoBetweenAndAutorizado(
                fechaInicio, finDeMes, idAsistencial, idEfector, AutorizadoTentativoEnum.ANULADO));
    }

    public CronogramaTentativoServicioDto obtenerServicio(Long idTentativo) {
        if (!activo(idTentativo)) {
            throw new EntityNotFoundException("Cronograma tentativo activo no encontrado con ID: " + idTentativo);
        }

        CronogramaTentativo cronograma = cronogramaTentativoRepository.findById(idTentativo)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cronograma tentativo no encontrado con ID: " + idTentativo));

        if (cronograma.getServicio() == null) {
            throw new IllegalStateException("El cronograma tentativo no tiene un servicio asociado");
        }

        return new CronogramaTentativoServicioDto(
                cronograma.getServicio().getId(),
                cronograma.getServicio().getDescripcion());
    }

    public LocalDateTime calcularHoraMaximaSalida(RegActivRegIngresoDto dto) {

        List<CronogramaTentativo> cronogramas = cronogramaTentativoRepository.findCronogramaParaRegistro(
                dto.getIdAsistencial(),
                dto.getIdEfector(),
                dto.getIdTipoGuardia(),
                dto.getIdServicio(),
                dto.getFechaIngreso(),
                dto.getHoraIngreso());

        System.out.println("Número de cronogramas encontrados: " + cronogramas.size());
        if (cronogramas.isEmpty()) {
            System.out.println("No se encontraron cronogramas tentativos");
            return null;
        }

        CronogramaTentativo tentativo = cronogramas.get(0);

        System.out.println("Cronograma encontrado - Detalles:");
        System.out.println("ID: " + tentativo.getId());
        System.out.println("FechaIngreso: " + tentativo.getFechaIngreso());
        System.out.println("HoraIngreso: " + tentativo.getHoraIngreso());
        System.out.println("FechaEgreso: " + tentativo.getFechaEgreso());
        System.out.println("HoraEgreso: " + tentativo.getHoraEgreso());
        System.out.println("Autorizado: " + tentativo.getAutorizado());

        // Calculamos la duración total de la guardia
        LocalDateTime inicioGuardia = LocalDateTime.of(tentativo.getFechaIngreso(), tentativo.getHoraIngreso());
        LocalDateTime finGuardia = LocalDateTime.of(tentativo.getFechaEgreso(), tentativo.getHoraEgreso());
        Duration duracionGuardia = Duration.between(inicioGuardia, finGuardia);

        // Calculamos la hora máxima de salida sumando la duración al ingreso recibido
        LocalDateTime resultado = LocalDateTime.of(dto.getFechaIngreso(), dto.getHoraIngreso())
                .plus(duracionGuardia)
                .plusMinutes(15);

        System.out.println("Resultado calculado: " + resultado);
        System.out.println("=== FIN calcularHoraMaximaSalida ===");

        return resultado;
    }

    public Long getIdAutoridadByIdUsuario(Long idUsuario) {
        // Buscar el usuario por id
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + idUsuario));

        // Obtener el id de la persona asociada al usuario
        Long personId = usuario.getPerson() != null ? usuario.getPerson().getId() : null;
        if (personId == null) {
            throw new EntityNotFoundException("El usuario no tiene una persona asociada");
        }

        // Buscar la autoridad activa y confirmada por persona
        Autoridad autoridad = autoridadRepository.findByPersonaId(personId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró autoridad activa para la persona con id: " + personId))
                .stream()
                .filter(a -> Boolean.TRUE.equals(a.isActivo()) && Boolean.TRUE.equals(a.getConfirmado()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró autoridad activa y confirmada para la persona con id: " + personId));

        return autoridad.getId();
    }

    public LocalDateTime calcularHoraMaximaIngreso(RegActivRegIngresoDto dto) {

        List<CronogramaTentativo> cronogramas = cronogramaTentativoRepository.findCronogramaParaRegistro(
                dto.getIdAsistencial(),
                dto.getIdEfector(),
                dto.getIdTipoGuardia(),
                dto.getIdServicio(),
                dto.getFechaIngreso(),
                dto.getHoraIngreso());

        System.out.println("Número de cronogramas encontrados: " + cronogramas.size());
        if (cronogramas.isEmpty()) {
            System.out.println("No se encontraron cronogramas tentativos");
            return null;
        }

        CronogramaTentativo tentativo = cronogramas.get(0);

        System.out.println("Cronograma encontrado - Detalles:");
        System.out.println("ID: " + tentativo.getId());
        System.out.println("FechaIngreso: " + tentativo.getFechaIngreso());
        System.out.println("HoraIngreso: " + tentativo.getHoraIngreso());

        // Tomamos la hora de inicio del cronograma y le sumamos 1 hora
        LocalDateTime horaMaximaEntrada = LocalDateTime.of(tentativo.getFechaIngreso(), tentativo.getHoraIngreso())
                .plusHours(1);

        System.out.println("hora maxima de entrada calculada: " + horaMaximaEntrada);
        System.out.println("=== FIN calcularHoraMaximaIngreso ===");

        return horaMaximaEntrada;
    }

    public TentativoIdsResponseDto obtenerIdsCronograma(TentativoSearchRequestDto request) {
        Optional<CronogramaTentativo> cronogramaOpt = cronogramaTentativoRepository.obtenerIdsCronograma(
                request.getIdAsistencial(),
                request.getIdEfector(),
                request.getFechaIngreso(),
                request.getHoraIngreso());

        if (cronogramaOpt.isPresent()) {
            CronogramaTentativo cronograma = cronogramaOpt.get();
            Long idServicio = cronograma.getServicio().getId();
            Long idTipoGuardia = cronograma.getTipoGuardia().getId();

            return new TentativoIdsResponseDto(idServicio, idTipoGuardia);
        }

        // Si no se encuentra el cronograma, devolvemos un DTO con ambos IDs null
        return new TentativoIdsResponseDto(null, null);
    }

}
