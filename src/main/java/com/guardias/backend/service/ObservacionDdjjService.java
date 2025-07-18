package com.guardias.backend.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ObservacionDdjjDto;
import com.guardias.backend.dto.ObservacionDdjj.ObservacionDdjjUltimoDto;
import com.guardias.backend.entity.ObservacionDdjj;
import com.guardias.backend.repository.ObservacionDdjjRepository;
import com.guardias.backend.security.service.UsuarioService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ObservacionDdjjService {

    @Autowired
    ObservacionDdjjRepository observacionDdjjRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    DdjjService ddjjService;

    public List<ObservacionDdjj> findByActivoTrue() {
        return observacionDdjjRepository.findByActivoTrue();
    }

    public List<ObservacionDdjj> findAll() {
        return observacionDdjjRepository.findAll();
    }

    public boolean activo(Long id) {
        return (observacionDdjjRepository.existsById(id) && observacionDdjjRepository.findById(id).get().isActivo());
    }

    public Optional<ObservacionDdjj> findById(Long id) {
        return observacionDdjjRepository.findById(id);
    }

    public ResponseEntity<?> validations(ObservacionDdjjDto observacionDdjjDto, Long id) {

        if (observacionDdjjDto.getMotivo() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar el motivo"),
                    HttpStatus.BAD_REQUEST);

        if (observacionDdjjDto.getTipoDph() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar si es de tipo DPH"),
                    HttpStatus.BAD_REQUEST);

        if (observacionDdjjDto.getIdUsuario() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar el id del usuario"),
                    HttpStatus.BAD_REQUEST);

        if (observacionDdjjDto.getIdDdjj() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar el id de la ddjj"),
                    HttpStatus.BAD_REQUEST);
        
        if (observacionDdjjDto.getFechaCreacion() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la fecha de creacion"),
                    HttpStatus.BAD_REQUEST);

        if (observacionDdjjDto.getHoraCreacion() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la hora de creacion"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    public ObservacionDdjj createUpdate(ObservacionDdjj observacionDdjj, ObservacionDdjjDto observacionDdjjDto) {

        if (observacionDdjj.getMotivo() != observacionDdjjDto.getMotivo())
            observacionDdjj.setMotivo(observacionDdjjDto.getMotivo());

        if (observacionDdjj.getTipoDph() != observacionDdjjDto.getTipoDph())
            observacionDdjj.setTipoDph(observacionDdjjDto.getTipoDph());

        if (observacionDdjj.getUsuario() == null
                || !Objects.equals(observacionDdjj.getUsuario().getId(), observacionDdjjDto.getIdUsuario()))
            observacionDdjj.setUsuario(usuarioService.findById(observacionDdjjDto.getIdUsuario()).get());

        if (observacionDdjj.getDdjj() == null
                || !Objects.equals(observacionDdjj.getDdjj().getId(), observacionDdjjDto.getIdDdjj()))
            observacionDdjj.setDdjj(ddjjService.findById(observacionDdjjDto.getIdDdjj()).get());

        if (observacionDdjj.getFechaCreacion() != observacionDdjjDto.getFechaCreacion())
            observacionDdjj.setFechaCreacion(observacionDdjjDto.getFechaCreacion());
        
        if (observacionDdjj.getHoraCreacion() != observacionDdjjDto.getHoraCreacion())
            observacionDdjj.setHoraCreacion(observacionDdjjDto.getHoraCreacion());
        observacionDdjj.setActivo(true);

        return observacionDdjj;
    }

    public void save(ObservacionDdjj observacionDdjj) {
        observacionDdjjRepository.save(observacionDdjj);
    }

    public boolean existsById(Long id) {
        return observacionDdjjRepository.existsById(id);
    }

    public void deleteById(Long id) {
        observacionDdjjRepository.deleteById(id);
    }

    public ObservacionDdjjUltimoDto getUltimaObservacionByDdjjAndTipoDph(Long idDdjj, Boolean tipoDph) {

        List<ObservacionDdjj> observaciones = observacionDdjjRepository
                .findUltimaObservacion(idDdjj, tipoDph);

        System.out.println("Cantidad de observaciones encontradas: " + observaciones.size());

        if (observaciones.isEmpty()) {
            return null;
        }

        ObservacionDdjj obs = observaciones.get(0);
        System.out.println("Última observación - ID: " + obs.getId());

        // Tomamos la primera observación (que es la última por el orden DESC)
        ObservacionDdjjUltimoDto resultado = convertToDto(observaciones.get(0));

        return resultado;
    }

    private ObservacionDdjjUltimoDto convertToDto(ObservacionDdjj observacion) {

        String nombre = "No disponible";
        String apellido = "No disponible";

        if (observacion.getUsuario() != null) {

            if (observacion.getUsuario().getPerson() != null) {
                nombre = observacion.getUsuario().getPerson().getNombre();
                apellido = observacion.getUsuario().getPerson().getApellido();
            } else {
                System.out.println("ADVERTENCIA: Usuario no tiene persona asociada");
            }
        } else {
            System.out.println("ADVERTENCIA: Observación no tiene usuario asociado");
        }

        ObservacionDdjjUltimoDto dto = new ObservacionDdjjUltimoDto(
                observacion.getId(),
                observacion.getMotivo(),
                nombre,
                apellido,
                observacion.getFechaCreacion(),
                observacion.getHoraCreacion());

        return dto;
    }

    public List<ObservacionDdjjUltimoDto> getAllObservacionesActivasByDdjjAndTipoDph(Long idDdjj, Boolean tipoDph) {

        List<ObservacionDdjj> observaciones = observacionDdjjRepository
                .findAllObservacionesActivas(idDdjj, tipoDph);

        System.out.println("Cantidad total de observaciones activas encontradas: " + observaciones.size());

        if (observaciones.isEmpty()) {
            return Collections.emptyList();
        }

        // Convertimos todas las observaciones a DTO
        return observaciones.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
