package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoResquestDto;
import com.guardias.backend.entity.DistribucionGira;
import com.guardias.backend.repository.DistribucionGiraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionGiraService {

    @Autowired
    DistribucionGiraRepository distribucionGiraRepository;

    @Autowired
    EfectorService efectorService;

    @Autowired
    PersonService personService;

    public Optional<List<DistribucionGira>> findByActivoTrue() {
        return distribucionGiraRepository.findByActivoTrue();
    }

    public List<DistribucionGira> findAll() {
        return distribucionGiraRepository.findAll();
    }

    public Optional<DistribucionGira> findById(Long id) {
        return distribucionGiraRepository.findById(id);
    }

    public List<DistribucionGira> findByFechaInicio(LocalDate fechaInicio) {
        return distribucionGiraRepository.findByFechaInicio(fechaInicio);
    }

    public List<DistribucionGira> findByActivoAndPersonaAndFechaInicio(boolean activo, Long personaId,
            LocalDate fechaInicio) {
        return distribucionGiraRepository.findByActivoAndPersonaIdAndFechaInicio(activo, personaId, fechaInicio);
    }

    public List<DistribucionGira> findByActivoAndPersonaAndFechaInicioAndFechaFin(boolean activo, Long personaId,
            LocalDate fechaInicio, LocalDate fechaFinalizacion) {
        return distribucionGiraRepository.findByActivoAndPersonaIdAndFechaInicioAndFechaFin(activo,
                personaId, fechaInicio, fechaFinalizacion);
    }

    public Optional<List<DistribucionGira>> findByPersonaId(Long personaId) {
        return distribucionGiraRepository.findByPersonaId(personaId);
    }

    public Optional<List<DistribucionGira>> findByEfectorId(Long efectorId) {
        return distribucionGiraRepository.findByEfectorId(efectorId);
    }

    public boolean existsById(Long id) {
        return distribucionGiraRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (distribucionGiraRepository.existsById(id)
                && distribucionGiraRepository.findById(id).get().isActivo());
    }

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionGiraRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionGiraRepository.existsByPersonaId(personaId) && personService.activoById(personaId);
    }

    public List<DistribucionGira> findByActivoPersonaAndFechaInicio(Long idPersona, int mes, int anio) {
        return distribucionGiraRepository.findByActivoPersonaAndFechaInicio(idPersona, mes, anio);
    }

    public boolean existsByActivoPersonaAndFechaInicio(Long idPersona, int mes, int anio) {
        return distribucionGiraRepository.existsByActivoPersonaAndFechaInicio(idPersona, mes, anio);
    }

    public void save(DistribucionGira distribucionGira) {
        distribucionGiraRepository.save(distribucionGira);
    }

    public void deleteById(Long id) {
        distribucionGiraRepository.deleteById(id);
    }

    public boolean validarCronogramaEnDistribucion(CronogramaTentativoResquestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo.");
        }

        // Convierto LocalTime a String antes de enviarlo para que SQL Server pueda
        // entenderlo luego como TIME en la comparacion
        String horaIngresoString = dto.getHoraIngreso().toString();
        String horaEgresoString = dto.getHoraEgreso().toString();

        // Busca una distribución de gira válida
        return distribucionGiraRepository.findValidDistribucion(
                dto.getIdAsistencial(), dto.getIdEfector(), dto.getFechaIngreso(),
                horaIngresoString, horaEgresoString).isPresent();

    }

}