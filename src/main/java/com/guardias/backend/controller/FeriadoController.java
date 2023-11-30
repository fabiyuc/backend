package com.guardias.backend.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guardias.backend.dto.FeriadoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Feriado;
import com.guardias.backend.service.FeriadoService;

import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/feriado")
@CrossOrigin(origins = "http://localhost:4200")
public class FeriadoController {

    @Autowired
    FeriadoService feriadoService;

    @GetMapping("/lista")
    public ResponseEntity<List<Feriado>> list() {
        List<Feriado> list = feriadoService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Feriado>> getById(@PathVariable("id") Long id) {
        if (!feriadoService.existsById(id))
            return new ResponseEntity(new Mensaje("Fecha no encontrada"), HttpStatus.NOT_FOUND);
        Feriado feriado = feriadoService.getById(id).get();
        return new ResponseEntity(feriado, HttpStatus.OK);
    }

    @GetMapping("/detalle/{motivo}")
    public ResponseEntity<List<Feriado>> getById(@PathVariable("motivo") String motivo) {
        if (!feriadoService.existsByMotivo(motivo))
            return new ResponseEntity(new Mensaje("Fecha no encontrada"), HttpStatus.NOT_FOUND);
        Feriado feriado = feriadoService.getByMotivo(motivo).get();
        return new ResponseEntity(feriado, HttpStatus.OK);
    }

    @GetMapping("/detalle/{fecha}")
    public ResponseEntity<List<Feriado>> getByFecha(@PathVariable("fecha") LocalDate fecha) {
        if (!feriadoService.existsByFecha(fecha))
            return new ResponseEntity(new Mensaje("Fecha no encontrada"), HttpStatus.NOT_FOUND);
        Feriado feriado = feriadoService.getByFecha(fecha).get();
        return new ResponseEntity(feriado, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody FeriadoDto feriadoDto) {
        if (StringUtils.isBlank(feriadoDto.getMotivo()))
            return new ResponseEntity(new Mensaje("El motivo es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(feriadoDto.getTipoFeriado()))
            return new ResponseEntity(new Mensaje("El tipo de feriado es obligatorio"), HttpStatus.BAD_REQUEST);
        if (feriadoDto.getDia() == null)
            return new ResponseEntity(new Mensaje("La fecha es obligatoria"), HttpStatus.BAD_REQUEST);

        if (feriadoService.existsByMotivo(feriadoDto.getMotivo()))
            return new ResponseEntity(new Mensaje("ese motivo ya existe"), HttpStatus.BAD_REQUEST);

        Feriado feriado = new Feriado();
        feriado.setFecha(feriadoDto.getDia());
        feriado.setMotivo(feriadoDto.getMotivo());
        feriado.setTipoFeriado(feriadoDto.getTipoFeriado());
        feriado.setDescripcion(feriadoDto.getDescripcion());

        feriadoService.save(feriado);
        return new ResponseEntity(new Mensaje("Feriado creado correctamente"), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody FeriadoDto feriadoDto) {

        if (!feriadoService.existsById(id))
            return new ResponseEntity(new Mensaje("El feriado no existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(feriadoDto.getMotivo()))
            return new ResponseEntity(new Mensaje("El motivo es obligatorio"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(feriadoDto.getTipoFeriado()))
            return new ResponseEntity(new Mensaje("El tipo de feriado es obligatorio"), HttpStatus.BAD_REQUEST);
        if (feriadoDto.getDia() == null)
            return new ResponseEntity(new Mensaje("La fecha es obligatoria"), HttpStatus.BAD_REQUEST);

        if (feriadoService.existsByMotivo(feriadoDto.getMotivo()))
            return new ResponseEntity(new Mensaje("ese motivo ya existe"), HttpStatus.BAD_REQUEST);

        Feriado feriado = feriadoService.getById(id).get();

        if (feriado.getFecha() != feriadoDto.getDia() && feriadoDto.getDia() != null)
            feriado.setFecha(feriadoDto.getDia());
        if (feriado.getMotivo() != feriadoDto.getMotivo() && feriadoDto.getMotivo() != null
                && !feriadoDto.getMotivo().isEmpty())
            feriado.setMotivo(feriadoDto.getMotivo());
        if (feriado.getTipoFeriado() != feriadoDto.getTipoFeriado() && feriadoDto.getTipoFeriado() != null
                && !feriadoDto.getTipoFeriado().isEmpty())
            feriado.setTipoFeriado(feriadoDto.getTipoFeriado());
        if (feriado.getDescripcion() != feriadoDto.getDescripcion() && feriadoDto.getDescripcion() != null
                && !feriadoDto.getDescripcion().isEmpty())
            feriado.setDescripcion(feriadoDto.getDescripcion());

        feriadoService.save(feriado);
        return new ResponseEntity(new Mensaje("Feriado modificado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!feriadoService.existsById(id))
            return new ResponseEntity(new Mensaje("el feriado no existe"), HttpStatus.NOT_FOUND);
        feriadoService.deleteById(id);
        return new ResponseEntity(new Mensaje("feriado eliminado"), HttpStatus.OK);
    }

}
