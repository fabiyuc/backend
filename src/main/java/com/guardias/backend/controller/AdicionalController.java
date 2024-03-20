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

import com.guardias.backend.dto.AdicionalDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Adicional;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.service.AdicionalService;
import com.guardias.backend.service.RevistaService;

@RestController
@RequestMapping("/adicional")
@CrossOrigin(origins = "http://localhost:4200")
public class AdicionalController {

    @Autowired
    AdicionalService adicionalService;

    @Autowired
    RevistaService revistaService;

    @GetMapping("/list")
    public ResponseEntity<List<Adicional>> list() {
        List<Adicional> list = adicionalService.findByActivoTrue();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Adicional>> listAll() {
        List<Adicional> list = adicionalService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Adicional> getById(@PathVariable("id") Long id) {
        if (!adicionalService.activo(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Adicional adicional = adicionalService.findById(id).get();
        return new ResponseEntity<>(adicional, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Adicional> getByNombre(@PathVariable("nombre") String nombre) {
        if (!adicionalService.existsByNombre(nombre))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Adicional adicional = adicionalService.findByNombre(nombre).get();
        return new ResponseEntity<>(adicional, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Mensaje> create(@RequestBody AdicionalDto adicionalDto) {
        Mensaje respuestaValidaciones = validations(adicionalDto);
        if (respuestaValidaciones != null)
            return new ResponseEntity<>(respuestaValidaciones, HttpStatus.BAD_REQUEST);
        Adicional adicional = createUpdate(new Adicional(), adicionalDto);

        if (adicionalService.existsByNombre(adicionalDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("Ese nombre ya existe"), HttpStatus.NOT_FOUND);

        adicionalService.save(adicional);
        return new ResponseEntity<>(new Mensaje("Adicional creado"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody AdicionalDto adicionalDto) {
        // Verificar si el adicional existe
        if (!adicionalService.existsById(id)) {
            return new ResponseEntity<>(new Mensaje("El adicional no existe"), HttpStatus.NOT_FOUND);
        }

        // obtener el adicional a actualizar
        Adicional adicionalExistente = adicionalService.findById(id).orElse(null);
        if (adicionalExistente == null) {
            return new ResponseEntity<>(new Mensaje("El adicional no existe"), HttpStatus.NOT_FOUND);
        }

        // Verificar si el adicional está activo
        if (!adicionalExistente.isActivo()) {
            return new ResponseEntity<>(new Mensaje("El adicional no está activo"), HttpStatus.BAD_REQUEST);
        }

        // Realizar otras validaciones y lógica de actualización aquí

        return new ResponseEntity<>(new Mensaje("Adicional actualizado"), HttpStatus.OK);
    }
    /*
     * Adicional adicional = adicionalService.findById(id).orElse(null);
     * if (adicional == null) {
     * return new ResponseEntity<>(new Mensaje("El adicional no existe"),
     * HttpStatus.NOT_FOUND);
     * }
     * // Verificar si se proporciona un nuevo nombre y si es diferente al actual
     * if (adicionalDto.getNombre() != null && !adicionalDto.getNombre().isEmpty()
     * && !adicionalDto.getNombre().equals(adicional.getNombre())
     * && adicionalService.existsByNombre(adicionalDto.getNombre())) {
     * return new ResponseEntity<>(new Mensaje("Ese nombre ya existe"),
     * HttpStatus.BAD_REQUEST);
     * }
     * 
     * adicional = createUpdate(adicional, adicionalDto);
     * adicionalService.save(adicional);
     * return new ResponseEntity<>(new Mensaje("Adicional actualizado"),
     * HttpStatus.OK);
     */

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Mensaje> logicDelete(@PathVariable("id") long id) {
        if (!adicionalService.activo(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Adicional adicional = adicionalService.findById(id).get();
        adicional.setActivo(false);
        adicionalService.save(adicional);
        return new ResponseEntity<>(new Mensaje("Adicional eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<Mensaje> fisicDelete(@PathVariable("id") long id) {
        if (!adicionalService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        adicionalService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Adicional eliminado FISICAMENTE"), HttpStatus.OK);
    }

    private Mensaje validations(AdicionalDto adicionalDto) {
        if (adicionalDto.getNombre() == null)
            return new Mensaje("El nombre es obligatorio");

        if (adicionalDto.getIdRevistas() == null)
            return new Mensaje("La revista es obligatorio");
        return null;
    }

    /*
     * private Mensaje validationsUpdate(AdicionalDto adicionalDto) {
     * if (adicionalDto.getIdRevistas() == null) {
     * return new Mensaje("La revista es obligatoria");
     * }
     * return null;
     * }
     */

    private Adicional createUpdate(Adicional adicional, AdicionalDto adicionalDto) {
        // Verificar si se proporciona un nuevo nombre y si es diferente al actual
        if (adicionalDto.getNombre() != null && !adicionalDto.getNombre().isEmpty()
                && !adicionalDto.getNombre().equals(adicional.getNombre())) {
            adicional.setNombre(adicionalDto.getNombre());
        }

        // Verificar si se proporciona un conjunto de revistas
        if (adicionalDto.getIdRevistas() != null) {
            // Agregar las revistas proporcionadas sin limpiar las existentes
            for (Long id : adicionalDto.getIdRevistas()) {
                Revista revista = revistaService.findById(id).orElse(null);
                if (revista != null) {
                    // Verificar si la revista ya está asociada al adicional
                    boolean revistaExistente = adicional.getRevistas().stream()
                            .anyMatch(r -> r.getId().equals(id));
                    if (!revistaExistente) {
                        adicional.getRevistas().add(revista);
                        revista.setAdicional(adicional);
                    }
                }
            }
        }
        adicional.setActivo(true);

        return adicional;
    }
}