package com.guardias.backend.controller;

import java.util.List;

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
import com.guardias.backend.dto.ProvinciaDTO;
import com.guardias.backend.entity.Provincia;
import com.guardias.backend.service.ProvinciaService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/provincia")
@CrossOrigin(origins = "http://localhost:4200")
public class ProvinciaController {

    @Autowired
    ProvinciaService provinciaService;

    @GetMapping("/lista")
    public ResponseEntity<List<Provincia>> list() {
        List<Provincia> list = provinciaService.list();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Provincia> getById(@PathVariable("id") Long id) {
        if (!provinciaService.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        Provincia provincia = provinciaService.getById(id).get();
        return ResponseEntity.ok(provincia);
    }

    @GetMapping("/detalle/{nombre}")
    public ResponseEntity<Provincia> getByNombre(@PathVariable("nombre") String nombre) {
        if (!provinciaService.existsByNombre(nombre))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        Provincia provincia = provinciaService.getByNombre(nombre).get();
        return ResponseEntity.ok(provincia);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProvinciaDTO provinciaDto) {
        if (StringUtils.isBlank(provinciaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        Provincia provincia = new Provincia();
        provincia.setNombre(provinciaDto.getNombre());

        provincia.setGentilicio(provinciaDto.getGentilicio());
        provincia.setPais(provinciaDto.getPais());
        provincia.setDepartamento(provinciaDto.getDepartamento());

        provinciaService.save(provincia);
        return ResponseEntity.ok(new Mensaje("Provincia creada"));
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ProvinciaDTO provinciaDto) {

        if (StringUtils.isBlank(provinciaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        Provincia provincia = provinciaService.getById(id).get();

        if (!provinciaDto.getNombre().equals(provincia.getNombre()))
            provincia.setNombre(provinciaDto.getNombre());

        if (!provinciaDto.getGentilicio().equals(provincia.getGentilicio()))
            provincia.setGentilicio(provinciaDto.getGentilicio());

        if (!provinciaDto.getPais().equals(provincia.getPais()))
            provincia.setPais(provinciaDto.getPais());

        if (!provinciaDto.getDepartamento().equals(provincia.getDepartamento()))
            provincia.setDepartamento(provinciaDto.getDepartamento());

        provinciaService.save(provincia);
        return ResponseEntity.ok(new Mensaje("Provincia modificada"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!provinciaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el provincia"), HttpStatus.NOT_FOUND);
        provinciaService.deleteById(id);
        return ResponseEntity.ok(new Mensaje("Provincia eliminada"));
    }
}
