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

    @GetMapping("/list")
    public ResponseEntity<List<Cargo>> list() {
        List<Cargo> list = cargoService.findByActivo(true);
        return new ResponseEntity<List<Cargo>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Cargo>> listAll() {
        List<Cargo> list = cargoService.findAll();
        return new ResponseEntity<List<Cargo>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Cargo>> getById(@PathVariable("id") Long id) {
        if (!cargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Cargo cargo = cargoService.findById(id).get();
        return new ResponseEntity(cargo, HttpStatus.OK);
    }

    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<List<Cargo>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!cargoService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Cargo cargo = cargoService.getByNombre(nombre).get();
        return new ResponseEntity(cargo, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(Long id, CargoDto cargoDto) {
        if (StringUtils.isBlank(cargoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isNotBlank(cargoDto.getNombre()))
            if (cargoService.existsByNombre(cargoDto.getNombre())
                    && cargoService.getByNombre(cargoDto.getNombre()).get().getId() != id)
                return new ResponseEntity<>(new Mensaje("Ese nombre ya existe"), HttpStatus.BAD_REQUEST);

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

        if (cargoDto.getAgrupacion() == null)
            return new ResponseEntity(new Mensaje("Agrupación obligatoria"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    // PREGUNTAR LAS VALIDACIONES PARA EL UPDATE

    private Cargo createUpdate(Cargo cargo, CargoDto cargoDto) {
        if (!cargoDto.getNombre().equals(cargo.getNombre()))
            cargo.setNombre(cargoDto.getNombre());
        if (StringUtils.isNotBlank(cargoDto.getNombre())) {
            cargo.setNombre(cargoDto.getNombre());
        }
        if (!cargoDto.getDescripcion().equals(cargo.getDescripcion()))
            cargo.setDescripcion(cargoDto.getDescripcion());
        if (!cargoDto.getNroresolucion().equals(cargo.getNroresolucion()))
            cargo.setNroresolucion(cargoDto.getNroresolucion());
        if (!cargoDto.getNrodecreto().equals(cargo.getNrodecreto()))
            cargo.setNrodecreto(cargoDto.getNrodecreto());
        if (!cargoDto.getFecharesolucion().equals(cargo.getFecharesolucion()))
            cargo.setFecharesolucion(cargoDto.getFecharesolucion());
        if (!cargoDto.getFechainicio().equals(cargo.getFechainicio()))
            cargo.setFechainicio(cargoDto.getFechainicio());
        if (!cargoDto.getFechafinal().equals(cargo.getFechafinal()))
            cargo.setFechafinal(cargoDto.getFechafinal());

        cargo.setLegajos(cargoDto.getLegajos());

        cargo.setActivo(cargoDto.getActivo() != null ? cargoDto.getActivo() : true);

        if (!cargoDto.getAgrupacion().equals(cargo.getAgrupacion()))
            cargo.setAgrupacion(cargoDto.getAgrupacion());

        return cargo;

    }

    /*
     * @PostMapping("/create")
     * public ResponseEntity<?> create(@RequestBody CargoDto cargoDto) {
     * // Validaciones
     * if (StringUtils.isBlank(cargoDto.getNombre()))
     * return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"),
     * HttpStatus.BAD_REQUEST);
     * 
     * if (cargoService.existsByNombre(cargoDto.getNombre()))
     * return new ResponseEntity(new Mensaje("Ese nombre ya existe"),
     * HttpStatus.BAD_REQUEST);
     * 
     * if (cargoDto.getDescripcion() == null)
     * return new ResponseEntity(new Mensaje("La descripción es obligatoria"),
     * HttpStatus.BAD_REQUEST);
     * 
     * if (cargoDto.getNroresolucion() == null)
     * return new ResponseEntity(new
     * Mensaje("El número de resolución es obligatorio"), HttpStatus.BAD_REQUEST);
     * 
     * if (cargoDto.getNrodecreto() == null)
     * return new ResponseEntity(new Mensaje("El número de decreto es obligatorio"),
     * HttpStatus.BAD_REQUEST);
     * 
     * if (cargoDto.getFecharesolucion() == null)
     * return new ResponseEntity(new Mensaje("Fecha de resolución obligatoria"),
     * HttpStatus.BAD_REQUEST);
     * 
     * if (cargoDto.getFechainicio() == null)
     * return new ResponseEntity(new Mensaje("Fecha de inicio obligatoria"),
     * HttpStatus.BAD_REQUEST);
     * 
     * if (cargoDto.getFechafinal() == null)
     * return new ResponseEntity(new Mensaje("Fecha final obligatoria"),
     * HttpStatus.BAD_REQUEST);
     * 
     * Cargo cargo = new Cargo();
     * cargo.setNombre(cargoDto.getNombre());
     * cargo.setDescripcion(cargoDto.getDescripcion());
     * cargo.setNroresolucion(cargoDto.getNroresolucion());
     * cargo.setNrodecreto(cargoDto.getNrodecreto());
     * cargo.setFecharesolucion(cargoDto.getFecharesolucion());
     * cargo.setFechainicio(cargoDto.getFechainicio());
     * cargo.setFechafinal(cargoDto.getFechafinal());
     * 
     * cargo.setLegajos(cargoDto.getLegajos());
     * cargo.setAgrupacion(cargoDto.getAgrupacion());
     * 
     * cargoService.save(cargo);
     * 
     * return new ResponseEntity<>(new Mensaje("Cargo creado"), HttpStatus.OK);
     * }
     */

    /*
     * @PostMapping("/create")
     * public ResponseEntity<?> create(@RequestBody CargoDto cargoDto) {
     * ResponseEntity<?> respuestaValidaciones = validations(cargoDto);
     * 
     * if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
     * Cargo cargo = createUpdate(new Cargo(), cargoDto);
     * cargoService.save(cargo);
     * return new ResponseEntity<Mensaje>(new Mensaje("Cargo creado correctamente"),
     * HttpStatus.OK);
     * }
     * return respuestaValidaciones;
     * }
     */

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CargoDto cargoDto) {

        Cargo cargo = new Cargo();
        ResponseEntity<?> respuestaValidaciones = validations(cargo.getId(), cargoDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            createUpdate(cargo, cargoDto);
            cargoService.save(cargo);
            return new ResponseEntity<Mensaje>(new Mensaje("Cargo creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    /*
     * @PutMapping("/update/{id}")
     * public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody
     * CargoDto cargoDto) {
     * if (!cargoService.existsById(id))
     * return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
     * 
     * if (cargoService.existsByNombre(cargoDto.getNombre())
     * && cargoService.getByNombre(cargoDto.getNombre()).get().getId() != id)
     * return new ResponseEntity(new Mensaje("ese nombre ya existe"),
     * HttpStatus.BAD_REQUEST);
     * 
     * ResponseEntity<?> respuestaValidaciones = validations(cargoDto);
     * 
     * if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
     * Cargo cargo = createUpdate(cargoService.findById(id).get(), cargoDto);
     * cargoService.save(cargo);
     * return new ResponseEntity<Mensaje>(new
     * Mensaje("Cargo actualizado correctamente"), HttpStatus.OK);
     * }
     * return respuestaValidaciones;
     * }
     */

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CargoDto cargoDto) {
        if (!cargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(id, cargoDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Cargo cargo = createUpdate(cargoService.findById(id).get(), cargoDto);
            cargoService.save(cargo);
            return new ResponseEntity<Mensaje>(new Mensaje("Cargo actualizado correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!cargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Cargo cargo = cargoService.findById(id).get();
        cargo.setActivo(false);
        cargoService.save(cargo);
        return new ResponseEntity<>(new Mensaje("Cargo eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!cargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        cargoService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Cargo eliminado FISICAMENTE"), HttpStatus.OK);

    }
}
