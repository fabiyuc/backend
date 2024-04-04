package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

import com.guardias.backend.dto.LocalidadDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Localidad;
import com.guardias.backend.service.DepartamentoService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.LocalidadService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/localidad")
@CrossOrigin(origins = "http://localhost:4200")
public class LocalidadController {

    @Autowired
    LocalidadService localidadService;
    @Autowired
    DepartamentoService departamentoService;
    @Autowired
    EfectorService efectorService;

    @GetMapping("/list")
    public ResponseEntity<List<Localidad>> list() {
        List<Localidad> list = localidadService.findByActivo();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Localidad>> listAll() {
        List<Localidad> list = localidadService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Localidad>> getById(@PathVariable("id") Long id) {
        if (!localidadService.activo(id))
            return new ResponseEntity(new Mensaje("localidad no existe"), HttpStatus.NOT_FOUND);
        Localidad localidad = localidadService.findById(id).get();
        return new ResponseEntity(localidad, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Localidad>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!localidadService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("localidad no existe"), HttpStatus.NOT_FOUND);
        Localidad localidad = localidadService.findByNombre(nombre).get();
        return new ResponseEntity(localidad, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(LocalidadDto localidadDto) {
        if (StringUtils.isBlank(localidadDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (localidadDto.getIdDepartamento() == null)
            return new ResponseEntity(new Mensaje("el departamento es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    // private Cargo createUpdate(Cargo cargo, CargoDto cargoDto) {
    private Localidad createUpdate(Localidad localidad, LocalidadDto localidadDto) {

        if (localidadDto.getNombre() != null && !localidadDto.getNombre().equals(localidad.getNombre())
                && !localidadDto.getNombre().isEmpty())
            localidad.setNombre(localidadDto.getNombre());

        if (localidad.getDepartamento() == null ||
                (localidadDto.getIdDepartamento() != null &&
                        !Objects.equals(localidad.getDepartamento().getId(),
                                localidadDto.getIdDepartamento()))) {
            localidad.setDepartamento(departamentoService.findById(localidadDto.getIdDepartamento()).get());
        }

        if (localidadDto.getIdEfectores() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (localidad.getEfectores() != null) {
                for (Efector efector : localidad.getEfectores()) {
                    for (Long id : localidadDto.getIdEfectores()) {
                        if (!efector.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                localidad.setEfectores(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? localidadDto.getIdEfectores() : idList;
            for (Long id : idsToAdd) {
                localidad.getEfectores().add(efectorService.findById(id));
                efectorService.findById(id).setLocalidad(localidad);
            }
        }

        localidad.setActivo(true);

        return localidad;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LocalidadDto localidadDto) {

        ResponseEntity<?> respuestaValidaciones = validations(localidadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Localidad localidad = createUpdate(new Localidad(), localidadDto);
            localidadService.save(localidad);
            return new ResponseEntity<>(new Mensaje("Localidad creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LocalidadDto localidadDto) {

        if (!localidadService.activo(id))
            return new ResponseEntity(new Mensaje("localidad no existe"), HttpStatus.NOT_FOUND);
        ResponseEntity<?> respuestaValidaciones = validations(localidadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Localidad localidad = createUpdate(localidadService.findById(id).get(), localidadDto);
            localidadService.save(localidad);
            return new ResponseEntity<>(new Mensaje("Localidad creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {

        if (!localidadService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la localidad"), HttpStatus.NOT_FOUND);

        Localidad localidad = localidadService.findById(id).get();
        localidad.setActivo(false);
        localidadService.save(localidad);
        return new ResponseEntity(new Mensaje("localidad eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!localidadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la localidad"), HttpStatus.NOT_FOUND);
        localidadService.deleteById(id);
        return new ResponseEntity(new Mensaje("localidad eliminada FISICAMENTE"), HttpStatus.OK);
    }
}
