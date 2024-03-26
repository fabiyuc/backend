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
            return new ResponseEntity(new Mensaje("No existe el adicional"), HttpStatus.NOT_FOUND);
        Adicional adicional = adicionalService.findById(id).get();
        return new ResponseEntity(adicional, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Adicional>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!adicionalService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("No existe el adicional"), HttpStatus.NOT_FOUND);
        Adicional adicional = adicionalService.findByNombre(nombre).get();
        return new ResponseEntity(adicional, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(AdicionalDto adicionalDto) {
        if (adicionalDto.getNombre() == null)
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("Valido"), HttpStatus.OK);
    }

    /*
     * private ResponseEntity<?> validations(AdicionalDto adicionalDto) {
     * if (adicionalDto.getNombre() == null)
     * return new ResponseEntity<Mensaje>(new Mensaje("El nombre es obligatorio"),
     * HttpStatus.BAD_REQUEST);
     * 
     * if (adicionalDto.getIdRevistas() == null)
     * return new ResponseEntity<Mensaje>(new
     * Mensaje("La situacion de revista es obligatoria"),
     * HttpStatus.BAD_REQUEST);
     * 
     * return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
     * }
     */

    /*
     * private Adicional createUpdate(Adicional adicional, AdicionalDto
     * adicionalDto) {
     * if (StringUtils.isNotBlank(adicionalDto.getNombre()))
     * adicional.setNombre(adicionalDto.getNombre());
     * if (adicionalDto.getNombre() != null && adicional.getNombre() !=
     * adicionalDto.getNombre())
     * adicional.setNombre(adicionalDto.getNombre());
     * 
     * if (adicionalDto.getIdRevistas() != null) {
     * List<Long> idList = new ArrayList<Long>();
     * if (adicional.getRevistas() != null) {
     * for (Revista revista : adicional.getRevistas()) {
     * for (Long id : adicionalDto.getIdRevistas()) {
     * if (!revista.getId().equals(id)) {
     * idList.add(id);
     * }
     * }
     * }
     * }
     * List<Long> idsToAdd = idList.isEmpty() ? adicionalDto.getIdRevistas() :
     * idList;
     * for (Long id : idsToAdd) {
     * adicional.getRevistas().add(revistaService.findById(id).get());
     * revistaService.findById(id).get().setAdicional(adicional);
     * }
     * }
     * adicional.setActivo(true);
     * return adicional;
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

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AdicionalDto adicionalDto) {
        if (adicionalService.activoByNombre(adicionalDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("Ese nombre ya existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(adicionalDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Adicional adicional = createUpdate(new Adicional(), adicionalDto);
            adicionalService.save(adicional);
            return new ResponseEntity<Mensaje>(new Mensaje("Articulo creado correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody AdicionalDto adicionalDto) {
        // Verificar si el adicional existe
        if (!adicionalService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("El adicional no existe"), HttpStatus.NOT_FOUND);
        }

        ResponseEntity<?> respuestaValidaciones = validations(adicionalDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Adicional adicional = createUpdate(adicionalService.findById(id).get(), adicionalDto);
            adicionalService.save(adicional);
            return new ResponseEntity<Mensaje>(new Mensaje("Articulo creado correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

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

}