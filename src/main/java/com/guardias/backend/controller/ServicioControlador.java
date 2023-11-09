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
import com.guardias.backend.dto.ServicioDto;
import com.guardias.backend.modelo.Servicio;
import com.guardias.backend.service.ServiceServicio;

@RestController
@RequestMapping("/servicio")
@CrossOrigin(origins = "http://localhost:4200")
public class ServicioControlador {

    @Autowired
    ServiceServicio serviceServicio;

    @GetMapping("/lista")
    public ResponseEntity<List<Servicio>> list() {
        List<Servicio> list = serviceServicio.list();
        return new ResponseEntity<List<Servicio>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Servicio> getById(@PathVariable("id") int id) {
        if (!serviceServicio.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el servicio"), HttpStatus.NOT_FOUND);
        Servicio servicio = serviceServicio.getOne(id).get();
        return new ResponseEntity<Servicio>(servicio, HttpStatus.OK);
    }

    @GetMapping("/detaildescripcion/{descripcion}")
    public ResponseEntity<Servicio> getByDescripcion(@PathVariable("descripcion") String descripcion) {
        if (!serviceServicio.existsByDescripcion(descripcion))
            return new ResponseEntity(new Mensaje("no existe el servicio"), HttpStatus.NOT_FOUND);
        Servicio servicio = serviceServicio.getByDescripcion(descripcion).get();
        return new ResponseEntity<Servicio>(servicio, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ServicioDto servicioDto) {
        if (StringUtils.isBlank(servicioDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (serviceServicio.existsByDescripcion(servicioDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("esa descripcion ya existe"),
                    HttpStatus.BAD_REQUEST);
        Servicio servicio = new Servicio(servicioDto.getDescripcion());
        serviceServicio.save(servicio);
        return new ResponseEntity(new Mensaje("servicio creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody ServicioDto servicioDto) {
        if (!serviceServicio.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el servicio"), HttpStatus.NOT_FOUND);

        if (serviceServicio.existsByDescripcion(servicioDto.getDescripcion()) &&
                serviceServicio.getByDescripcion(servicioDto.getDescripcion()).get().getId() == id)
            return new ResponseEntity(new Mensaje("esa descripcion ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(servicioDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"), HttpStatus.BAD_REQUEST);

        Servicio servicio = serviceServicio.getOne(id).get();
        servicio.setDescripcion(servicioDto.getDescripcion());
        serviceServicio.save(servicio);
        return new ResponseEntity(new Mensaje("servicio actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        
        if (!serviceServicio.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el servicio"), HttpStatus.NOT_FOUND);
        serviceServicio.delete(id);
        return new ResponseEntity(new Mensaje("servicio eliminado"), HttpStatus.OK);
    }

}
