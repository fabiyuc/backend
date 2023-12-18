package com.guardias.backend.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NotificacionDto;
import com.guardias.backend.entity.Notificacion;
import com.guardias.backend.service.NotificacionService;

@RestController
@RequestMapping("/notificacion")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificacionController {

    @Autowired
    NotificacionService notificacionService;

    @GetMapping("/lista")
    public ResponseEntity<List<Notificacion>> list() {
        List<Notificacion> list = notificacionService.list();
        return new ResponseEntity<List<Notificacion>>(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Notificacion> getById(@PathVariable("id") Long id) {
        if (!notificacionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe una notificación con ese nombre"), HttpStatus.NOT_FOUND);
        Notificacion notificacion = notificacionService.getone(id).get();
        return new ResponseEntity<Notificacion>(notificacion, HttpStatus.OK);
    }

    @GetMapping("/detalletipo/{tipo}")
    public ResponseEntity<Notificacion> getByTipo(@PathVariable("tipo") String tipo) {
        if (!notificacionService.existsByTipo(tipo))
            return new ResponseEntity(new Mensaje("no existe ese tipo de Notificaión"), HttpStatus.NOT_FOUND);
        Notificacion notificacion = notificacionService.getByTipo(tipo).get();
        return new ResponseEntity<Notificacion>(notificacion, HttpStatus.OK);
    }

    // por q no me deja crear la fechaNotificacion

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NotificacionDto notificacionDto) {
        // Validaciones
        if (StringUtils.isBlank(notificacionDto.getTipo()))
            return new ResponseEntity<>(new Mensaje("El Tipo es obligatorio"), HttpStatus.BAD_REQUEST);

        if (notificacionService.existsByTipo(notificacionDto.getTipo()))
            return new ResponseEntity(new Mensaje("El Tipo ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(notificacionDto.getCategoria())) {
            return new ResponseEntity<>(new Mensaje("La Categoria es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if (notificacionDto.getFechaNotificacion() == null)
            return new ResponseEntity<>(new Mensaje("La Fecha de Notificacion es obligatoria"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(notificacionDto.getDetalle())) {
            return new ResponseEntity(new Mensaje("El Detalle es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (notificacionDto.getUrl() == null)
            return new ResponseEntity(new Mensaje("El Url es obligatorio"), HttpStatus.BAD_REQUEST);

        Notificacion notificacion = new Notificacion();
        notificacion.setTipo(notificacionDto.getTipo());
        notificacion.setCategoria(notificacionDto.getCategoria());
        notificacion.setFechanotificacion(notificacionDto.getFechaNotificacion());
        notificacion.setDetalle(notificacionDto.getDetalle());
        notificacion.setUrl(notificacionDto.getUrl());

        notificacionService.save(notificacion);

        return new ResponseEntity<>(new Mensaje("Notificación creada"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody NotificacionDto notificacionDto) {
        if (!notificacionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        if (notificacionService.existsByTipo(notificacionDto.getTipo())
                && notificacionService.getByTipo(notificacionDto.getTipo()).get().getId() != id)
            return new ResponseEntity(new Mensaje("La notificacion ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(notificacionDto.getTipo()))
            return new ResponseEntity<>(new Mensaje("el Tipo es obligatorio"), HttpStatus.BAD_REQUEST);

        if (notificacionDto.getCategoria() == null)
            return new ResponseEntity(new Mensaje("La Categoria es obligatoria"), HttpStatus.BAD_REQUEST);

        if (notificacionDto.getFechaNotificacion() == null)
            return new ResponseEntity(new Mensaje("La Fecha de Notificacion es obligatoria"), HttpStatus.BAD_REQUEST);

        if (notificacionDto.getDetalle() == null)
            return new ResponseEntity(new Mensaje("El Detalle es obligatorio"), HttpStatus.BAD_REQUEST);

        if (notificacionDto.getUrl() == null)
            return new ResponseEntity(new Mensaje("El Url es obligatorio"), HttpStatus.BAD_REQUEST);

        Notificacion notificacion = notificacionService.getone(id).get();
        notificacion.setTipo(notificacionDto.getTipo());
        notificacion.setCategoria(notificacionDto.getCategoria());
        notificacion.setFechanotificacion(notificacionDto.getFechaNotificacion());
        notificacion.setDetalle(notificacionDto.getDetalle());
        notificacion.setUrl(notificacionDto.getUrl());
        notificacionService.save(notificacion);

        return new ResponseEntity<>(new Mensaje("Notificaión Actualizada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!notificacionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        notificacionService.delete(id);
        return new ResponseEntity<>(new Mensaje("Notificación eliminada"), HttpStatus.OK);

    }

}
