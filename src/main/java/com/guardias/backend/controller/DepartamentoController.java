package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Departamento;
import com.guardias.backend.service.DepartamentoService;

@RestController
@RequestMapping("/departamento")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartamentoController {
    @Autowired
    DepartamentoService departamentoService;

    @GetMapping("/lista")
    public ResponseEntity<List<Departamento>> list() {
        List<Departamento> list = departamentoService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Departamento>> getById(@PathVariable("id") Long id) {
        if (!departamentoService.existsById(id))
            return new ResponseEntity(new Mensaje("departamento no existe"), HttpStatus.NOT_FOUND);
        Departamento departamento = departamentoService.getById(id).get();
        return new ResponseEntity(departamento, HttpStatus.OK);
    }

    @GetMapping("/detalle/{nombre}")
    public ResponseEntity<List<Departamento>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!departamentoService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("departamento no existe"), HttpStatus.NOT_FOUND);
        Departamento departamento = departamentoService.getByNombre(nombre).get();
        return new ResponseEntity(departamento, HttpStatus.OK);
    }

    // TODO (DepartamentoController create)
    // TODO (DepartamentoController update)

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!departamentoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el departamento"), HttpStatus.NOT_FOUND);
        departamentoService.deleteById(id);
        return new ResponseEntity(new Mensaje("departamento eliminado"), HttpStatus.OK);
    }
}
