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
import com.guardias.backend.dto.TipoCargoDto;
import com.guardias.backend.entity.TipoCargo;
import com.guardias.backend.service.TipoCargoService;

@RestController
@RequestMapping("/tipocargo")
@CrossOrigin(origins = "http://localhost:4200")

public class TipoCargoController {
    @Autowired
    TipoCargoService tipoCargoService;

    @GetMapping("/lista")
    public ResponseEntity<List<TipoCargo>> list() {
        List<TipoCargo> list = tipoCargoService.list();
        return new ResponseEntity<List<TipoCargo>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TipoCargo> getById(@PathVariable("id") long id) {
        if (!tipoCargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        TipoCargo tipoCargo = tipoCargoService.getone(id).get();
        return new ResponseEntity<TipoCargo>(tipoCargo, HttpStatus.OK);

    }

    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<TipoCargo> getByNombre(@PathVariable("nombre") String nombre) {
        if (!tipoCargoService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        TipoCargo tipoCargo = tipoCargoService.getByNombre(nombre).get();
        return new ResponseEntity<TipoCargo>(tipoCargo, HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoCargoDto tipoCargoDto) {

        if (StringUtils.isBlank(tipoCargoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (tipoCargoService.existsByNombre(tipoCargoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("Ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (tipoCargoDto.getDescripcion() == null)
            return new ResponseEntity(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);
        TipoCargo tipoCargo = new TipoCargo();

        tipoCargo.setNombre(tipoCargoDto.getNombre());
        tipoCargo.setDescripcion(tipoCargoDto.getDescripcion());
        tipoCargo.setEshospitalario(tipoCargoDto.isEshospitalario());

        tipoCargoService.save(tipoCargo);

        return new ResponseEntity<>(new Mensaje("Tipo cargo creado"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody TipoCargoDto tipoCargoDto) {
        if (!tipoCargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        if (tipoCargoService.existsByNombre(tipoCargoDto.getNombre())) {
            TipoCargo existingTipoCargo = tipoCargoService.getByNombre(tipoCargoDto.getNombre()).orElse(null);
            if (existingTipoCargo != null && existingTipoCargo.getId() != id) {
                return new ResponseEntity(new Mensaje("Ya existe un TipoCargo con ese nombre"), HttpStatus.BAD_REQUEST);
            }
        }

        if (StringUtils.isBlank(tipoCargoDto.getNombre())) {
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(tipoCargoDto.getDescripcion())) {
            return new ResponseEntity<>(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        // Obtén el TipoCargo actual
        TipoCargo tipoCargo = tipoCargoService.getone(id).orElse(null);
        if (tipoCargo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Mensaje("No se encontró el TipoCargo para actualizar"));

        }

        // Actualiza los campos
        tipoCargo.setNombre(tipoCargoDto.getNombre());
        tipoCargo.setDescripcion(tipoCargoDto.getDescripcion());
        tipoCargo.setEshospitalario(tipoCargoDto.isEshospitalario());

        // Guarda la actualización
        tipoCargoService.save(tipoCargo);

        return ResponseEntity.ok(new Mensaje("Tipo cargo actualizado"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!tipoCargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        tipoCargoService.delete(id);
        return new ResponseEntity<>(new Mensaje("Tipo Cargo eliminado"), HttpStatus.OK);

    }

}
