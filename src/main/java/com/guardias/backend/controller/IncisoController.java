package com.guardias.backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.guardias.backend.dto.IncisoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.entity.Ley;
import com.guardias.backend.service.IncisoService;

@Controller
@RequestMapping("/inciso")
@CrossOrigin(origins = "http://localhost:4200")
public class IncisoController {
    @Autowired
    IncisoService incisoService;
    @Autowired
    LeyController leyController;

    @GetMapping("/list")
    public ResponseEntity<List<Inciso>> list() {
        List<Inciso> list = incisoService.findByActivoTrue();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Inciso>> listAll() {
        List<Inciso> list = incisoService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Inciso>> getById(@PathVariable("id") Long id) {
        if (!incisoService.existsById(id))
            return new ResponseEntity(new Mensaje("Inciso no encontrado"), HttpStatus.NOT_FOUND);
        Inciso inciso = incisoService.findById(id).get();
        return new ResponseEntity(inciso, HttpStatus.OK);
    }

    private Inciso createUpdate(Inciso inciso, IncisoDto incisoDto) {

        Ley ley = leyController.createUpdate(inciso, incisoDto);
        inciso = (Inciso) ley;

        // TODO Verificar esto!!!
        if ((inciso.getArticulo() != incisoDto.getArticulo()) && incisoDto.getArticulo() != null) {
            incisoDto.setArticulo(incisoDto.getArticulo());
        }

        if (inciso.getSubIncisos() == null || incisoDto.getIdSubIncisos() != null) {
            Set<Long> idList = new HashSet<Long>();
            for (Inciso incisoList : inciso.getSubIncisos()) {
                for (Long id : incisoDto.getIdSubIncisos()) {
                    if (!incisoList.getId().equals(id)) {
                        idList.add(id);
                    }
                }
            }
            Set<Long> idsToAdd = idList.isEmpty() ? incisoDto.getIdSubIncisos() : idList;
            for (Long id : idsToAdd) {
                incisoService.findById(id).get().setInciso(inciso);
                inciso.getSubIncisos().add(incisoService.findById(id).get());
            }
        }

        return inciso;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody IncisoDto incisoDto) {
        ResponseEntity<?> respuestaValidaciones = leyController.validationsCreate(incisoDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Inciso inciso = createUpdate(new Inciso(), incisoDto);
            incisoService.save(inciso);
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso creado correctamente"), HttpStatus.OK);
        } else {
            return new ResponseEntity<Mensaje>(new Mensaje("Error al crear el elemento"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody IncisoDto incisoDto) {
        if (!incisoService.activo(id))
            return new ResponseEntity(new Mensaje("El inciso no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = leyController.validations(incisoDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Inciso inciso = createUpdate(incisoService.findById(id).get(), incisoDto);
            incisoService.save(inciso);
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso modificado correctamente"), HttpStatus.OK);
        } else {
            return new ResponseEntity<Mensaje>(new Mensaje("Error al crear el elemento"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!incisoService.activo(id))
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso no encontrado"), HttpStatus.NOT_FOUND);

        Inciso inciso = incisoService.findById(id).get();
        inciso.setActivo(false);
        incisoService.save(inciso);
        return new ResponseEntity<Mensaje>(new Mensaje("Inciso  eliminado"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!incisoService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso no encontrado"), HttpStatus.NOT_FOUND);
        incisoService.deleteById(id);
        return new ResponseEntity<Mensaje>(new Mensaje("Inciso  eliminado FISICAMENTE"), HttpStatus.OK);
    }
}
