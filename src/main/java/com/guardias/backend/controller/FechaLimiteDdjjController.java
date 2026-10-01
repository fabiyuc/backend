package com.guardias.backend.controller;

import java.time.LocalDateTime;
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

import com.guardias.backend.dto.FechaLimiteDdjj.FechaLimiteDdjjBajaDto;
import com.guardias.backend.dto.FechaLimiteDdjj.FechaLimiteDdjjDto;
import com.guardias.backend.dto.FechaLimiteDdjj.FechaLimiteDdjjListDto;
import com.guardias.backend.dto.FechaLimiteDdjj.FechaLimiteVigenteDto;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.service.FechaLimiteDdjjService;
import com.guardias.backend.service.FechaSistemaService;

@RestController
@RequestMapping("/fechaLimiteDdjj")
@CrossOrigin(origins = "http://localhost:4200")
public class FechaLimiteDdjjController {

    @Autowired
    FechaLimiteDdjjService fechaLimiteDdjjService;
    @Autowired
    FechaSistemaService fechaSistemaService;

    // Fechas límite vigentes
    @GetMapping("/list")
    public ResponseEntity<List<FechaLimiteDdjjListDto>> list() {
        return new ResponseEntity<>(fechaLimiteDdjjService.listVigentes(), HttpStatus.OK);
    }

    // Histórico completo
    @GetMapping("/historial")
    public ResponseEntity<List<FechaLimiteDdjjListDto>> historialAll() {
        return new ResponseEntity<>(fechaLimiteDdjjService.historialAll(), HttpStatus.OK);
    }

    // Histórico de un mes/año/tipo de guardia
    @GetMapping("/historial/{mes}/{anio}/{idTipoGuardia}")
    public ResponseEntity<List<FechaLimiteDdjjListDto>> historial(@PathVariable("mes") MesesEnum mes,
            @PathVariable("anio") int anio, @PathVariable("idTipoGuardia") Long idTipoGuardia) {
        return new ResponseEntity<>(fechaLimiteDdjjService.historial(mes, anio, idTipoGuardia), HttpStatus.OK);
    }

    // Fecha límite que aplica (la cargada o la regla por defecto)
    @GetMapping("/vigente/{mes}/{anio}/{idTipoGuardia}")
    public ResponseEntity<FechaLimiteVigenteDto> vigente(@PathVariable("mes") MesesEnum mes,
            @PathVariable("anio") int anio, @PathVariable("idTipoGuardia") Long idTipoGuardia) {
        return new ResponseEntity<>(fechaLimiteDdjjService.obtenerFechaLimite(mes, anio, idTipoGuardia),
                HttpStatus.OK);
    }

    // Igual que /vigente, indicando el tipo de guardia por nombre (ej: CONTRAFACTURA)
    @GetMapping("/vigenteByTipo/{mes}/{anio}/{tipoGuardia}")
    public ResponseEntity<FechaLimiteVigenteDto> vigenteByTipo(@PathVariable("mes") MesesEnum mes,
            @PathVariable("anio") int anio, @PathVariable("tipoGuardia") TipoGuardiaEnum tipoGuardia) {
        return new ResponseEntity<>(fechaLimiteDdjjService.obtenerFechaLimite(mes, anio, tipoGuardia),
                HttpStatus.OK);
    }

    // Fecha "de hoy" del backend (real o simulada con app.fecha-simulada)
    @GetMapping("/hoy")
    public ResponseEntity<LocalDateTime> hoy() {
        return new ResponseEntity<>(fechaSistemaService.ahora(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody FechaLimiteDdjjDto dto) {
        return fechaLimiteDdjjService.crear(dto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody FechaLimiteDdjjDto dto) {
        return fechaLimiteDdjjService.modificar(id, dto);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id, @RequestBody FechaLimiteDdjjBajaDto bajaDto) {
        return fechaLimiteDdjjService.eliminar(id, bajaDto);
    }
}
