//service/DistribucionHorariaService
package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.DistribucionHorariaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asignacionHorasEfector.HorasDisponiblesEfectorDto;
import com.guardias.backend.entity.AsignacionHorasEfector;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.Person;
import com.guardias.backend.repository.AsignacionHorasEfectorRepository;
import com.guardias.backend.repository.DistribucionConsultorioRepository;
import com.guardias.backend.repository.DistribucionGiraRepository;
import com.guardias.backend.repository.DistribucionGuardiaRepository;
import com.guardias.backend.repository.DistribucionOtraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionHorariaService {

    @Autowired
    DistribucionConsultorioRepository distribucionConsultorioRepository;
    @Autowired
    DistribucionGiraRepository distribucionGiraRepository;
    @Autowired
    DistribucionGuardiaRepository distribucionGuardiaRepository;
    @Autowired
    DistribucionOtraRepository distribucionOtraRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    PersonService personService;
    @Autowired
    AsignacionHorasEfectorRepository asignacionHorasEfectorRepository;

    public ResponseEntity<?> validations(DistribucionHorariaDto distribucionHorariaDto) {
        if (distribucionHorariaDto.getDia() == null)
            return new ResponseEntity(new Mensaje("El dia es obligatorio fabiana"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getIdPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getIdEfector() == null)
            return new ResponseEntity(new Mensaje("El efector es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionHorariaDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        // --- NUEVO: validación de tope de horas contra AsignacionHorasEfector ---

        Person persona = personService.findById(distribucionHorariaDto.getIdPersona());
        if (persona == null) {
            return new ResponseEntity(new Mensaje("La persona indicada no existe"), HttpStatus.BAD_REQUEST);
        }

        List<Legajo> legajosPersona = persona.getLegajos();
        if (legajosPersona == null || legajosPersona.isEmpty()) {
            return new ResponseEntity(new Mensaje("La persona no tiene legajos registrados"), HttpStatus.BAD_REQUEST);
        }

        Legajo legajoValido = legajosPersona.stream()
                .filter(legajo -> Boolean.FALSE.equals(legajo.getEsAutoridad()))
                .findFirst()
                .orElse(null);

        if (legajoValido == null) {
            return new ResponseEntity(new Mensaje("La persona no tiene un legajo válido (no es autoridad)"),
                    HttpStatus.BAD_REQUEST);
        }

        Integer anio = distribucionHorariaDto.getFechaInicio().getYear();
        Integer mes = distribucionHorariaDto.getFechaInicio().getMonthValue();
        LocalDate inicioMes = LocalDate.of(anio, mes, 1);
        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        AsignacionHorasEfector asignacion = asignacionHorasEfectorRepository
                .findSolapadasMismoEfector(legajoValido.getId(), distribucionHorariaDto.getIdEfector(), inicioMes,
                        finMes)
                .stream()
                .findFirst()
                .orElse(null);

        if (asignacion == null) {
            return new ResponseEntity(new Mensaje(
                    "Este efector no tiene horas asignadas para este profesional en este período"),
                    HttpStatus.BAD_REQUEST);
        }

        BigDecimal horasCargadas = sumarHorasCargadas(
                legajoValido.getPersona().getId(), distribucionHorariaDto.getIdEfector(), inicioMes, finMes);

        BigDecimal horasDisponibles = asignacion.getHorasAsignadas().subtract(horasCargadas);

        if (distribucionHorariaDto.getCantidadHoras().compareTo(horasDisponibles) > 0) {
            return new ResponseEntity(new Mensaje(
                    "La cantidad de horas supera lo disponible para este efector en el período"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public DistribucionHoraria createUpdate(DistribucionHoraria distribucionHoraria,
            DistribucionHorariaDto distribucionHorariaDto) {
        if (distribucionHorariaDto.getDia() != distribucionHoraria.getDia() && distribucionHorariaDto.getDia() != null)
            distribucionHoraria.setDia(distribucionHorariaDto.getDia());

        if (distribucionHorariaDto.getCantidadHoras() != distribucionHoraria.getCantidadHoras()
                && distribucionHorariaDto.getCantidadHoras() != null)
            distribucionHoraria.setCantidadHoras(distribucionHorariaDto.getCantidadHoras());

        distribucionHoraria.setActivo(distribucionHorariaDto.isActivo());

        if (distribucionHoraria.getPersona() == null ||
                (distribucionHorariaDto.getIdPersona() != null &&
                        !Objects.equals(distribucionHoraria.getPersona().getId(),
                                distribucionHorariaDto.getIdPersona()))) {
            distribucionHoraria.setPersona(personService.findById(distribucionHorariaDto.getIdPersona()));
        }

        // Verifica si la persona tiene legajos y si el Efector del legajo coincide con
        // el Efector del DTO
        if (distribucionHoraria.getPersona() != null) {
            Person persona = distribucionHoraria.getPersona();
            List<Legajo> legajosPersona = persona.getLegajos();

            // Validar que la persona tenga legajos
            if (legajosPersona == null || legajosPersona.isEmpty()) {
                throw new IllegalArgumentException("La persona no tiene legajos registrados.");
            }

            // Buscar el primer legajo válido (que no sea autoridad)
            Legajo legajoValido = legajosPersona.stream()
                    .filter(legajo -> Boolean.FALSE.equals(legajo.getEsAutoridad()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "La persona no tiene un legajo válido (no es autoridad)."));

            // Validar que el legajo tenga un Efector asociado
            List<Efector> efectoresPersona = legajoValido.getEfectores();
            if (efectoresPersona == null || efectoresPersona.isEmpty()) {
                throw new IllegalArgumentException("El legajo válido de la persona no tiene efectores asociados.");
            }

            // Validar que el Efector especificado en el DTO existe
            Efector efectorDto = efectorService.findById(distribucionHorariaDto.getIdEfector());
            if (efectorDto == null) {
                throw new IllegalArgumentException("El Efector especificado en el DTO no existe.");
            }

            // Validar si el Efector del DTO está en la lista de efectores del legajo válido
            boolean efectorCoincide = efectoresPersona.stream()
                    .anyMatch(efector -> Objects.equals(efector.getId(), efectorDto.getId()));

            if (!efectorCoincide) {
                throw new IllegalArgumentException(
                        "El Efector del DTO no coincide con los efectores asociados al legajo válido de la persona.");
            }
        } else {
            throw new IllegalArgumentException("No se ha encontrado la persona asociada a la Distribución Horaria.");
        }

        if (distribucionHoraria.getEfector() == null ||
                (distribucionHorariaDto.getIdEfector() != null &&
                        !Objects.equals(distribucionHoraria.getEfector().getId(),
                                distribucionHorariaDto.getIdEfector()))) {
            distribucionHoraria.setEfector(efectorService.findById(distribucionHorariaDto
                    .getIdEfector()));
        }

        if (distribucionHorariaDto.getFechaInicio() != distribucionHoraria.getFechaInicio()
                && distribucionHorariaDto.getFechaInicio() != null)
            distribucionHoraria.setFechaInicio(distribucionHorariaDto.getFechaInicio());

        if (distribucionHorariaDto.getFechaFinalizacion() != distribucionHoraria.getFechaFinalizacion()
                && distribucionHorariaDto.getFechaFinalizacion() != null)
            distribucionHoraria.setFechaFinalizacion(distribucionHorariaDto.getFechaFinalizacion());

        if (distribucionHorariaDto.getHoraIngreso() != distribucionHoraria.getHoraIngreso()
                && distribucionHorariaDto.getHoraIngreso() != null)
            distribucionHoraria.setHoraIngreso(distribucionHorariaDto.getHoraIngreso());

        return distribucionHoraria;
    }

    public DistribucionHoraria findById(Long id) {
        DistribucionHoraria distribucionHoraria = distribucionConsultorioRepository.findById(id).orElse(null);

        if (distribucionHoraria == null)
            distribucionHoraria = distribucionGiraRepository.findById(id).orElse(null);

        if (distribucionHoraria == null)
            distribucionHoraria = distribucionGuardiaRepository.findById(id).orElse(null);

        if (distribucionHoraria == null)
            distribucionHoraria = distribucionOtraRepository.findById(id).orElse(null);

        return distribucionHoraria;
    }

    public BigDecimal sumarHorasCargadas(Long idPersona, Long idEfector, LocalDate fechaInicio, LocalDate fechaFin) {
        BigDecimal total = BigDecimal.ZERO;

        total = total.add(sumarOZero(distribucionGuardiaRepository
                .sumCantidadHorasByPersonaIdAndEfectorIdAndRango(idPersona, idEfector, fechaInicio, fechaFin)));
        total = total.add(sumarOZero(distribucionConsultorioRepository
                .sumCantidadHorasByPersonaIdAndEfectorIdAndRango(idPersona, idEfector, fechaInicio, fechaFin)));
        total = total.add(sumarOZero(distribucionGiraRepository
                .sumCantidadHorasByPersonaIdAndEfectorIdAndRango(idPersona, idEfector, fechaInicio, fechaFin)));
        total = total.add(sumarOZero(distribucionOtraRepository
                .sumCantidadHorasByPersonaIdAndEfectorIdAndRango(idPersona, idEfector, fechaInicio, fechaFin)));

        return total;
    }

    private BigDecimal sumarOZero(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

}
