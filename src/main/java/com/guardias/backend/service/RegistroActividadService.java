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

import com.guardias.backend.controller.RegistroMensualController;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RegistroActividadDto;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.entity.ValorGmi;
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
   /*  @Autowired
    RegistroMensualController registroMensualController; */

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

        // para calcular ZONA porcentajePorZona
        Float porcentajePorZona = registroActividad.getEfector().getPorcentajePorZona();
        float zona = porcentajePorZona != null ? porcentajePorZona : 1.0f;

        // Valor de la GMI segun la fecha de inicio de la guardia
        TipoGuardiaEnum tipoGuardia = registroActividad.getTipoGuardia().getNombre();
        System.out.println(tipoGuardia);
        ValorGmi valorGmi = new ValorGmi();
        try {

            System.out.println("fecha ingreso: "+registroActividad.getFechaIngreso());

            valorGmi = valorGmiService.getByFechaAndTipoGuardia(registroActividad.getFechaIngreso(), tipoGuardia)
                    .get();

        } catch (Exception e) {
            System.out.println(" registroActividadService Ln177 - valor GMI no encontrado: " + e.getMessage());
        }

        // Valor por hora de la guardia segun la GMI correspondiente a la fechaInicio y a la zona
        BigDecimal valorHora = new BigDecimal(0);
        try {

            
            System.out.println("###### monto" + valorGmi.getMonto() );
            //Divide el monto de la GMI entre 24 para obtener el valor por hora y multiplica por ek valor de zona
            valorHora = BigDecimal.valueOf(zona)
                    .multiply(valorGmi.getMonto().divide(BigDecimal.valueOf(24), 2, RoundingMode.HALF_UP));

                    System.out.println("valor hora: "+valorHora);

        } catch (Exception e) {
            System.out.println("error registroActividadService Ln187 - " + e.getMessage());
        }

        // calcula BONO
        if (registroActividad.getServicio().isCritico()) {
            //está pasando las horas
            horas.setBonoLav(horas.getHorasLav());
            horas.setBonoSdf(horas.getHorasSdf());
        } else {
            horas.setBonoLav(0);
            horas.setBonoSdf(0);
        }
        // FALTA calcular el tipo de guardia para multiplicar con el montoHoras....

        horas.setMontoHorasLav(valorHora.multiply(BigDecimal.valueOf(horas.getHorasLav())));
        System.out.println("valor hora-----" + valorHora);
        System.out.println("horas ------" + horas.getHorasLav());
        System.out.println("set montohorasLav: ####"+ valorHora.multiply(BigDecimal.valueOf(horas.getHorasLav())));

        horas.setMontoHorasSdf(valorHora.multiply(BigDecimal.valueOf(horas.getHorasSdf())));
        System.out.println("setMontoHorasSdf: ####"+ valorHora.multiply(BigDecimal.valueOf(horas.getHorasSdf())));

        horas.setMontoBonoLav(valorHora.multiply(BigDecimal.valueOf(horas.getBonoLav())));
        System.out.println("setMontoBonoLav: ####"+ valorHora.multiply(BigDecimal.valueOf(horas.getBonoLav())));
        
        horas.setMontoBonoSdf(valorHora.multiply(BigDecimal.valueOf(horas.getBonoSdf())));
        System.out.println("setMontoBonoSdf: ####"+ valorHora.multiply(BigDecimal.valueOf(horas.getBonoSdf())));

        BigDecimal total = horas.getMontoHorasLav()
                .add(horas.getMontoHorasSdf().add(horas.getMontoBonoLav().add(horas.getMontoBonoSdf())));
        horas.setMontoTotal(total);

        return horas;
    }

    public ResponseEntity<?> registrarSalida(Long id, RegistroActividadDto registroActividadDto) {

        if (!activo(id))
            return new ResponseEntity(new Mensaje("Registro de actividad no existe"), HttpStatus.NOT_FOUND);

        RegistroActividad registroActividad = findById(id).get();

        System.out.println("id: " + registroActividad.getRegistrosPendientes().getId());

        if (registroActividad.getFechaEgreso() != registroActividadDto.getFechaEgreso() &&
                registroActividadDto.getFechaEgreso() != null)
            registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());

        if (registroActividad.getHoraEgreso() != registroActividadDto.getHoraEgreso() &&
                registroActividadDto.getHoraEgreso() != null)
            registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());

        SumaHoras horas = calcularHoras(registroActividad);
        sumaHorasService.save(horas);
        registroActividad.setHorasRealizadas(horas);

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