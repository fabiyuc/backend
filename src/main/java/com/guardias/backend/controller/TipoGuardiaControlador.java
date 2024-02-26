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
import com.guardias.backend.dto.TipoGuardiaDto;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.service.TipoGuardiaService;

@RestController
@RequestMapping("/tipoGuardia")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoGuardiaControlador {
    @Autowired
    TipoGuardiaService tipoGuardiaService;

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<TipoGuardia> getByNombre(@PathVariable("nombre") String nombre) {
        if (!tipoGuardiaService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe el tipo de guardia"), HttpStatus.NOT_FOUND);
        TipoGuardia tipoGuardia = tipoGuardiaService.findByNombre(nombre).get();
        return new ResponseEntity<TipoGuardia>(tipoGuardia, HttpStatus.OK);
    }

    @GetMapping("/detaildescripcion/{descripcion}")
    public ResponseEntity<TipoGuardia> getByDescripcion(@PathVariable("descripcion") String descripcion) {
        if (!tipoGuardiaService.existsByDescripcion(descripcion))
            return new ResponseEntity(new Mensaje("no existe el tipo de guardia"), HttpStatus.NOT_FOUND);
        TipoGuardia tipoGuardia = tipoGuardiaService.findByDescripcion(descripcion).get();
        return new ResponseEntity<TipoGuardia>(tipoGuardia, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<TipoGuardia>> list() {
        List<TipoGuardia> list = tipoGuardiaService.list();
        return new ResponseEntity<List<TipoGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TipoGuardia> getById(@PathVariable("id") Long id) {
        if (!tipoGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el tipo de guardia"), HttpStatus.NOT_FOUND);
        TipoGuardia tipoGuardia = tipoGuardiaService.findById(id).get();
        return new ResponseEntity<TipoGuardia>(tipoGuardia, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoGuardiaDto tipoGuardiaDto) {
        if (StringUtils.isBlank(tipoGuardiaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (tipoGuardiaService.existsByNombre(tipoGuardiaDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);

        if (tipoGuardiaDto.getTipoGuardia() == null)
            return new ResponseEntity<>(new Mensaje("El Tipo de Guardia es obligatorio"), HttpStatus.BAD_REQUEST);

        TipoGuardia tipoGuardia = new TipoGuardia();
        tipoGuardia.setNombre(tipoGuardiaDto.getNombre());
        tipoGuardia.setDescripcion(tipoGuardiaDto.getDescripcion());
        tipoGuardiaService.save(tipoGuardia);
        return new ResponseEntity(new Mensaje("tipo de guardia creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TipoGuardiaDto tipoGuardiaDto) {
        if (!tipoGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el tipo de guardia"), HttpStatus.NOT_FOUND);

        if (tipoGuardiaService.existsByNombre(tipoGuardiaDto.getNombre()) &&
                tipoGuardiaService.findByNombre(tipoGuardiaDto.getNombre()).get().getId() == id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(tipoGuardiaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (tipoGuardiaDto.getTipoGuardia() == null)
            return new ResponseEntity<>(new Mensaje("El Tipo de Guardia es obligatorio"), HttpStatus.BAD_REQUEST);

        TipoGuardia tipoGuardia = tipoGuardiaService.findById(id).get();
        tipoGuardia.setNombre(tipoGuardiaDto.getNombre());
        tipoGuardia.setDescripcion(tipoGuardiaDto.getDescripcion());
        tipoGuardiaService.save(tipoGuardia);
        return new ResponseEntity(new Mensaje("servicio actualizado"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!tipoGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        TipoGuardia tipoGuardia = tipoGuardiaService.findById(id).get();
        tipoGuardia.setActivo(false);
        tipoGuardiaService.save(tipoGuardia);
        return new ResponseEntity<>(new Mensaje("Tipo Guardia eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!tipoGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        tipoGuardiaService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Tipo Guardia eliminado FISICAMENTE"), HttpStatus.OK);
    }

}