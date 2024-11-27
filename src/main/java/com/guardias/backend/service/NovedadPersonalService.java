package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NovedadPersonalDto;
import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.repository.NovedadPersonalRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class NovedadPersonalService {

    @Autowired
    NovedadPersonalRepository novedadPersonalRepository;
    @Autowired
    PersonService personaService;
    @Autowired
    TipoLicenciaService tipoLicenciaService;

    public Optional<List<NovedadPersonal>> findByActivoTrue() {
        return novedadPersonalRepository.findByActivoTrue();
    }

    public List<NovedadPersonal> findAll() {
        return novedadPersonalRepository.findAll();
    }

    public Optional<List<NovedadPersonal>> findByPersona(Long idPersona) {
        return novedadPersonalRepository.findByPersona(idPersona);
    }

    public boolean activoByPersona(Long idPersona) {
        return personaService.activoById(idPersona);
    }

    public Optional<List<NovedadPersonal>> findByFechaInicio(LocalDate fecha) {
        return novedadPersonalRepository.findByFechaInicio(fecha);
    }

    public Optional<NovedadPersonal> findById(Long id) {
        return novedadPersonalRepository.findById((Long) id);
    }

    public boolean existsByFechaInicio(LocalDate fechaInicio) {
        return novedadPersonalRepository.existsByFechaInicio(fechaInicio);
    }

    public boolean existsById(Long id) {
        return novedadPersonalRepository.existsById((Long) id);
    }

    public boolean activo(Long id) {
        return (novedadPersonalRepository.existsById(id)
                && novedadPersonalRepository.findById(id).get().isActivo());
    }

    public void save(NovedadPersonal novedadPersonal) {
        novedadPersonalRepository.save(novedadPersonal);
    }

    public void deleteById(Long id) {
        novedadPersonalRepository.deleteById((Long) id);
    }

    public ResponseEntity<?> validations(NovedadPersonalDto novedadPersonalDto) {
        if (novedadPersonalDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (novedadPersonalDto.getIdPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public NovedadPersonal createUpdate(NovedadPersonal novedadPersonal, NovedadPersonalDto novedadPersonalDto) {

        if (novedadPersonalDto.getFechaInicio() != null
                && !novedadPersonalDto.getFechaInicio().equals(novedadPersonal.getFechaInicio()))
            novedadPersonal.setFechaInicio(novedadPersonalDto.getFechaInicio());

        if (novedadPersonalDto.getFechaFinal() != null
                && !novedadPersonalDto.getFechaFinal().equals(novedadPersonal.getFechaFinal()))
            novedadPersonal.setFechaFinal(novedadPersonalDto.getFechaFinal());

        novedadPersonal.setPuedeRealizarGuardia(novedadPersonalDto.isPuedeRealizarGuardia());
        novedadPersonal.setCobraSueldo(novedadPersonalDto.isCobraSueldo());
        novedadPersonal.setNecesitaReemplazo(novedadPersonalDto.isNecesitaReemplazo());

        // Si el suplente es nulo, se puede asignar null
        if (novedadPersonal.getPersona() == null ||
                (novedadPersonalDto.getIdPersona() != null &&
                        !Objects.equals(novedadPersonal.getPersona().getId(), novedadPersonalDto.getIdPersona()))) {
            novedadPersonal.setPersona(personaService.findById(novedadPersonalDto.getIdPersona()));
        }

        // Si el suplente es nulo, se puede asignar null
        if (novedadPersonalDto.getIdSuplente() == null) {
            novedadPersonal.setSuplente(null);
        } else if (novedadPersonal.getSuplente() == null ||
                (novedadPersonalDto.getIdSuplente() != null &&
                        !Objects.equals(novedadPersonal.getSuplente().getId(), novedadPersonalDto.getIdSuplente()))) {
            novedadPersonal.setSuplente(personaService.findById(novedadPersonalDto.getIdSuplente()));
        }

        if (novedadPersonal.getTipoLicencia() == null ||
                (novedadPersonalDto.getIdTipoLicencia() != null &&
                        !Objects.equals(novedadPersonal.getTipoLicencia().getId(),
                                novedadPersonalDto.getIdTipoLicencia()))) {
            novedadPersonal.setTipoLicencia(tipoLicenciaService.findById(novedadPersonalDto.getIdTipoLicencia()).get());
        }

        novedadPersonal.setActivo(true);
        return novedadPersonal;
    }

    public boolean puedeHacerGuardia(Long idPersona) {

        if (idPersona == null) {
            throw new IllegalArgumentException("el id de la persona no pueden ser nulo.");
        }

        if (!personaService.activoById(idPersona)) {
            throw new EntityNotFoundException("La persona con ID " + idPersona + " no existe.");
        }

        // Lista de nombres de licencias que se deben verificar
        List<String> nombresLicencias = Arrays.asList("MATERNIDAD");
        /* List<String> nombresLicencias = Arrays.asList("Compensatorio", "MATERNIDAD", "Licencia anual ordinaria"); */

        // Buscar coincidencias en Novedades personales
        Boolean esElegibleParaGuardia = novedadPersonalRepository.existsByPersonaIdAndTipoLicenciaNombreIn(idPersona, nombresLicencias);

        return  Boolean.TRUE.equals(esElegibleParaGuardia);

    }

}
