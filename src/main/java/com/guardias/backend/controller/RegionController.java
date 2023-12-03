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
import com.guardias.backend.entity.Region;
import com.guardias.backend.service.RegionService;

@RestController
@RequestMapping("/region")
@CrossOrigin(origins = "http://localhost:4200")
public class RegionController {

    @Autowired
    RegionService regionService;

    @GetMapping("/lista")
    public ResponseEntity<List<Region>> list() {
        List<Region> list = regionService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Region>> getById(@PathVariable("id") Long id) {
        if (!regionService.existsById(id))
            return new ResponseEntity(new Mensaje("region no existe"), HttpStatus.NOT_FOUND);
        Region region = regionService.getById(id).get();
        return new ResponseEntity(region, HttpStatus.OK);
    }

    @GetMapping("/detalle/{nombre}")
    public ResponseEntity<List<Region>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!regionService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("region no existe"), HttpStatus.NOT_FOUND);
        Region region = regionService.getByNombre(nombre).get();
        return new ResponseEntity(region, HttpStatus.OK);
    }

    // TODO (RegionController create)
    // TODO (RegionController update)

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!regionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el region"), HttpStatus.NOT_FOUND);
        regionService.deleteById(id);
        return new ResponseEntity(new Mensaje("region eliminado"), HttpStatus.OK);
    }

}
