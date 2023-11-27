package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.MinisterioDto;
import com.guardias.backend.entity.Ministerio;
import com.guardias.backend.service.MinisterioService;

import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/ministerio")
@CrossOrigin(origins = "http://localhost:4200")
public class MinisterioController {

    @Autowired
    MinisterioService ministerioService;

    @GetMapping("/lista")
    public ResponseEntity<List<Ministerio>> list() {
        List<Ministerio> list = ministerioService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Ministerio>> getById(@PathVariable("id") Long id) {
        if (!ministerioService.existsById(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Ministerio ministerio = ministerioService.getById(id).get();
        return new ResponseEntity(ministerio, HttpStatus.OK);
    }

    @GetMapping("/detalle/{nombre}")
    public ResponseEntity<List<Ministerio>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!ministerioService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Ministerio ministerio = ministerioService.getMinisterioByNombre(nombre).get();
        return new ResponseEntity(ministerio, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MinisterioDto ministerioDto) {
        if (StringUtils.isBlank(ministerioDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(ministerioDto.getDomicilio()))
            return new ResponseEntity(new Mensaje("el domicilio es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (ministerioService.existsByNombre(ministerioDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);

        Ministerio ministerio = new Ministerio();
        ministerio.setNombre(ministerioDto.getNombre());
        ministerio.setDomicilio(ministerioDto.getDomicilio());
        ministerio.setTelefono(ministerioDto.getTelefono());
        ministerio.setEstado(ministerioDto.isEstado());
        ministerio.setIdRegion(ministerioDto.getIdRegion());
        ministerio.setIdLocalidad(ministerioDto.getIdLocalidad());
        ministerio.setObservacion(ministerioDto.getObservacion());

        ministerio.setIdCabecera(ministerioDto.getIdCabecera());

        ministerioService.save(ministerio);
        return new ResponseEntity(new Mensaje("Ministerio creado correctamente"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody MinisterioDto ministerioDto) {
        if (!ministerioService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        // if (ministerioService.existsByNombre(ministerioDto.getNombre()) &&
        // ministerioService.getMinisterioByNombre(ministerioDto.getNombre()).get().getId()
        // ==
        // id)
        // return new ResponseEntity(new Mensaje("ese ministerio ya existe"),
        // HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(ministerioDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        // TODO verificar los valores de ministerioDto para el update
        Ministerio ministerio = ministerioService.getById(id).get();
        ministerio.setNombre(ministerioDto.getNombre());
        ministerio.setDomicilio(ministerioDto.getDomicilio());
        ministerio.setTelefono(ministerioDto.getTelefono());
        ministerio.setEstado(ministerioDto.isEstado());
        ministerio.setIdRegion(ministerioDto.getIdRegion());
        ministerio.setIdLocalidad(ministerioDto.getIdLocalidad());
        ministerio.setObservacion(ministerioDto.getObservacion());
        ministerio.setIdCabecera(ministerioDto.getIdCabecera());

        ministerioService.save(ministerio);
        return new ResponseEntity(new Mensaje("Ministerio actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity <?> delete(@PathVariable("id") Long id){
        if(!ministerioService.existsById(id))
        return new ResponseEntity(new Mensaje("efector no encontrado"),HttpStatus.NOT_FOUND)
        ministerioService.deleteById(id);
        return new ResponseEntity(new Mensaje("Efector eliminado"), HttpStatus.OK)
    }

}
