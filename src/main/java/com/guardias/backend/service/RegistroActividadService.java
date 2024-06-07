package com.guardias.backend.service;

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
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.repository.RegistroActividadRepository;
import com.guardias.backend.security.service.UsuarioService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistroActividadService {
    @Autowired
    RegistroActividadRepository registroActividadRepository;
    @Autowired
    RegistroMensualService registroMensualService;
    @Autowired
    RegistrosPendientesService registrosPendientesService;
    @Autowired
    ServicioService servicioService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    TipoGuardiaService tipoGuardiaService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    SumaHorasService sumaHorasService;

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
        registroActividad.setUsuario(usuarioService.findById(registroActividadDto.getIdUsuario()).get());
        registroActividad.setActivo(true);
        return registroActividad;
    }

    public ResponseEntity<?> create(RegistroActividadDto registroActividadDto) {

        ResponseEntity<?> respuestaValidaciones = validations(registroActividadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroActividad registroActividad = createUpdate(new RegistroActividad(),
                    registroActividadDto);

            // Set de fecha y hora en la que se realiza el registroActividad
            registroActividad.setFechaRegistroIngreso(LocalDate.now());
            registroActividad.setHoraRegistroIngreso(LocalTime.now());

            registroActividad = registrosPendientesService.addRegistroActividad(registroActividad);
            save(registroActividad);

            if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity(new Mensaje("Registro de Actividad creado"), HttpStatus.OK);
            } else {
                return new ResponseEntity(new Mensaje("error"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return respuestaValidaciones;
        }
    }

    public ResponseEntity<?> registrarSalida(Long id, RegistroActividadDto registroActividadDto) {

        RegistroActividad registroActividad = findById(id).get();

        if (registroActividad.getFechaEgreso() != registroActividadDto.getFechaEgreso() &&
                registroActividadDto.getFechaEgreso() != null)
            registroActividad.setFechaEgreso(registroActividadDto.getFechaEgreso());

        if (registroActividad.getHoraEgreso() != registroActividadDto.getHoraEgreso() &&
                registroActividadDto.getHoraEgreso() != null)
            registroActividad.setHoraEgreso(registroActividadDto.getHoraEgreso());

        // debe calcular las horas y crear el SUMAHORAS
        SumaHoras horas = sumaHorasService.calcularHoras(registroActividad);
        registroActividad.setHoras(horas);

        ResponseEntity<?> respuestaDeletePendiente = registrosPendientesService
                .deleteRegistroActividad(registroActividad);

        // Set de fecha y hora en la que se realiza el registroActividad
        registroActividad.setFechaRegistroEgreso(LocalDate.now());
        registroActividad.setHoraRegistroEgreso(LocalTime.now());

        if (respuestaDeletePendiente.getStatusCode() == HttpStatus.OK) {
            // mando y traigo nuevamente el registroActividad para evitar la referencia
            // ciclica
            registroActividad = registroMensualService.addToRegistroMensual(registroActividad);
            // pongo activo en FALSE porque ya esta en el registro mensual

            // VER el registro mensual deberia crear un json para poder eliminar el registro
            // de actividad a fin de mes!!!!
            registroActividad.setActivo(false);
            save(registroActividad);
        }

        return respuestaDeletePendiente;
    }

}