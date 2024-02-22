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
import com.guardias.backend.dto.LocalidadDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Localidad;
import com.guardias.backend.service.LocalidadService;
import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/localidad")
@CrossOrigin(origins = "http://localhost:4200")
public class LocalidadController {

    @Autowired
    LocalidadService localidadService;

    @GetMapping("/lista")
    public ResponseEntity<List<Localidad>> list() {
        List<Localidad> list = localidadService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Localidad>> getById(@PathVariable("id") Long id) {
        if (!localidadService.existsById(id))
            return new ResponseEntity(new Mensaje("localidad no existe"), HttpStatus.NOT_FOUND);
        Localidad localidad = localidadService.getById(id).get();
        return new ResponseEntity(localidad, HttpStatus.OK);
    }

    @GetMapping("/detallenombre/{nombre}")
    public ResponseEntity<List<Localidad>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!localidadService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("localidad no existe"), HttpStatus.NOT_FOUND);
        Localidad localidad = localidadService.getByNombre(nombre).get();
        return new ResponseEntity(localidad, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LocalidadDto localidadDto) {
        if (StringUtils.isBlank(localidadDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (localidadService.existsByNombre(localidadDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (localidadDto.getDepartamento() == null)
            return new ResponseEntity(new Mensaje("el departamento es obligatorio"),
                            HttpStatus.BAD_REQUEST); 

        Localidad localidad = new Localidad();
        localidad.setNombre(localidadDto.getNombre());
        localidad.setDepartamento(localidadDto.getDepartamento());

        localidadService.save(localidad);
        return new ResponseEntity(new Mensaje("Localidad creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LocalidadDto localidadDto) {
        if (StringUtils.isBlank(localidadDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (localidadService.existsByNombre(localidadDto.getNombre()) &&
            localidadService.getByNombre(localidadDto.getNombre()).get().getId() != id)
        return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (localidadDto.getDepartamento() == null)
            return new ResponseEntity(new Mensaje("el departamento es obligatorio"),
                            HttpStatus.BAD_REQUEST); 

        Localidad localidad = localidadService.getById(id).get();
        //******* La validacion antes de setear los valores me gusta que sea en la misma linea pero no muestra mensajes de error

        //******* Ahora está mostrando los msjs de error por la validacion previa, ver como queda para limpiar el codigo  */
        
        if (!localidadDto.getNombre().equals(localidad.getNombre()))
            localidad.setNombre(localidadDto.getNombre());
        if (!localidadDto.getDepartamento().equals(localidad.getDepartamento()))
            localidad.setDepartamento(localidadDto.getDepartamento());

        localidadService.save(localidad);
        return new ResponseEntity(new Mensaje("Localidad modificada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!localidadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la localidad"), HttpStatus.NOT_FOUND);
        localidadService.deleteById(id);
        return new ResponseEntity(new Mensaje("localidad eliminada"), HttpStatus.OK);
    }
}
