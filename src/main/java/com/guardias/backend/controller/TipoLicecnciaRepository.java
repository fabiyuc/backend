package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.TipoLicenciaDto;
import com.guardias.backend.entity.TipoLicencia;
import com.guardias.backend.service.TipoLicenciaService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/tipoLicencia")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoLicecnciaRepository {

    @Autowired
    TipoLicenciaService tipoLicenciaService;

    @GetMapping("/lista")
    public ResponseEntity<List<TipoLicencia>> list() {
        List<TipoLicencia> list = tipoLicenciaService.list();
        return new ResponseEntity<List<TipoLicencia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TipoLicencia> getById(@PathVariable("id") Long id) {
        if (!tipoLicenciaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        TipoLicencia tipoLicencia = tipoLicenciaService.getById(id).get();
        return new ResponseEntity<TipoLicencia>(tipoLicencia, HttpStatus.OK);
    }

    @GetMapping("/detail/{nombre}")
    public ResponseEntity<TipoLicencia> getByNombre(@PathVariable("nombre") String nombre) {
        if (!tipoLicenciaService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        TipoLicencia tipoLicencia = tipoLicenciaService.getByNombre(nombre).get();
        return new ResponseEntity<TipoLicencia>(tipoLicencia, HttpStatus.OK);
    }

    @GetMapping("/detail/{ley}")
    public ResponseEntity<TipoLicencia> getByLey(@PathVariable("ley") String ley) {
        if (!tipoLicenciaService.existsByLey(ley))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        TipoLicencia tipoLicencia = tipoLicenciaService.getByLey(ley).get();
        return new ResponseEntity<TipoLicencia>(tipoLicencia, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoLicenciaDto tipoLicenciaDto) {
        if (StringUtils.isBlank(tipoLicenciaDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(tipoLicenciaDto.getLey()))
            return new ResponseEntity<>(new Mensaje("el número de Ley es obligatorio"), HttpStatus.BAD_REQUEST);

        if (tipoLicenciaService.existsByNombre(tipoLicenciaDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        TipoLicencia tipoLicencia = new TipoLicencia();
        tipoLicencia.setNombre(tipoLicenciaDto.getNombre());
        tipoLicencia.setLey(tipoLicenciaDto.getLey());
        tipoLicencia.setArticulo(tipoLicenciaDto.getArticulo());
        tipoLicencia.setInciso(tipoLicenciaDto.getInciso());
        tipoLicenciaService.save(tipoLicencia);
        return new ResponseEntity<>(new Mensaje("TipoLicencia creada"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TipoLicenciaDto tipoLicenciaDto) {
        if (!tipoLicenciaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        if (StringUtils.isBlank(tipoLicenciaDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(tipoLicenciaDto.getLey()))
            return new ResponseEntity<>(new Mensaje("el número de Ley es obligatorio"), HttpStatus.BAD_REQUEST);

        if (tipoLicenciaService.existsByNombre(tipoLicenciaDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        TipoLicencia tipoLicencia = tipoLicenciaService.getById(id).get();

        if (!tipoLicenciaDto.getNombre().equals(tipoLicencia.getNombre()))
            tipoLicencia.setNombre(tipoLicenciaDto.getNombre());

        if (!tipoLicenciaDto.getLey().equals(tipoLicencia.getLey()))
            tipoLicencia.setLey(tipoLicenciaDto.getLey());

        if (!tipoLicenciaDto.getArticulo().equals(tipoLicencia.getArticulo()))
            tipoLicencia.setArticulo(tipoLicenciaDto.getArticulo());

        if (!tipoLicenciaDto.getInciso().equals(tipoLicencia.getInciso()))
            tipoLicencia.setInciso(tipoLicenciaDto.getInciso());

        tipoLicenciaService.save(tipoLicencia);
        return new ResponseEntity<>(new Mensaje("TipoLicencia creada"), HttpStatus.OK);
    }
}
