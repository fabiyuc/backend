package com.guardias.backend.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.CronogramaTentativoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.CronogramaTentativo;
import com.guardias.backend.repository.CronogramaTentativoRepository;

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
    EfectorService efectorService;

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

        if (cronogramaTentativoDto.getIdEfector() == null) 
            return new ResponseEntity<>(new Mensaje("indicar el efector"), HttpStatus.BAD_REQUEST);
        
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public CronogramaTentativo createUpdate(CronogramaTentativo cronogramaTentativo, CronogramaTentativoDto cronogramaTentativoDto) {

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

        if (cronogramaTentativo.getTipoGuardia() == null || (cronogramaTentativoDto.getIdTipoGuardia() != null && !Objects.equals  (cronogramaTentativo.getTipoGuardia().getId(), cronogramaTentativoDto.getIdTipoGuardia()))) {
            cronogramaTentativo.setTipoGuardia(tipoGuardiaService.findById(cronogramaTentativoDto.getIdTipoGuardia()).get());
        }

        if (cronogramaTentativo.getAsistencial() == null ||
                (cronogramaTentativoDto.getIdAsistencial() != null &&
                        !Objects.equals(cronogramaTentativo.getAsistencial().getId(),
                        cronogramaTentativoDto.getIdAsistencial()))) {
                            cronogramaTentativo.setAsistencial(asistencialService.findById(cronogramaTentativoDto.getIdAsistencial()).get());
        }

        if (cronogramaTentativo.getEfector() == null ||
                (cronogramaTentativoDto.getIdEfector() != null &&
                        !Objects.equals(cronogramaTentativo.getEfector().getId(),
                        cronogramaTentativoDto.getIdEfector()))) {
                                    cronogramaTentativo.setEfector(efectorService.findById(cronogramaTentativoDto.getIdEfector()));
        }

        
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

}
