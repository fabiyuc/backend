package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.service.CapsService;
import com.guardias.backend.service.HospitalService;

@Controller
@RequestMapping("/caps")
@CrossOrigin(origins = "http://localhost:4200")
public class CapsController {

    @Autowired
    HospitalService hospitalService;
    @Autowired
    CapsService capsService;

    @Autowired
    EfectorController efectorController;

    @GetMapping("/list")
    public ResponseEntity<List<Caps>> list() {
        List<Caps> capsList = capsService.findByActivoTrue().orElse(new ArrayList<>());
        List<Caps> filteredList = new ArrayList<>();

        for (Caps caps : capsList) {
            List<RegistroActividad> activeRegActividades = new ArrayList<>();
            for (RegistroActividad registroActividad : caps.getRegistrosActividades()) {
                if (registroActividad.isActivo()) {
                    activeRegActividades.add(registroActividad);
                }
            }
            caps.setRegistrosActividades(activeRegActividades);
            filteredList.add(caps);
        }

        return new ResponseEntity<List<Caps>>(filteredList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Caps>> listAll() {
        List<Caps> list = capsService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Caps>> getById(@PathVariable("id") Long id) {
        if (!capsService.activo(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Caps caps = capsService.findById(id).get();
        return new ResponseEntity(caps, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Caps>> getByName(@PathVariable("nombre") String nombre) {
        if (!capsService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Caps caps = capsService.findByNombre(nombre).get();
        return new ResponseEntity(caps, HttpStatus.OK);
    }

    private Caps createUpdate(Caps caps, CapsDto capsDto) {
        Efector efector = efectorController.createUpdate(caps, capsDto);
        caps = (Caps) efector;

        if (caps.getCabecera() == null ||
                (capsDto.getIdCabecera() != null &&
                        !Objects.equals(caps.getCabecera().getId(),
                                capsDto.getIdRegion()))) {
            caps.setCabecera(hospitalService.findById(capsDto.getIdCabecera()).get());
        }

        if (caps.getTipoCaps() == null
                || (capsDto.getTipoCaps() != null &&
                        !Objects.equals(caps.getTipoCaps(), capsDto.getTipoCaps())))
            caps.setTipoCaps(capsDto.getTipoCaps());

        if (!Objects.equals(caps.getAreaProgramatica(), capsDto.getAreaProgramatica()))
            caps.setAreaProgramatica(capsDto.getAreaProgramatica());

        return caps;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CapsDto capsDto) {
        ResponseEntity<?> respuestaValidaciones = efectorController.validations(capsDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Caps caps = createUpdate(new Caps(), capsDto);
            caps.setActivo(true);
            capsService.save(caps);
            return new ResponseEntity(new Mensaje("Caps creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CapsDto capsDto) {
        if (!capsService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = efectorController.validations(capsDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Caps caps = createUpdate(capsService.findById(id).get(), capsDto);
            capsService.save(caps);
            return new ResponseEntity(new Mensaje("Caps creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!capsService.activo(id))
            return new ResponseEntity(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);

        Caps caps = capsService.findById(id).get();
        caps.setActivo(false);
        capsService.save(caps);
        return new ResponseEntity(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!capsService.existsById(id))
            return new ResponseEntity(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);
        capsService.deleteById(id);
        return new ResponseEntity(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
