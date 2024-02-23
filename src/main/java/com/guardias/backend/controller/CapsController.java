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
import com.guardias.backend.dto.CapsDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Caps;
import com.guardias.backend.service.CapsService;
import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/caps")
@CrossOrigin(origins = "http://localhost:4200")
public class CapsController {

    @Autowired
    CapsService capsService;

    @GetMapping("/list")
    public ResponseEntity<List<Caps>> list() {
        List<Caps> list = capsService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Caps>> getById(@PathVariable("id") Long id) {
        if (!capsService.existsById(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Caps caps = capsService.findById(id).get();
        return new ResponseEntity(caps, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Caps>> getById(@PathVariable("nombre") String nombre) {
        if (!capsService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Caps caps = capsService.findByNombre(nombre).get();
        return new ResponseEntity(caps, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CapsDto capsDto) {
        if (StringUtils.isBlank(capsDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(capsDto.getDomicilio()))
            return new ResponseEntity(new Mensaje("el domicilio es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (capsService.existsByNombre(capsDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);
        Caps caps = new Caps();
        caps.setNombre(capsDto.getNombre());
        caps.setDomicilio(capsDto.getDomicilio());
        caps.setTelefono(capsDto.getTelefono());
        caps.setEstado(capsDto.isEstado());
        caps.setRegion(capsDto.getRegion());
        caps.setLocalidad(capsDto.getLocalidad());
        caps.setObservacion(capsDto.getObservacion());
        caps.setCabecera(capsDto.getCabecera());
        caps.setTipoCaps(capsDto.getTipoCaps());
        caps.setAreaProgramatica(capsDto.getAreaProgramatica());

        capsService.save(caps);
        return new ResponseEntity(new Mensaje("Caps creado correctamente"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CapsDto capsDto) {
        if (!capsService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        if (StringUtils.isBlank(capsDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        Caps caps = capsService.findById(id).get();

        if (caps.getNombre() != capsDto.getNombre() && capsDto.getNombre() != null && !capsDto.getNombre().isEmpty()) {
            caps.setNombre(capsDto.getNombre());
        }
        if (caps.getDomicilio() != capsDto.getDomicilio() && capsDto.getDomicilio() != null
                && !capsDto.getDomicilio().isEmpty()) {
            caps.setDomicilio(capsDto.getDomicilio());
        }
        if (caps.getTelefono() != capsDto.getTelefono() && capsDto.getTelefono() != null
                && !capsDto.getTelefono().isEmpty()) {
            caps.setTelefono(capsDto.getTelefono());
        }
        if (caps.isEstado() != capsDto.isEstado()) {
            caps.setEstado(capsDto.isEstado());
        }
        if (!capsDto.getRegion().equals(caps.getRegion())) {
            caps.setRegion(capsDto.getRegion());
        }
        if (!capsDto.getLocalidad().equals(caps.getLocalidad())) {
            caps.setLocalidad(capsDto.getLocalidad());
        }
        if (caps.getObservacion() != capsDto.getObservacion() && capsDto.getObservacion() != null
                && !capsDto.getObservacion().isEmpty()) {
            caps.setObservacion(capsDto.getObservacion());
        }
        if (caps.getCabecera() != capsDto.getCabecera() && capsDto.getCabecera() != null) {
            caps.setCabecera(capsDto.getCabecera());
        }
        if (caps.getTipoCaps() != capsDto.getTipoCaps() && capsDto.getTipoCaps() != null
                && !capsDto.getTipoCaps().isEmpty()) {
            caps.setTipoCaps(capsDto.getTipoCaps());
        }

        if (capsDto.getAreaProgramatica() != caps.getAreaProgramatica()) {
            caps.setAreaProgramatica(capsDto.getAreaProgramatica());
        }

        capsService.save(caps);
        return new ResponseEntity(new Mensaje("Caps actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (!capsService.existsById(id))
            return new ResponseEntity(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);
        capsService.deleteById(id);
        return new ResponseEntity(new Mensaje("Efector eliminado"), HttpStatus.OK);
    }

}
