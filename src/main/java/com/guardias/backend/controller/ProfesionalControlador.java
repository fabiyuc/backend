package com.guardias.backend.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ProfesionalDto;
import com.guardias.backend.modelo.Profesional;
import com.guardias.backend.service.ProfesionalServicio;

@RestController
@RequestMapping("/profesional")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfesionalControlador {
    
    @Autowired
    ProfesionalServicio profesionalServicio;

    @GetMapping("/lista")
    public ResponseEntity<List<Profesional>> list() {
        List<Profesional> list = profesionalServicio.list();
        return new ResponseEntity<List<Profesional>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Profesional> getById(@PathVariable("id") Long id) {
        if (!profesionalServicio.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el profesional"), HttpStatus.NOT_FOUND);
        Profesional profesional = profesionalServicio.getOne(id).get();
        return new ResponseEntity<Profesional>(profesional, HttpStatus.OK);
    }

    @GetMapping("/detaildni/{dni}")
    public ResponseEntity<Profesional> getByDni(@PathVariable("dni") int dni) {
        if (profesionalServicio.existsByDni(dni))
            return new ResponseEntity(new Mensaje("no existe el profesional"), HttpStatus.NOT_FOUND);
        Profesional profesional = profesionalServicio.getByDni(dni).get();
        return new ResponseEntity<Profesional>(profesional, HttpStatus.OK);
    }

   /* FALTA ADAPTAR EL METODO A LO QUE SE NECESITA
     @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProfesionalDto profesionalDto) {
        if (StringUtils.isBlank(profesionalDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (profesionalServicio.existsByNombre(profesionalDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);
        Profesional servicio = new Profesional(profesionalDto.getDescripcion());
        profesionalServicio.save(profesional);
        return new ResponseEntity(new Mensaje("servicio creado"), HttpStatus.OK);
    } */

    /* @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody ServicioDto servicioDto) {
        if (!profesionalServicio.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el servicio"), HttpStatus.NOT_FOUND);

        if (profesionalServicio.existsByDescripcion(servicioDto.getDescripcion()) &&
                profesionalServicio.getByDescripcion(servicioDto.getDescripcion()).get().getId() != id)
            return new ResponseEntity(new Mensaje("esa descripcion ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(servicioDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"), HttpStatus.BAD_REQUEST);

        Profesional profesional = profesionalServicio.getOne(id).get();
        profesional.setDescripcion(servicioDto.getDescripcion());
        profesionalServicio.save(profesional);
        return new ResponseEntity(new Mensaje("servicio actualizado"), HttpStatus.OK);
    } */

   /*  @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        
        if (!profesionalServicio.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el servicio"), HttpStatus.NOT_FOUND);
        profesionalServicio.delete(id);
        return new ResponseEntity(new Mensaje("servicio eliminado"), HttpStatus.OK);
    } */

}