package com.guardias.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RegistroActividadDto;
import com.guardias.backend.dto.registroActividad.RegActivAsistenciaDto;
import com.guardias.backend.dto.registroActividad.RegActivMotivoDto;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.entity.ValorGuardiaCargoYagrup;
import com.guardias.backend.entity.ValorGuardiaExtrayCF;
import com.guardias.backend.enums.EstadoDdjjEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.DdjjRepository;
import com.guardias.backend.repository.RegistroActividadRepository;
import com.guardias.backend.security.service.UsuarioService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import com.guardias.backend.entity.Ddjj; // añadido

@Service
@Transactional
public class RegistroActividadService {
    @Autowired
    RegistroActividadRepository registroActividadRepository;
    @Autowired
    ServicioService servicioService;
    @Autowired
    TipoGuardiaService tipoGuardiaService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RegistroMensualService registroMensualService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    SumaHorasService sumaHorasService;
    @Autowired
    RegistrosPendientesService registrosPendientesService;
    @Autowired
    ValorGuardiaCargoYagrupService valorGuardiaCargoYagrupService;
    @Autowired
    ValorGuardiaExtraYcfService valorGuardiaExtraYcfService;
    @Autowired
    HospitalService hospitalService;
    @Autowired
    DdjjRepository ddjjRepository;

    // Inyectar EntityManager para consultas flexibles que puedan devolver múltiples resultados
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<List<RegistroActividad>> findByActivoTrue() {
        return registroActividadRepository.findByActivoTrue();
    }

    public List<RegistroActividad> findAll() {
        return registroActividadRepository.findAll();
    }

    public Optional<RegistroActividad> findById(Long id) {
        return registroActividadRepository.findById(id);
    }

    public void save(RegistroActividad registroActividad) {
        registroActividadRepository.save(registroActividad);
    }

    public void deleteById(Long id) {
        registroActividadRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return registroActividadRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (registroActividadRepository.existsById(id)
                && registroActividadRepository.findById(id).get().isActivo());
    }

