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
import com.guardias.backend.dto.RevistaDto;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.service.RevistaService;

@RestController
@RequestMapping("/revista")
@CrossOrigin(origins = "http://localhost:4200")
public class RevistaController {

    @Autowired
    RevistaService revistaService;

    @GetMapping("/lista")
    public ResponseEntity<List<Revista>> list() {
        List<Revista> list = revistaService.list();
        return new ResponseEntity<List<Revista>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Revista> getById(@PathVariable("id") Long id) {
        if (!revistaService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la situacion de revista"), HttpStatus.NOT_FOUND);
        Revista revista = revistaService.getOne(id).get();
        return new ResponseEntity<Revista>(revista, HttpStatus.OK);
    }

    @GetMapping("/detail/{nombre}")
    public ResponseEntity<Revista> getByNombre(@PathVariable("nombre") String nombre) {
        if (!revistaService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("No existe la situacion de revista"), HttpStatus.NOT_FOUND);
        Revista revista = revistaService.getByNombre(nombre).get();
        return new ResponseEntity<Revista>(revista, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RevistaDto revistaDto) {
        if (revistaDto.getTipoRevista()==null) {
            return new ResponseEntity(new Mensaje("el tipo de revista es obligatorio"),
                    HttpStatus.BAD_REQUEST);

                    if (revistaDto.getCargaHoraria()==null) {
            return new ResponseEntity(new Mensaje("la carga horaria es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        Revista revista = new Revista();
        revista.setTipoRevista(revistaDto.getTipoRevista());
        revista.setCargaHoraria(revistaDto.getCargaHoraria());
        revista.setAdicional(revistaDto.getAdicional());
        revista.setLegajos(revistaDto.getLegajos());

        revistaService.save(revista);
        return new ResponseEntity(new Mensaje("Revista creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody RevistaDto revistaDto) {
        if (revistaDto.getTipoRevista()==null) {
            return new ResponseEntity(new Mensaje("el tipo de revista es obligatorio"),
                    HttpStatus.BAD_REQUEST);

                    if (revistaDto.getCargaHoraria()==null) {
            return new ResponseEntity(new Mensaje("la carga horaria es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        Revista revista = revistaService.getOne(id).get();

        if (!revistaDto.getTipoRevista().equals(revista.getTipoRevista()))
        revista.setTipoRevista(revistaDto.getTipoRevista());

        if (!revistaDto.getCargaHoraria().equals(revista.getCargaHoraria()))
        revista.setCargaHoraria(revistaDto.getCargaHoraria());

        if (!revistaDto.getAdicional().equals(revista.getAdicional()))
        revista.setAdicional(revistaDto.getAdicional());

        if (!revistaDto.getLegajos().equals(revista.getLegajos()))
        revista.setLegajos(revistaDto.getLegajos());

        revistaService.save(revista);
        return new ResponseEntity(new Mensaje("Revista modificada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!revistaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el revista"), HttpStatus.NOT_FOUND);
        revistaService.deleteById(id);
        return new ResponseEntity(new Mensaje("revista eliminado"), HttpStatus.OK);
    }

}
