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

import com.guardias.backend.dto.CargoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Cargo;
import com.guardias.backend.service.CargoService;

@RestController
@RequestMapping("/cargo")
@CrossOrigin(origins = "http://localhost:4200")
public class CargoController {

    @Autowired
    CargoService cargoService;

    @GetMapping("/list")
    public ResponseEntity<List<Cargo>> list() {
        List<Cargo> list = cargoService.findByActivoTrue().get();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Cargo>> listAll() {
        List<Cargo> list = cargoService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Cargo>> getById(@PathVariable("id") Long id) {
        if (!cargoService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Cargo cargo = cargoService.findById(id).get();
        return new ResponseEntity(cargo, HttpStatus.OK);
    }

    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<List<Cargo>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!cargoService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Cargo cargo = cargoService.findByNombre(nombre).get();
        return new ResponseEntity(cargo, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CargoDto cargoDto) {

        ResponseEntity<?> respuestaValidaciones = cargoService.validations(cargoDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Cargo cargo = cargoService.createUpdate(new Cargo(), cargoDto);
            cargoService.save(cargo);
            return new ResponseEntity<>(new Mensaje("Cargo creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CargoDto cargoDto) {
        if (!cargoService.activo(id))
            return new ResponseEntity(new Mensaje("El cargo no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = cargoService.validations(cargoDto, id);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Cargo cargo = cargoService.createUpdate(cargoService.findById(id).get(), cargoDto);
            cargoService.save(cargo);
            return new ResponseEntity(new Mensaje("Cargo actualizado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!cargoService.activo(id))
            return new ResponseEntity(new Mensaje("El cargo no existe"), HttpStatus.NOT_FOUND);

        Cargo cargo = cargoService.findById(id).get();
        cargo.setActivo(false);
        cargoService.save(cargo);
        return new ResponseEntity(new Mensaje("Cargo eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!cargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        cargoService.deleteById(id);
        return new ResponseEntity(new Mensaje("Cargo eliminado FISICAMENTE"), HttpStatus.OK);
    }
}