    public ResponseEntity<?> validations(RegistroActividadDto registroActividadDto) {

        if (registroActividadDto.getFechaIngreso() == null)
            return new ResponseEntity(new Mensaje("la fecha de ingreso es obligatoria"), HttpStatus.BAD_REQUEST);

        if (registroActividadDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public RegistroActividad createUpdate(RegistroActividad registroActividad,
            RegistroActividadDto registroActividadDto) {

        if (registroActividad.getServicio() == null ||
                (registroActividadDto.getIdServicio() != null &&
                        !Objects.equals(registroActividad.getServicio().getId(),
                                registroActividadDto.getIdServicio()))) {
            registroActividad.setServicio(servicioService.findById(registroActividadDto.getIdServicio()).get());
        }

        if (registroActividad.getTipoGuardia() == null ||
                (registroActividadDto.getIdTipoGuardia() != null &&
                        !Objects.equals(registroActividad.getTipoGuardia().getId(),
                                registroActividadDto.getIdTipoGuardia()))) {
            registroActividad
                    .setTipoGuardia(tipoGuardiaService.findById(registroActividadDto.getIdTipoGuardia()).get());
        }

        if (registroActividad.getFechaIngreso() != registroActividadDto.getFechaIngreso() &&
                registroActividadDto.getFechaIngreso() != null)
            registroActividad.setFechaIngreso(registroActividadDto.getFechaIngreso());

        if (registroActividad.getFechaEgreso() != registroActividadDto.getFechaEgreso() &&
                registroActividadDto.getFechaEgreso() != null)
            registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());

        if (registroActividad.getHoraIngreso() != registroActividadDto.getHoraIngreso() &&
                registroActividadDto.getHoraIngreso() != null)
            registroActividad.setHoraIngreso(registroActividadDto.getHoraIngreso());

        if (registroActividad.getHoraEgreso() != registroActividadDto.getHoraEgreso() &&
                registroActividadDto.getHoraEgreso() != null)
            registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());

        if (registroActividadDto.getIdAsistencial() != null && registroActividadDto.getIdEfector() != null) {
            boolean esPlanta = asistencialService.esPlanta(registroActividadDto.getIdAsistencial(),
                    registroActividadDto.getIdEfector());

            if (!esPlanta && !(esExtraoCf(registroActividadDto.getIdTipoGuardia()))) {
                throw new IllegalArgumentException("El asistencial no pertenece a la planta del efector");
            }
        }

        if (registroActividad.getAsistencial() == null ||
                (registroActividadDto.getIdAsistencial() != null &&
                        !Objects.equals(registroActividad.getAsistencial().getId(),
                                registroActividadDto.getIdAsistencial()))) {
            registroActividad
                    .setAsistencial(asistencialService.findById(registroActividadDto.getIdAsistencial()).get());
        }

        if (registroActividad.getEfector() == null ||
                (registroActividadDto.getIdEfector() != null &&
                        !Objects.equals(registroActividad.getEfector().getId(),
                                registroActividadDto.getIdEfector()))) {
            registroActividad.setEfector(efectorService.findById(registroActividadDto.getIdEfector()));
        }

        if (registroActividadDto.getIdRegistroMensual() != null && (registroActividad.getRegistroMensual() == null
                || !Objects.equals(registroActividad.getRegistroMensual().getId(),
                        registroActividadDto.getIdRegistroMensual()))) {
            registroActividad.setRegistroMensual(
                    registroMensualService.findById(registroActividadDto.getIdRegistroMensual()).get());
        }

        if (registroActividad.getMotivoIngreso() != registroActividadDto.getMotivoIngreso() &&
                registroActividadDto.getMotivoIngreso() != null)
            registroActividad.setMotivoIngreso(registroActividadDto.getMotivoIngreso());

        registroActividad.setUsuarioIngreso(usuarioService.findById(registroActividadDto.getIdUsuarioIngreso()).get());
        registroActividad.setHoraRegistroIngreso(LocalTime.now());
        registroActividad.setFechaRegistroIngreso(LocalDate.now());
        registroActividad.setActivo(true);
        return registroActividad;
    }

    private boolean esExtraoCf(Long idTipoguardia) {
        TipoGuardiaEnum nombre = tipoGuardiaService.findById(idTipoguardia).get().getNombre();
        if (nombre == TipoGuardiaEnum.EXTRA || nombre == TipoGuardiaEnum.CONTRAFACTURA) {
            return true;
        } else
            return false;
    }

