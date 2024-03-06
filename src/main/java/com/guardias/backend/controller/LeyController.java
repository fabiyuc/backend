package com.guardias.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.LeyDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Ley;
import com.guardias.backend.service.LeyService;

@RestController
public class LeyController {

    @Autowired
    LeyService leyService;

    public ResponseEntity<?> validations(LeyDto leyDto) {

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

        if (leyService.existsByNumero(leyDto.getNumero()))
            return new ResponseEntity<Mensaje>(new Mensaje("Ese numero ya existe"),
                    HttpStatus.BAD_REQUEST);
        if (leyService.existsByDenominacion(leyDto.getDenominacion()))
            return new ResponseEntity<Mensaje>(new Mensaje("Esa denominacion ya existe"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Ley createUpdate(Ley ley, LeyDto leyDto) {
        if (!leyDto.getNumero().equals(ley.getNumero()))
            ley.setNumero(leyDto.getNumero());
        if (!leyDto.getDenominacion().equals(ley.getDenominacion()))
            ley.setDenominacion(leyDto.getDenominacion());
        if (!leyDto.getDetalle().equals(ley.getDetalle()))
            ley.setDetalle(leyDto.getDetalle());
        if (!leyDto.getEstado().equals(ley.getEstado()))
            ley.setEstado(leyDto.getEstado());
        if (!leyDto.getFechaAlta().equals(ley.getFechaAlta()))
            ley.setFechaAlta(leyDto.getFechaAlta());
        if (!leyDto.getFechaBaja().equals(ley.getFechaBaja()))
            ley.setFechaBaja(leyDto.getFechaBaja());
        if (!leyDto.getFechaModificacion().equals(ley.getFechaModificacion()))
            ley.setFechaModificacion(leyDto.getFechaModificacion());
        if (!leyDto.getMotivoModificacion().equals(ley.getMotivoModificacion()))
            ley.setMotivoModificacion(leyDto.getMotivoModificacion());

        return ley;
    }

}
