package com.guardias.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asistencial.AsistencialSummaryDto;
import com.guardias.backend.dto.registroActividad.RegActivNombresDto;
import com.guardias.backend.dto.registroActividad.RegActivRegSalidaDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistrosPendientes;
import com.guardias.backend.enums.TipoGuardiaEnum;
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

    public boolean tieneRegistroPendiente(Long idEfector, int mes, int anio,
            Long idAsistencial) {

        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con id " + idEfector + " no existe.");
        }
        if (!asistencialService.existsById(idAsistencial)) {
            throw new EntityNotFoundException("El asistencial con id " + idAsistencial + " no existe.");
        }

        try {
            return registrosPendientesRepository.existByEfectorMonthYearAndAsistencial(idEfector, mes, anio,
                    idAsistencial);
        } catch (Exception e) {
            System.err.println("Error en la búsqueda de registros: " + e.getMessage());
            return false;
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

    public List<AsistencialSummaryDto> findConPendientes(Long idEfector, String tipoGuardia) {

        // Validamos y convertimos el tipo de guardia
        TipoGuardiaEnum tipoGuardiaEnum;
        try {
            tipoGuardiaEnum = TipoGuardiaEnum.valueOf(tipoGuardia.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El tipo de guardia proporcionado no es válido: " + tipoGuardia);
        }

        // Primero obtenemos todos los registros pendientes que cumplen con los
        // criterios
        List<RegistrosPendientes> registrosPendientes = registrosPendientesRepository
                .findByEfectorId(idEfector);

        // Filtramos por tipo de guardia y mapeamos a asistenciales únicos
        return registrosPendientes.stream()
                .flatMap(rp -> rp.getRegistrosActividades().stream())
                .filter(ra -> ra.getTipoGuardia().getNombre().equals(tipoGuardiaEnum) && ra.isActivo())
                .map(RegistroActividad::getAsistencial)
                .distinct()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AsistencialSummaryDto convertToDto(Asistencial asistencial) {
        // Obtiene el legajo activo
        Optional<Legajo> legajoActivo = asistencial.getLegajos().stream()
                .filter(legajo -> legajo.getFechaFinal() == null && Boolean.FALSE.equals(legajo.getEsAutoridad()))
                .findFirst();

        // Obtiene el nombre de la profesión (o null si no hay legajo activo o
        // profesión)
        String profesion = legajoActivo
                .map(legajo -> legajo.getProfesion() != null ? legajo.getProfesion().getNombre() : null)
                .orElse(null);

        // Obtiene el idEfector del legajo no autoridad
        Long idEfector = legajoActivo
                .map(legajo -> legajo.getEfectores().isEmpty() ? null : legajo.getEfectores().get(0).getId())
                .orElse(null);

        // Obtiene los tipos de guardia de los registros de actividad
        List<String> tiposGuardia = asistencial.getRegistrosActividades().stream()
                .map(ra -> ra.getTipoGuardia().getNombre().name())
                .distinct()
                .collect(Collectors.toList());

        return new AsistencialSummaryDto(
                asistencial.getId(),
                asistencial.getNombre(),
                asistencial.getApellido(),
                asistencial.getCuil(),
                profesion,
                tiposGuardia,
                idEfector);
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

    /* Elimina un registro de actividad de la lista de pendientes */
    public ResponseEntity<?> deleteRegistroActividad(RegistroActividad registroActividad) {

        // busca el registro pendiente
        Long id = registroActividad.getRegistrosPendientes().getId();
        try {
            if (!activo(id))
                return new ResponseEntity(new Mensaje("No se encontraron registros pendientes"), HttpStatus.NOT_FOUND);

            RegistrosPendientes registrosPendientes = findById(id).get();

            // remueve el registro de actividad de la lista
            registrosPendientes.getRegistrosActividades().remove(registroActividad);
            save(registrosPendientes);

            // Si la lista queda vacia, eliminar el registro pendiente de BD
            if (registrosPendientes.getRegistrosActividades().isEmpty()) {
                registrosPendientes.setEfector(null);

                deleteById(registrosPendientes.getId());
            }

            return new ResponseEntity(new Mensaje("Registro de Actividad eliminado"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    public RegActivRegSalidaDto obtenerRegistroPendienteDto(Long idAsistencial, Long idEfector) {
        RegistroActividad registro = obtenerRegistroActividadPendiente(idAsistencial, idEfector);
        return registro != null ? convertToDto(registro) : null;
    }

    public RegistroActividad obtenerRegistroActividadPendiente(Long idAsistencial, Long idEfector) {
        List<RegistrosPendientes> registrosPendientes = registrosPendientesRepository
                .findAllByEfectorIdAndActivoTrue(idEfector);

        if (registrosPendientes.isEmpty()) {
            return null;
        }

        // Buscar en todos los registros pendientes del asistencial
        for (RegistrosPendientes registroPendiente : registrosPendientes) {
            Optional<RegistroActividad> registroActividad = registroPendiente.getRegistrosActividades().stream()
                    .filter(ra -> ra.getAsistencial() != null && ra.getAsistencial().getId().equals(idAsistencial))
                    .filter(RegistroActividad::isActivo)
                    .findFirst();

            if (registroActividad.isPresent()) {
                return registroActividad.get();
            }
        }

        return null;
    }

    private RegActivRegSalidaDto convertToDto(RegistroActividad registro) {
        if (registro == null) {
            return null;
        }

        RegActivRegSalidaDto dto = new RegActivRegSalidaDto();
        dto.setId(registro.getId());
        dto.setFechaIngreso(registro.getFechaIngreso());

        // Convertir LocalTime a String "HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        dto.setHoraIngreso(registro.getHoraIngreso().format(formatter));

        // dto.setHoraIngreso(registro.getHoraIngreso());

        if (registro.getTipoGuardia() != null) {
            dto.setIdTipoGuardia(registro.getTipoGuardia().getId());
        }
        if (registro.getAsistencial() != null) {
            dto.setIdAsistencial(registro.getAsistencial().getId());
        }

        if (registro.getServicio() != null) {
            dto.setIdServicio(registro.getServicio().getId());
        }

        if (registro.getEfector() != null) {
            dto.setIdEfector(registro.getEfector().getId());
        }

        if (registro.getUsuarioIngreso() != null) {
            dto.setIdUsuarioIngreso(registro.getUsuarioIngreso().getId());
        }

        return dto;
    }

    public List<RegActivNombresDto> listarRegistrosPendientesPorEfector(Long idEfector) {
        List<RegistrosPendientes> registrosPendientes = registrosPendientesRepository.findByEfectorId(idEfector);
        return registrosPendientes.stream()
                .flatMap(rp -> rp.getRegistrosActividades().stream())
                .map(ra -> new RegActivNombresDto(
                        ra.getId(),
                        ra.getFechaIngreso(),
                        ra.getHoraIngreso() != null ? ra.getHoraIngreso().toString() : null,
                        ra.getTipoGuardia() != null ? ra.getTipoGuardia().getNombre() : null,
                        ra.getAsistencial() != null
                                ? ra.getAsistencial().getNombre() + " " + ra.getAsistencial().getApellido()
                                : null,
                        ra.getServicio() != null ? ra.getServicio().getDescripcion() : null,
                        ra.getEfector() != null ? ra.getEfector().getId() : null,
                        ra.getUsuarioIngreso() != null ? ra.getUsuarioIngreso().getId() : null))
                .collect(Collectors.toList());
    }
}