    /* Calcula horas trabajadas (LAV/SDF) y montos según el tipo de guardia */
    private SumaHoras calcularHoras(RegistroActividad registroActividad) {
        /* A. Cálculo de horas brutas */
        SumaHoras horas = sumaHorasService.calcularHoras(registroActividad.getFechaIngreso(),
                registroActividad.getFechaEgreso(), registroActividad.getHoraIngreso(),
                registroActividad.getHoraEgreso());

        System.out.println("DEBUG [1] - Hoooras calculadas (LAV/SDF): " +
                horas.getHorasLav() + "/" + horas.getHorasSdf());

        horas.setActivo(true);
        TipoGuardiaEnum tipoGuardia = registroActividad.getTipoGuardia().getNombre();

        Efector efector = registroActividad.getEfector();

        Hospital hospital = hospitalService.findById(efector.getId()).orElse(null);

        if (hospital == null) {
            throw new RuntimeException("Hospital no encontrado para ID: " + efector.getId());
        }

        System.out.println("DEBUG 2 - Tipo de guardia: " + tipoGuardia);
        System.out.println("DEBUG 3 - Hospital ID: " + (hospital != null ? hospital.getId() : "null"));
        /* B. Determinar el tipo de guardia */
        // si es Cargo o Agrupacion
        if (tipoGuardia == TipoGuardiaEnum.CARGO || tipoGuardia == TipoGuardiaEnum.AGRUPACION) {
            System.out.println("DEBUG [4] - es tipo guardia cargo o agrup");

            try {
                // Valor de la guardia segun tipoGuardia y efector

                /* Obtiene valores de guardia */
                ValorGuardiaCargoYagrup valorGuardiaBase = valorGuardiaCargoYagrupService
                        .obtenerValorGuardiaCargoPorHospital(hospital.getId()).get();
                System.out.println("DEBUG 5 - ValorGuardiaBase obtenido: " + valorGuardiaBase);
                System.out.println("DEBUG 6a - Total LAV/SDF: " + valorGuardiaBase.getTotalLav() + "/"
                        + valorGuardiaBase.getTotalSdf());

                /* Calcula montos para LAV/SDF (dividiendo el total entre 24hs) */
                /* LAV */
                BigDecimal valorHoraLav = valorGuardiaBase.getTotalLav().divide(BigDecimal.valueOf(24), 2,
                        RoundingMode.HALF_UP);
                BigDecimal totalMontoLav = BigDecimal.valueOf(horas.getHorasLav()).multiply(valorHoraLav);
                horas.setMontoLav(totalMontoLav);
                System.out.println("DEBUG 7 - Valor hora LAV: " + valorHoraLav);
                System.out.println("DEBUG 8 - Monto LAV calculado: " + totalMontoLav);

                /* SDF */
                BigDecimal valorHoraSdf = valorGuardiaBase.getTotalSdf().divide(BigDecimal.valueOf(24), 2,
                        RoundingMode.HALF_UP);
                BigDecimal totalMontoSdf = BigDecimal.valueOf(horas.getHorasSdf()).multiply(valorHoraSdf);
                horas.setMontoSdf(totalMontoSdf);
                System.out.println("DEBUG 9 - Valor hora SDF: " + valorHoraSdf);
                System.out.println("DEBUG 10 - Monto SDF calculado: " + totalMontoSdf);

                BigDecimal total = horas.getMontoLav().add(horas.getMontoSdf());
                horas.setMontoTotal(total);
                System.out.println("DEBUG [11] - Monto total calculado: " + total);

            } catch (Exception e) {
                System.out.println("Error al buscar ValorGuardiaCargoYagrup: " + e.getMessage());
            }
        } else {
            if (tipoGuardia == TipoGuardiaEnum.EXTRA || tipoGuardia == TipoGuardiaEnum.CONTRAFACTURA) {
                /* Si es Extra o CF */
                System.out.println("DEBUG - es tipo guardia extra o cf");
                /* Obtiene valores de guardia */
                /*
                 * ValorGuardiaExtrayCF valorGuardiaBase1 = valorGuardiaExtraYcfService
                 * .obtenerValorGuardiaExtraPorHospital(hospital.getId()).get();
                 * System.out.println("DEBUG 5 - ValorGuardiaBase obtenido: " +
                 * valorGuardiaBase1);
                 * System.out.println("DEBUG 6b - Total LAV/SDF: " +
                 * valorGuardiaBase1.getTotalLav() + "/"
                 * + valorGuardiaBase1.getTotalSdf());
                 */

                try {

                    /* Obtiene valores de guardia */
                    ValorGuardiaExtrayCF valorGuardiaBase = valorGuardiaExtraYcfService
                            .obtenerValorGuardiaExtraPorHospital(hospital.getId()).get();

                    System.out.println("DEBUG 5 CF- ValorGuardiaBase obtenido: " + valorGuardiaBase);
                    System.out.println("DEBUG 6c CF- Total LAV/SDF: " + valorGuardiaBase.getTotalLav() + "/"
                            + valorGuardiaBase.getTotalSdf());
                    /* Calcula montos para LAV/SDF (dividiendo el total entre 24hs) */
                    /* LAV */
                    BigDecimal valorHoraLav = valorGuardiaBase.getTotalLav().divide(BigDecimal.valueOf(24), 2,
                            RoundingMode.HALF_UP);
                    System.out.println("DEBUG 6.1 CF - valor de la hora LAV: " + valorHoraLav);
                    BigDecimal totalMontoLav = BigDecimal.valueOf(horas.getHorasLav()).multiply(valorHoraLav);

                    horas.setMontoLav(totalMontoLav);
                    System.out.println("DEBUG 7 CF - Valor hora LAV: " + valorHoraLav);
                    System.out.println("DEBUG 8 CF - Monto LAV calculado: " + totalMontoLav);

                    /* SDF */
                    BigDecimal valorHoraSdf = valorGuardiaBase.getTotalSdf().divide(BigDecimal.valueOf(24), 2,
                            RoundingMode.HALF_UP);
                    BigDecimal totalMontoSdf = BigDecimal.valueOf(horas.getHorasSdf()).multiply(valorHoraSdf);
                    horas.setMontoSdf(totalMontoSdf);
                    System.out.println("DEBUG 9 CF- Valor hora SDF: " + valorHoraSdf);
                    System.out.println("DEBUG 10 CF - Monto SDF calculado: " + totalMontoSdf);

                    BigDecimal total = horas.getMontoLav().add(horas.getMontoSdf());
                    horas.setMontoTotal(total);
                    System.out.println("DEBUG 11 CF - Monto total calculado: " + total);

                } catch (Exception e) {
                    System.out.println("Error al buscar ValorGuardiaExtraYcf: " + e.getMessage());
                }
            }
        }
        /* Retorna objeto SumaHoras con horas y montos calculados */
        return horas;
    }

