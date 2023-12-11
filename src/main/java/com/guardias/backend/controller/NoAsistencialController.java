package com.guardias.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NoAsistencialDto;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.service.NoAsistencialService;

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

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NoAsistencialDto noAsistencialDto) {
        /*
         * if (StringUtils.isBlank(servicioDto.getDescripcion()))
         * return new ResponseEntity(new Mensaje("la descripcion es obligatoria"),
         * HttpStatus.BAD_REQUEST);
         * if (serviceServicio.existsByDescripcion(servicioDto.getDescripcion()))
         * return new ResponseEntity(new Mensaje("esa descripcion ya existe"),
         * HttpStatus.BAD_REQUEST);
         */

        NoAsistencial noAsistencial = new NoAsistencial();

        noAsistencial.setApellido(noAsistencialDto.getApellido());
        noAsistencial.setNombre(noAsistencialDto.getNombre());
        noAsistencial.setDni(noAsistencialDto.getDni());
        noAsistencial.setCuil(noAsistencialDto.getCuil());
        noAsistencial.setFechaNac(noAsistencialDto.getFechaNac());
        noAsistencial.setSexo(noAsistencialDto.getSexo());
        noAsistencial.setTelefono(noAsistencialDto.getTelefono());
        noAsistencial.setEmail(noAsistencialDto.getEmail());
        noAsistencial.setDomicilio(noAsistencialDto.getDomicilio());
        noAsistencial.setEstado(noAsistencialDto.getEstado());
        noAsistencial.setDescripcion(noAsistencialDto.getDescripcion());
        noAsistencial.setLegajos(noAsistencialDto.getLegajos());

        noAsistencialService.save(noAsistencial);
        return new ResponseEntity(new Mensaje("no asistencial creado"), HttpStatus.OK);
    }
}
