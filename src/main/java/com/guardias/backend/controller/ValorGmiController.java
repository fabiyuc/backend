package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.guardias.backend.dto.ValorGmiDto;
import com.guardias.backend.entity.ValorGmi;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.service.DdjjService;
import com.guardias.backend.service.ValorGmiService;
import com.guardias.backend.service.ValorGuardiaCargoYagrupService;

@RestController
@RequestMapping("/valorGmi")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorGmiController {

    @Autowired
    ValorGmiService valorGmiService;

    @Autowired 
    ValorGuardiaCargoYagrupService valorGuardiaCargoYagrupService;
    @Autowired
    DdjjService ddjjService;

    @GetMapping("/list")
    public ResponseEntity<List<ValorGmi>> list() {
        List<ValorGmi> list = valorGmiService.findByActivoTrue().get();
        return new ResponseEntity<List<ValorGmi>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ValorGmi>> listAll() {
        List<ValorGmi> list = valorGmiService.findAll();
        return new ResponseEntity<List<ValorGmi>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ValorGmi> getById(@PathVariable("id") Long id) {

        if (!valorGmiService.activo(id))
            return new ResponseEntity(new Mensaje("Valor no encontrado"), HttpStatus.NOT_FOUND);
        ValorGmi valorGmi = valorGmiService.findById(id).get();
        return new ResponseEntity(valorGmi, HttpStatus.OK);
    }

    @GetMapping("/detailByFechaAndTipoGuardia/{fecha}/{tipoGuardia}")
    public ResponseEntity<ValorGmi> getByFechaAndTipoGuardia(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @PathVariable("tipoGuardia") String tipoGuardia) {
        TipoGuardiaEnum guardia = TipoGuardiaEnum.valueOf(tipoGuardia.toUpperCase());
        Optional<ValorGmi> valorGmi = valorGmiService.getByFechaAndTipoGuardia(fecha, guardia);
        return valorGmi.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/detailByFecha/{fecha}")
    public ResponseEntity<List<ValorGmi>> getByFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        Optional<List<ValorGmi>> valorGmi = valorGmiService.getByFecha(fecha);
        return valorGmi.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ValorGmiDto valorGmiDto) {
        ResponseEntity<?> respuestaValidaciones = valorGmiService.validations(valorGmiDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            ValorGmi valorGmi = valorGmiService.createUpdate(new ValorGmi(), valorGmiDto);
            valorGmiService.save(valorGmi);

             // Llamada al método para crear valores de guardia cargo y agrup
            valorGuardiaCargoYagrupService.crearValoresGuardiaCargoYagrup();

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
    }
}
