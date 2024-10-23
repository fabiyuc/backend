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
    ValorGmiService valorGmiService;
    @Autowired
    RegistrosPendientesService registrosPendientesService;
    @Autowired
    ValorGuardiaCargoYagrupService valorGuardiaCargoYagrupService;
    @Autowired
    ValorGuardiaExtraYcfService valorGuardiaExtraYcfService;
    @Autowired
    HospitalService hospitalService;
    /*
     * @Autowired
     * RegistroMensualController registroMensualController;
     */

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

        registroActividad.setUsuarioIngreso(usuarioService.findById(registroActividadDto.getIdUsuario()).get());
        registroActividad.setHoraRegistroIngreso(LocalTime.now());
        registroActividad.setFechaRegistroIngreso(LocalDate.now());
        registroActividad.setActivo(true);
        return registroActividad;
    }

    private SumaHoras calcularHoras(RegistroActividad registroActividad) {
        SumaHoras horas = sumaHorasService.calcularHoras(registroActividad.getFechaIngreso(),
                registroActividad.getFechaEgreso(), registroActividad.getHoraIngreso(),
                registroActividad.getHoraEgreso());

        System.out.println("....... total horas Lav:  " + horas.getHorasLav());
        System.out.println("....... total horas Sdf: " + horas.getHorasSdf());

        TipoGuardiaEnum tipoGuardia = registroActividad.getTipoGuardia().getNombre();
        System.out.println("....... tipo guardia: " + tipoGuardia);

        Efector efector = registroActividad.getEfector();
        System.out.println("....... efector del regActiv: " + efector.getNombre());

        Hospital hospital = hospitalService.findById(efector.getId()).orElse(null);
        System.out.println("....... nombre del hospital  " + hospital.getNombre());

        // Long idValorGuardia = hospitalRegActiv.getValoresGuardiaBase();

        /* if (tipoGuardia.equals("CARGO") || tipoGuardia.equals("AGRUPACION")) { */
        if (tipoGuardia == TipoGuardiaEnum.CARGO || tipoGuardia == TipoGuardiaEnum.AGRUPACION) {
            System.out.println("es tipo guardia cargo o agrup");

            try {
                // Valor de la guardia segun tipoGuardia y efector
                ValorGuardiaCargoYagrup valorGuardiaBase = (ValorGuardiaCargoYagrup) efectorService
                        .obtenerValorGuardiaActivo(hospital.getId()).get();

                System.out.println("... valor de guardia base total Lav : " + valorGuardiaBase.getTotalLav());
            

                BigDecimal valorHoraLav = valorGuardiaBase.getTotalLav().divide(BigDecimal.valueOf(24), 2,
                        RoundingMode.HALF_UP);

                BigDecimal totalMontoLav = BigDecimal.valueOf(horas.getHorasLav()).multiply(valorHoraLav);

                horas.setMontoLav(totalMontoLav);
                System.out.println("... monto Lav : " + horas.getMontoLav());

                BigDecimal valorHoraSdf = valorGuardiaBase.getTotalSdf().divide(BigDecimal.valueOf(24), 2,
                        RoundingMode.HALF_UP);

                BigDecimal totalMontoSdf = BigDecimal.valueOf(horas.getHorasLav()).multiply(valorHoraSdf);

                horas.setMontoSdf(totalMontoSdf);
                System.out.println("... monto Sdf : " + horas.getMontoSdf());

                BigDecimal total = horas.getMontoLav().add(horas.getMontoSdf());
                horas.setMontoTotal(total);
                System.out.println("... monto total : " + horas.getMontoTotal());

            } catch (Exception e) {
                System.out.println("Error al buscar ValorGuardiaCargoYagrup: " + e.getMessage());
            }
        } else {
            System.out.println("es tipo guardia extra o cf");
            try {
                ValorGuardiaExtrayCF valorGuardiaBase = (ValorGuardiaExtrayCF) efectorService
                        .obtenerValorGuardiaActivo(hospital.getId()).get();
                /*
                 * ValorGuardiaExtrayCF valorGuardiaBase =
                 * valorGuardiaExtraYcfService.buscarPorIdEfector(idEfector).get();
                 */

                BigDecimal valorHoraLav = valorGuardiaBase.getTotalLav().divide(BigDecimal.valueOf(24), 2,
                        RoundingMode.HALF_UP);

                BigDecimal totalMontoLav = BigDecimal.valueOf(horas.getHorasLav()).multiply(valorHoraLav);

                horas.setMontoLav(totalMontoLav);

                BigDecimal valorHoraSdf = valorGuardiaBase.getTotalSdf().divide(BigDecimal.valueOf(24), 2,
                        RoundingMode.HALF_UP);

                BigDecimal totalMontoSdf = BigDecimal.valueOf(horas.getHorasLav()).multiply(valorHoraSdf);

                horas.setMontoSdf(totalMontoSdf);

                BigDecimal total = horas.getMontoLav().add(horas.getMontoSdf());
                horas.setMontoTotal(total);

            } catch (Exception e) {
                System.out.println("Error al buscar ValorGuardiaExtraYcf: " + e.getMessage());
            }
        }

        return horas;
    }

    public ResponseEntity<?> registrarSalida(Long id, RegistroActividadDto registroActividadDto) {

        if (!activo(id))
            return new ResponseEntity(new Mensaje("Registro de actividad no existe"), HttpStatus.NOT_FOUND);

        RegistroActividad registroActividad = findById(id).get();

        if (registroActividad.getFechaEgreso() != registroActividadDto.getFechaEgreso() &&
                registroActividadDto.getFechaEgreso() != null)
            registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());

        if (registroActividad.getHoraEgreso() != registroActividadDto.getHoraEgreso() &&
                registroActividadDto.getHoraEgreso() != null)
            registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());

        SumaHoras horas = calcularHoras(registroActividad);
        System.out.println("... valor de horas : " + horas);

        
        sumaHorasService.save(horas);
        System.out.println("... GUARDE POR 1RA VEZ SUMAHORAS : ");

        registroActividad.setHorasRealizadas(horas);
        System.out.println("... SETEO horas realizadas en reg activ : " + registroActividad.getHorasRealizadas());

        registroActividad.setHoraRegistroEgreso(LocalTime.now());
        registroActividad.setFechaRegistroEgreso(LocalDate.now());
        registroActividad.setUsuarioEgreso(usuarioService.findById(registroActividadDto.getIdUsuario()).get());

        ResponseEntity<?> respuestaDeletePendiente = registrosPendientesService
                .deleteRegistroActividad(registroActividad);

        if (respuestaDeletePendiente.getStatusCode() == HttpStatus.OK) {
            registroActividad.setRegistrosPendientes(null);
            registroActividad = registroMensualService.setRegistroMensual(registroActividad);
        }
        save(registroActividad);
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