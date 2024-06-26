package com.guardias.backend.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.LeyDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Ley;
import com.guardias.backend.service.LeyService;
import com.guardias.backend.service.NovedadPersonalService;
import com.guardias.backend.service.TipoLeyService;

@RestController
public class LeyController {

    @Autowired
    LeyService leyService;
    @Autowired
    TipoLeyService tipoLeyService;
    @Autowired
    NovedadPersonalService novedadPersonalService;

    public ResponseEntity<?> validations(LeyDto leyDto, Long id) {
        // ResponseEntity<?> respuestaValidaciones = validations(leyDto);

        if (leyDto.getNumero() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El numero es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (leyDto.getDenominacion() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La denominacion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (leyDto.getEstado() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El estado es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (leyDto.getFechaAlta() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La fecha de alta es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (leyDto.getIdTipoLey() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El tipo de Ley es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (leyService.existsByNumero(leyDto.getNumero())
                && (leyService.findByNumero(leyDto.getNumero()).getId() != id))
            return new ResponseEntity<Mensaje>(new Mensaje("Ese numero ya existe"),
                    HttpStatus.BAD_REQUEST);
        if (leyService.existsByDenominacion(leyDto.getDenominacion())
                && (leyService.findByDenominacion(leyDto.getDenominacion()).getId() != id))
            return new ResponseEntity<Mensaje>(new Mensaje("Esa denominacion ya existe"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    // public ResponseEntity<?> validations(LeyDto leyDto, Long id) {

    // if (leyService.existsByNumero(leyDto.getNumero())
    // && (leyService.findByNumero(leyDto.getNumero()).getId() != id))
    // return new ResponseEntity<Mensaje>(new Mensaje("Ese numero ya existe"),
    // HttpStatus.BAD_REQUEST);
    // if (leyService.existsByDenominacion(leyDto.getDenominacion())
    // && (leyService.findByDenominacion(leyDto.getDenominacion()).getId() != id))
    // return new ResponseEntity<Mensaje>(new Mensaje("Esa denominacion ya existe"),
    // HttpStatus.BAD_REQUEST);

    // return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    // }

    public Ley createUpdate(Ley ley, LeyDto leyDto) {

        if (leyDto.getNumero() != null && !leyDto.getNumero().isEmpty()
                && !leyDto.getNumero().equals(ley.getNumero()))
            ley.setNumero(leyDto.getNumero());

        if (leyDto.getDenominacion() != null && !leyDto.getDenominacion().isEmpty()
                && !leyDto.getDenominacion().equals(ley.getDenominacion()))
            ley.setDenominacion(leyDto.getDenominacion());

        if (leyDto.getDetalle() != null && !leyDto.getDetalle().isEmpty()
                && !leyDto.getDetalle().equals(ley.getDetalle()))
            ley.setDetalle(leyDto.getDetalle());

        if (leyDto.getEstado() != null && !leyDto.getEstado().equals(ley.getEstado()))
            ley.setEstado(leyDto.getEstado());

        if (leyDto.getFechaAlta() != null && !leyDto.getFechaAlta().equals(ley.getFechaAlta()))
            ley.setFechaAlta(leyDto.getFechaAlta());

        if (ley.getFechaBaja() != leyDto.getFechaBaja() && leyDto.getFechaBaja() != null)
            ley.setFechaBaja(leyDto.getFechaBaja());
        if (ley.getFechaModificacion() != leyDto.getFechaModificacion() && leyDto.getFechaModificacion() != null)
            ley.setFechaModificacion(leyDto.getFechaModificacion());
        if (ley.getMotivoModificacion() != leyDto.getMotivoModificacion() && leyDto.getMotivoModificacion() != null
                && !leyDto.getMotivoModificacion().isEmpty())
            ley.setMotivoModificacion(leyDto.getMotivoModificacion());

        try {
            if (ley.getTipoLey() == null ||
                    (leyDto.getIdTipoLey() != null &&
                            !Objects.equals(ley.getTipoLey().getId(),
                                    leyDto.getIdTipoLey()))) {
                ley.setTipoLey(tipoLeyService.findById(leyDto.getIdTipoLey()).get());
            }
        } catch (Exception ex) {
            String mensajePersonalizado = "Error en TipoLey: " + leyDto.getIdTipoLey() + " - Exception:"
                    + ex.getMessage();
            System.out.println(mensajePersonalizado);
            ex.printStackTrace();
        }

        ley.setActivo(true);
        return ley;
    }
}
