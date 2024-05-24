package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistrosPendientes;
import com.guardias.backend.repository.RegistrosPendientesRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistrosPendientesService {
    @Autowired
    RegistrosPendientesRepository registrosPendientesRepository;
    // @Autowired
    // RegistroActividadService registroActividadService;

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

    public ResponseEntity<?> deleteRegistroActividad(RegistroActividad registroActividad) {
        Long id = registroActividad.getRegistrosPendientes().getId();
        try {
            if (!activo(id))
                return new ResponseEntity(new Mensaje("No se encontraron registros pendientes"), HttpStatus.NOT_FOUND);
            RegistrosPendientes registrosPendientes = findById(id).get();

            registroActividad.setRegistrosPendientes(null);
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

    public RegistroActividad addRegistroActividad(RegistroActividad registroActividad) {

        RegistrosPendientes registrosPendientes = new RegistrosPendientes();
        try {
            registrosPendientes = findByEfectorAndFecha(registroActividad.getEfector(),
                    registroActividad.getFechaIngreso()).get();
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
}
