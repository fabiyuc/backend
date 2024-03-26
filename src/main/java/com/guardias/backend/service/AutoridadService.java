package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.repository.AutoridadRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AutoridadService {

    @Autowired
    AutoridadRepository autoridadRepository;

    @Autowired
    EfectorService efectorService;

    @Autowired
    @Lazy
    PersonService personaService;

    public Optional<List<Autoridad>> findByActivoTrue() {
        return autoridadRepository.findByActivoTrue();
    }

    public List<Autoridad> findAll() {
        return autoridadRepository.findAll();
    }

    public Optional<Autoridad> findById(Long id) {
        return autoridadRepository.findById((Long) id);
    }

    public Optional<List<Autoridad>> findByPersonaId(Long personaId) {
        return autoridadRepository.findByPersonaId(personaId);
    }

    public Optional<List<Autoridad>> findByFechaInicio(LocalDate fechaInicio) {
        return autoridadRepository.findByFechaInicio(fechaInicio);
    }

    public Optional<List<Autoridad>> findByEfectorId(Long efectorId) {
        return autoridadRepository.findByEfectorId(efectorId);
    }

    public Optional<Autoridad> findByNombre(String nombre) {
        return autoridadRepository.findByNombre(nombre);
    }

    public boolean existsByNombreAndIdNot(String nombre, Long id) {
        return autoridadRepository.existsByNombreAndIdNot(nombre, id);
    }

    public boolean existsById(Long id) {
        return autoridadRepository.existsById((Long) id);
    }

    public boolean existsByNombre(String nombre) {
        return autoridadRepository.existsByNombre(nombre);
    }

    public boolean activo(Long id) {
        return (autoridadRepository.existsById(id) && autoridadRepository.findById(id).get().isActivo());
    }

    public boolean activoByNombre(String nombre) {
        return (autoridadRepository.existsByNombre(nombre)
                && autoridadRepository.findByNombre(nombre).get().isActivo());
    }

    public boolean existsByEfectorId(Long efectorId) {
        return autoridadRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public boolean activoByEfectorId(Long efectorId) {
        return autoridadRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return autoridadRepository.existsByPersonaId(personaId);
    }

    public boolean activoByPersonaId(Long personaId) {
        return autoridadRepository.existsByPersonaId(personaId) && personaService.activoById(personaId);
    }

    public void save(Autoridad autoridad) {
        autoridadRepository.save(autoridad);
    }

    public void deleteById(Long id) {
        autoridadRepository.deleteById((Long) id);
    }

    /*
     * public void agregarEfector(Long efectorId, Long idAutoridad) {
     * Efector efector = efectorService.findEfector(efectorId);
     * if (efector == null)
     * throw new EntityNotFoundException("No se encontró el efector con el ID: " +
     * efectorId);
     * 
     * Autoridad autoridad = findById(idAutoridad).orElseThrow(
     * () -> new EntityNotFoundException("No se encontró la autoridad con el ID: " +
     * idAutoridad));
     * 
     * autoridad.setEfector(efector);
     * efector.getAutoridades().add(autoridad);
     * 
     * save(autoridad);
     * efectorService.saveEfector(efector);
     * }
     * 
     * public void agregarPersona(Long idPersona, Long idAutoridad) {
     * Person persona = personaService.findPerson(idPersona);
     * if (persona == null)
     * throw new EntityNotFoundException("No se encontró la persona con el ID: " +
     * idPersona);
     * 
     * Autoridad autoridad = findById(idAutoridad).orElseThrow(
     * () -> new EntityNotFoundException("No se encontró la autoridad con el ID: " +
     * idAutoridad));
     * 
     * autoridad.setPersona(persona);
     * save(autoridad);
     * persona.getAutoridades().add(autoridad);
     * 
     * personaService.savePersona(persona);
     * }
     */

}
