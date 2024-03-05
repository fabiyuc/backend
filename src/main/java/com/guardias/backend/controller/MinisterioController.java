package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.MinisterioDto;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Ministerio;
import com.guardias.backend.service.MinisterioService;

@Controller
@RequestMapping("/ministerio")
@CrossOrigin(origins = "http://localhost:4200")
public class MinisterioController {

    @Autowired
    MinisterioService ministerioService;
    @Autowired
    EfectorController efectorController;

    @GetMapping("/list")
    public ResponseEntity<List<Ministerio>> list() {
        List<Ministerio> list = ministerioService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Ministerio>> getById(@PathVariable("id") Long id) {
        if (!ministerioService.existsById(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Ministerio ministerio = ministerioService.findById(id).get();
        return new ResponseEntity(ministerio, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Ministerio>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!ministerioService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Ministerio ministerio = ministerioService.findByNombre(nombre).get();
        return new ResponseEntity(ministerio, HttpStatus.OK);
    }

    private Ministerio createUpdate(Ministerio ministerio, MinisterioDto ministerioDto) {
        Efector efector = efectorController.createUpdate(ministerio, ministerioDto);
        ministerio = (Ministerio) efector;

        if (!ministerioDto.getCabecera().equals(ministerio.getCabecera()))
            ministerio.setCabecera(ministerioDto.getCabecera());

        return ministerio;
    }

    public ResponseEntity<?> create(@RequestBody MinisterioDto ministerioDto) {
        ResponseEntity<?> respuestaValidaciones = efectorController.validations(ministerioDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Ministerio ministerio = createUpdate(new Ministerio(), ministerioDto);
            ministerioService.save(ministerio);
            return new ResponseEntity(new Mensaje("Ministerio creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody MinisterioDto ministerioDto) {
        if (!ministerioService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = efectorController.validations(ministerioDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Ministerio ministerio = createUpdate(ministerioService.findById(id).get(), ministerioDto);
            ministerioService.save(ministerio);
            return new ResponseEntity(new Mensaje("Ministerio creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PostMapping("/{idEfector}/addAutoridad/{idAutoridad}")
    public ResponseEntity<?> agregarAutoridad(@PathVariable("idEfector") Long idEfector,
            @PathVariable("idAutoridad") Long idAutoridad) {
        ResponseEntity<?> respuestaValidaciones = efectorController.agregarAutoridad(idEfector, idAutoridad);
        return respuestaValidaciones;
    }

    // @PostMapping("/{idEfector}/addNotificacion/{idNotificacion}")
    // public ResponseEntity<?> agregarNotificacion(@PathVariable("idEfector") Long
    // idEfector,
    // @PathVariable("idNotificacion") Long idNotificacion) {
    // ResponseEntity<?> respuestaValidaciones =
    // efectorController.agregarNotificacion(idEfector, idNotificacion);
    // return respuestaValidaciones;
    // }

    @PostMapping("/{idEfector}/addLegajo/{idLegajo}")
    public ResponseEntity<?> agregarLegajo(@PathVariable("idEfector") Long idEfector,
            @PathVariable("idLegajo") Long idLegajo) {
        ResponseEntity<?> respuestaValidaciones = efectorController.agregarLegajo(idEfector, idLegajo);
        return respuestaValidaciones;
    }

    @PostMapping("/{idEfector}/addUdo/{idLegajoUdo}")
    public ResponseEntity<?> agregarLegajoUdo(@PathVariable("idEfector") Long idEfector,
            @PathVariable("idLegajoUdo") Long idLegajoUdo) {
        ResponseEntity<?> respuestaValidaciones = efectorController.agregarLegajoUdo(idEfector, idLegajoUdo);
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        ResponseEntity<?> respuestaValidaciones = efectorController.logicDelete(id);
        return respuestaValidaciones;
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!ministerioService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);
        ministerioService.deleteById(id);
        return new ResponseEntity<Mensaje>(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
