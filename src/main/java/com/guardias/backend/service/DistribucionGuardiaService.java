package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoResquestDto;
import com.guardias.backend.dto.cronogramaTentativo.ValidacionCronogramaResponseDto;
import com.guardias.backend.dto.distribucionGuardia.DistribucionCheckDto;
import com.guardias.backend.entity.DistribucionGuardia;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.enums.DiasEnum;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.DistribucionConsultorioRepository;
import com.guardias.backend.repository.DistribucionGiraRepository;
import com.guardias.backend.repository.DistribucionGuardiaRepository;
import com.guardias.backend.repository.DistribucionOtraRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionGuardiaService {

    @Autowired
    DistribucionGuardiaRepository distribucionGuardiaRepository;
    @Autowired
    DistribucionConsultorioRepository distribucionConsultorioRepository;
    @Autowired
    DistribucionGiraRepository distribucionGiraRepository;
    @Autowired
    DistribucionOtraRepository distribucionOtraRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    PersonService personService;
    @Autowired
    AsistencialRepository asistencialRepository;

    public Optional<List<DistribucionGuardia>> findByActivoTrue() {
        return distribucionGuardiaRepository.findByActivoTrue();
    }

    public List<DistribucionGuardia> findAll() {
        return distribucionGuardiaRepository.findAll();
    }

    public Optional<DistribucionGuardia> findById(Long id) {
        return distribucionGuardiaRepository.findById(id);
    }

    public Optional<List<DistribucionGuardia>> findByPersonaId(Long personaId) {
        return distribucionGuardiaRepository.findByPersonaId(personaId);
    }

    public List<DistribucionGuardia> findByFechaInicio(LocalDate fechaInicio) {
        return distribucionGuardiaRepository.findByFechaInicio(fechaInicio);
    }

    public List<DistribucionGuardia> findByActivoAndPersonaAndFechaInicio(boolean activo, Long personaId,
            LocalDate fechaInicio) {
        return distribucionGuardiaRepository.findByActivoAndPersonaIdAndFechaInicio(activo, personaId, fechaInicio);
    }

    public Optional<List<DistribucionGuardia>> findByEfectorId(Long efectorId) {
        return distribucionGuardiaRepository.findByEfectorId(efectorId);
    }

    public boolean existsById(Long id) {
        return distribucionGuardiaRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (distribucionGuardiaRepository.existsById(id)
                && distribucionGuardiaRepository.findById(id).get().isActivo());
    }

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionGuardiaRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionGuardiaRepository.existsByPersonaId(personaId) && personService.activoById(personaId);
    }

    public List<DistribucionGuardia> findByActivoPersonaAndFechaInicio(Long idPersona, int mes, int anio) {
        return distribucionGuardiaRepository.findByActivoPersonaAndFechaInicio(idPersona, mes, anio);
    }

    public boolean existsByActivoPersonaAndFechaInicio(Long idPersona, int mes, int anio) {
        return distribucionGuardiaRepository.existsByActivoPersonaAndFechaInicio(idPersona, mes, anio);
    }

    public void save(DistribucionGuardia distribucionGuardia) {
        distribucionGuardiaRepository.save(distribucionGuardia);
    }

    public void deleteById(Long id) {
        distribucionGuardiaRepository.deleteById(id);
    }

    public boolean existDistribucion(DiasEnum dia, LocalDate fecha, Long idAsistencial, Long idEfector) {

        if (dia == null || fecha == null || idAsistencial == null || idEfector == null) {
            throw new IllegalArgumentException(
                    "Los parámetros de día, fecha, horaIngreso, idAsistencial y idEfector no pueden ser nulos.");
        }

        if (!asistencialRepository.existsById(idAsistencial)) {
            throw new EntityNotFoundException("El asistencial con ID " + idAsistencial + " no existe.");
        }

        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con ID " + idEfector + " no existe.");
        }

        // Buscar coincidencias en DistribucionGuardia
        boolean guardiaExists = distribucionGuardiaRepository.existsByDiaAndFechaAndIdPersonaAndIdEfector(
                dia, fecha, idAsistencial, idEfector);

        // Si existe DistribucionGuardia que coincida, retornar true
        if (guardiaExists)
            return true;

        // Buscar coincidencias en DistribucionConsultorio
        boolean consultorioExists = distribucionConsultorioRepository.existsByDiaAndFechaAndPersonaAndEfector(
                dia, fecha, idAsistencial, idEfector);

        // Retornar true si existe alguna coincidencia en cualquiera de las
        // distribuciones
        return consultorioExists;

    }

    public ValidacionCronogramaResponseDto validarCronogramaEnDistribucion(CronogramaTentativoResquestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo.");
        }

        // Convierto LocalTime a String antes de enviarlo para que SQL Server pueda
        // entenderlo luego como TIME en la comparacion
        String horaIngresoString = dto.getHoraIngreso().toString();
        String horaEgresoString = dto.getHoraEgreso().toString();

        // 1. Primero verificamos si hay coincidencia exacta
        boolean coincideExactamente = distribucionGuardiaRepository.findValidDistribucion(
                dto.getIdAsistencial(),
                dto.getIdEfector(),
                dto.getTipoGuardia(),
                dto.getFechaIngreso(),
                horaIngresoString,
                horaEgresoString).isPresent();

        if (coincideExactamente) {
            return new ValidacionCronogramaResponseDto(true, false, false);
        }

        // 2. Verificación de distribución activa parcial (mismo mes y año)
        boolean existeDistribucionParcial = distribucionGuardiaRepository.existsByPersonaAndEfectorAndTipoInMonth(
                        dto.getIdAsistencial(),
                        dto.getIdEfector(),
                        dto.getTipoGuardia(),
                        dto.getFechaIngreso().getMonthValue(),
                        dto.getFechaIngreso().getYear());

        // 3. Determinar si no hay ninguna distribución
        boolean sinDistribucion = !existeDistribucionParcial;

        return new ValidacionCronogramaResponseDto(false, existeDistribucionParcial,sinDistribucion);
    }

    public boolean esGuardia(DiasEnum dia, LocalDate fecha, Long idAsistencial, Long idEfector) {

        // Verifica si existe en DistribucionGuardia
        if (distribucionGuardiaRepository.existsByDiaAndFechaAndIdPersonaAndIdEfector(dia, fecha, idAsistencial,
                idEfector)) {
            return true;
        }

        // Verifica si existe en DistribucionConsultorio
        return !distribucionConsultorioRepository.existsByDiaAndFechaAndPersonaAndEfector(dia, fecha, idAsistencial,
                idEfector);

    }

    public boolean tieneDistribucionActiva(DistribucionCheckDto request) {
        Long idPersona = request.getIdPersona();
        int mes = request.getFecha().getMonthValue();
        int anio = request.getFecha().getYear();

        // 1. Verifica DistribucionGuardia
        if (distribucionGuardiaRepository.existsByPersonaIdAndActivoTrue(idPersona)) {
            boolean existe = distribucionGuardiaRepository
                    .findByPersonaIdAndActivoTrue(idPersona)
                    .stream()
                    .anyMatch(d -> esFechaValida(d, mes, anio));
            if (existe)
                return true;
        }

        // 2. Verifica DistribucionConsultorio
        if (distribucionConsultorioRepository.existsByPersonaIdAndActivoTrue(idPersona)) {
            boolean existe = distribucionConsultorioRepository
                    .findByPersonaIdAndActivoTrue(idPersona)
                    .stream()
                    .anyMatch(d -> esFechaValida(d, mes, anio));
            if (existe)
                return true;
        }

        // 3. Verifica DistribucionGira
        if (distribucionGiraRepository.existsByPersonaIdAndActivoTrue(idPersona)) {
            boolean existe = distribucionGiraRepository
                    .findByPersonaIdAndActivoTrue(idPersona)
                    .stream()
                    .anyMatch(d -> esFechaValida(d, mes, anio));
            if (existe)
                return true;
        }

        // 4. Verifica DistribucionOtra
        if (distribucionOtraRepository.existsByPersonaIdAndActivoTrue(idPersona)) {
            return distribucionOtraRepository
                    .findByPersonaIdAndActivoTrue(idPersona)
                    .stream()
                    .anyMatch(d -> esFechaValida(d, mes, anio));
        }

        return false;
    }

    private boolean esFechaValida(DistribucionHoraria distribucion, int mes, int anio) {
        LocalDate fechaInicio = distribucion.getFechaInicio();
        return fechaInicio.getYear() == anio && fechaInicio.getMonthValue() == mes;
    }

}