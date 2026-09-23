package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.entity.ValorGuardiaExtrayCF;
import com.guardias.backend.service.HospitalService;
import com.guardias.backend.service.ValorGuardiaExtraYcfService;

@RestController
@RequestMapping("/valorGuardiaExtraYcf")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorGuardiaExtraYcfController {

    @Autowired
    ValorGuardiaExtraYcfService valorGuardiaExtraYcfService;
    @Autowired
    HospitalService hospitalService;

    @GetMapping("/list")
    public ResponseEntity<List<ValorGuardiaExtrayCF>> list() {
        List<ValorGuardiaExtrayCF> valorGuardiaEyCfList = valorGuardiaExtraYcfService.findByActivoTrue()
                .orElse(new ArrayList<>());

        return new ResponseEntity<List<ValorGuardiaExtrayCF>>(valorGuardiaEyCfList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ValorGuardiaExtrayCF>> listAll() {
        List<ValorGuardiaExtrayCF> list = valorGuardiaExtraYcfService.findAll();
        return new ResponseEntity<List<ValorGuardiaExtrayCF>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ValorGuardiaExtrayCF> getById(@PathVariable("id") Long id) {
        if (!valorGuardiaExtraYcfService.activo(id))
            return new ResponseEntity(new Mensaje("No existe el valor de guardia"), HttpStatus.NOT_FOUND);
        ValorGuardiaExtrayCF valorGuardiaEyCf = valorGuardiaExtraYcfService.findById(id).get();
        return new ResponseEntity<ValorGuardiaExtrayCF>(valorGuardiaEyCf, HttpStatus.OK);
    }

    // Buscar el valor activo para este hospital
    @GetMapping("/valorByEfector/{idHospital}")
    public ResponseEntity<ValorGuardiaExtrayCF> valorByEfector(@PathVariable("idHospital") Long idHospital) {

        Optional<Hospital> hospitalOpt = hospitalService.findById(idHospital);
        if (hospitalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return valorGuardiaExtraYcfService
            .obtenerValorGuardiaExtraPorHospital(hospitalOpt.get(),false,LocalDate.now())
            .map(valor -> ResponseEntity.ok(valor))
            .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
