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

    @GetMapping("/list")
    public ResponseEntity<List<TipoGuardia>> list() {
        List<TipoGuardia> list = tipoGuardiaService.list();
        return new ResponseEntity<List<TipoGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TipoGuardia> getById(@PathVariable("id") Long id) {
        if (!tipoGuardiaService.activo(id))
            return new ResponseEntity(new Mensaje("No existe el tipo de guardia"), HttpStatus.NOT_FOUND);
        TipoGuardia tipoGuardia = tipoGuardiaService.findById(id).get();
        return new ResponseEntity<TipoGuardia>(tipoGuardia, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<TipoGuardia> getByNombre(@PathVariable("nombre") String nombre) {
        if (!tipoGuardiaService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe el tipo de guardia"), HttpStatus.NOT_FOUND);
        TipoGuardia tipoGuardia = tipoGuardiaService.findByNombre(nombre).get();
        return new ResponseEntity<TipoGuardia>(tipoGuardia, HttpStatus.OK);
    }

    @GetMapping("/detaildescripcion/{descripcion}")
    public ResponseEntity<TipoGuardia> getByDescripcion(@PathVariable("descripcion") String descripcion) {
        if (!tipoGuardiaService.activoByDescripcion(descripcion))
            return new ResponseEntity(new Mensaje("no existe el tipo de guardia"), HttpStatus.NOT_FOUND);
        TipoGuardia tipoGuardia = tipoGuardiaService.findByDescripcion(descripcion).get();
        return new ResponseEntity<TipoGuardia>(tipoGuardia, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(TipoGuardiaDto tipoGuardiaDto) {
        if (StringUtils.isBlank(tipoGuardiaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private TipoGuardia createUpdate(TipoGuardia tipoGuardia, TipoGuardiaDto tipoGuardiaDto) {

        if (tipoGuardia.getNombre() != tipoGuardiaDto.getNombre() && tipoGuardiaDto.getNombre() != null
                && !tipoGuardiaDto.getNombre().isEmpty())
            tipoGuardia.setNombre(tipoGuardiaDto.getNombre());

        if (tipoGuardia.getDescripcion() != tipoGuardiaDto.getDescripcion() && tipoGuardiaDto.getDescripcion() != null
                && !tipoGuardiaDto.getDescripcion().isEmpty())
            tipoGuardia.setDescripcion(tipoGuardiaDto.getDescripcion());

        tipoGuardia.setActivo(true);

        return tipoGuardia;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoGuardiaDto tipoGuardiaDto) {

        ResponseEntity<?> respuestaValidaciones = validations(tipoGuardiaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            TipoGuardia tipoGuardia = createUpdate(new TipoGuardia(), tipoGuardiaDto);
            tipoGuardiaService.save(tipoGuardia);
            return new ResponseEntity(new Mensaje("tipo de guardia creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TipoGuardiaDto tipoGuardiaDto) {
        if (!tipoGuardiaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el tipo de guardia"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(tipoGuardiaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            TipoGuardia tipoGuardia = createUpdate(tipoGuardiaService.findById(id).get(), tipoGuardiaDto);
            tipoGuardiaService.save(tipoGuardia);
            return new ResponseEntity(new Mensaje("tipo de guardia modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!tipoGuardiaService.activo(id))
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