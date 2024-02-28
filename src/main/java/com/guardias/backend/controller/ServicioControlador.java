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
import com.guardias.backend.entity.Servicio;
import com.guardias.backend.service.ServiceService;

@RestController
@RequestMapping("/servicio")
@CrossOrigin(origins = "http://localhost:4200")
public class ServicioControlador {

    @Autowired
    ServiceService serviceServicio;

    @GetMapping("/list")
    public ResponseEntity<List<Servicio>> list() {
        List<Servicio> list = serviceServicio.findByActivo(true);
        return new ResponseEntity<List<Servicio>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Servicio>> listAll() {
        List<Servicio> list = serviceServicio.findAll();
        return new ResponseEntity<List<Servicio>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Servicio> getById(@PathVariable("id") Long id) {
        if (!serviceServicio.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el servicio"), HttpStatus.NOT_FOUND);
        Servicio servicio = serviceServicio.findById(id).get();
        return new ResponseEntity<Servicio>(servicio, HttpStatus.OK);
    }

    // @GetMapping("/detaildescripcion/{descripcion}")
    // public ResponseEntity<?> getByDescripcion(@PathVariable("descripcion") String
    // descripcion) {
    // try {
    // Servicio servicio = serviceServicio.getByDescripcion(descripcion).get();
    // return new ResponseEntity<>(servicio, HttpStatus.OK);
    // } catch (NoSuchElementException e) {
    // return new ResponseEntity<>(new Mensaje("No existe el servicio"),
    // HttpStatus.NOT_FOUND);
    // }
    // }

    @GetMapping("/detaildescripcion/{descripcion}")
    public ResponseEntity<Servicio> getByDescripcion(@PathVariable("descripcion") String descripcion) {
        if (!serviceServicio.existsByDescripcion(descripcion))
            return new ResponseEntity(new Mensaje("no existe el servicio"),
                    HttpStatus.NOT_FOUND);
        Servicio servicio = serviceServicio.findByDescripcion(descripcion).get();
        return new ResponseEntity<Servicio>(servicio, HttpStatus.OK);
    }

    @GetMapping("/detailnivel/{nivel}")
    public ResponseEntity<List<Servicio>> getByNivel(@PathVariable("nivel") int nivel) {
        List<Servicio> servicios = serviceServicio.findByNivel(nivel);
        if (!serviceServicio.existsByNivel(nivel))
            return new ResponseEntity(new Mensaje("no existe el nivel"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(servicios, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ServicioDto servicioDto) {
        if (StringUtils.isBlank(servicioDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (serviceServicio.existsByDescripcion(servicioDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("esa descripcion ya existe"),
                    HttpStatus.BAD_REQUEST);
        if (servicioDto.getNivel() <= 0)
            return new ResponseEntity(new Mensaje("el nivel debe ser mayor que 0"),
                    HttpStatus.BAD_REQUEST);
        Servicio servicio = new Servicio();
        servicio.setDescripcion(servicioDto.getDescripcion());
        servicio.setNivel(servicioDto.getNivel());
        serviceServicio.save(servicio);
        return new ResponseEntity(new Mensaje("servicio creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ServicioDto servicioDto) {
        if (!serviceServicio.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el servicio"), HttpStatus.NOT_FOUND);

        if (serviceServicio.existsByDescripcion(servicioDto.getDescripcion()) &&
                serviceServicio.findByDescripcion(servicioDto.getDescripcion()).get().getId() == id)
            return new ResponseEntity(new Mensaje("esa descripcion ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(servicioDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"), HttpStatus.BAD_REQUEST);

        if (servicioDto.getNivel() <= 0)
            return new ResponseEntity(new Mensaje("el nivel debe ser mayor que 0"), HttpStatus.BAD_REQUEST);

        Servicio servicio = serviceServicio.findById(id).get();
        if (servicio.getDescripcion() != servicioDto.getDescripcion() && servicioDto.getDescripcion() != null
                && !servicioDto.getDescripcion().isEmpty())
            servicio.setDescripcion(servicioDto.getDescripcion());
        servicio.setNivel(servicioDto.getNivel());
        serviceServicio.save(servicio);
        return new ResponseEntity(new Mensaje("servicio actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!serviceServicio.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el servicio"), HttpStatus.NOT_FOUND);
        serviceServicio.deleteById(id);
        return new ResponseEntity(new Mensaje("servicio eliminado"), HttpStatus.OK);
    }

}
