package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.distribucionGuardia.DistribucionGuardiaRequestDto;
import com.guardias.backend.entity.DistribucionGuardia;
import com.guardias.backend.enums.DiasEnum;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.DistribucionConsultorioRepository;
import com.guardias.backend.repository.DistribucionGuardiaRepository;

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
    EfectorService efectorService;
    @Autowired
    PersonService personService;
    @Autowired
    AsistencialRepository asistencialRepository;;

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

    public Long verificarDistribucionGuardia(DistribucionGuardiaRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo.");
        }

        //Convierto LocalTime a String antes de enviarlo para que SQL Server pueda entenderlo luego como TIME en la comparacion
        String horaIngresoString = dto.getHoraIngreso().toString(); // Convierte "08:00" a String

        return distribucionGuardiaRepository.findIdByDistribucionGuardiaDto(
                dto.getIdPersona(),
                dto.getIdEfector(),
                dto.getTipoGuardia(),
                dto.getFechaInicio(),
                dto.getFechaFinalizacion(),
                horaIngresoString,
                BigDecimal.valueOf(dto.getCantidadHoras()) // Conversión a BigDecimal
        );
    }

    public boolean esGuardia(DiasEnum dia, LocalDate fecha, Long idAsistencial, Long idEfector) {

        // Buscar coincidencias en DistribucionGuardia
        boolean guardiaExists = distribucionGuardiaRepository.existsByDiaAndFechaAndIdPersonaAndIdEfector(
                dia, fecha, idAsistencial, idEfector);

        // Si existe DistribucionGuardia que coincida, retornar true
        if (guardiaExists)
            return true;

        // Buscar coincidencias en DistribucionConsultorio
        boolean consultorioExists = distribucionConsultorioRepository.existsByDiaAndFechaAndPersonaAndEfector(
                dia, fecha, idAsistencial, idEfector);

        // Si existe DistribucionConsultorio que coincida, retornar false
        return !consultorioExists;

    }

}