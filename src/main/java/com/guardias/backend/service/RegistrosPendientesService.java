package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asistencial.AsistencialSummaryDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistrosPendientes;
import com.guardias.backend.repository.RegistrosPendientesRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistrosPendientesService {
    @Autowired
    RegistrosPendientesRepository registrosPendientesRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    AsistencialService asistencialService;

    public List<RegistrosPendientes> findByActivo() {
        return registrosPendientesRepository.findByActivoTrue();
    }

    public List<RegistrosPendientes> findAll() {
        return registrosPendientesRepository.findAll();
    }

    public Optional<RegistrosPendientes> findById(Long id) {
        return registrosPendientesRepository.findById(id);
    }

    public List<RegistrosPendientes> findByEfectorId(Efector efector) {
        return registrosPendientesRepository.findByEfector(efector);
    }

    public Optional<RegistrosPendientes> findByEfectorAndFecha(Efector efector, LocalDate fecha) {
        return registrosPendientesRepository.findByEfectorAndFecha(efector, fecha);
    }

    public List<RegistrosPendientes> findByEfectorAndMonthYear(Long idEfector, int mes, int anio) {

        boolean efectorExists = efectorService.existsById(idEfector);
        if (!efectorExists) {
            throw new EntityNotFoundException("El efector con id " + idEfector + " no existe.");
        }
        try {
            return registrosPendientesRepository.findByEfectorAndMonthYear(idEfector, mes, anio);
        } catch (Exception e) {
            System.err.println("Error en la búsqueda de registros: " + e.getMessage());
            return null; // o ver de lanzar una excepción personalizada
        }
    }

    public RegistrosPendientes findByEfectorMonthYearAndAsistencial(Long idEfector, int mes, int anio,
            Long idAsistencial) {

        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con id " + idEfector + " no existe.");
        }
        if (!asistencialService.existsById(idAsistencial)) {
            throw new EntityNotFoundException("El asistencial con id " + idAsistencial + " no existe.");
        }

        try {
            return registrosPendientesRepository.findByEfectorMonthYearAndAsistencial(idEfector, mes, anio,
                    idAsistencial);
        } catch (Exception e) {
            System.err.println("Error en la búsqueda de registros: " + e.getMessage());
            return null;
        }
    }

    public List<AsistencialSummaryDto> findAsistencialesConPendientes(Long idEfector, int mes, int anio,
            Long idTipoGuardia) {
        // Primero obtenemos todos los registros pendientes que cumplen con los
        // criterios
        List<RegistrosPendientes> registrosPendientes = registrosPendientesRepository
                .findByEfectorIdAndFechaMonthAndFechaYear(idEfector, mes, anio);

        // Filtramos por tipo de guardia y mapeamos a asistenciales únicos
        return registrosPendientes.stream()
                .flatMap(rp -> rp.getRegistrosActividades().stream())
                .filter(ra -> ra.getTipoGuardia().getId().equals(idTipoGuardia) && ra.isActivo())
                .map(RegistroActividad::getAsistencial)
                .distinct()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AsistencialSummaryDto> findConPendientes(Long idEfector, Long idTipoGuardia) {
        // Primero obtenemos todos los registros pendientes que cumplen con los
        // criterios
        List<RegistrosPendientes> registrosPendientes = registrosPendientesRepository
                .findByEfectorId(idEfector);

        // Filtramos por tipo de guardia y mapeamos a asistenciales únicos
        return registrosPendientes.stream()
                .flatMap(rp -> rp.getRegistrosActividades().stream())
                .filter(ra -> ra.getTipoGuardia().getId().equals(idTipoGuardia) && ra.isActivo())
                .map(RegistroActividad::getAsistencial)
                .distinct()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AsistencialSummaryDto convertToDto(Asistencial asistencial) {
        return new AsistencialSummaryDto(
                asistencial.getId(),
                asistencial.getNombre(),
                asistencial.getApellido(),
                asistencial.getRegistrosActividades().stream()
                        .map(ra -> ra.getTipoGuardia().getNombre().name())
                        .distinct()
                        .collect(Collectors.toList()));
    }

    public void save(RegistrosPendientes registrosPendientes) {
        registrosPendientesRepository.save(registrosPendientes);
    }

    public void deleteById(Long id) {
        registrosPendientesRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return registrosPendientesRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (registrosPendientesRepository.existsById(id)
                && registrosPendientesRepository.findById(id).get().isActivo());
    }

    public RegistroActividad addRegistroActividad(RegistroActividad registroActividad) {

        RegistrosPendientes registrosPendientes = new RegistrosPendientes();
        try {
            registrosPendientes = findByEfectorAndFecha(registroActividad.getEfector(),
                    registroActividad.getFechaIngreso())
                    .get();
        } catch (Exception e) {
            registrosPendientes.setEfector(registroActividad.getEfector());
            registrosPendientes.setFecha(registroActividad.getFechaIngreso());
            registrosPendientes.setActivo(true);
        }
        registrosPendientes.getRegistrosActividades().add(registroActividad);
        save(registrosPendientes);
        registroActividad.setRegistrosPendientes(registrosPendientes);

        return registroActividad;
    }

    public ResponseEntity<?> deleteRegistroActividad(RegistroActividad registroActividad) {
        Long id = registroActividad.getRegistrosPendientes().getId();
        try {
            if (!activo(id))
                return new ResponseEntity(new Mensaje("No se encontraron registros pendientes"), HttpStatus.NOT_FOUND);
            RegistrosPendientes registrosPendientes = findById(id).get();

            registrosPendientes.getRegistrosActividades().remove(registroActividad);
            save(registrosPendientes);

            // Si el listado de registros de actividad esta vacio, eliminar el registro de
            // pendientes
            if (registrosPendientes.getRegistrosActividades().isEmpty()) {
                registrosPendientes.setEfector(null);

                deleteById(registrosPendientes.getId());
            }

            return new ResponseEntity(new Mensaje("Registro de Actividad eliminado"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
