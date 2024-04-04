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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ProvinciaDTO;
import com.guardias.backend.entity.Departamento;
import com.guardias.backend.entity.Provincia;
import com.guardias.backend.service.DepartamentoService;
import com.guardias.backend.service.PaisService;
import com.guardias.backend.service.ProvinciaService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/provincia")
@CrossOrigin(origins = "http://localhost:4200")
public class ProvinciaController {

    @Autowired
    ProvinciaService provinciaService;
    @Autowired
    PaisService paisService;
    @Autowired
    DepartamentoService departamentoService;

    @GetMapping("/list")
    public ResponseEntity<List<Provincia>> list() {
        List<Provincia> list = provinciaService.findByActivo();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Provincia>> listAll() {
        List<Provincia> list = provinciaService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Provincia> getById(@PathVariable("id") Long id) {
        if (!provinciaService.activo(id))
            return new ResponseEntity(new Mensaje("Provincia no existe"), HttpStatus.NOT_FOUND);
        Provincia provincia = provinciaService.findById(id).get();
        return ResponseEntity.ok(provincia);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Provincia> getByNombre(@PathVariable("nombre") String nombre) {
        if (!provinciaService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("Provincia no existe"), HttpStatus.NOT_FOUND);
        Provincia provincia = provinciaService.findByNombre(nombre).get();
        return ResponseEntity.ok(provincia);
    }

    private ResponseEntity<?> validations(ProvinciaDTO provinciaDto) {
        if (StringUtils.isBlank(provinciaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(provinciaDto.getGentilicio()))
            return new ResponseEntity(new Mensaje("el gentilicio es obligatorio"), HttpStatus.BAD_REQUEST);

        if (provinciaDto.getIdPais() == null)
            return new ResponseEntity(new Mensaje("es obligatorio indicar el pais"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Provincia createUpdate(Provincia provincia, ProvinciaDTO provinciaDto) {

        if (provincia.getNombre() != provinciaDto.getNombre() && provinciaDto.getNombre() != null
                && !provinciaDto.getNombre().isEmpty())
            provincia.setNombre(provinciaDto.getNombre());

        if (provincia.getGentilicio() != provinciaDto.getGentilicio() && provinciaDto.getGentilicio() != null
                && !provinciaDto.getGentilicio().isEmpty())
            provincia.setGentilicio(provinciaDto.getGentilicio());

        if (provincia.getPais() == null ||
                (provinciaDto.getIdPais() != null &&
                        !Objects.equals(provincia.getPais().getId(),
                                provinciaDto.getIdPais()))) {
            provincia.setPais(paisService.findById(provinciaDto.getIdPais()).get());
        }

        if (provinciaDto.getIdDepartamentos() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (provincia.getDepartamentos() != null) {
                for (Departamento departamento : provincia.getDepartamentos()) {
                    for (Long id : provinciaDto.getIdDepartamentos()) {
                        if (!departamento.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                provincia.setDepartamentos(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? provinciaDto.getIdDepartamentos() : idList;
            for (Long id : idsToAdd) {
                provincia.getDepartamentos().add(departamentoService.findById(id).get());
                departamentoService.findById(id).get().setProvincia(provincia);
            }
        }
        provincia.setActivo(true);
        return provincia;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProvinciaDTO provinciaDto) {

        if (provinciaService.existsByNombre(provinciaDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        ResponseEntity<?> respuestaValidaciones = validations(provinciaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Provincia provincia = createUpdate(new Provincia(), provinciaDto);
            provinciaService.save(provincia);
            return ResponseEntity.ok(new Mensaje("Provincia creada correctamente"));
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ProvinciaDTO provinciaDto) {

        if (!provinciaService.activo(id))
            return new ResponseEntity(new Mensaje("Provincia no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(provinciaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Provincia provincia = createUpdate(provinciaService.findById(id).get(), provinciaDto);
            provinciaService.save(provincia);
            return ResponseEntity.ok(new Mensaje("Provincia creada correctamente"));
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!provinciaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la provincia"), HttpStatus.NOT_FOUND);

        Provincia provincia = provinciaService.findById(id).get();
        provincia.setActivo(false);
        provinciaService.save(provincia);
        return ResponseEntity.ok(new Mensaje("Provincia eliminada correctamente"));
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {

        if (!provinciaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la provincia"), HttpStatus.NOT_FOUND);
        provinciaService.deleteById(id);
        return ResponseEntity.ok(new Mensaje("Provincia eliminada FISICAMENTE"));
    }
}
