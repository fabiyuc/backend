package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.distribucionConsultorio.DistribucionConsultorioRequestDto;
import com.guardias.backend.dto.distribucionGuardia.DistribucionGuardiaRequestDto;
import com.guardias.backend.entity.DistribucionConsultorio;
import com.guardias.backend.repository.DistribucionConsultorioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionConsultorioService {

    @Autowired
    DistribucionConsultorioRepository distribucionConsultorioRepository;

    @Autowired
    EfectorService efectorService;

    @Autowired
    PersonService personService;

    public Optional<List<DistribucionConsultorio>> findByActivoTrue() {
        return distribucionConsultorioRepository.findByActivoTrue();
    }

    public List<DistribucionConsultorio> findAll() {
        return distribucionConsultorioRepository.findAll();
    }

    public Optional<DistribucionConsultorio> findById(Long id) {
        return distribucionConsultorioRepository.findById(id);
    }

    public boolean activo(Long id) {
        return (distribucionConsultorioRepository.existsById(id)
                && distribucionConsultorioRepository.findById(id).get().isActivo());
    }

    public List<DistribucionConsultorio> findByFechaInicio(LocalDate fechaInicio) {
        return distribucionConsultorioRepository.findByFechaInicio(fechaInicio);
    }

    public Optional<List<DistribucionConsultorio>> findByPersonaId(Long personaId) {
        return distribucionConsultorioRepository.findByPersonaId(personaId);
    }

    public Optional<List<DistribucionConsultorio>> findByEfectorId(Long efectorId) {
        return distribucionConsultorioRepository.findByEfectorId(efectorId);
    }

    public boolean existsById(Long id) {
        return distribucionConsultorioRepository.existsById(id);
    }

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionConsultorioRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionConsultorioRepository.existsByPersonaId(personaId) && personService.activoById(personaId);
    }

    public void save(DistribucionConsultorio distribucionConsultorio) {
        distribucionConsultorioRepository.save(distribucionConsultorio);
    }

    public void deleteById(Long id) {
        distribucionConsultorioRepository.deleteById(id);
    }

    public Long verificarDistribucion(DistribucionConsultorioRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo.");
        }

        //Convierto LocalTime a String antes de enviarlo para que SQL Server pueda entenderlo luego como TIME en la comparacion
        String horaIngresoString = dto.getHoraIngreso().toString(); // Convierte "08:00" a String

        return distribucionConsultorioRepository.findIdByDistribucionConsultorioDto(
                dto.getIdPersona(),
                dto.getIdEfector(),
                dto.getFechaInicio(),
                dto.getFechaFinalizacion(),
                horaIngresoString,
                BigDecimal.valueOf(dto.getCantidadHoras()) // Conversión a BigDecimal
        );
    }

}
