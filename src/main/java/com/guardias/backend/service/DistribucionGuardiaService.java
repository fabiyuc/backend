package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.cronogramaTentativo.CronogramaTentativoResquestDto;
import com.guardias.backend.dto.cronogramaTentativo.ValidacionCronogramaResponseDto;
import com.guardias.backend.dto.distribucionGuardia.DistribucionCheckDto;
import com.guardias.backend.entity.DistribucionGuardia;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.enums.DiasEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.DistribucionConsultorioRepository;
import com.guardias.backend.repository.DistribucionGiraRepository;
import com.guardias.backend.repository.DistribucionGuardiaRepository;
import com.guardias.backend.repository.DistribucionOtraRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DistribucionGuardiaService {

    @Autowired
    DistribucionGuardiaRepository distribucionGuardiaRepository;
    @Autowired
    DistribucionConsultorioRepository distribucionConsultorioRepository;
    @Autowired
    DistribucionGiraRepository distribucionGiraRepository;
    @Autowired
    DistribucionOtraRepository distribucionOtraRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    PersonService personService;
    @Autowired
    AsistencialRepository asistencialRepository;

    public Optional<List<DistribucionGuardia>> findByActivoTrue() {
        return distribucionGuardiaRepository.findByActivoTrue();
    }

    public List<DistribucionGuardia> findAll() {
        return distribucionGuardiaRepository.findAll();
    }

    public Optional<DistribucionGuardia> findById(Long id) {
        return distribucionGuardiaRepository.findById(id);
    }

    public Optional<List<DistribucionGuardia>> findByPersonaId(Long personaId) {
        return distribucionGuardiaRepository.findByPersonaId(personaId);
    }

    public List<DistribucionGuardia> findByFechaInicio(LocalDate fechaInicio) {
        return distribucionGuardiaRepository.findByFechaInicio(fechaInicio);
    }

    public List<DistribucionGuardia> findByActivoAndPersonaAndFechaInicio(boolean activo, Long personaId,
            LocalDate fechaInicio) {
        return distribucionGuardiaRepository.findByActivoAndPersonaIdAndFechaInicio(activo, personaId, fechaInicio);
    }

    public List<DistribucionGuardia> findByActivoAndPersonaAndFechaInicioAndFechaFin(boolean activo, Long personaId,
            LocalDate fechaInicio, LocalDate fechaFinalizacion) {
        return distribucionGuardiaRepository.findByActivoAndPersonaIdAndFechaInicioAndFechaFin(activo,
                personaId, fechaInicio, fechaFinalizacion);
    }

    public Optional<List<DistribucionGuardia>> findByEfectorId(Long efectorId) {
        return distribucionGuardiaRepository.findByEfectorId(efectorId);
    }

    public boolean existsById(Long id) {
        return distribucionGuardiaRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (distribucionGuardiaRepository.existsById(id)
                && distribucionGuardiaRepository.findById(id).get().isActivo());
    }

    public boolean existsByEfectorId(Long efectorId) {
        return distribucionGuardiaRepository.existsByEfectorId(efectorId) && efectorService.activoById(efectorId);
    }

    public boolean existsByPersonaId(Long personaId) {
        return distribucionGuardiaRepository.existsByPersonaId(personaId) && personService.activoById(personaId);
    }

    public List<DistribucionGuardia> findByActivoPersonaAndFechaInicio(Long idPersona, int mes, int anio) {
        return distribucionGuardiaRepository.findByActivoPersonaAndFechaInicio(idPersona, mes, anio);
    }

    public boolean existsByActivoPersonaAndFechaInicio(Long idPersona, int mes, int anio) {
        return distribucionGuardiaRepository.existsByActivoPersonaAndFechaInicio(idPersona, mes, anio);
    }

    public void save(DistribucionGuardia distribucionGuardia) {
        distribucionGuardiaRepository.save(distribucionGuardia);
    }

    public void deleteById(Long id) {
        distribucionGuardiaRepository.deleteById(id);
    }

    public boolean existDistribucion(DiasEnum dia, LocalDate fecha, Long idAsistencial, Long idEfector) {

        if (dia == null || fecha == null || idAsistencial == null || idEfector == null) {
            throw new IllegalArgumentException(
                    "Los parámetros de día, fecha, horaIngreso, idAsistencial y idEfector no pueden ser nulos.");
        }

        if (!asistencialRepository.existsById(idAsistencial)) {
            throw new EntityNotFoundException("El asistencial con ID " + idAsistencial + " no existe.");
        }

        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con ID " + idEfector + " no existe.");
        }

        // Buscar coincidencias en DistribucionGuardia
        boolean guardiaExists = distribucionGuardiaRepository.existsByDiaAndFechaAndIdPersonaAndIdEfector(
                dia, fecha, idAsistencial, idEfector);

        // Si existe DistribucionGuardia que coincida, retornar true
        if (guardiaExists)
            return true;

        // Buscar coincidencias en DistribucionConsultorio
        boolean consultorioExists = distribucionConsultorioRepository.existsByDiaAndFechaAndPersonaAndEfector(
                dia, fecha, idAsistencial, idEfector);

        // Retornar true si existe alguna coincidencia en cualquiera de las
        // distribuciones
        return consultorioExists;

    }

    public ValidacionCronogramaResponseDto validarCronogramaEnDistribucion(CronogramaTentativoResquestDto dto) {

        System.out.println("=== INICIO validarCronogramaEnDistribucion ===");
    
    if (dto == null) {
        System.out.println("ERROR: DTO recibido es nulo");
        throw new IllegalArgumentException("El DTO no puede ser nulo.");
    }

    System.out.println("Validando cronograma para: ");
    System.out.println("  - idAsistencial: " + dto.getIdAsistencial());
    System.out.println("  - idEfector: " + dto.getIdEfector());
    System.out.println("  - tipoGuardia: " + dto.getTipoGuardia());
    System.out.println("  - fechaIngreso: " + dto.getFechaIngreso());
    System.out.println("  - horaIngreso: " + dto.getHoraIngreso());
    System.out.println("  - horaEgreso: " + dto.getHoraEgreso());

    // Convierto LocalTime a String antes de enviarlo para que SQL Server pueda
    // entenderlo luego como TIME en la comparacion
    String horaIngresoString = dto.getHoraIngreso().toString();
    String horaEgresoString = dto.getHoraEgreso().toString();
    
    System.out.println("Hora ingreso convertida: " + horaIngresoString);
    System.out.println("Hora egreso convertida: " + horaEgresoString);

    // 1. Primero verificamos si hay coincidencia exacta
    System.out.println("Buscando coincidencia exacta en repository...");
    boolean coincideExactamente = distribucionGuardiaRepository.findValidDistribucion(
            dto.getIdAsistencial(),
            dto.getIdEfector(),
            dto.getTipoGuardia(),
            dto.getFechaIngreso(),
            horaIngresoString,
            horaEgresoString).isPresent();

    System.out.println("Coincidencia exacta encontrada: " + coincideExactamente);

    if (coincideExactamente) {
        System.out.println("RETURN: Coincidencia exacta - true, false, false");
        return new ValidacionCronogramaResponseDto(true, false, false);
    }

    // 2. Verificación de distribución activa parcial (mismo mes y año)
    System.out.println("Buscando distribución parcial...");
    
    LocalDate fechaIngreso = dto.getFechaIngreso();
    LocalDate inicioSemana = fechaIngreso.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate finSemana = fechaIngreso.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    DiasEnum diaTentativo = obtenerDiaSemana(fechaIngreso);
    
    System.out.println("Semana analizada: " + inicioSemana + " a " + finSemana);
    System.out.println("Día tentativo: " + diaTentativo);

    TipoGuardiaEnum tipoGuardiaEnum = TipoGuardiaEnum.valueOf(dto.getTipoGuardia());
    
    System.out.println("Tipo guardia enum: " + tipoGuardiaEnum);

    boolean existeDistribucionParcial = distribucionGuardiaRepository.existsDistribucionParcialSemanal(
            dto.getIdAsistencial(),
            dto.getIdEfector(),
            tipoGuardiaEnum,
            inicioSemana,
            finSemana);

    System.out.println("Distribución parcial encontrada: " + existeDistribucionParcial);

    // 3. Determinar si no hay ninguna distribución
    boolean sinDistribucion = !existeDistribucionParcial;
    
    System.out.println("Sin distribución: " + sinDistribucion);
    System.out.println("RETURN: false, " + existeDistribucionParcial + ", " + sinDistribucion);
    System.out.println("=== FIN validarCronogramaEnDistribucion ===");

    return new ValidacionCronogramaResponseDto(false, existeDistribucionParcial, sinDistribucion);
}
        /* if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo.");
        }

        // Convierto LocalTime a String antes de enviarlo para que SQL Server pueda
        // entenderlo luego como TIME en la comparacion
        String horaIngresoString = dto.getHoraIngreso().toString();
        String horaEgresoString = dto.getHoraEgreso().toString();

        // 1. Primero verificamos si hay coincidencia exacta
        boolean coincideExactamente = distribucionGuardiaRepository.findValidDistribucion(
                dto.getIdAsistencial(),
                dto.getIdEfector(),
                dto.getTipoGuardia(),
                dto.getFechaIngreso(),
                horaIngresoString,
                horaEgresoString).isPresent();

        if (coincideExactamente) {
            return new ValidacionCronogramaResponseDto(true, false, false);
        }

        // 2. Verificación de distribución activa parcial (mismo mes y año)

        LocalDate fechaIngreso = dto.getFechaIngreso();
        LocalDate inicioSemana = fechaIngreso.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate finSemana = fechaIngreso.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        DiasEnum diaTentativo = obtenerDiaSemana(fechaIngreso);

        TipoGuardiaEnum tipoGuardiaEnum = TipoGuardiaEnum.valueOf(dto.getTipoGuardia());

        boolean existeDistribucionParcial = distribucionGuardiaRepository.existsDistribucionParcialSemanal(
                dto.getIdAsistencial(),
                dto.getIdEfector(),
                tipoGuardiaEnum,
                inicioSemana,
                finSemana,
                diaTentativo);

        // 3. Determinar si no hay ninguna distribución
        boolean sinDistribucion = !existeDistribucionParcial;

        return new ValidacionCronogramaResponseDto(false, existeDistribucionParcial, sinDistribucion);
    } */

    public DiasEnum obtenerDiaSemana(LocalDate fecha) {
        DayOfWeek dayOfWeek = fecha.getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> DiasEnum.LUNES;
            case TUESDAY -> DiasEnum.MARTES;
            case WEDNESDAY -> DiasEnum.MIERCOLES;
            case THURSDAY -> DiasEnum.JUEVES;
            case FRIDAY -> DiasEnum.VIERNES;
            case SATURDAY -> DiasEnum.SABADO;
            case SUNDAY -> DiasEnum.DOMINGO;
        };
    }

    public boolean tieneDistribucionEnSemana(CronogramaTentativoResquestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }

        LocalDate fechaIngreso = dto.getFechaIngreso();
        LocalDate inicioSemana = fechaIngreso.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate finSemana = fechaIngreso.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<DistribucionGuardia> distribuciones = distribucionGuardiaRepository
                .findDistribucionesInWeek(
                        dto.getIdAsistencial(),
                        dto.getIdEfector(),
                        inicioSemana,
                        finSemana);

        // Obtener día de la semana como enum
        DiasEnum diaSolicitado = convertirDia(dto.getFechaIngreso().getDayOfWeek());

        // Verificar superposición horaria
        return distribuciones.stream().anyMatch(dist -> {

            // 1. Verificar coincidencia de día (comparación directa de enums)
            if (dist.getDia() != diaSolicitado) {
                return false;
            }
            // 2. Tratamiento especial para guardias de 24 horas
            if (dist.getCantidadHoras().compareTo(BigDecimal.valueOf(24)) == 0) {
                return true;
            }
            // 3. Para guardias normales, calcular solapamiento
            LocalTime horaFinDist = dist.getHoraIngreso().plusHours(dist.getCantidadHoras().longValue());
            return !dto.getHoraIngreso().isAfter(horaFinDist) &&
                    !dto.getHoraEgreso().isBefore(dist.getHoraIngreso());
        });
    }

    // Cambia el método convertirDia para que devuelva DiasEnum
    private DiasEnum convertirDia(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiasEnum.LUNES;
            case TUESDAY -> DiasEnum.MARTES;
            case WEDNESDAY -> DiasEnum.MIERCOLES;
            case THURSDAY -> DiasEnum.JUEVES;
            case FRIDAY -> DiasEnum.VIERNES;
            case SATURDAY -> DiasEnum.SABADO;
            case SUNDAY -> DiasEnum.DOMINGO;
        };
    }

    public boolean esGuardia(DiasEnum dia, LocalDate fecha, Long idAsistencial, Long idEfector) {

        // Verifica si existe en DistribucionGuardia
        if (distribucionGuardiaRepository.existsByDiaAndFechaAndIdPersonaAndIdEfector(dia, fecha, idAsistencial,
                idEfector)) {
            return true;
        }

        // Verifica si existe en DistribucionConsultorio
        return !distribucionConsultorioRepository.existsByDiaAndFechaAndPersonaAndEfector(dia, fecha, idAsistencial,
                idEfector);

    }

    public boolean tieneDistribucionActiva(DistribucionCheckDto request) {
        Long idPersona = request.getIdPersona();
        int mes = request.getFecha().getMonthValue();
        int anio = request.getFecha().getYear();

        // 1. Verifica DistribucionGuardia
        if (distribucionGuardiaRepository.existsByPersonaIdAndActivoTrue(idPersona)) {
            boolean existe = distribucionGuardiaRepository
                    .findByPersonaIdAndActivoTrue(idPersona)
                    .stream()
                    .anyMatch(d -> esFechaValida(d, mes, anio));
            if (existe)
                return true;
        }

        // 2. Verifica DistribucionConsultorio
        if (distribucionConsultorioRepository.existsByPersonaIdAndActivoTrue(idPersona)) {
            boolean existe = distribucionConsultorioRepository
                    .findByPersonaIdAndActivoTrue(idPersona)
                    .stream()
                    .anyMatch(d -> esFechaValida(d, mes, anio));
            if (existe)
                return true;
        }

        // 3. Verifica DistribucionGira
        if (distribucionGiraRepository.existsByPersonaIdAndActivoTrue(idPersona)) {
            boolean existe = distribucionGiraRepository
                    .findByPersonaIdAndActivoTrue(idPersona)
                    .stream()
                    .anyMatch(d -> esFechaValida(d, mes, anio));
            if (existe)
                return true;
        }

        // 4. Verifica DistribucionOtra
        if (distribucionOtraRepository.existsByPersonaIdAndActivoTrue(idPersona)) {
            return distribucionOtraRepository
                    .findByPersonaIdAndActivoTrue(idPersona)
                    .stream()
                    .anyMatch(d -> esFechaValida(d, mes, anio));
        }

        return false;
    }

    private boolean esFechaValida(DistribucionHoraria distribucion, int mes, int anio) {
        LocalDate fechaInicio = distribucion.getFechaInicio();
        return fechaInicio.getYear() == anio && fechaInicio.getMonthValue() == mes;
    }

    public boolean existeSuperposicionConCargo(
            Long idPersona,
            LocalDate fechaInicioNovedad,
            LocalDate fechaFinNovedad,
            LocalTime horaInicioNovedad,
            LocalTime horaFinNovedad) {

        System.out.println("\n=== INICIO VALIDACIÓN ===");
        System.out.println("[Parámetros Novedad] Persona: " + idPersona
                + " | Fechas: " + fechaInicioNovedad + " a " + fechaFinNovedad
                + " | Horas: " + horaInicioNovedad + " a " + horaFinNovedad);

        // 1. Buscar distribuciones de CARGO de 24h activas para la persona
        List<DistribucionGuardia> distribuciones = distribucionGuardiaRepository
                .findByPersonaIdAndTipoGuardiaAndCantidadHorasAndActivoIsTrue(
                        idPersona,
                        TipoGuardiaEnum.CARGO,
                        BigDecimal.valueOf(24.00));

        System.out.println("[Distribuciones de 24h encontradas]: " + distribuciones.size());
        distribuciones.forEach(d -> System.out.println(
                "  - ID: " + d.getId() +
                        " | Día: " + d.getDia() +
                        " | Fechas: " + d.getFechaInicio() + " a " + d.getFechaFinalizacion() +
                        " | Horario: " + d.getHoraIngreso() + " por " + d.getCantidadHoras() + "h"));

        // 2. Verificar solapamiento para cada distribución
        for (DistribucionGuardia distribucion : distribuciones) {
            System.out.println("\n[Evaluando Distribución] ID: " + distribucion.getId());

            // Verificar que la novedad esté dentro del rango de la guardia considerando las
            // 24h
            LocalDate fechaFinEfectivaGuardia = distribucion.getFechaFinalizacion().plusDays(1);
            if (fechaInicioNovedad.isAfter(fechaFinEfectivaGuardia) ||
                    fechaFinNovedad.isBefore(distribucion.getFechaInicio())) {
                System.out.println("  → Novedad fuera del rango efectivo de la guardia");
                continue;
            }

            if (haySolapamiento24h(
                    fechaInicioNovedad, horaInicioNovedad, horaFinNovedad,
                    distribucion.getDia(),
                    distribucion.getHoraIngreso(),
                    distribucion.getFechaInicio(),
                    distribucion.getFechaFinalizacion())) {
                System.out.println("=== RESULTADO: HAY SOLAPAMIENTO ===");
                return true;
            }
        }

        System.out.println("=== RESULTADO: NO HAY SOLAPAMIENTO ===");
        return false;
    }

    private boolean haySolapamiento24h(
            LocalDate fechaNovedad,
            LocalTime horaInicioNovedad,
            LocalTime horaFinNovedad,
            DiasEnum diaGuardia,
            LocalTime horaIngresoGuardia,
            LocalDate fechaInicioGuardia,
            LocalDate fechaFinalizacionGuardia) {

        System.out.println("\n--- Cálculo para guardia de 24h ---");
        System.out.println("[Datos Guardia] Día: " + diaGuardia + " | Horario: " + horaIngresoGuardia + " por 24h");
        System.out.println("[Rango Fechas Guardia] " + fechaInicioGuardia + " a " + fechaFinalizacionGuardia);

        // 1. Verificar si la fecha de novedad está en el último día + 1 de la guardia
        boolean esDiaSiguienteAlFinal = fechaNovedad.equals(fechaFinalizacionGuardia.plusDays(1));

        // 2. Ajustar la comparación de días para permitir el día siguiente al final
        DayOfWeek diaNovedad = fechaNovedad.getDayOfWeek();
        DayOfWeek diaGuardiaConvertido = convertirDiasEnumADayOfWeek(diaGuardia);

        boolean diaValido = (diaNovedad == diaGuardiaConvertido) ||
                (esDiaSiguienteAlFinal && diaNovedad == diaGuardiaConvertido.plus(1));

        System.out.println("[Comparación días] Novedad: " + diaNovedad +
                " | Guardia: " + diaGuardiaConvertido +
                " | Día siguiente válido: " + esDiaSiguienteAlFinal);

        if (!diaValido) {
            System.out.println("  → No coincide el día de la semana");
            return false;
        }

        // 3. Calcular rango de guardia (06:00 a 06:00 del día siguiente)
        LocalDateTime inicioGuardia = LocalDateTime.of(
                esDiaSiguienteAlFinal ? fechaFinalizacionGuardia : fechaNovedad,
                horaIngresoGuardia);
        LocalDateTime finGuardia = inicioGuardia.plusHours(24);
        System.out.println("[Rango Guardia Ajustado]: " + inicioGuardia + " a " + finGuardia);

        // Resto de la lógica igual...
        LocalDateTime inicioNovedad = LocalDateTime.of(fechaNovedad, horaInicioNovedad);
        LocalDateTime finNovedad = LocalDateTime.of(fechaNovedad, horaFinNovedad);
        System.out.println("[Rango Novedad]: " + inicioNovedad + " a " + finNovedad);

        boolean solapa = inicioNovedad.isBefore(finGuardia) && finNovedad.isAfter(inicioGuardia);
        System.out.println("  → Solapamiento: " + solapa);

        return solapa;
    }

    private DayOfWeek convertirDiasEnumADayOfWeek(DiasEnum dia) {
        return switch (dia) {
            case LUNES -> DayOfWeek.MONDAY;
            case MARTES -> DayOfWeek.TUESDAY;
            case MIERCOLES -> DayOfWeek.WEDNESDAY;
            case JUEVES -> DayOfWeek.THURSDAY;
            case VIERNES -> DayOfWeek.FRIDAY;
            case SABADO -> DayOfWeek.SATURDAY;
            case DOMINGO -> DayOfWeek.SUNDAY;
        };
    }
}