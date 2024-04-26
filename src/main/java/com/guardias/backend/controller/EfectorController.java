package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.EfectorDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.Notificacion;
import com.guardias.backend.service.AutoridadService;
import com.guardias.backend.service.DistribucionHorariaService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.LegajoService;
import com.guardias.backend.service.NotificacionService;
import com.guardias.backend.service.RegionService;

@RestController
public class EfectorController {

    @Autowired
    EfectorService efectorService;
    @Autowired
    RegionService regionService;
    @Autowired
    DistribucionHorariaService distribucionHorariaService;
    @Autowired
    AutoridadService autoridadService;
    @Autowired
    LegajoService legajoService;
    @Autowired
    NotificacionService notificacionService;

    public ResponseEntity<?> validations(EfectorDto efectorDto, Long id) {
        if (StringUtils.isBlank(efectorDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(efectorDto.getDomicilio()))
            return new ResponseEntity(new Mensaje("el domicilio es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        // SI EXISTEN CAPS CON NOMBRES IGUALES!!!!!!!!!!!!!!!!!!!!!!
        // VER si conviene comparar segun ele tipo de efector
        // if (efectorService.existsByName(efectorDto.getNombre()))
        // return new ResponseEntity<Mensaje>(new Mensaje("Ese nombre ya existe"),
        // HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Efector createUpdate(Efector efector, EfectorDto efectorDto) {

        if (efector.getNombre() == null
                || (efectorDto.getNombre() != null && !Objects.equals(efector.getNombre(), efectorDto.getNombre())))
            efector.setNombre(efectorDto.getNombre());

        if (efector.getDomicilio() == null || (efectorDto.getDomicilio() != null
                && !Objects.equals(efector.getDomicilio(), efectorDto.getDomicilio())))
            efector.setDomicilio(efectorDto.getDomicilio());

        if (efector.getTelefono() == null || (efectorDto.getTelefono() != null
                && !Objects.equals(efector.getTelefono(), efectorDto.getTelefono())))
            efector.setTelefono(efectorDto.getTelefono());

        efector.setEstado(efectorDto.isEstado());

        if (efector.getObservacion() == null || (efectorDto.getObservacion() != null
                && !Objects.equals(efector.getObservacion(), efectorDto.getObservacion())))
            efector.setObservacion(efectorDto.getObservacion());

        if (efector.getRegion() == null ||
                (efectorDto.getIdRegion() != null &&
                        !Objects.equals(efector.getRegion().getId(),
                                efectorDto.getIdRegion()))) {
            efector.setRegion(regionService.findById(efectorDto.getIdRegion()).get());
        }

        if (efectorDto.getIdDistribucionesHorarias() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (efector.getDistribucionesHorarias() != null) {
                for (DistribucionHoraria distribucionHoraria : efector.getDistribucionesHorarias()) {
                    for (Long id : efectorDto.getIdDistribucionesHorarias()) {
                        if (!distribucionHoraria.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                efector.setDistribucionesHorarias(new ArrayList<DistribucionHoraria>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? efectorDto.getIdDistribucionesHorarias() : idList;

            for (Long id : idsToAdd) {
                efector.getDistribucionesHorarias().add(distribucionHorariaService.findById(id));
                distribucionHorariaService.findById(id).setEfector(efector);
            }
        }

        if (efectorDto.getIdAutoridades() != null) {
            List<Long> idList = new ArrayList();
            if (efector.getAutoridades() != null) {
                for (Autoridad autoridad : efector.getAutoridades()) {
                    for (Long id : efectorDto.getIdAutoridades()) {
                        if (!autoridad.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }
            List<Long> idsToAdd = idList.isEmpty() ? efectorDto.getIdAutoridades() : idList;
            for (Long id : idsToAdd) {
                efector.getAutoridades().add(autoridadService.findById(id).get());
                autoridadService.findById(id).get().setEfector(efector);
            }
        }

        if (efectorDto.getIdLegajosUdo() != null) {
            List<Long> idList = new ArrayList();
            if (efector.getLegajosUdo() != null) {
                for (Legajo LegajoUdo : efector.getLegajosUdo()) {
                    for (Long id : efectorDto.getIdLegajosUdo()) {
                        if (!LegajoUdo.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }
            List<Long> idsToAdd = idList.isEmpty() ? efectorDto.getIdLegajosUdo() : idList;
            for (Long id : idsToAdd) {
                efector.getLegajosUdo().add(legajoService.findById(id).get());
                legajoService.findById(id).get().setUdo(efector);
            }
        }

        if (efectorDto.getIdLegajos() != null) {
            List<Long> idList = new ArrayList();
            if (efector.getLegajos() != null) {
                for (Legajo legajo : efector.getLegajos()) {
                    for (Long id : efectorDto.getIdLegajos()) {
                        if (!legajo.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }
            List<Long> idsToAdd = idList.isEmpty() ? efectorDto.getIdLegajos() : idList;
            for (Long id : idsToAdd) {
                efector.getLegajos().add(legajoService.findById(id).get());
                legajoService.findById(id).get().getEfectores().add(efector);
            }
        }

        if (efectorDto.getIdNotificaciones() != null) {
            List<Long> idList = new ArrayList();
            if (efector.getNotificaciones() != null) {
                for (Notificacion notificacion : efector.getNotificaciones()) {
                    for (Long id : efectorDto.getIdNotificaciones()) {
                        if (!notificacion.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }
            List<Long> idsToAdd = idList.isEmpty() ? efectorDto.getIdNotificaciones() : idList;
            for (Long id : idsToAdd) {
                efector.getNotificaciones().add(notificacionService.findById(id).get());
                notificacionService.findById(id).get().getEfectores().add(efector);
            }
        }

        efector.setActivo(true);

        return efector;
    }

    public ResponseEntity<?> logicDelete(Long id) {
        if (!efectorService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        Efector efector = efectorService.findById(id);
        efector.setActivo(false);
        efectorService.saveEfector(efector);
        return new ResponseEntity(new Mensaje("Efector eliminado correctamente"), HttpStatus.OK);
    }
}
