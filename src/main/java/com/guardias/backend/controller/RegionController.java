package com.guardias.backend.controller;

import java.util.ArrayList;
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
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Region;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegionService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/region")
@CrossOrigin(origins = "http://localhost:4200")
public class RegionController {

    @Autowired
    RegionService regionService;
    @Autowired
    EfectorService efectorService;

    @GetMapping("/list")
    public ResponseEntity<List<Region>> list() {
        List<Region> list = regionService.findByActivo();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Region>> listAll() {
        List<Region> list = regionService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Region>> getById(@PathVariable("id") Long id) {
        if (!regionService.activo(id))
            return new ResponseEntity(new Mensaje("region no existe"), HttpStatus.NOT_FOUND);
        Region region = regionService.findById(id).get();
        return new ResponseEntity(region, HttpStatus.OK);
    }

    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<List<Region>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!regionService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("region no existe"), HttpStatus.NOT_FOUND);
        Region region = regionService.findByNombre(nombre).get();
        return new ResponseEntity(region, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(RegionDto regionDto) {
        if (StringUtils.isBlank(regionDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Region createUpdate(Region region, RegionDto regionDto) {

        if (region.getNombre() != regionDto.getNombre() && regionDto.getNombre() != null
                && !regionDto.getNombre().isEmpty())
            region.setNombre(regionDto.getNombre());

        if (regionDto.getIdEfectores() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (region.getEfectores() != null) {
                for (Efector efector : region.getEfectores()) {
                    for (Long id : regionDto.getIdEfectores()) {
                        if (!efector.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                region.setEfectores(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? regionDto.getIdEfectores() : idList;
            for (Long id : idsToAdd) {
                region.getEfectores().add(efectorService.findById(id));
                efectorService.findById(id).setRegion(region);
            }
        }

        region.setActivo(true);
        return region;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegionDto regionDto) {

        Region region = new Region();
        region.setNombre(regionDto.getNombre());

        regionService.save(region);
        return new ResponseEntity(new Mensaje("Region creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody RegionDto regionDto) {

        if (!regionService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el region"), HttpStatus.NOT_FOUND);

        if (StringUtils.isBlank(regionDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        Region region = regionService.findById(id).get();
        region.setNombre(regionDto.getNombre());
        regionService.save(region);
        return new ResponseEntity(new Mensaje("Region modificada"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!regionService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el region"), HttpStatus.NOT_FOUND);

        Region region = regionService.findById(id).get();
        region.setActivo(false);
        regionService.save(region);
        return new ResponseEntity(new Mensaje("region eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!regionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el region"), HttpStatus.NOT_FOUND);
        regionService.deleteById(id);
        return new ResponseEntity(new Mensaje("region eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
