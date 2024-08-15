package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ValorGuardiaDto;
import com.guardias.backend.entity.ValorGuardia;
import com.guardias.backend.service.ValorGuardiaService;

@RestController
@RequestMapping("/valorGuardia")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorGuardiaController {
    
    @Autowired
    ValorGuardiaService valorGuardiaService;

    @GetMapping("/list")
    public ResponseEntity<List<ValorGuardia>> list() {
        List<ValorGuardia> list = valorGuardiaService.findByActivoTrue().get();
        return new ResponseEntity<List<ValorGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ValorGuardia>> listAll() {
        List<ValorGuardia> list = valorGuardiaService.findAll();
        return new ResponseEntity<List<ValorGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ValorGuardia> getById(@PathVariable("id") Long id) {

        if (!valorGuardiaService.activo(id))
            return new ResponseEntity(new Mensaje("Valor de guardia no encontrado"), HttpStatus.NOT_FOUND);
            ValorGuardia valorGuardia = valorGuardiaService.findById(id).get();
        return new ResponseEntity(valorGuardia, HttpStatus.OK);
    }

    /* @GetMapping("/detailByFecha/{fecha}")
    public ResponseEntity<List<ValorGuardia>> getByFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        Optional<List<ValorGuardia>> valorGuardia = valorGuardiaService.getByFecha(fecha);
        return valorGuardia.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    } */

   /*  @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ValorGuardiaDto valorGuardiaDto) {
        ResponseEntity<?> respuestaValidaciones = valorGuardiaService.validations(valorGuardiaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            ValorGuardia valorGuardia = valorGuardiaService.createUpdate(new ValorGuardia(), valorGuardiaDto);
            valorGmiService.save(valorGmi);
            return new ResponseEntity(new Mensaje("Valor creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ValorGmiDto valorGmiDto) {

        if (!valorGmiService.activo(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);
        ResponseEntity<?> respuestaValidaciones = valorGmiService.validations(valorGmiDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            ValorGmi valorGmi = valorGmiService.createUpdate(valorGmiService.findById(id).get(), valorGmiDto);
            valorGmiService.save(valorGmi);
            return new ResponseEntity(new Mensaje("Valor actualizado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!valorGmiService.activo(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);

        return valorGmiService.logicDelete(id);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!valorGmiService.existsById(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);

        valorGmiService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Valor eliminado FISICAMENTEE"), HttpStatus.OK);
    } */

}
