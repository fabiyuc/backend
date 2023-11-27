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

    @GetMapping("/lista")
    public ResponseEntity<List<Cargo>> list() {
        List<Cargo> list = cargoService.list();
        return new ResponseEntity<List<Cargo>>(list, HttpStatus.OK);

    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Cargo> getById(@PathVariable("id") Long id) {
        if (!cargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Cargo cargo = cargoService.getone(id).get();
        return new ResponseEntity<Cargo>(cargo, HttpStatus.OK);
    }

    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<Cargo> getByNombre(@PathVariable("nombre") String nombre) {
        if (!cargoService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Cargo cargo = cargoService.getByNombre(nombre).get();
        return new ResponseEntity<Cargo>(cargo, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CargoDto cargoDto) {
        // Validaciones
        if (StringUtils.isBlank(cargoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cargoService.existsById(cargoDto.getId()))
            return new ResponseEntity(new Mensaje("El ID ya existe"), HttpStatus.BAD_REQUEST);

        if (cargoService.existsByNombre(cargoDto.getNombre()))
            return new ResponseEntity(new Mensaje("Ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getDescripcion() == null)
            return new ResponseEntity(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getNroresolucion() == null)
            return new ResponseEntity(new Mensaje("El número de resolución es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getNrodecreto() == null)
            return new ResponseEntity(new Mensaje("El número de decreto es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getFecharesolucion() == null)
            return new ResponseEntity(new Mensaje("Fecha de resolución obligatoria"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getFechainicio() == null)
            return new ResponseEntity(new Mensaje("Fecha de inicio obligatoria"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getFechafinal() == null)
            return new ResponseEntity(new Mensaje("Fecha final obligatoria"), HttpStatus.BAD_REQUEST);

        Cargo cargo = new Cargo();
        cargo.setId(cargoDto.getId());
        cargo.setNombre(cargoDto.getNombre());
        cargo.setDescripcion(cargoDto.getDescripcion());
        cargo.setNroresolucion(cargoDto.getNroresolucion());
        cargo.setNrodecreto(cargoDto.getNrodecreto());
        cargo.setFecharesolucion(cargoDto.getFecharesolucion());
        cargo.setFechainicio(cargoDto.getFechainicio());
        cargo.setFechafinal(cargoDto.getFechafinal());

        cargoService.save(cargo);

        return new ResponseEntity<>(new Mensaje("Cargo creado"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody CargoDto cargoDto) {
        if (!cargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        if (cargoService.existsByNombre(cargoDto.getNombre())
                && cargoService.getByNombre(cargoDto.getNombre()).get().getId() != id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(cargoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cargoService.existsById(cargoDto.getId()) && cargoDto.getId() != id)
            return new ResponseEntity(new Mensaje("El id ya existe"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getDescripcion() == null)
            return new ResponseEntity(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getNroresolucion() == null)
            return new ResponseEntity(new Mensaje("El número de resolución es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getNrodecreto() == null)
            return new ResponseEntity(new Mensaje("El número de decreto es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getFecharesolucion() == null)
            return new ResponseEntity(new Mensaje("Fecha de resolución obligatoria"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getFechainicio() == null)
            return new ResponseEntity(new Mensaje("Fecha de inicio obligatoria"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getFechafinal() == null)
            return new ResponseEntity(new Mensaje("Fecha final obligatoria"), HttpStatus.BAD_REQUEST);

        Cargo cargo = cargoService.getone(id).get();
        cargo.setNombre(cargoDto.getNombre());
        cargo.setId(cargoDto.getId());
        cargo.setDescripcion(cargoDto.getDescripcion());
        cargo.setNroresolucion(cargoDto.getNroresolucion());
        cargo.setNrodecreto(cargoDto.getNrodecreto());
        cargo.setFecharesolucion(cargoDto.getFecharesolucion());
        cargo.setFechainicio(cargoDto.getFechainicio());
        cargo.setFechafinal(cargoDto.getFechafinal());
        cargoService.save(cargo);

        return new ResponseEntity<>(new Mensaje("Cargo Actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!cargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        cargoService.delete(id);
        return new ResponseEntity<>(new Mensaje("Cargo eliminado"), HttpStatus.OK);

    }
}
