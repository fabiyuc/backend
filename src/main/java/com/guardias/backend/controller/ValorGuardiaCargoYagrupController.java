package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.valorGuardia.GrillaValorGuardiaCompletaDto;
import com.guardias.backend.dto.valorGuardia.ValorGuardiaManualDto;
import com.guardias.backend.entity.ValorGuardiaCargoYagrup;
import com.guardias.backend.repository.HospitalRepository;
import com.guardias.backend.service.ValorGuardiaCargoYagrupService;

@RestController
@RequestMapping("/valorGuardiaCargoYagrup")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorGuardiaCargoYagrupController {

    @Autowired
    ValorGuardiaCargoYagrupService valorGuardiaCargoYagrupService;
    @Autowired
    HospitalRepository hospitalRepository;

    @GetMapping("/list")
    public ResponseEntity<List<ValorGuardiaCargoYagrup>> list() {
        List<ValorGuardiaCargoYagrup> valorGuardiaCyAList = valorGuardiaCargoYagrupService.findByActivoTrue()
                .orElse(new ArrayList<>());

        return new ResponseEntity<List<ValorGuardiaCargoYagrup>>(valorGuardiaCyAList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ValorGuardiaCargoYagrup>> listAll() {
        List<ValorGuardiaCargoYagrup> list = valorGuardiaCargoYagrupService.findAll();
        return new ResponseEntity<List<ValorGuardiaCargoYagrup>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ValorGuardiaCargoYagrup> getById(@PathVariable("id") Long id) {
        if (!valorGuardiaCargoYagrupService.activo(id))
            return new ResponseEntity(new Mensaje("No existe el valor de guardia"), HttpStatus.NOT_FOUND);
        ValorGuardiaCargoYagrup valorGuardiaCyA = valorGuardiaCargoYagrupService.findById(id).get();
        return new ResponseEntity<ValorGuardiaCargoYagrup>(valorGuardiaCyA, HttpStatus.OK);
    }

    // Buscar el valor activo para este hospital
    @GetMapping("/valorByEfector/{idHospital}")
    public ResponseEntity<ValorGuardiaCargoYagrup> valorByEfector(@PathVariable("idHospital") Long idHospital) {

        return valorGuardiaCargoYagrupService
                .obtenerValorGuardiaCargoPorHospital(idHospital)
                .map(valor -> ResponseEntity.ok(valor))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/cargar-manual")
    public ResponseEntity<?> cargarValoresManual(@RequestBody List<ValorGuardiaManualDto> listaValores) {
        try {
            valorGuardiaCargoYagrupService.guardarCargaManual(listaValores);
            return new ResponseEntity<>(new Mensaje("Valores de guardia cargados correctamente"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Mensaje("Error al cargar valores: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/grilla-completa/{fecha}")
    public ResponseEntity<List<GrillaValorGuardiaCompletaDto>> getGrillaCompleta(@PathVariable("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
    
        List<GrillaValorGuardiaCompletaDto> grilla = valorGuardiaCargoYagrupService.obtenerGrillaJerarquica(fecha);
        return ResponseEntity.ok(grilla);
    }

}
