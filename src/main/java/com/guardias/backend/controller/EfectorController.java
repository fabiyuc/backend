package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guardias.backend.dto.EfectorDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.service.EfectorService;

import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/efector")
@CrossOrigin(origins = "http://localhost:4200")
public class EfectorController {

    @Autowired
    EfectorService efectorService;

    @GetMapping("/lista")
    public ResponseEntity<List<Efector>> list() {
        List<Efector> list = efectorService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Efector>> getEfectorById(@PathVariable("id") Long id){
        if(!efectorService.existById(id))
        return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND)
        Efector efector = efectorService.getById(id).get();
        return new ResponseEntity(efector, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Efector>> getById(@PathVariable("id") Long id) {
        if (!efectorService.existById(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Efector efector = efectorService.getById(id).get();
        return new ResponseEntity(efector, HttpStatus.OK);
    }

    @GetMapping("/detalle/{nombre}")
    public ResponseEntity<List<Efector>> getById(@PathVariable("nomrbre") String nombre) {
        if (!efectorService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Efector efector = efectorService.getEfectorByNombre(nombre).get();
        return new ResponseEntity(efector, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EfectorDto efectorDto) {
        if (StringUtils.isBlank(efectorDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(efectorDto.getDomicilio()))
            return new ResponseEntity(new Mensaje("el domicilio es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (efectorService.existsByNombre(efectorDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);

        Efector efector = new Efector();
        efector.setNombre(efectorDto.getNombre());
        efector.setDomicilio(efectorDto.getDomicilio());
        efector.setTelefono(efectorDto.getTelefono());
        efector.setEstado(efectorDto.isEstado());
        efector.setIdLocalidad(efectorDto.getIdLocalidad());
        efector.setObservacion(efectorDto.getObservacion());
        efector.setIdRegion(efectorDto.getIdRegion());

        efectorService.save(efector);
        return new ResponseEntity(new Mensaje("Efector creado correctamente"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody EfectorDto efectorDto) {
        if (!efectorService.existById(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        // if (efectorService.existsByNombre(efectorDto.getNombre()) &&
        // efectorService.getEfectorByNombre(efectorDto.getNombre()).get().getIdEfector()
        // == id)
        // return new ResponseEntity(new Mensaje("ese efector ya existe"),
        // HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(efectorDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        Efector efector = efectorService.getById(id).get();
        efector.setNombre(efectorDto.getNombre());
        efector.setDomicilio(efectorDto.getDomicilio());
        efector.setTelefono(efectorDto.getTelefono());
        efector.setEstado(efectorDto.isEstado());
        efector.setIdLocalidad(efectorDto.getIdLocalidad());
        efector.setObservacion(efectorDto.getObservacion());
        efector.setIdRegion(efectorDto.getIdRegion());

        efectorService.save(efector);
        return new ResponseEntity(new Mensaje("Hospital actualizado"), HttpStatus.OK);
    }

}
