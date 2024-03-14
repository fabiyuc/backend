package com.guardias.backend.controller;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.LeyDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Ley;
import com.guardias.backend.entity.NovedadPersonal;
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

    // TODO VER que esta validacion debe hacerse en el update teniendo en cuenta q
    // el id sea diferente!!
    public ResponseEntity<?> validationsCreate(LeyDto leyDto) {
        ResponseEntity<?> respuestaValidaciones = validations(leyDto);

        if (leyService.existsByNumero(leyDto.getNumero()))
            return new ResponseEntity<Mensaje>(new Mensaje("Ese numero ya existe"),
                    HttpStatus.BAD_REQUEST);
        if (leyService.existsByDenominacion(leyDto.getDenominacion()))
            return new ResponseEntity<Mensaje>(new Mensaje("Esa denominacion ya existe"),
                    HttpStatus.BAD_REQUEST);

        return respuestaValidaciones;
    }

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
        if (ley.getFechaBaja() != leyDto.getFechaBaja() && leyDto.getFechaBaja() != null)
            ley.setFechaBaja(leyDto.getFechaBaja());
        if (ley.getFechaModificacion() != leyDto.getFechaModificacion() && leyDto.getFechaModificacion() != null)
            ley.setFechaModificacion(leyDto.getFechaModificacion());
        if (ley.getMotivoModificacion() != leyDto.getMotivoModificacion() && leyDto.getMotivoModificacion() != null
                && !leyDto.getMotivoModificacion().isEmpty())
            ley.setMotivoModificacion(leyDto.getMotivoModificacion());

        if (ley.getTipoLey() == null ||
                (leyDto.getIdTipoLey() != null &&
                        !Objects.equals(ley.getTipoLey().getId(),
                                leyDto.getIdTipoLey()))) {
            ley.setTipoLey(tipoLeyService.findById(leyDto.getIdTipoLey()).get());
        }

        if (ley.getNovedadesPersonales() == null || leyDto.getIdNovedadesPersonales() != null) {
            Set<Long> idList = new HashSet<Long>();
            for (NovedadPersonal novedadPersonal : ley.getNovedadesPersonales()) {
                for (Long id : leyDto.getIdNovedadesPersonales()) {
                    if (!novedadPersonal.getId().equals(id)) {
                        idList.add(id);
                    }
                }

                if (idList.size() > 0) {
                    for (Long id : idList) {
                        ley.getNovedadesPersonales().add(novedadPersonalService.findById(id).get());
                    }
                }
            }
        }
        return ley;
    }

    public boolean existeNovedadPersonal(Set<NovedadPersonal> novedadesPersonales, Long idBuscado) {
        for (NovedadPersonal novedadPersonal : novedadesPersonales) {
            if (novedadPersonal.getId().equals(idBuscado)) {
                return true;
            }
        }
        return false;
    }

}
