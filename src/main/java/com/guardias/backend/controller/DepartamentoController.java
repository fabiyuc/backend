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

import com.guardias.backend.dto.DepartamentoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Departamento;
import com.guardias.backend.entity.Localidad;
import com.guardias.backend.service.DepartamentoService;
import com.guardias.backend.service.LocalidadService;
import com.guardias.backend.service.ProvinciaService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/departamento")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartamentoController {
    @Autowired
    DepartamentoService departamentoService;

    @Autowired
    ProvinciaService provinciaService;

    @Autowired
    LocalidadService localidadService;

    @GetMapping("/list")
    public ResponseEntity<List<Departamento>> list() {
        List<Departamento> list = departamentoService.findByActivoTrue().get();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Departamento>> listAll() {
        List<Departamento> list = departamentoService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Departamento>> getById(@PathVariable("id") Long id) {
        if (!departamentoService.activo(id))
            return new ResponseEntity(new Mensaje("departamento no existe"), HttpStatus.NOT_FOUND);
        Departamento departamento = departamentoService.findById(id).get();
        return new ResponseEntity(departamento, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Departamento>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!departamentoService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("departamento no existe"), HttpStatus.NOT_FOUND);
        Departamento departamento = departamentoService.getByNombre(nombre).get();
        return new ResponseEntity(departamento, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(DepartamentoDto departamentoDto, Long id) {

        if (StringUtils.isBlank(departamentoDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(departamentoDto.getCodigoPostal()))
            return new ResponseEntity(new Mensaje("el codigo postal es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (departamentoDto.getIdProvincia() == null)
            return new ResponseEntity(new Mensaje("la provincia es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    // private Adicional createUpdate(Adicional adicional, AdicionalDto
    // adicionalDto)

    private Departamento createUpdate(Departamento departamento, DepartamentoDto departamentoDto) {

        departamento.setNombre(departamentoDto.getNombre());

        if (departamentoDto.getNombre() != null
                && departamento.getNombre() != departamentoDto.getNombre()
                && !departamentoDto.getNombre().isEmpty())
            departamento.setNombre(departamentoDto.getNombre());

        if (departamentoDto.getCodigoPostal() != null
                && departamento.getCodigoPostal() != departamentoDto.getCodigoPostal()
                && !departamentoDto.getCodigoPostal().isEmpty())
            departamento.setCodigoPostal(departamentoDto.getCodigoPostal());

        // VER PROVINCIAS!!!!!!!!!!!!!!!!
        if (departamentoDto.getIdProvincia() != null) {
            if (departamento.getProvincia() == null
                    || !Objects.equals(departamento.getProvincia().getId(), departamentoDto.getIdProvincia())) {
                departamento.setProvincia(provinciaService.findById(departamentoDto.getIdProvincia()).get());
            }
        }

        if (departamentoDto.getIdLocalidades() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (departamento.getLocalidades() != null) {
                for (Localidad localidad : departamento.getLocalidades()) {
                    for (Long id : departamentoDto.getIdLocalidades()) {
                        if (!localidad.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                departamento.setLocalidades(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? departamentoDto.getIdLocalidades() : idList;
            for (Long id : idsToAdd) {
                departamento.getLocalidades().add(localidadService.findById(id).get());
                localidadService.findById(id).get().setDepartamento(departamento);
            }
        }

        departamento.setActivo(true);
        return departamento;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DepartamentoDto departamentoDto) {

        ResponseEntity<?> respuestaValidaciones = validations(departamentoDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Departamento departamento = createUpdate(new Departamento(), departamentoDto);
            departamentoService.save(departamento);
            return new ResponseEntity<>(new Mensaje("Departamento creado correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody DepartamentoDto departamentoDto) {
        if (!departamentoService.activo(id))
            return new ResponseEntity(new Mensaje("El departamento no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(departamentoDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Departamento departamento = createUpdate(departamentoService.findById(id).get(), departamentoDto);
            departamentoService.save(departamento);
            return new ResponseEntity<>(new Mensaje("Departamento modificado correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;

    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!departamentoService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el departamento"), HttpStatus.NOT_FOUND);

        Departamento departamento = departamentoService.findById(id).get();
        departamento.setActivo(false);
        departamentoService.save(departamento);
        return new ResponseEntity(new Mensaje("departamento eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!departamentoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el departamento"), HttpStatus.NOT_FOUND);
        departamentoService.deleteById(id);
        return new ResponseEntity(new Mensaje("departamento eliminado FISICAMENTE"), HttpStatus.OK);
    }
}
