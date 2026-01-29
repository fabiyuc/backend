package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.CronogramaTentativoDto;
import com.guardias.backend.dto.DistribucionConsultorioDto;
import com.guardias.backend.dto.DistribucionGiraDto;
import com.guardias.backend.dto.DistribucionGuardiaDto;
import com.guardias.backend.dto.DistribucionOtraDto;
import com.guardias.backend.dto.DistribucionesConCronogramasDto;
import com.guardias.backend.dto.ResultadoCreacionDto;
import com.guardias.backend.entity.CronogramaTentativo;
import com.guardias.backend.entity.DistribucionConsultorio;
import com.guardias.backend.entity.DistribucionGira;
import com.guardias.backend.entity.DistribucionGuardia;
import com.guardias.backend.entity.DistribucionOtra;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.AutorizadoTentativoEnum;
import com.guardias.backend.enums.DiasEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionCompletaService {

    @Autowired
    private DistribucionGuardiaService distribucionGuardiaService;

    @Autowired
    private DistribucionConsultorioService distribucionConsultorioService;

    @Autowired
    private DistribucionGiraService distribucionGiraService;

    @Autowired
    private DistribucionOtraService distribucionOtraService;

    @Autowired
    private CronogramaTentativoService cronogramaTentativoService;

    @Autowired
    private TipoGuardiaService tipoGuardiaService;

    @Autowired
    DistribucionHorariaService distribucionHorariaService;

    public ResultadoCreacionDto crearDistribucionesConCronogramas(DistribucionesConCronogramasDto dto) {
        int distribucionesCreadas = 0;
        int cronogramasCreados = 0;

        // 1. Crear Distribuciones de Guardias
        if (dto.getGuardias() != null && !dto.getGuardias().isEmpty()) {
            for (DistribucionGuardiaDto guardiaDto : dto.getGuardias()) {

                // Validar la guardia
                ResponseEntity<?> validacion = distribucionHorariaService.validations(guardiaDto);

                if (validacion.getStatusCode() == HttpStatus.OK) {
                    // Crear y guardar la distribución de guardia
                    DistribucionGuardia distribucionGuardia = distribucionGuardiaService.createUpdate(
                            new DistribucionGuardia(), guardiaDto);
                    distribucionGuardiaService.save(distribucionGuardia);
                    distribucionesCreadas++;

                    // Crear Cronogramas Tentativos SOLO para Guardias
                    if (dto.isCrearCronogramasParaGuardias()) {
                        List<CronogramaTentativo> cronogramasGuardia = crearCronogramasDesdeGuardia(
                                distribucionGuardia);
                        cronogramasCreados += cronogramasGuardia.size();
                    }
                }
            }
        }

        // 2. Crear Distribuciones de Consultorios (sin cronogramas)
        if (dto.getConsultorios() != null && !dto.getConsultorios().isEmpty()) {
            for (DistribucionConsultorioDto consultorioDto : dto.getConsultorios()) {

                // Validar la guardia
                ResponseEntity<?> validacion = distribucionHorariaService.validations(consultorioDto);
                if (validacion.getStatusCode() == HttpStatus.OK) {
                    // Crear y guardar la distribución de guardia
                    DistribucionConsultorio distribucionConsultorio = distribucionConsultorioService
                            .createUpdate(new DistribucionConsultorio(), consultorioDto);
                    distribucionConsultorioService.save(distribucionConsultorio);
                    distribucionesCreadas++;
                }
            }
        }

        // 3. Crear Distribuciones de Giras (sin cronogramas)
        if (dto.getGiras() != null && !dto.getGiras().isEmpty()) {
            for (DistribucionGiraDto giraDto : dto.getGiras()) {
                // Validar la guardia
                ResponseEntity<?> validacion = distribucionHorariaService.validations(giraDto);
                if (validacion.getStatusCode() == HttpStatus.OK) {
                    // Crear y guardar la distribución de giras
                    DistribucionGira distribucionGira = distribucionGiraService.createUpdate(new DistribucionGira(),
                            giraDto);
                    distribucionGiraService.save(distribucionGira);
                    distribucionesCreadas++;
                }
            }
        }

        // 4. Crear Otras Distribuciones (sin cronogramas)
        if (dto.getOtras() != null && !dto.getOtras().isEmpty()) {
            for (DistribucionOtraDto otraDto : dto.getOtras()) {
                // Validar la guardia
                ResponseEntity<?> validacion = distribucionHorariaService.validations(otraDto);
                if (validacion.getStatusCode() == HttpStatus.OK) {
                    // Crear y guardar la distribución de otras
                    DistribucionOtra distribucionOtra = distribucionOtraService.createUpdate(new DistribucionOtra(),
                            otraDto);
                    distribucionOtraService.save(distribucionOtra);
                    distribucionesCreadas++;
                }
            }
        }

        return new ResultadoCreacionDto(distribucionesCreadas, cronogramasCreados);
    }

    private List<CronogramaTentativo> crearCronogramasDesdeGuardia(DistribucionGuardia guardia) {
        List<CronogramaTentativo> cronogramas = new ArrayList<>();

        // 1. Calcular todas las fechas específicas para esta distribución
        List<LocalDate> fechas = calcularFechasParaDistribucion(
                guardia.getDia(),
                guardia.getFechaInicio(),
                guardia.getFechaFinalizacion());

        // 2. Para cada fecha crear un cronograma tentativo
        for (LocalDate fecha : fechas) {
            try {
                // Crear DTO para el cronograma
                CronogramaTentativoDto cronogramaDto = mapearGuardiaACronogramaDto(guardia, fecha);

                // Validar el cronograma antes de crearlo
                ResponseEntity<?> validacion = cronogramaTentativoService.validations(cronogramaDto);
                if (validacion.getStatusCode() == HttpStatus.OK) {
                    CronogramaTentativo cronograma = cronogramaTentativoService.createUpdate(
                            new CronogramaTentativo(), cronogramaDto);
                    cronogramaTentativoService.save(cronograma);
                    cronogramas.add(cronograma);
                }
            } catch (Exception e) {
                // Log del error pero continuar con las demás fechas
                System.err.println("Error creando cronograma para fecha " + fecha + ": " + e.getMessage());
            }
        }

        return cronogramas;
    }

    private CronogramaTentativoDto mapearGuardiaACronogramaDto(DistribucionGuardia guardia, LocalDate fecha) {
        CronogramaTentativoDto dto = new CronogramaTentativoDto();

        // Fechas de ingreso
        dto.setFechaIngreso(fecha);
        dto.setHoraIngreso(guardia.getHoraIngreso());

        dto.setFechaEgreso(fecha); // Misma fecha para guardias diarias

        // Calcular fecha y hora de egreso
        LocalTime horaEgreso = calcularHoraEgreso(guardia.getHoraIngreso(), guardia.getCantidadHoras());
        LocalDate fechaEgreso = calcularFechaEgreso(fecha, guardia.getHoraIngreso(), guardia.getCantidadHoras());

        dto.setFechaEgreso(fechaEgreso);
        dto.setHoraEgreso(horaEgreso);

        // Relaciones
        dto.setIdAsistencial(guardia.getPersona().getId());
        dto.setIdEfector(guardia.getEfector().getId());
        dto.setIdServicio(guardia.getServicio().getId());
        dto.setIdTipoGuardia(obtenerIdTipoGuardia(guardia.getTipoGuardia()));

        // Estado por defecto
        dto.setAceptado(false);

        // Si es CARGO o AGRUPACION -> CONFIRMADO, sino -> PENDIENTE
        if (guardia.getTipoGuardia() == TipoGuardiaEnum.CARGO || guardia.getTipoGuardia() == TipoGuardiaEnum.AGRUPACION) {
            dto.setAutorizado(AutorizadoTentativoEnum.CONFIRMADO);
        } else {
            dto.setAutorizado(AutorizadoTentativoEnum.PENDIENTE);
        }

        dto.setActivo(true);

        return dto;
    }

    private List<LocalDate> calcularFechasParaDistribucion(DiasEnum diaSemana, LocalDate fechaInicio,
            LocalDate fechaFinalizacion) {
        List<LocalDate> fechas = new ArrayList<>();

        DayOfWeek diaTarget = convertirDiasEnumADayOfWeek(diaSemana);

        LocalDate fechaActual = fechaInicio;
        while (!fechaActual.isAfter(fechaFinalizacion)) {
            if (fechaActual.getDayOfWeek() == diaTarget) {
                fechas.add(fechaActual);
            }
            fechaActual = fechaActual.plusDays(1);
        }

        return fechas;
    }

    private DayOfWeek convertirDiasEnumADayOfWeek(DiasEnum dia) {
        switch (dia) {
            case LUNES:
                return DayOfWeek.MONDAY;
            case MARTES:
                return DayOfWeek.TUESDAY;
            case MIERCOLES:
                return DayOfWeek.WEDNESDAY;
            case JUEVES:
                return DayOfWeek.THURSDAY;
            case VIERNES:
                return DayOfWeek.FRIDAY;
            case SABADO:
                return DayOfWeek.SATURDAY;
            case DOMINGO:
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Día no válido: " + dia);
        }
    }

    private LocalTime calcularHoraEgreso(LocalTime horaIngreso, BigDecimal cantidadHoras) {

        // Convertir BigDecimal a double
        double horasDecimal = cantidadHoras.doubleValue();

        int horas = (int) horasDecimal;
        int minutos = (int) ((horasDecimal - horas) * 60);
        return horaIngreso.plusHours(horas).plusMinutes(minutos);
    }

    private LocalDate calcularFechaEgreso(LocalDate fechaIngreso, LocalTime horaIngreso, BigDecimal cantidadHoras) {
        LocalTime horaEgreso = calcularHoraEgreso(horaIngreso, cantidadHoras);

        // Si la hora de egreso es menor que la de ingreso, significa que pasó a otro día
        if (horaEgreso.isBefore(horaIngreso) || horaEgreso.equals(horaIngreso)) {
            return fechaIngreso.plusDays(1);
        } else {
            return fechaIngreso;
        }
    }

    private Long obtenerIdTipoGuardia(TipoGuardiaEnum tipoGuardiaNombre) {
        // Buscar el TipoGuardia por nombre
        return tipoGuardiaService.findByNombre(tipoGuardiaNombre)
                .map(TipoGuardia::getId)
                .orElseThrow(() -> new RuntimeException("Tipo guardia no encontrado: " + tipoGuardiaNombre));
    }

}
