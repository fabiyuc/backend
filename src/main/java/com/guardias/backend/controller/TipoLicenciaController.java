package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.TipoLicenciaDto;
import com.guardias.backend.entity.TipoLicencia;
import com.guardias.backend.service.TipoLicenciaService;

import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/tipoLicencia")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoLicenciaController {

    @Autowired
    TipoLicenciaService tipoLicenciaService;

    @GetMapping("/list")
    public ResponseEntity<List<TipoLicencia>> list() {
        List<TipoLicencia> list = tipoLicenciaService.findByActivoTrue().get();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<TipoLicencia>> listAll() {
        List<TipoLicencia> list = tipoLicenciaService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<TipoLicencia>> getById(@PathVariable("id") Long id) {
        if (!tipoLicenciaService.activo(id))
            return new ResponseEntity(new Mensaje("Tipo de licencia no encontrada"), HttpStatus.NOT_FOUND);
        TipoLicencia tipoLicencia = tipoLicenciaService.findById(id).get();
        return new ResponseEntity(tipoLicencia, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<TipoLicencia>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!tipoLicenciaService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("Tipo de licencia no encontrada"), HttpStatus.NOT_FOUND);
        TipoLicencia tipoLicencia = tipoLicenciaService.findByNombre(nombre).get();
        return new ResponseEntity(tipoLicencia, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoLicenciaDto tipoLicenciaDto) {
        if (StringUtils.isBlank(tipoLicenciaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(tipoLicenciaDto.getLey()))
            return new ResponseEntity(new Mensaje("el numero de ley es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        TipoLicencia tipoLicencia = new TipoLicencia();

        tipoLicencia.setNombre(tipoLicenciaDto.getNombre());
        tipoLicencia.setLey(tipoLicenciaDto.getLey());
        tipoLicencia.setArticulo(tipoLicenciaDto.getArticulo());
        tipoLicencia.setInciso(tipoLicenciaDto.getInciso());

        tipoLicenciaService.save(tipoLicencia);
        return new ResponseEntity(new Mensaje("Tipo de licencia creada correctamente"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TipoLicenciaDto tipoLicenciaDto) {
        if (!tipoLicenciaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el tipo de licencia"), HttpStatus.NOT_FOUND);
        if (StringUtils.isBlank(tipoLicenciaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(tipoLicenciaDto.getLey()))
            return new ResponseEntity(new Mensaje("el numero de ley es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        TipoLicencia tipoLicencia = tipoLicenciaService.findById(id).get();

        if (!tipoLicenciaDto.getNombre().equals(tipoLicencia.getNombre()))
            tipoLicencia.setNombre(tipoLicenciaDto.getNombre());

        if (!tipoLicenciaDto.getLey().equals(tipoLicencia.getLey()))
            tipoLicencia.setLey(tipoLicenciaDto.getLey());

        if (!tipoLicenciaDto.getArticulo().equals(tipoLicencia.getArticulo()))
            tipoLicencia.setArticulo(tipoLicenciaDto.getArticulo());

        if (!tipoLicenciaDto.getInciso().equals(tipoLicencia.getInciso()))
            tipoLicencia.setInciso(tipoLicenciaDto.getInciso());

        tipoLicenciaService.save(tipoLicencia);
        return new ResponseEntity(new Mensaje("Tipo de licencia modificado correctamente"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!tipoLicenciaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        TipoLicencia tipoLicencia = tipoLicenciaService.findById(id).get();
        tipoLicencia.setActivo(false);
        tipoLicenciaService.save(tipoLicencia);
        return new ResponseEntity<>(new Mensaje("Tipo Licencia eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!tipoLicenciaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        tipoLicenciaService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Tipo Licencia eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
