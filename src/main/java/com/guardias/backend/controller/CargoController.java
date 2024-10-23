package com.guardias.backend.controller;

import java.util.ArrayList;
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
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.service.CargoService;
import com.guardias.backend.service.LegajoService;

@RestController
@RequestMapping("/cargo")
@CrossOrigin(origins = "http://localhost:4200")
public class CargoController {

    @Autowired
    CargoService cargoService;

    @Autowired
    LegajoService legajoService;

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

    private ResponseEntity<?> validations(CargoDto cargoDto, Long id) {
        if (cargoDto.getNombre() == null)
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getDescripcion() == null)
            return new ResponseEntity(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getNroresolucion() == null)
            return new ResponseEntity(new Mensaje("El número de resolución es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getNrodecreto() == null)
            return new ResponseEntity(new Mensaje("El número de decreto es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getFechaResolucion() == null)
            return new ResponseEntity(new Mensaje("Fecha de resolución obligatoria"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("Fecha de inicio obligatoria"), HttpStatus.BAD_REQUEST);

        if (cargoDto.getAgrupacion() == null)
            return new ResponseEntity(new Mensaje("Agrupación obligatoria"), HttpStatus.BAD_REQUEST);

        if (cargoService.activoByNombre(cargoDto.getNombre())
                && (cargoService.findByNombre(cargoDto.getNombre()).get().getId() != id))
            return new ResponseEntity<>(new Mensaje("Ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    private Cargo createUpdate(Cargo cargo, CargoDto cargoDto) {

        if (cargoDto.getNombre() != null && !cargoDto.getNombre().isEmpty()
                && !cargoDto.getNombre().equals(cargo.getNombre()))
            cargo.setNombre(cargoDto.getNombre());

        if (cargoDto.getDescripcion() != null && !cargoDto.getDescripcion().isEmpty()
                && !cargoDto.getDescripcion().equals(cargo.getDescripcion()))
            cargo.setDescripcion(cargoDto.getDescripcion());

        if (cargoDto.getNroresolucion() != null && cargo.getNroresolucion() != cargoDto.getNroresolucion()
                && !cargoDto.getNroresolucion().isEmpty())
            cargo.setNroresolucion(cargoDto.getNroresolucion());
        if (cargoDto.getNrodecreto() != null && cargo.getNrodecreto() != cargoDto.getNrodecreto())
            cargo.setNrodecreto(cargoDto.getNrodecreto());

        if (cargoDto.getFechaResolucion() != null && cargo.getFechaResolucion() != cargoDto.getFechaResolucion())
            cargo.setFechaResolucion(cargoDto.getFechaResolucion());

        if (cargoDto.getFechaInicio() != null && cargo.getFechaInicio() != cargoDto.getFechaInicio())
            cargo.setFechaInicio(cargoDto.getFechaInicio());

        if (cargoDto.getFechaFinal() != null
                && cargo.getFechaFinal() != cargoDto.getFechaFinal())
            cargo.setFechaFinal(cargoDto.getFechaFinal());

        if (cargoDto.getAgrupacion() != null && cargo.getAgrupacion() != cargoDto.getAgrupacion())
            cargo.setAgrupacion(cargoDto.getAgrupacion());

        if (cargoDto.getIdLegajos() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (cargo.getLegajos() != null) {
                for (Legajo legajo : cargo.getLegajos()) {
                    for (Long id : cargoDto.getIdLegajos()) {
                        if (!legajo.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }
            List<Long> idsToAdd = idList.isEmpty() ? cargoDto.getIdLegajos() : idList;
            for (Long id : idsToAdd) {
                cargo.getLegajos().add(legajoService.findById(id).get());
                legajoService.findById(id).get().setCargo(cargo);
            }
        }

        cargo.setActivo(true);
        return cargo;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CargoDto cargoDto) {

        ResponseEntity<?> respuestaValidaciones = validations(cargoDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Cargo cargo = createUpdate(new Cargo(), cargoDto);
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

        ResponseEntity<?> respuestaValidaciones = validations(cargoDto, id);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Cargo cargo = createUpdate(cargoService.findById(id).get(), cargoDto);
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
