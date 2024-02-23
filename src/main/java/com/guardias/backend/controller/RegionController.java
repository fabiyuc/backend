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
import com.guardias.backend.dto.RegionDto;
import com.guardias.backend.entity.Region;
import com.guardias.backend.service.RegionService;
import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/region")
@CrossOrigin(origins = "http://localhost:4200")
public class RegionController {

    @Autowired
    RegionService regionService;

    @GetMapping("/list")
    public ResponseEntity<List<Region>> list() {
        List<Region> list = regionService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Region>> getById(@PathVariable("id") Long id) {
        if (!regionService.existsById(id))
            return new ResponseEntity(new Mensaje("region no existe"), HttpStatus.NOT_FOUND);
        Region region = regionService.getById(id).get();
        return new ResponseEntity(region, HttpStatus.OK);
    }

    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<List<Region>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!regionService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("region no existe"), HttpStatus.NOT_FOUND);
        Region region = regionService.findByNombre(nombre).get();
        return new ResponseEntity(region, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegionDto regionDto) {
        if (StringUtils.isBlank(regionDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        Region region = new Region();
        region.setNombre(regionDto.getNombre());

        regionService.save(region);
        return new ResponseEntity(new Mensaje("Region creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody RegionDto regionDto) {
        if (StringUtils.isBlank(regionDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        Region region = regionService.getById(id).get();
        region.setNombre(regionDto.getNombre());
        regionService.save(region);
        return new ResponseEntity(new Mensaje("Region modificada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!regionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el region"), HttpStatus.NOT_FOUND);
        regionService.deleteById(id);
        return new ResponseEntity(new Mensaje("region eliminado"), HttpStatus.OK);
    }

}