    public ResponseEntity<?> registrarSalida(Long id, RegistroActividadDto registroActividadDto) {

        /* A. obtengo el registro de actividad */
        RegistroActividad registroActividad = findById(id).get();

        /* B. Calcular duración exacta */
        Duration duracionExacta = Duration.between(
                LocalDateTime.of(registroActividad.getFechaIngreso(), registroActividad.getHoraIngreso()),
                LocalDateTime.of(registroActividadDto.getFechaEgreso(), registroActividadDto.getHoraEgreso()));
        long horasTotales = duracionExacta.toHours();

        /* c. Asignar valor a esGuardiaCorta */
        boolean esGuardiaCorta = horasTotales < 4;
        registroActividad.setEsGuardiaIncompleta(esGuardiaCorta ? true : null);

        /* D. actualizo los datos de salida al registro de actividad */
        if (registroActividad.getFechaEgreso() != registroActividadDto.getFechaEgreso() &&
                registroActividadDto.getFechaEgreso() != null)
            registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());

        if (registroActividad.getHoraEgreso() != registroActividadDto.getHoraEgreso() &&
                registroActividadDto.getHoraEgreso() != null)
            registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());

        if (registroActividad.getMotivoEgreso() != registroActividadDto.getMotivoEgreso() &&
                registroActividadDto.getMotivoEgreso() != null)
            registroActividad.setMotivoEgreso(registroActividadDto.getMotivoEgreso());

        registroActividad.setHoraRegistroEgreso(LocalTime.now());
        registroActividad.setFechaRegistroEgreso(LocalDate.now());
        registroActividad.setServicio(servicioService.findById(registroActividadDto.getIdServicio()).get());
        registroActividad.setUsuarioEgreso(usuarioService.findById(registroActividadDto.getIdUsuarioEgreso()).get());

        ResponseEntity<?> respuestaDeletePendiente = null;
        
        if (!esGuardiaCorta) {
            /* E. Cálculo de horas y montos */
            SumaHoras horas = calcularHoras(registroActividad);
            System.out
                    .println("DEBUG - Horas calculadas (LAV/SDF): " + horas.getHorasLav() + "/" + horas.getHorasSdf());

            // guarda las horas calculadas en BD
            sumaHorasService.save(horas);
            registroActividad.setHorasRealizadas(horas);

            /* F. Gestión de registros pendientes */
            // elimina el registro de la lista de pendientes
            respuestaDeletePendiente = registrosPendientesService
                    .deleteRegistroActividad(registroActividad);

            // si la eliminacion fue exitosa desvincula el reg pendiente
            if (respuestaDeletePendiente.getStatusCode() == HttpStatus.OK) {
                registroActividad.setRegistrosPendientes(null);

                /* Actualización de registro mensual */
                registroActividad = registroMensualService.setRegistroMensual(registroActividad);
            }
        } else {
            // Guardia incompleta: limpia las horas realizadas y no suma al registro mensual
            registroActividad.setHorasRealizadas(null);

            /* Gestión de registros pendientes */
            // elimina el registro de la lista de pendientes
            respuestaDeletePendiente = registrosPendientesService
                    .deleteRegistroActividad(registroActividad);

            // si la eliminacion fue exitosa desvincula el reg pendiente
            if (respuestaDeletePendiente.getStatusCode() == HttpStatus.OK) {
                registroActividad.setRegistrosPendientes(null);

                /* Actualización de registro mensual sin horas */
                registroActividad = registroMensualService.setRegistroMensualSinHoras(registroActividad);
            }
        }

        save(registroActividad);

        // devuelvo el resultado de deleteRegistroActividad
        return respuestaDeletePendiente;
    }

    public ResponseEntity<?> logicDelete(Long id) {
        if (!activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        RegistroActividad registroActividad = findById(id).get();
        registroActividad.setActivo(false);
        save(registroActividad);

        return new ResponseEntity<>(new Mensaje("Registro de actividad eliminada correctamente"), HttpStatus.OK);
    }

    public List<RegActivMotivoDto> listarMotivos(Long idEfector, int mes, int anio, Long idServicio) {
        List<RegistroActividad> registros = registroActividadRepository
                .findMotivosByEfectorServicioMesAnio(idEfector, mes, anio);
        return registros.stream()
                .filter(ra -> ra.getServicio().getId().equals(idServicio) && ra.isActivo())
                // Filtrar registros donde al menos uno de los motivos tenga datos
                .filter(ra -> ra.getMotivoIngreso() != null || ra.getMotivoEgreso() != null)
                .map(ra -> new RegActivMotivoDto(
                        ra.getId(),
                        ra.getAsistencial().getId(),
                        ra.getFechaIngreso(),
                        ra.getFechaEgreso(),
                        ra.getHoraIngreso(),
                        ra.getHoraEgreso(),
                        ra.getUsuarioIngreso() != null ? ra.getUsuarioIngreso().getId() : null,
                        ra.getUsuarioEgreso() != null ? ra.getUsuarioEgreso().getId() : null,
                        ra.getMotivoIngreso(),
                        ra.getMotivoEgreso()))
                .collect(Collectors.toList());
    }

    public List<RegActivMotivoDto> listarMotivosByAsistencial(Long idAsistencial, Long idEfector, int mes, int anio,
            Long idServicio) {
        List<RegistroActividad> registros = registroActividadRepository
                .findMotivosByEfectorServicioMesAnio(idEfector, mes, anio);
        return registros.stream()
                .filter(ra -> ra.getAsistencial().getId().equals(idAsistencial)
                        && ra.getServicio().getId().equals(idServicio) && ra.isActivo())
                // Filtrar registros donde al menos uno de los motivos tenga datos
                .filter(ra -> ra.getMotivoIngreso() != null || ra.getMotivoEgreso() != null)
                .map(ra -> new RegActivMotivoDto(
                        ra.getId(),
                        ra.getAsistencial().getId(),
                        ra.getFechaIngreso(),
                        ra.getFechaEgreso(),
                        ra.getHoraIngreso(),
                        ra.getHoraEgreso(),
                        ra.getUsuarioIngreso() != null ? ra.getUsuarioIngreso().getId() : null,
                        ra.getUsuarioEgreso() != null ? ra.getUsuarioEgreso().getId() : null,
                        ra.getMotivoIngreso(),
                        ra.getMotivoEgreso()))
                .collect(Collectors.toList());
    }

    public boolean validarPrecondicionesCronograma(Long idEfector, int mes, int anio) {
        System.out.println("[SERVICE] Iniciando validación para efector: " + idEfector);

        // 1. Verifica existencia de registros por cada tipo de guardia
        System.out.println("[SERVICE] Verificando registros de actividad...");
        boolean tieneCargo = registroActividadRepository.existsByEfectorAndMesAndAnioAndTipoGuardia(
                idEfector, mes, anio, TipoGuardiaEnum.CARGO);
        System.out.println(" - CARGO: " + tieneCargo);

        boolean tieneAgrupacion = registroActividadRepository.existsByEfectorAndMesAndAnioAndTipoGuardia(
                idEfector, mes, anio, TipoGuardiaEnum.AGRUPACION);
        System.out.println(" - AGRUPACION: " + tieneAgrupacion);

        boolean tieneExtra = registroActividadRepository.existsByEfectorAndMesAndAnioAndTipoGuardia(
                idEfector, mes, anio, TipoGuardiaEnum.EXTRA);
        System.out.println(" - EXTRA: " + tieneExtra);

        boolean tieneContrafactura = registroActividadRepository.existsByEfectorAndMesAndAnioAndTipoGuardia(
                idEfector, mes, anio, TipoGuardiaEnum.CONTRAFACTURA);
        System.out.println(" - CONTRAFACTURA: " + tieneContrafactura);

        // 2. Para cada tipo con registros, verificar DDJJ aprobada
        System.out.println("[SERVICE] Verificando DDJJ aprobadas...");

        if ((tieneCargo || tieneAgrupacion)) {
            boolean ddjjCargoAprobada = ddjjPreAprobadaExistente(idEfector, mes, anio, TipoGuardiaEnum.CARGO);
            System.out.println(" - DDJJ CARGO/AGRUPACION aprobada: " + ddjjCargoAprobada);
            if (!ddjjCargoAprobada) {
                System.out.println("[SERVICE] Validación fallida: Falta DDJJ aprobada para CARGO/AGRUPACION");
                return false;
            }
        }

        if (tieneExtra) {
            boolean ddjjExtraAprobada = ddjjPreAprobadaExistente(idEfector, mes, anio, TipoGuardiaEnum.EXTRA);
            System.out.println(" - DDJJ EXTRA aprobada: " + ddjjExtraAprobada);
            if (!ddjjExtraAprobada) {
                System.out.println("[SERVICE] Validación fallida: Falta DDJJ aprobada para EXTRA");
                return false;
            }
        }

        if (tieneContrafactura) {
            boolean ddjjContrafacturaAprobada = ddjjPreAprobadaExistente(idEfector, mes, anio,
                    TipoGuardiaEnum.CONTRAFACTURA);
            System.out.println(" - DDJJ CONTRAFACTURA aprobada: " + ddjjContrafacturaAprobada);
            if (!ddjjContrafacturaAprobada) {
                System.out.println("[SERVICE] Validación fallida: Falta DDJJ aprobada para CONTRAFACTURA");
                return false;
            }
        }

        System.out.println("[SERVICE] Todas las validaciones fueron exitosas");
        return true;
    }

    private boolean ddjjPreAprobadaExistente(Long idEfector, int mes, int anio, TipoGuardiaEnum tipo) {

        // Convertir int a MesesEnum
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(mes);
        System.out.println("[SERVICE] Buscando DDJJ para tipo: " + tipo +
                ", mes: " + mesEnum + ", efector: " + idEfector);
        boolean exists = ddjjRepository.existsByEfectorIdAndMesAndAnioAndTipoGuardiaAndEstadoDdjjDirector(
                idEfector, mesEnum, anio, tipo, EstadoDdjjEnum.APROBADO);

        System.out.println(" - Resultado búsqueda DDJJ: " + exists);
        return exists;
    }

    public List<Long> obtenerIdsDdjjAprobadas(Long idEfector, int mes, int anio) {
        System.out.println("=== INICIO obtenerIdsDdjjAprobadas ===");
        System.out.println("Parámetros - idEfector: " + idEfector + ", mes: " + mes + ", anio: " + anio);

        List<Long> idsDdjjAprobadas = new ArrayList<>();
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(mes);
        System.out.println("MesEnum convertido: " + mesEnum);

        // 1. Verificar registros de actividad
        System.out.println("\n--- Verificando registros de actividad ---");

        boolean tieneCargo = registroActividadRepository.existsByEfectorAndMesAndAnioAndTipoGuardia(
                idEfector, mes, anio, TipoGuardiaEnum.CARGO);
        System.out.println("¿Tiene CARGO? " + tieneCargo);

        boolean tieneAgrupacion = registroActividadRepository.existsByEfectorAndMesAndAnioAndTipoGuardia(
                idEfector, mes, anio, TipoGuardiaEnum.AGRUPACION);
        System.out.println("¿Tiene AGRUPACION? " + tieneAgrupacion);

        boolean tieneExtra = registroActividadRepository.existsByEfectorAndMesAndAnioAndTipoGuardia(
                idEfector, mes, anio, TipoGuardiaEnum.EXTRA);
        System.out.println("¿Tiene EXTRA? " + tieneExtra);

        boolean tieneContrafactura = registroActividadRepository.existsByEfectorAndMesAndAnioAndTipoGuardia(
                idEfector, mes, anio, TipoGuardiaEnum.CONTRAFACTURA);
        System.out.println("¿Tiene CONTRAFACTURA? " + tieneContrafactura);

        // 2. Buscar DDJJ aprobadas por el director (usar consulta que devuelve lista de ids)
        System.out.println("\n--- Buscando DDJJ aprobadas ---");

        if (tieneCargo || tieneAgrupacion) {
            System.out.println("Buscando DDJJ CARGO aprobada...");
            List<Long> idsCargo = buscarIdsDdjjAprobadasPorTipo(idEfector, mesEnum, anio, TipoGuardiaEnum.CARGO);
            if (!idsCargo.isEmpty()) {
                System.out.println("DDJJ CARGO encontradas - IDs: " + idsCargo);
                idsDdjjAprobadas.addAll(idsCargo);
            } else {
                System.out.println("No se encontró DDJJ CARGO aprobada");
            }
        }

        if (tieneExtra) {
            System.out.println("Buscando DDJJ EXTRA aprobada...");
            List<Long> idsExtra = buscarIdsDdjjAprobadasPorTipo(idEfector, mesEnum, anio, TipoGuardiaEnum.EXTRA);
            if (!idsExtra.isEmpty()) {
                System.out.println("DDJJ EXTRA encontradas - IDs: " + idsExtra);
                idsDdjjAprobadas.addAll(idsExtra);
            } else {
                System.out.println("No se encontró DDJJ EXTRA aprobada");
            }
        }

        if (tieneContrafactura) {
            System.out.println("Buscando DDJJ CONTRAFACTURA aprobada...");
            List<Long> idsContrafactura = buscarIdsDdjjAprobadasPorTipo(idEfector, mesEnum, anio,
                    TipoGuardiaEnum.CONTRAFACTURA);
            if (!idsContrafactura.isEmpty()) {
                System.out.println("DDJJ CONTRAFACTURA encontradas - IDs: " + idsContrafactura);
                idsDdjjAprobadas.addAll(idsContrafactura);
            } else {
                System.out.println("No se encontró DDJJ CONTRAFACTURA aprobada");
            }
        }

        System.out.println("Lista de IDs encontrados: " + idsDdjjAprobadas);
        System.out.println("=== FIN obtenerIdsDdjjAprobadas ===\n");
        return idsDdjjAprobadas;
    }

    /**
     * Helper que consulta directamente la entidad DDJJ y devuelve todos los IDs
     * que cumplan con efector/mes/anio/tipo/estado = APROBADO.
     * Implementación usando Criteria API para resolver la entidad por clase.
     */
    private List<Long> buscarIdsDdjjAprobadasPorTipo(Long idEfector, MesesEnum mesEnum, int anio,
            TipoGuardiaEnum tipo) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Ddjj> root = cq.from(Ddjj.class);

        // construir predicados
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("efector").get("id"), idEfector));
        predicates.add(cb.equal(root.get("mes"), mesEnum));
        predicates.add(cb.equal(root.get("anio"), anio));
        predicates.add(cb.equal(root.get("tipoGuardia"), tipo));
        predicates.add(cb.equal(root.get("estadoDdjjDirector"), EstadoDdjjEnum.APROBADO));

        cq.select(root.get("id")).where(predicates.toArray(new Predicate[0]));

        TypedQuery<Long> query = entityManager.createQuery(cq);
        List<Long> results = query.getResultList();
        System.out.println("buscarIdsDdjjAprobadasPorTipo -> tipo: " + tipo + ", resultados: " + results);
        return results;
    }

    public List<RegActivAsistenciaDto> listarAsistenciaPorProfesionalYEfector(Long idAsistencial, Long idEfector,
            int mes, int anio) {
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(mes);
        int mesNumero = mesEnum.getNumeroMes();
        List<RegistroActividad> registros = registroActividadRepository
                .findMotivosByEfectorServicioMesAnio(idEfector, mesNumero, anio);

        return registros.stream()
                .filter(ra -> ra.getAsistencial().getId().equals(idAsistencial) && ra.isActivo())
                .map(ra -> {
                    RegActivAsistenciaDto dto = new RegActivAsistenciaDto();
                    dto.setId(ra.getId());
                    dto.setFechaIngreso(ra.getFechaIngreso());
                    dto.setFechaEgreso(ra.getFechaEgreso());
                    dto.setFechaRegistroIngreso(ra.getFechaRegistroIngreso());
                    dto.setFechaRegistroEgreso(ra.getFechaRegistroEgreso());
                    dto.setHoraIngreso(ra.getHoraIngreso());
                    dto.setHoraEgreso(ra.getHoraEgreso());
                    dto.setHoraRegistroIngreso(ra.getHoraRegistroIngreso());
                    dto.setHoraRegistroEgreso(ra.getHoraRegistroEgreso());
                    dto.setTipoGuardia(ra.getTipoGuardia() != null ? ra.getTipoGuardia().getNombre() : null);
                    dto.setActivo(ra.isActivo());

                    dto.setIdAsistencial(ra.getAsistencial() != null ? ra.getAsistencial().getId() : null);
                    dto.setServicio(ra.getServicio() != null ? ra.getServicio().getDescripcion() : null);
                    dto.setIdEfector(ra.getEfector() != null ? ra.getEfector().getId() : null);
                    dto.setUsuarioIngreso(
                            ra.getUsuarioIngreso() != null && ra.getUsuarioIngreso().getPerson() != null
                                    ? ra.getUsuarioIngreso().getPerson().getNombre() + " "
                                            + ra.getUsuarioIngreso().getPerson().getApellido()
                                    : null);
                    dto.setIdUsuarioEgreso(ra.getUsuarioEgreso() != null ? ra.getUsuarioEgreso().getId() : null);
                    dto.setMotivoIngreso(ra.getMotivoIngreso());
                    dto.setMotivoEgreso(ra.getMotivoEgreso());

                    return dto;
                })
                .collect(Collectors.toList());
    }
}