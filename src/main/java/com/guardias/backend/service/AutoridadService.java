package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.AutoridadDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.Person;
import com.guardias.backend.enums.TipoGuardiaEnum;
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

    @Autowired
    CargoService cargoService;

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

    public boolean existsById(Long id) {
        return autoridadRepository.existsById((Long) id);
    }

    public boolean activo(Long id) {
        return (autoridadRepository.existsById(id)
                && autoridadRepository.findById(id).get().isActivo());
    }

    public boolean existsByPersonaId(Long personaId) {
        return autoridadRepository.existsByPersonaId(personaId);
    }

    public boolean activoByPersonaId(Long personaId) {
        return autoridadRepository.existsByPersonaId(personaId) && personaService.activoById(personaId);
    }

    public boolean isAutoridad(Long idPersona) {
        // Busca si la persona ya tiene una autoridad activa
        return autoridadRepository.existsByPersonaIdAndActivoTrueAndConfirmadoTrue(idPersona);
    }

    public ResponseEntity<?> validations(AutoridadDto autoridadDto) {

        if (autoridadDto.getIdPersona() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("la persona es obligatoria"), HttpStatus.BAD_REQUEST);

        // Verifico que no exista una autoridad activa para esta persona
        if (autoridadRepository.existsByPersonaIdAndActivoTrue(autoridadDto.getIdPersona())) {
            return new ResponseEntity<>(new Mensaje("La persona ya tiene una autoridad activa"),
                    HttpStatus.BAD_REQUEST);
        }

        /*
         * Person persona = personaService.findById(autoridadDto.getIdPersona());
         * if (persona != null) {
         * 
         * // Filtrar los legajos activos
         * List<Legajo> legajosActivos = persona.getLegajos().stream()
         * .filter(Legajo::isActivo)
         * .collect(Collectors.toList());
         * 
         * // Si no hay legajos activos, permito la creación de la autoridad
         * if (legajosActivos.isEmpty()) {
         * return new ResponseEntity<>(new Mensaje("Válido"), HttpStatus.OK);
         * }
         * 
         * // Verifico los tipos de guardia en los legajos activos
         * boolean tieneGuardiaCargoOAgrupacion = legajosActivos.stream()
         * .flatMap(legajo -> legajo.getTipoGuardias().stream())
         * .anyMatch(tipoGuardia -> tipoGuardia.getNombre() == TipoGuardiaEnum.CARGO ||
         * tipoGuardia.getNombre() == TipoGuardiaEnum.AGRUPACION);
         * 
         * boolean tieneGuardiaExtraOContraFactura = legajosActivos.stream()
         * .flatMap(legajo -> legajo.getTipoGuardias().stream())
         * .anyMatch(tipoGuardia -> tipoGuardia.getNombre() == TipoGuardiaEnum.EXTRA ||
         * tipoGuardia.getNombre() == TipoGuardiaEnum.CONTRAFACTURA);
         * 
         * // Validación basada en los tipos de guardia
         * if (tieneGuardiaCargoOAgrupacion) {
         * return new ResponseEntity<>(
         * new
         * Mensaje("La persona ya tiene un legajo activo con guardia de tipo 'cargo' o 'agrupacion'"
         * ),
         * HttpStatus.BAD_REQUEST);
         * } else if (tieneGuardiaExtraOContraFactura || legajosActivos.isEmpty()) {
         * return new ResponseEntity<>(new Mensaje("Válido"), HttpStatus.OK);
         * }
         * }
         */

        /*
         * return new ResponseEntity<>(new Mensaje("Persona no encontrada"),
         * HttpStatus.NOT_FOUND);
         */

        return new ResponseEntity<>(new Mensaje("valido"), HttpStatus.OK);
    }

    public boolean esValidaParaCrearAutoridad(Long idPersona) {
        Person persona = personaService.findById(idPersona);
        if (persona == null) {
            return false; // La persona no existe
        }

        // Filtrar los legajos activos
        List<Legajo> legajosActivos = persona.getLegajos().stream()
                .filter(Legajo::isActivo)
                .collect(Collectors.toList());

        // Si no hay legajos activos, permito la creación de la autoridad
        if (legajosActivos.isEmpty()) {
            return true; // Es válido porque no tiene legajos activos
        }

        // Verifico los tipos de guardia en los legajos activos
        boolean tieneGuardiaCargoOAgrupacion = legajosActivos.stream()
                .flatMap(legajo -> legajo.getTipoGuardias().stream())
                .anyMatch(tipoGuardia -> tipoGuardia.getNombre() == TipoGuardiaEnum.CARGO ||
                        tipoGuardia.getNombre() == TipoGuardiaEnum.AGRUPACION);

        // Si tiene guardia de tipo "cargo" o "agrupación", no es válido
        if (tieneGuardiaCargoOAgrupacion) {
            return false;
        }

        // Si no tiene guardias de "cargo" o "agrupación", es válido
        return true;
    }

    public Autoridad create(AutoridadDto autoridadDto) {

        Autoridad autoridad = new Autoridad();
        autoridad.setActivo(true);
        autoridad.setPersona(personaService.findById(autoridadDto.getIdPersona()));
        return autoridad;
    }

    public void save(Autoridad autoridad) {
        autoridadRepository.save(autoridad);
    }

    public void deleteById(Long id) {
        autoridadRepository.deleteById((Long) id);
    }

    public boolean hasActiveAutoridadLegajo(Long idPersona) {
        return autoridadRepository.findActiveAutoridadLegajoByPersonaId(idPersona).isPresent();
    }

}
