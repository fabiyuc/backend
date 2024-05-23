package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.guardias.backend.dto.PendientesSemanalDto;
import com.guardias.backend.entity.PendientesSemanal;
import com.guardias.backend.entity.RegistrosPendientes;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.PendientesSemanalService;
import com.guardias.backend.service.RegistrosPendientesService;

@RestController
@RequestMapping("/pendientesSemanal")
@CrossOrigin(origins = "http://localhost:4200")
public class PendientesSemanalController {
    @Autowired
    PendientesSemanalService pendientesSemanalService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    RegistrosPendientesService registrosPendientesService;

    @GetMapping("/list")
    public ResponseEntity<List<PendientesSemanal>> list() {
        List<PendientesSemanal> list = pendientesSemanalService.findByActivo();
        return new ResponseEntity<List<PendientesSemanal>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<PendientesSemanal>> listAll() {
        List<PendientesSemanal> list = pendientesSemanalService.findAll();
        return new ResponseEntity<List<PendientesSemanal>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PendientesSemanal> getById(@PathVariable("id") Long id) {
        if (!pendientesSemanalService.activo(id))
            return new ResponseEntity(new Mensaje("No se encontraron pendientes semanales"), HttpStatus.NOT_FOUND);
        PendientesSemanal pendientesSemanal = pendientesSemanalService.findById(id).get();
        return new ResponseEntity(pendientesSemanal, HttpStatus.OK);
    }

    @GetMapping("/listMes/{idEfector}/{mes}/{anio}")
    public ResponseEntity<List<PendientesSemanal>> getByMes(@PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("mes") String mes, @PathVariable("anio") int anio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<PendientesSemanal> list = pendientesSemanalService.findByAnioAndMesAndEfectorId(anio, mesEnum,
                    idEfector);
            return new ResponseEntity<List<PendientesSemanal>>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registro no encontrado"), HttpStatus.BAD_REQUEST);
        }
    }

    public PendientesSemanal createUpdate(PendientesSemanal pendientesSemanal,
            PendientesSemanalDto pendientesSemanalDto) {

        if (pendientesSemanalDto.getFechaInicio() != pendientesSemanal.getFechaInicio()
                && pendientesSemanalDto.getFechaInicio() != null)
            pendientesSemanal.setFechaInicio(pendientesSemanalDto.getFechaInicio());

        if (pendientesSemanalDto.getFechaFin() != pendientesSemanal.getFechaFin()
                && pendientesSemanalDto.getFechaFin() != null)
            pendientesSemanal.setFechaFin(pendientesSemanalDto.getFechaFin());

        if (pendientesSemanalDto.getMes() != pendientesSemanal.getMes() && pendientesSemanalDto.getMes() != null)
            pendientesSemanal.setMes(pendientesSemanalDto.getMes());

        if (pendientesSemanalDto.getAnio() != pendientesSemanal.getAnio())
            pendientesSemanal.setAnio(pendientesSemanalDto.getAnio());

        if (pendientesSemanalDto.getIdEfector() != pendientesSemanal.getEfector().getId()
                && pendientesSemanalDto.getIdEfector() != null)
            pendientesSemanal.setEfector(efectorService.findById(pendientesSemanalDto.getIdEfector()));

        if (pendientesSemanalDto.getIdRegistrosPendientes() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (pendientesSemanal.getRegistrosPendientes() != null) {
                for (RegistrosPendientes registrosPendientes : pendientesSemanal.getRegistrosPendientes()) {
                    for (Long id : pendientesSemanalDto.getIdRegistrosPendientes()) {
                        if (!registrosPendientes.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                pendientesSemanal.setRegistrosPendientes(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? pendientesSemanalDto.getIdRegistrosPendientes() : idList;
            for (Long id : idsToAdd) {
                pendientesSemanal.getRegistrosPendientes().add(registrosPendientesService.findById(id).get());
                registrosPendientesService.findById(id).get().setPendientesSemanal(pendientesSemanal);
            }
        }

        pendientesSemanal.setActivo(true);
        return pendientesSemanal;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PendientesSemanalDto pendientesSemanalDto) {

        PendientesSemanal pendientesSemanal = createUpdate(new PendientesSemanal(), pendientesSemanalDto);
        pendientesSemanalService.save(pendientesSemanal);
        return ResponseEntity.ok(new Mensaje("pendiente semanal registrado correctamente"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody PendientesSemanalDto pendientesSemanalDto) {

        if (!pendientesSemanalService.activo(id))
            return new ResponseEntity(new Mensaje("pendiente semanal no existe"), HttpStatus.NOT_FOUND);

        PendientesSemanal pendientesSemanal = createUpdate(pendientesSemanalService.findById(id).get(),
                pendientesSemanalDto);
        pendientesSemanalService.save(pendientesSemanal);

        return ResponseEntity.ok(new Mensaje("pendiente semanal modificado correctamente"));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!pendientesSemanalService.activo(id))
            return new ResponseEntity(new Mensaje("pendiente semanal no existe"), HttpStatus.NOT_FOUND);

        PendientesSemanal pendientesSemanal = pendientesSemanalService.findById(id).get();
        pendientesSemanal.setActivo(false);
        pendientesSemanalService.save(pendientesSemanal);
        return new ResponseEntity<>(new Mensaje("pendiente semanal eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!pendientesSemanalService.existsById(id))
            return new ResponseEntity(new Mensaje("pendiente semanal no existe"), HttpStatus.NOT_FOUND);

        pendientesSemanalService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("pendiente semanal eliminado FISICAMENTE"), HttpStatus.OK);
    }
}
