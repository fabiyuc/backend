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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ProvinciaDTO;
import com.guardias.backend.entity.Provincia;
import com.guardias.backend.service.ProvinciaService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/provincia")
@CrossOrigin(origins = "http://localhost:4200")
public class ProvinciaController {

    @Autowired
    ProvinciaService provinciaService;

    @GetMapping("/list")
    public ResponseEntity<List<Provincia>> list() {
        List<Provincia> list = provinciaService.findByActivo();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Provincia>> listAll() {
        List<Provincia> list = provinciaService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Provincia> getById(@PathVariable("id") Long id) {
        if (!provinciaService.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        Provincia provincia = provinciaService.findById(id).get();
        return ResponseEntity.ok(provincia);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Provincia> getByNombre(@PathVariable("nombre") String nombre) {
        if (!provinciaService.existsByNombre(nombre))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        Provincia provincia = provinciaService.findByNombre(nombre).get();
        return ResponseEntity.ok(provincia);
    }

    private ResponseEntity<?> validations(ProvinciaDTO provinciaDto, Long id) {
        if (StringUtils.isBlank(provinciaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (provinciaService.existsByNombre(provinciaDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(provinciaDto.getGentilicio()))
            return new ResponseEntity(new Mensaje("el gentilicio es obligatorio"), HttpStatus.BAD_REQUEST);
        // ****** Necesitamos validar que el gentilicio sea unico? en caso de sí agregar
        // esa validacion */

        if (provinciaDto.getPais() == null)
            return new ResponseEntity(new Mensaje("es obligatorio indicar el pais"), HttpStatus.BAD_REQUEST);

        if (provinciaService.existsByNombre(provinciaDto.getNombre())
                && (provinciaService.findByNombre(provinciaDto.getNombre()).get().getId() != id))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Provincia createUpdate(Provincia provincia, ProvinciaDTO provinciaDto) {

        provincia.setNombre(provinciaDto.getNombre());
        provincia.setGentilicio(provinciaDto.getGentilicio());
        provincia.setPais(provinciaDto.getPais());
        provincia.setActivo(true);
        return provincia;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProvinciaDTO provinciaDto) {

        ResponseEntity<?> respuestaValidaciones = validations(provinciaDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Provincia provincia = createUpdate(new Provincia(), provinciaDto);
            provinciaService.save(provincia);
            return ResponseEntity.ok(new Mensaje("Provincia creada correctamente"));
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ProvinciaDTO provinciaDto) {

        if (!provinciaService.activo(id))
            return new ResponseEntity(new Mensaje("Provincia no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(provinciaDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Provincia provincia = createUpdate(provinciaService.findById(id).get(), provinciaDto);
            provinciaService.save(provincia);
            return ResponseEntity.ok(new Mensaje("Provincia creada correctamente"));
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!provinciaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la provincia"), HttpStatus.NOT_FOUND);

        Provincia provincia = provinciaService.findById(id).get();
        provincia.setActivo(false);
        provinciaService.save(provincia);
        return ResponseEntity.ok(new Mensaje("Provincia eliminada correctamente"));
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {

        if (!provinciaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la provincia"), HttpStatus.NOT_FOUND);
        provinciaService.deleteById(id);
        return ResponseEntity.ok(new Mensaje("Provincia eliminada FISICAMENTE"));
    }
}
