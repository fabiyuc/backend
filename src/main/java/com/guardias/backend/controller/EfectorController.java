package com.guardias.backend.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.EfectorDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.service.EfectorService;

@RestController
public class EfectorController {

    @Autowired
    EfectorService efectorService;

    public ResponseEntity<?> validationsCreate(EfectorDto efectorDto) {
        ResponseEntity<?> respuestaValidaciones = validations(efectorDto);

        if (efectorService.existsByName(efectorDto.getNombre()))
            return new ResponseEntity<Mensaje>(new Mensaje("Ese numero ya existe"),
                    HttpStatus.BAD_REQUEST);

        return respuestaValidaciones;
    }

    public ResponseEntity<?> validations(EfectorDto efectorDto) {
        if (StringUtils.isBlank(efectorDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(efectorDto.getDomicilio()))
            return new ResponseEntity(new Mensaje("el domicilio es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Efector createUpdate(Efector efector, EfectorDto efectorDto) {

        if (!efectorDto.getNombre().equals(efector.getNombre()))
            efector.setNombre(efectorDto.getNombre());

        if (!efectorDto.getDomicilio().equals(efector.getDomicilio()))
            efector.setDomicilio(efectorDto.getDomicilio());

        if (!efectorDto.getTelefono().equals(efector.getTelefono()))
            efector.setTelefono(efectorDto.getTelefono());

        efector.setEstado(efectorDto.isEstado());

        if (!efectorDto.getRegion().equals(efector.getRegion()))
            efector.setRegion(efectorDto.getRegion());

        if (!efectorDto.getLocalidad().equals(efector.getLocalidad()))
            efector.setLocalidad(efectorDto.getLocalidad());

        if (!efectorDto.getObservacion().equals(efector.getObservacion()))
            efector.setObservacion(efectorDto.getObservacion());

        efector.setActivo(efectorDto.isActivo());

        return efector;
    }

    // autoridades - notificaciones - legajos - legajosUdo - distribucionesHorarias

    // public ResponseEntity<?> agregarAutoridad(Long idEfector, Long idAutoridad) {
    // try {
    // efectorService.agregarAutoridad(idEfector, idAutoridad);
    // return new ResponseEntity<>(new Mensaje("Autoridad agregada al Efector
    // correctamente"), HttpStatus.OK);
    // } catch (EntityNotFoundException e) {
    // return new ResponseEntity<>(new Mensaje("No se encontró el Efector con el ID
    // proporcionado"),
    // HttpStatus.NOT_FOUND);
    // } catch (Exception e) {
    // return new ResponseEntity<>(new Mensaje("Error al agregar la autoridad al
    // Efector"),
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    // public ResponseEntity<?> agregarNotificacion(Long idEfector, Long
    // idNotificacion) {
    // try {
    // efectorService.agregarNotificacion(idEfector, idNotificacion);
    // return new ResponseEntity<>(new Mensaje("Notificacion agregada al Efector
    // correctamente"), HttpStatus.OK);
    // } catch (EntityNotFoundException e) {
    // return new ResponseEntity<>(new Mensaje("No se encontró el Efector con el ID
    // proporcionado"),
    // HttpStatus.NOT_FOUND);
    // } catch (Exception e) {
    // return new ResponseEntity<>(new Mensaje("Error al agregar la notificacion al
    // Efector"),
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // // }

    // public ResponseEntity<?> agregarLegajo(Long idEfector, Long idLegajo) {
    // try {
    // efectorService.agregarLegajo(idEfector, idLegajo);
    // return new ResponseEntity<>(new Mensaje("Legajo agregado al Efector
    // correctamente"), HttpStatus.OK);
    // } catch (EntityNotFoundException e) {
    // return new ResponseEntity<>(new Mensaje("No se encontró el Efector con el ID
    // proporcionado"),
    // HttpStatus.NOT_FOUND);
    // } catch (Exception e) {
    // return new ResponseEntity<>(new Mensaje("Error al agregar el Legajo al
    // Efector"),
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    // public ResponseEntity<?> agregarLegajoUdo(Long idEfector, Long idLegajoUdo) {
    // try {
    // efectorService.agregarLegajoUdo(idEfector, idLegajoUdo);
    // return new ResponseEntity<>(new Mensaje("UdO agregada al Efector
    // correctamente"), HttpStatus.OK);
    // } catch (EntityNotFoundException e) {
    // return new ResponseEntity<>(new Mensaje("No se encontró el Efector con el ID
    // proporcionado"),
    // HttpStatus.NOT_FOUND);
    // } catch (Exception e) {
    // return new ResponseEntity<>(new Mensaje("Error al agregar el UdO al
    // Efector"),
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    public ResponseEntity<?> logicDelete(Long id) {
        if (!efectorService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        Efector efector = efectorService.findEfector(id);
        efector.setActivo(false);
        efectorService.saveEfector(efector);
        return new ResponseEntity(new Mensaje("Efector eliminado correctamente"), HttpStatus.OK);
    }
}
