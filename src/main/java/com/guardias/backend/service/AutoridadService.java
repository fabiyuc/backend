package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Person;
import com.guardias.backend.repository.AutoridadRepository;

import jakarta.persistence.EntityNotFoundException;
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

    public List<Autoridad> findByActivoTrue() {
        return autoridadRepository.findByActivoTrue();
    }

    public List<Autoridad> findAll() {
        return autoridadRepository.findAll();
    }

    public Optional<Autoridad> findById(Long id) {
        return autoridadRepository.findById((Long) id);
    }

    public Optional<Autoridad> getByNombre(String nombre) {
        return autoridadRepository.findByNombre(nombre);
    }

    public Optional<Autoridad> findByNombre(String nombre) {
        return autoridadRepository.findByNombre(nombre);
    }

    public void save(Autoridad autoridad) {
        autoridadRepository.save(autoridad);
    }

    public void deleteById(Long id) {
        autoridadRepository.deleteById((Long) id);
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
     */

    public void agregarEfector(Long idAutoridad, Long idEfector) {
        Autoridad autoridad = autoridadRepository.findById(idAutoridad)
                .orElseThrow(
                        () -> new EntityNotFoundException("No se encontró la autoridad con el ID: " + idAutoridad));

        Efector efector = efectorService.findEfector(idEfector);

        if (efector != null) {
            efector.getAutoridades().add(autoridad);
            autoridad.getEfector().add(efector);

            efectorService.saveEfector(efector);
            autoridadRepository.save(autoridad);
        } else {
            throw new EntityNotFoundException("No se encontró el efector con el ID: " + idEfector);
        }
    }

    public void agregarPersona(Long idPersona, Long idAutoridad) {
        Person persona = personaService.findPerson(idPersona);
        if (persona == null)
            throw new EntityNotFoundException("No se encontró la persona con el ID: " +
                    idPersona);

        Autoridad autoridad = findById(idAutoridad).orElseThrow(
                () -> new EntityNotFoundException("No se encontró la autoridad con el ID: " +
                        idAutoridad));

        autoridad.setPersona(persona);
        save(autoridad);
        persona.getAutoridades().add(autoridad);

        personaService.savePersona(persona);
    }

}
