package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NoAsistencialDto;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.service.NoAsistencialService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/noasistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class NoAsistencialController {

    @Autowired
    NoAsistencialService noAsistencialService;

    @GetMapping("/lista")
    public ResponseEntity<List<NoAsistencial>> list() {
        List<NoAsistencial> list = noAsistencialService.list();
        return new ResponseEntity<List<NoAsistencial>>(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<NoAsistencial> getById(@PathVariable("id") Long id) {
        if (!noAsistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("Profesional no entontrado"), HttpStatus.NOT_FOUND);
        NoAsistencial noAsistencial = noAsistencialService.findById(id).get();
        return new ResponseEntity<NoAsistencial>(noAsistencial, HttpStatus.OK);
    }

    @GetMapping("/detalleDni/{dni}")
    public ResponseEntity<NoAsistencial> getById(@PathVariable("dni") int dni) {
        if (!noAsistencialService.existsByDni(dni))
            return new ResponseEntity(new Mensaje("Profesional no entontrado"), HttpStatus.NOT_FOUND);
        NoAsistencial noAsistencial = noAsistencialService.findByDni(dni).get();
        return new ResponseEntity<NoAsistencial>(noAsistencial, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NoAsistencialDto noAsistencialDto) {

        if (noAsistencialDto.getDni() < 1000000)
            return new ResponseEntity(new Mensaje("DNI incorrecto"), HttpStatus.NOT_FOUND);

        if (StringUtils.isBlank(noAsistencialDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(noAsistencialDto.getApellido()))
            return new ResponseEntity(new Mensaje("el apellido es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(noAsistencialDto.getCuil()))
            return new ResponseEntity(new Mensaje("el cuil es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        NoAsistencial noAsistencial = new NoAsistencial();

        noAsistencial.setApellido(noAsistencialDto.getApellido());
        noAsistencial.setNombre(noAsistencialDto.getNombre());
        noAsistencial.setDni(noAsistencialDto.getDni());
        noAsistencial.setCuil(noAsistencialDto.getCuil());
        noAsistencial.setFechaNacimiento(noAsistencialDto.getFechaNacimiento());
        noAsistencial.setSexo(noAsistencialDto.getSexo());
        noAsistencial.setTelefono(noAsistencialDto.getTelefono());
        noAsistencial.setEmail(noAsistencialDto.getEmail());
        noAsistencial.setDomicilio(noAsistencialDto.getDomicilio());
        noAsistencial.setEstado(noAsistencialDto.getEstado());
        noAsistencial.setDescripcion(noAsistencialDto.getDescripcion());
        noAsistencial.setLegajos(noAsistencialDto.getLegajos());
        noAsistencial.setDistribucionesHorarias(noAsistencialDto.getDistribucionesHorarias());

        noAsistencialService.save(noAsistencial);
        return new ResponseEntity(new Mensaje("no asistencial creado"), HttpStatus.OK);
    }
}
