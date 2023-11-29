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
import com.guardias.backend.dto.TipoRevistaDto;
import com.guardias.backend.entity.TipoRevista;
import com.guardias.backend.service.TipoRevistaService;

@RestController
@RequestMapping("/tipoRevista")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoRevistaController {

    @Autowired
    TipoRevistaService tipoRevistaService;

    @GetMapping("/lista")
    public ResponseEntity<List<TipoRevista>> list() {
        List<TipoRevista> list = tipoRevistaService.list();
        return new ResponseEntity<List<TipoRevista>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TipoRevista> getById(@PathVariable("id") Long id) {
    public ResponseEntity<TipoRevista> getById(@PathVariable("id") Long id) {
        if (!tipoRevistaService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el tipo de revista"), HttpStatus.NOT_FOUND);
        TipoRevista tipoRevista = tipoRevistaService.getOne(id).get();
        return new ResponseEntity<TipoRevista>(tipoRevista, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<TipoRevista> getByNombre(@PathVariable("nombre") String nombre) {
        if (!tipoRevistaService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe el tipo de revista"), HttpStatus.NOT_FOUND);
        TipoRevista tipoRevista = tipoRevistaService.getByNombre(nombre).get();
        return new ResponseEntity<TipoRevista>(tipoRevista, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoRevistaDto tipoRevistaDto) {
        if (StringUtils.isBlank(tipoRevistaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (tipoRevistaService.existsByNombre(tipoRevistaDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);
        TipoRevista tipoRevista = new TipoRevista(tipoRevistaDto.getNombre());
        tipoRevistaService.save(tipoRevista);
        return new ResponseEntity(new Mensaje("tipo de revista creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TipoRevistaDto tipoRevistaDto) {
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TipoRevistaDto tipoRevistaDto) {
        if (!tipoRevistaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el tipo de revista"), HttpStatus.NOT_FOUND);

        if (tipoRevistaService.existsByNombre(tipoRevistaDto.getNombre()) &&
                tipoRevistaService.getByNombre(tipoRevistaDto.getNombre()).get().getId() == id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(tipoRevistaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        TipoRevista tipoRevista = tipoRevistaService.getOne(id).get();
        if (tipoRevista.getNombre() != tipoRevistaDto.getNombre() && tipoRevistaDto.getNombre() != null && !tipoRevistaDto.getNombre().isEmpty()) {
        tipoRevista.setNombre(tipoRevistaDto.getNombre());
        tipoRevistaService.save(tipoRevista);
        return new ResponseEntity(new Mensaje("Tipo de servicio actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        
        if (!tipoRevistaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el tipo de revista"), HttpStatus.NOT_FOUND);
        tipoRevistaService.delete(id);
        return new ResponseEntity(new Mensaje("tipo de revista eliminado"), HttpStatus.OK);
    }

}
