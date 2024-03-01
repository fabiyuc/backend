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

import com.guardias.backend.dto.IncisoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.service.IncisoService;

import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/inciso")
@CrossOrigin(origins = "http://localhost:4200")
public class IncisoController {
    @Autowired
    IncisoService incisoService;

    @GetMapping("/list")
    public ResponseEntity<List<Inciso>> list() {
        List<Inciso> list = incisoService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Inciso>> getById(@PathVariable("id") Long id) {
        if (!incisoService.existsById(id))
            return new ResponseEntity(new Mensaje("Inciso no encontrado"), HttpStatus.NOT_FOUND);
        Inciso inciso = incisoService.findById(id).get();
        return new ResponseEntity(inciso, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody IncisoDto incisoDto) {
        if (StringUtils.isBlank(incisoDto.getNumero()))
            return new ResponseEntity<Mensaje>(new Mensaje("El numero es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(incisoDto.getDenominacion()))
            return new ResponseEntity<Mensaje>(new Mensaje("La denominacion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (incisoDto.getEstado() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El estado es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (incisoDto.getFechaAlta() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La fecha de alta es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (incisoService.existsByNumero(incisoDto.getNumero()))
            return new ResponseEntity<Mensaje>(new Mensaje("Ese numero ya existe"),
                    HttpStatus.BAD_REQUEST);

        if (incisoService.existsByDenominacion(incisoDto.getDenominacion()))
            return new ResponseEntity<Mensaje>(new Mensaje("Esa denominacion ya existe"),
                    HttpStatus.BAD_REQUEST);

        Inciso inciso = new Inciso();
        inciso.setNumero(incisoDto.getNumero());
        inciso.setDenominacion(incisoDto.getDenominacion());
        inciso.setDetalle(incisoDto.getDetalle());
        inciso.setEstado(incisoDto.getEstado());
        inciso.setFechaAlta(incisoDto.getFechaAlta());
        inciso.setFechaBaja(incisoDto.getFechaBaja());
        inciso.setFechaModificacion(incisoDto.getFechaModificacion());
        inciso.setMotivoModificacion(incisoDto.getMotivoModificacion());
        inciso.setSubIncisos(incisoDto.getSubIncisos());
        inciso.setArticulo(incisoDto.getArticulo());
        inciso.setNovedadPersonal(incisoDto.getNovedadPersonal());

        incisoService.save(inciso);
        return new ResponseEntity<Mensaje>(new Mensaje("Inciso creado correctamente"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody IncisoDto incisoDto) {
        if (StringUtils.isBlank(incisoDto.getNumero()))
            return new ResponseEntity<Mensaje>(new Mensaje("El numero es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(incisoDto.getDenominacion()))
            return new ResponseEntity<Mensaje>(new Mensaje("La denominacion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (incisoDto.getEstado() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El estado es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (incisoDto.getFechaAlta() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La fecha de alta es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (incisoService.existsByNumero(incisoDto.getNumero()))
            return new ResponseEntity<Mensaje>(new Mensaje("Ese numero ya existe"),
                    HttpStatus.BAD_REQUEST);

        if (incisoService.existsByDenominacion(incisoDto.getDenominacion()))
            return new ResponseEntity<Mensaje>(new Mensaje("Esa denominacion ya existe"),
                    HttpStatus.BAD_REQUEST);

        Inciso inciso = incisoService.findById(id).get();
        if (!incisoDto.getNumero().equals(inciso.getNumero()))
            inciso.setNumero(incisoDto.getNumero());
        if (!incisoDto.getDenominacion().equals(inciso.getDenominacion()))
            inciso.setDenominacion(incisoDto.getDenominacion());
        if (!incisoDto.getDetalle().equals(inciso.getDetalle()))
            inciso.setDetalle(incisoDto.getDetalle());
        if (!incisoDto.getEstado().equals(inciso.getEstado()))
            inciso.setEstado(incisoDto.getEstado());
        if (!incisoDto.getFechaAlta().equals(inciso.getFechaAlta()))
            inciso.setFechaAlta(incisoDto.getFechaAlta());
        if (!incisoDto.getFechaBaja().equals(inciso.getFechaBaja()))
            inciso.setFechaBaja(incisoDto.getFechaBaja());
        if (!incisoDto.getFechaModificacion().equals(inciso.getFechaModificacion()))
            inciso.setFechaModificacion(incisoDto.getFechaModificacion());
        if (!incisoDto.getMotivoModificacion().equals(inciso.getMotivoModificacion()))
            inciso.setMotivoModificacion(incisoDto.getMotivoModificacion());
        if (!incisoDto.getSubIncisos().equals(inciso.getSubIncisos()))
            inciso.setSubIncisos(incisoDto.getSubIncisos());
        if (!incisoDto.getArticulo().equals(inciso.getArticulo()))
            inciso.setArticulo(incisoDto.getArticulo());
        if (!incisoDto.getNovedadPersonal().equals(inciso.getNovedadPersonal()))
            inciso.setNovedadPersonal(incisoDto.getNovedadPersonal());

        incisoService.save(inciso);
        return new ResponseEntity<Mensaje>(new Mensaje("Inciso modificado correctamente"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!incisoService.activo(id))
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso no encontrado"), HttpStatus.NOT_FOUND);

        Inciso inciso = incisoService.findById(id).get();
        inciso.setActivo(false);
        incisoService.save(inciso);
        return new ResponseEntity<Mensaje>(new Mensaje("Inciso  eliminado"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!incisoService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso no encontrado"), HttpStatus.NOT_FOUND);
        incisoService.deleteById(id);
        return new ResponseEntity<Mensaje>(new Mensaje("Inciso  eliminado FISICAMENTE"), HttpStatus.OK);
    }
}
