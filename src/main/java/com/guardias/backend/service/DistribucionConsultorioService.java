package com.guardias.backend.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.DistribucionConsultorioDto;
import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoResquestDto;
import com.guardias.backend.entity.DistribucionConsultorio;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.enums.DiasEnum;
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
    @Autowired
    ServicioService servicioService;
    @Autowired
    DistribucionHorariaService distribucionHorariaService;

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

    public List<DistribucionConsultorio> findByActivoAndPersonaAndFechaInicio(boolean activo, Long personaId,
            LocalDate fechaInicio) {
        return distribucionConsultorioRepository.findByActivoAndPersonaIdAndFechaInicio(activo, personaId, fechaInicio);
    }

    public List<DistribucionConsultorio> findByActivoAndPersonaAndFechaInicioAndFechaFin(boolean activo, Long personaId,
            LocalDate fechaInicio, LocalDate fechaFinalizacion) {
        return distribucionConsultorioRepository.findByActivoAndPersonaIdAndFechaInicioAndFechaFin(activo,
                personaId, fechaInicio, fechaFinalizacion);
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

    public List<DistribucionConsultorio> findByActivoPersonaAndFechaInicio(Long idPersona, int mes, int anio) {
        return distribucionConsultorioRepository.findByActivoPersonaAndFechaInicio(idPersona, mes, anio);
    }

    public boolean existsByActivoPersonaAndFechaInicio(Long idPersona, int mes, int anio) {
        return distribucionConsultorioRepository.existsByActivoPersonaAndFechaInicio(idPersona, mes, anio);
    }

    public void save(DistribucionConsultorio distribucionConsultorio) {
        distribucionConsultorioRepository.save(distribucionConsultorio);
    }

    public void deleteById(Long id) {
        distribucionConsultorioRepository.deleteById(id);
    }

    public DistribucionConsultorio createUpdate(DistribucionConsultorio distribucionConsultorio,
            DistribucionConsultorioDto distribucionConsultorioDto) {
        DistribucionHoraria distribucionHoraria = distribucionHorariaService.createUpdate(distribucionConsultorio,
                distribucionConsultorioDto);

        distribucionConsultorio = (DistribucionConsultorio) distribucionHoraria;

        if (distribucionConsultorio.getServicio() == null ||
                (distribucionConsultorioDto.getIdServicio() != null &&
                        !Objects.equals(distribucionConsultorio.getServicio().getId(),
                                distribucionConsultorioDto.getIdServicio()))) {
            distribucionConsultorio
                    .setServicio(servicioService.findById(distribucionConsultorioDto.getIdServicio()).get());
        }

        if (distribucionConsultorioDto.getTipoConsultorio() != distribucionConsultorio.getTipoConsultorio()
                && distribucionConsultorioDto.getTipoConsultorio() != null)
            distribucionConsultorio.setTipoConsultorio(distribucionConsultorioDto.getTipoConsultorio());

        if (distribucionConsultorioDto.getLugar() != distribucionConsultorio.getLugar()
                && distribucionConsultorioDto.getLugar() != null)
            distribucionConsultorio.setLugar(distribucionConsultorioDto.getLugar());

        /*
         * if (distribucionConsultorioDto.getEspecialidad() !=
         * distribucionConsultorio.getEspecialidad()
         * && distribucionConsultorioDto.getEspecialidad() != null)
         * distribucionConsultorio.setEspecialidad(distribucionConsultorioDto.
         * getEspecialidad());
         * if (distribucionConsultorioDto.getCantidadTurnos() !=
         * distribucionConsultorio.getCantidadTurnos())
         * distribucionConsultorio.setCantidadTurnos(distribucionConsultorioDto.
         * getCantidadTurnos());
         */
        distribucionConsultorio.setActivo(true);
        return distribucionConsultorio;
    }


    public boolean validarCronogramaEnDistribucion(CronogramaTentativoResquestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo.");
        }

        // Buscar todas las distribuciones válidas que cubran la fecha de ingreso
        List<DistribucionConsultorio> distribuciones = distribucionConsultorioRepository
                .findValidDistribuciones(dto.getIdAsistencial(), dto.getIdEfector(), dto.getFechaIngreso());

        if (distribuciones.isEmpty()) {
            return false;
        }
        // Obtener día en formato compatible
        String diaSolicitado = convertirDia(dto.getFechaIngreso().getDayOfWeek());

        return distribuciones.stream().anyMatch(dist -> {

            // Comparación robusta de días
            if (!compararDias(dist.getDia(), diaSolicitado)) {
                return false;
            }

            // Cálculo de horarios
            LocalTime horaFinDist = dist.getHoraIngreso().plusHours(dist.getCantidadHoras().longValue());

            return dto.getHoraIngreso().isBefore(horaFinDist) &&
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
