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
import com.guardias.backend.enums.TipoNotificacionEnum;
import com.guardias.backend.service.NotificacionService;

@RestController
@RequestMapping("/notificacion")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificacionController {

    @Autowired
    NotificacionService notificacionService;

    @GetMapping("/list")
    public ResponseEntity<List<Notificacion>> list() {
        List<Notificacion> list = notificacionService.findByActivo();
        return new ResponseEntity<List<Notificacion>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Notificacion>> listAll() {
        List<Notificacion> list = notificacionService.findAll();
        return new ResponseEntity<List<Notificacion>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Notificacion> getById(@PathVariable("id") Long id) {
        if (!notificacionService.activo(id))
            return new ResponseEntity(new Mensaje("no existe una notificación con ese nombre"), HttpStatus.NOT_FOUND);
        Notificacion notificacion = notificacionService.findById(id).get();
        return new ResponseEntity<Notificacion>(notificacion, HttpStatus.OK);
    }

    @GetMapping("/detailtipo/{tipo},{activo}")
    public ResponseEntity<List<Notificacion>> getByTipoAndActivo(@PathVariable("tipo") TipoNotificacionEnum tipo,
            @PathVariable("activo") boolean activo) {
        List<Notificacion> notificaciones = notificacionService.findByTipoAndActivo(tipo, activo);

        if (notificaciones.isEmpty()) {
            return new ResponseEntity(new Mensaje("No se encontraron notificaciones para ese tipo"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(notificaciones, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(NotificacionDto notificacionDto) {

        if (StringUtils.isBlank(notificacionDto.getCategoria())) {
            return new ResponseEntity<>(new Mensaje("La Categoria es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        if (notificacionDto.getTipo() == null)
            return new ResponseEntity<>(new Mensaje("El Tipo es obligatorio"), HttpStatus.BAD_REQUEST);

        if (notificacionDto.getFechaNotificacion() == null)
            return new ResponseEntity<>(new Mensaje("La Fecha de Notificacion es obligatoria"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(notificacionDto.getDetalle())) {
            return new ResponseEntity(new Mensaje("El Detalle es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    private Notificacion createUpdate(Notificacion notificacion, NotificacionDto notificacionDto) {

        notificacion.setTipo(notificacionDto.getTipo());
        notificacion.setCategoria(notificacionDto.getCategoria());
        notificacion.setFechaNotificacion(notificacionDto.getFechaNotificacion());
        notificacion.setDetalle(notificacionDto.getDetalle());
        notificacion.setUrl(notificacionDto.getUrl());
        notificacion.setActivo(notificacionDto.isActivo());
        notificacion.setFechaBaja(notificacionDto.getFechaBaja());
        notificacion.setActivo(true);

        return notificacion;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NotificacionDto notificacionDto) {

        ResponseEntity<?> respuestaValidaciones = validations(notificacionDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Notificacion notificacion = createUpdate(new Notificacion(), notificacionDto);
            notificacionService.save(notificacion);
            return new ResponseEntity<>(new Mensaje("Notificación creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    // Método update
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody NotificacionDto notificacionDto) {
        if (!notificacionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(notificacionDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Notificacion notificacion = createUpdate(notificacionService.findById(id).get(), notificacionDto);
            notificacionService.save(notificacion);
            return new ResponseEntity<>(new Mensaje("Notificación modificada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!notificacionService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Notificacion notificacion = notificacionService.findById(id).get();
        notificacion.setActivo(false);
        notificacionService.save(notificacion);
        return new ResponseEntity<>(new Mensaje("Notificación eliminada"), HttpStatus.OK);

    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!notificacionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        notificacionService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Notificación eliminada FISICAMENTE"), HttpStatus.OK);
    }
}
