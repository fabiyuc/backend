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
import com.guardias.backend.dto.PaisDto;
import com.guardias.backend.entity.Pais;
import com.guardias.backend.service.PaisService;

@RestController
@RequestMapping("/pais")
@CrossOrigin(origins = "http://localhost:4200")
public class PaisController {
    
    @Autowired
    PaisService paisService;

    @GetMapping("/lista")
    public ResponseEntity<List<Pais>> list() {
        List<Pais> list = paisService.list();
        return new ResponseEntity<List<Pais>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Pais> getById(@PathVariable("id") int id) {
        if (!paisService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el tipo de revista"), HttpStatus.NOT_FOUND);
        Pais tipoRevista = paisService.getOne(id).get();
        return new ResponseEntity<Pais>(pais, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Pais> getByNombre(@PathVariable("nombre") String nombre) {
        if (!paisService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe el tipo de revista"), HttpStatus.NOT_FOUND);
        Pais pais = paisService.getByNombre(nombre).get();
        return new ResponseEntity<Pais>(pais, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PaisDto PaisDto) {
        if (StringUtils.isBlank(PaisDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (paisService.existsByNombre(PaisDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);
        Pais tipoRevista = new Pais(PaisDto.getNombre());
        paisService.save(pais);
        return new ResponseEntity(new Mensaje("tipo de revista creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody PaisDto PaisDto) {
        if (!paisService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el tipo de revista"), HttpStatus.NOT_FOUND);

        if (paisService.existsByNombre(PaisDto.getNombre()) &&
                paisService.getByNombre(PaisDto.getNombre()).get().getId() == id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(PaisDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        Pais pais = paisService.getOne(id).get();
        pais.setNombre(PaisDto.getNombre());
        paisService.save(pais);
        return new ResponseEntity(new Mensaje("Tipo de servicio actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        
        if (!paisService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el tipo de revista"), HttpStatus.NOT_FOUND);
        paisService.delete(id);
        return new ResponseEntity(new Mensaje("tipo de revista eliminado"), HttpStatus.OK);
    }

}