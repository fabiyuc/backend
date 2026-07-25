package com.guardias.backend.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.DistribucionOtraDto;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoResquestDto;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.entity.DistribucionOtra;
import com.guardias.backend.enums.DiasEnum;
import com.guardias.backend.repository.DistribucionOtraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionOtraService {

    @Autowired
    DistribucionOtraRepository distribucionOtraRepository;

    @Autowired
    PersonService personService;

    @Autowired
    EfectorService efectorService;
    @Autowired
    DistribucionHorariaService distribucionHorariaService;

    public Optional<List<DistribucionOtra>> findByActivoTrue() {
        return distribucionOtraRepository.findByActivoTrue();
    }

    public List<DistribucionOtra> findAll() {
        return distribucionOtraRepository.findAll();
    }

    public Optional<DistribucionOtra> findById(Long id) {
        return distribucionOtraRepository.findById(id);
    }

    public Optional<List<DistribucionOtra>> findByPersonaId(Long personaId) {
        return distribucionOtraRepository.findByPersonaId(personaId);
    }

    public List<DistribucionOtra> findByFechaInicio(LocalDate fechaInicio) {
        return distribucionOtraRepository.findByFechaInicio(fechaInicio);
    }

    public List<DistribucionOtra> findByActivoAndPersonaAndFechaInicio(boolean activo, Long personaId,
            LocalDate fechaInicio) {
        return distribucionOtraRepository.findByActivoAndPersonaIdAndFechaInicio(activo, personaId, fechaInicio);
    }

    public List<DistribucionOtra> findByActivoAndPersonaAndFechaInicioAndFechaFin(boolean activo, Long personaId,
            LocalDate fechaInicio, LocalDate fechaFinalizacion) {
        return distribucionOtraRepository.findByActivoAndPersonaIdAndFechaInicioAndFechaFin(activo,
                personaId, fechaInicio, fechaFinalizacion);
    }

    public Optional<List<DistribucionOtra>> findByEfectorId(Long efectorId) {
        return distribucionOtraRepository.findByEfectorId(efectorId);
    }

    public boolean existsById(Long id) {
        return distribucionOtraRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (distribucionOtraRepository.existsById(id)
                && distribucionOtraRepository.findById(id).get().isActivo());
    }

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionOtraRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionOtraRepository.existsByPersonaId(personaId) && personService.activoById(personaId);
    }

    public List<DistribucionOtra> findByActivoPersonaAndFechaInicio(Long idPersona, int mes, int anio) {
        return distribucionOtraRepository.findByActivoPersonaAndFechaInicio(idPersona, mes, anio);
    }

    public boolean existsByActivoPersonaAndFechaInicio(Long idPersona, int mes, int anio) {
        return distribucionOtraRepository.existsByActivoPersonaAndFechaInicio(idPersona, mes, anio);
    }

    public void save(DistribucionOtra distribucionOtra) {
        distribucionOtraRepository.save(distribucionOtra);
    }

    public void deleteById(Long id) {
        distribucionOtraRepository.deleteById(id);
    }

    public DistribucionOtra createUpdate(DistribucionOtra distribucionOtra,
            DistribucionOtraDto distribucionOtraDto) {
        DistribucionHoraria distribucionHoraria = distribucionHorariaService.createUpdate(distribucionOtra,
                distribucionOtraDto);
        distribucionOtra = (DistribucionOtra) distribucionHoraria;

        if (distribucionOtraDto.getDescripcion() != (distribucionOtra.getDescripcion())
                && distribucionOtraDto.getDescripcion() != null)
            distribucionOtra.setDescripcion(distribucionOtraDto.getDescripcion());

        if (distribucionOtraDto.getLugar() != (distribucionOtra.getLugar())
                && distribucionOtraDto.getLugar() != null)
            distribucionOtra.setLugar(distribucionOtraDto.getLugar());

        if (distribucionOtraDto.getTipo() != (distribucionOtra.getTipo())
                && distribucionOtraDto.getTipo() != null)
            distribucionOtra.setTipo(distribucionOtraDto.getTipo());

        distribucionOtra.setActivo(true);

        return distribucionOtra;
    }

    public boolean validarCronogramaEnDistribucion(CronogramaTentativoResquestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo.");
        }

        // Buscar todas las distribuciones válidas que cubran la fecha de ingreso
        List<DistribucionOtra> distribuciones = distribucionOtraRepository
                .findValidDistribuciones(dto.getIdAsistencial(), dto.getIdEfector(), dto.getFechaIngreso());

        if (distribuciones.isEmpty()) {
            return false;
        }
        // Obtener día en formato compatible
        String diaSolicitado = convertirDia(dto.getFechaIngreso().getDayOfWeek());

        /// Verificar si hay solapamiento con alguna distribución
        return distribuciones.stream().anyMatch(dist -> {
            // Comparación robusta de días
            if (!compararDias(dist.getDia(), diaSolicitado)) {
                return false;
            }
            LocalTime horaFinDistribucion = dist.getHoraIngreso().plusHours(dist.getCantidadHoras().longValue());

            // Condición de solapamiento:
            // El horario tentativo NO termina antes del inicio de la distribución Y
            // NO empieza después del fin de la distribución
            return dto.getHoraIngreso().isBefore(horaFinDistribucion) &&
                    dto.getHoraEgreso().isAfter(dist.getHoraIngreso());

        });
    }

    // Métodos auxiliares mejorados
    private boolean compararDias(DiasEnum diaDist, String diaSolicitado) {
        // Normalizar strings (eliminar acentos, espacios, etc.)
        String diaDistStr = normalizeString(diaDist.toString());
        String diaSolicitadoStr = normalizeString(diaSolicitado);

        return diaDistStr.equalsIgnoreCase(diaSolicitadoStr);
    }

    private String convertirDia(DayOfWeek dayOfWeek) {
        // Mapeo completo considerando posibles variaciones
        return switch (dayOfWeek) {
            case MONDAY -> "LUNES";
            case TUESDAY -> "MARTES";
            case WEDNESDAY -> "MIERCOLES";
            case THURSDAY -> "JUEVES";
            case FRIDAY -> "VIERNES";
            case SATURDAY -> "SABADO";
            case SUNDAY -> "DOMINGO";
        };
    }

    private String normalizeString(String input) {
        return input.trim()
                .toUpperCase()
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U");
    }

}