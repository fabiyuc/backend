package com.guardias.backend.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.CronogramaTentativoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoListAtorizadoDto;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoSummaryDto;
import com.guardias.backend.dto.registroActividad.RegActivRegIngresoDto;
import com.guardias.backend.entity.CronogramaTentativo;
import com.guardias.backend.enums.AutorizadoTentativoEnum;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.CronogramaTentativoRepository;

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

        cronogramaTentativo.setAceptado(cronogramaTentativoDto.isAceptado());
        cronogramaTentativo.setAutorizado(cronogramaTentativoDto.getAutorizado());
        cronogramaTentativo.setActivo(true);
        return cronogramaTentativo;
    }

    public void save(CronogramaTentativo cronogramaTentativo) {
        cronogramaTentativoRepository.save(cronogramaTentativo);
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
                dto.getIdAsistencial(), dto.getIdEfector());
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

     public Optional<List<CronogramaTentativoSummaryDto>> findByEfectorIdAndActivoTrueAndAutorizadoFalse(Long idEfector) {

        List<CronogramaTentativo> tentativos = cronogramaTentativoRepository.findByEfectorIdAndActivoTrueAndAutorizadoFalse(idEfector)
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

    public boolean verificarRegistroIngresoEnTentativo(RegActivRegIngresoDto dto) {
        List<CronogramaTentativo> cronogramas = cronogramaTentativoRepository.findCronogramaParaRegistro(
            dto.getIdAsistencial(),
            dto.getIdEfector(),
            dto.getIdTipoGuardia(),
            dto.getIdServicio(),
            dto.getFechaIngreso(),
            dto.getHoraIngreso()
        );
        
        return !cronogramas.isEmpty(); // True si hay al menos una coincidencia
    }

    public Optional<List<CronogramaTentativoListAtorizadoDto>> findByEfectorIdAndAutorizado(Long efectorId, AutorizadoTentativoEnum autorizado) {
        List<CronogramaTentativo> tentativos = cronogramaTentativoRepository.findByEfectorIdAndAutorizado(efectorId, autorizado).orElse(Collections.emptyList());
        
        List<CronogramaTentativoListAtorizadoDto> dtos = tentativos.stream()
        .map(this::convertToDtoAutorizados)
        .collect(Collectors.toList());

    return Optional.of(dtos);
}

public CronogramaTentativoListAtorizadoDto convertToDtoAutorizados(CronogramaTentativo tentativo) {
        CronogramaTentativoListAtorizadoDto dto = new CronogramaTentativoListAtorizadoDto();
        
        dto.setId(tentativo.getId());
        dto.setIdAsistencial(tentativo.getAsistencial().getId());
        dto.setIdEfector(tentativo.getEfector().getId());
        dto.setTipoGuardia(tentativo.getTipoGuardia().getNombre().name());
        dto.setFechaIngreso(tentativo.getFechaIngreso());
        dto.setFechaEgreso(tentativo.getFechaEgreso());
        dto.setHoraIngreso(tentativo.getHoraIngreso());
        dto.setHoraEgreso(tentativo.getHoraEgreso());
        dto.setAutorizado(tentativo.getAutorizado());
        
        return dto;
    }

    public Long countPendientesByEfectorId(Long idEfector) {
        return cronogramaTentativoRepository.countByEfectorIdAndEstado(
                idEfector, 
                AutorizadoTentativoEnum.PENDIENTE);
    }

}
