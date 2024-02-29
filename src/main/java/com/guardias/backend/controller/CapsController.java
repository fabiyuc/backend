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

import com.guardias.backend.dto.CapsDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Caps;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.service.CapsService;

@Controller
@RequestMapping("/caps")
@CrossOrigin(origins = "http://localhost:4200")
public class CapsController {

    @Autowired
    CapsService capsService;

    @Autowired
    EfectorController efectorController;

    @GetMapping("/list")
    public ResponseEntity<List<Caps>> list() {
        List<Caps> list = capsService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Caps>> getById(@PathVariable("id") Long id) {
        if (!capsService.existsById(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Caps caps = capsService.findById(id).get();
        return new ResponseEntity(caps, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Caps>> getById(@PathVariable("nombre") String nombre) {
        if (!capsService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Caps caps = capsService.findByNombre(nombre).get();
        return new ResponseEntity(caps, HttpStatus.OK);
    }

    private Caps createUpdate(Caps caps, CapsDto capsDto) {
        Efector efector = efectorController.createUpdate(caps, capsDto);
        caps = (Caps) efector;

        if (!capsDto.getCabecera().equals(caps.getCabecera()))
            caps.setCabecera(capsDto.getCabecera());

        if (!capsDto.getTipoCaps().equals(caps.getTipoCaps()))
            caps.setTipoCaps(capsDto.getTipoCaps());

        caps.setAreaProgramatica(capsDto.getAreaProgramatica());

        return caps;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CapsDto capsDto) {
        ResponseEntity<?> respuestaValidaciones = efectorController.validations(capsDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Caps caps = createUpdate(new Caps(), capsDto);
            capsService.save(caps);
            return new ResponseEntity(new Mensaje("Caps creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CapsDto capsDto) {
        if (!capsService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = efectorController.validations(capsDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Caps caps = createUpdate(capsService.findById(id).get(), capsDto);
            capsService.save(caps);
            return new ResponseEntity(new Mensaje("Caps creado correctamente"), HttpStatus.OK);
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

    @PostMapping("/{idEfector}/addNotificacion/{idNotificacion}")
    public ResponseEntity<?> agregarNotificacion(@PathVariable("idEfector") Long idEfector,
            @PathVariable("idNotificacion") Long idNotificacion) {
        ResponseEntity<?> respuestaValidaciones = efectorController.agregarNotificacion(idEfector, idNotificacion);
        return respuestaValidaciones;
    }

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
        if (!capsService.existsById(id))
            return new ResponseEntity(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);
        capsService.deleteById(id);
        return new ResponseEntity(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
