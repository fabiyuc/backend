package com.guardias.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RegistroActividadDto;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.entity.ValorGuardiaCargoYagrup;
import com.guardias.backend.entity.ValorGuardiaExtrayCF;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.RegistroActividadRepository;
import com.guardias.backend.security.service.UsuarioService;

import jakarta.transaction.Transactional;

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

        System.out.println("DEBUG [1] - Horas calculadas (LAV/SDF): " +
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
                System.out.println("DEBUG 6 - Total LAV/SDF: " + valorGuardiaBase.getTotalLav() + "/"
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
                System.out.println("DEBUG 9 - Valor hora SDF: " + valorHoraLav);
                System.out.println("DEBUG 10 - Monto SDF calculado: " + totalMontoLav);

                BigDecimal total = horas.getMontoLav().add(horas.getMontoSdf());
                horas.setMontoTotal(total);
                System.out.println("DEBUG [11] - Monto total calculado: " + total);

            } catch (Exception e) {
                System.out.println("Error al buscar ValorGuardiaCargoYagrup: " + e.getMessage());
            }
        } else {
            if (tipoGuardia == TipoGuardiaEnum.EXTRA || tipoGuardia == TipoGuardiaEnum.CONTRAFACTURA) {
                /* Si es Extra o CF */
                System.out.println("es tipo guardia extra o cf");
                /* Obtiene valores de guardia */
                ValorGuardiaExtrayCF valorGuardiaBase1 = valorGuardiaExtraYcfService
                        .obtenerValorGuardiaExtraPorHospital(hospital.getId()).get();
                System.out.println("DEBUG 5 - ValorGuardiaBase obtenido: " + valorGuardiaBase1);
                System.out.println("DEBUG 6 - Total LAV/SDF: " + valorGuardiaBase1.getTotalLav() + "/"
                        + valorGuardiaBase1.getTotalSdf());

                try {

                    /* Obtiene valores de guardia */
                    ValorGuardiaExtrayCF valorGuardiaBase = valorGuardiaExtraYcfService
                            .obtenerValorGuardiaExtraPorHospital(hospital.getId()).get();

                    System.out.println("DEBUG 5 - ValorGuardiaBase obtenido: " + valorGuardiaBase);
                    System.out.println("DEBUG 6 - Total LAV/SDF: " + valorGuardiaBase.getTotalLav() + "/"
                            + valorGuardiaBase.getTotalSdf());
                    /* Calcula montos para LAV/SDF (dividiendo el total entre 24hs) */
                    /* LAV */
                    BigDecimal valorHoraLav = valorGuardiaBase.getTotalLav().divide(BigDecimal.valueOf(24), 2,
                            RoundingMode.HALF_UP);
                    System.out.println("DEBUG 6.1 - valor de la hora LAV: " + valorHoraLav);
                    BigDecimal totalMontoLav = BigDecimal.valueOf(horas.getHorasLav()).multiply(valorHoraLav);

                    horas.setMontoLav(totalMontoLav);

                    /* SDF */
                    BigDecimal valorHoraSdf = valorGuardiaBase.getTotalSdf().divide(BigDecimal.valueOf(24), 2,
                            RoundingMode.HALF_UP);
                    BigDecimal totalMontoSdf = BigDecimal.valueOf(horas.getHorasSdf()).multiply(valorHoraSdf);
                    horas.setMontoSdf(totalMontoSdf);

                    System.out.println("DEBUG 6.2 - valor de la hora SDF: " + valorHoraSdf);

                    BigDecimal total = horas.getMontoLav().add(horas.getMontoSdf());
                    horas.setMontoTotal(total);

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

        /* B. actualizo los datos de salida al registro de actividad */
        if (registroActividad.getFechaEgreso() != registroActividadDto.getFechaEgreso() &&
                registroActividadDto.getFechaEgreso() != null)
            registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());

        if (registroActividad.getHoraEgreso() != registroActividadDto.getHoraEgreso() &&
                registroActividadDto.getHoraEgreso() != null)
            registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());

        registroActividad.setHoraRegistroEgreso(LocalTime.now());
        registroActividad.setFechaRegistroEgreso(LocalDate.now());
        registroActividad.setServicio(servicioService.findById(registroActividadDto.getIdServicio()).get());
        registroActividad.setUsuarioEgreso(usuarioService.findById(registroActividadDto.getIdUsuarioEgreso()).get());

        /* C. Cálculo de horas y montos */
        //
        SumaHoras horas = calcularHoras(registroActividad);
        System.out.println("DEBUG - Horas calculadas (LAV/SDF): " + horas.getHorasLav() + "/" + horas.getHorasSdf());

        // guarda las horas calculadas en BD
        sumaHorasService.save(horas);
        registroActividad.setHorasRealizadas(horas);

        /* D. Gestión de registros pendientes */
        // elimina el registro de la lista de pendientes
        ResponseEntity<?> respuestaDeletePendiente = registrosPendientesService
                .deleteRegistroActividad(registroActividad);

        // si la eliminacion fue exitosa desvincula el reg pendiente
        if (respuestaDeletePendiente.getStatusCode() == HttpStatus.OK) {
            registroActividad.setRegistrosPendientes(null);

            /* Actualización de registro mensual */
            registroActividad = registroMensualService.setRegistroMensual(registroActividad);
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

}