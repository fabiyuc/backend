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
import com.guardias.backend.dto.PaisDto;
import com.guardias.backend.entity.Pais;
import com.guardias.backend.service.PaisService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/pais")
@CrossOrigin(origins = "http://localhost:4200")
public class PaisController {

    @Autowired
    PaisService paisService;

    @GetMapping("/lista")
    public ResponseEntity<List<Pais>> list() {
        List<Pais> list = paisService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Pais>> getById(@PathVariable("id") Long id) {
        if (!paisService.existsById(id))
            return new ResponseEntity(new Mensaje("pais no existe"), HttpStatus.NOT_FOUND);
        Pais pais = paisService.getById(id).get();
        return new ResponseEntity(pais, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PaisDto paisDto) {
        if (StringUtils.isBlank(paisDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (paisService.existsByNombre(paisDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);
        Pais pais = new Pais();
        pais.setCodigo(paisDto.getCodigo());
        pais.setNacionalidad(paisDto.getNacionalidad());
        pais.setNombre(paisDto.getNacionalidad());
        paisService.save(pais);
        return new ResponseEntity(new Mensaje("tipo de revista creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody PaisDto paisDto) {
        if (!paisService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el pais"), HttpStatus.NOT_FOUND);

        if (paisService.existsByNombre(paisDto.getNombre()) &&
                paisService.getPaisByNombre(paisDto.getNombre()).get().getId() == id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(paisDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        Pais pais = paisService.getById(id).get();
        if (pais.getNombre() != paisDto.getNombre() && paisDto.getNombre() != null && !paisDto.getNombre().isEmpty())
            pais.setNombre(paisDto.getNombre());

        if (pais.getCodigo() != paisDto.getCodigo() && paisDto.getCodigo() != null && !paisDto.getCodigo().isEmpty())
            pais.setCodigo(paisDto.getCodigo());

        if (pais.getNacionalidad() != paisDto.getNacionalidad() && paisDto.getNacionalidad() != null
                && !paisDto.getNacionalidad().isEmpty())
            pais.setNacionalidad(paisDto.getNacionalidad());

        paisService.save(pais);
        return new ResponseEntity(new Mensaje("pais actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!paisService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el pais"), HttpStatus.NOT_FOUND);
        paisService.deleteById(id);
        return new ResponseEntity(new Mensaje("pais eliminado"), HttpStatus.OK);
    }

}