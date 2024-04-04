package com.guardias.backend.controller;

import java.util.ArrayList;
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

import com.guardias.backend.dto.HospitalDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Caps;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.service.CapsService;
import com.guardias.backend.service.HospitalService;

@Controller
@RequestMapping("/hospital")
@CrossOrigin(origins = "http://localhost:4200")
public class HospitalController {

    @Autowired
    HospitalService hospitalService;

    @Autowired
    CapsService capsService;

    @Autowired
    EfectorController efectorController;

    @GetMapping("/list")
    public ResponseEntity<List<Hospital>> list() {
        List<Hospital> list = hospitalService.findByActivoTrue();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Hospital>> listAll() {
        List<Hospital> list = hospitalService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listPasivas")
    public ResponseEntity<List<Hospital>> listPasivas() {
        List<Hospital> list = hospitalService.findByAdmitePasiva();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Hospital>> getById(@PathVariable("id") Long id) {
        if (!hospitalService.activo(id))
            return new ResponseEntity(new Mensaje("Hospital no encontrado"), HttpStatus.NOT_FOUND);
        Hospital hospital = hospitalService.findById(id).get();
        return new ResponseEntity(hospital, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Hospital>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!hospitalService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("Hospital no encontrado"), HttpStatus.NOT_FOUND);
        Hospital hospital = hospitalService.findByNombre(nombre).get();
        return new ResponseEntity(hospital, HttpStatus.OK);
    }

    private Hospital createUpdate(Hospital hospital, HospitalDto hospitalDto) {
        Efector efector = efectorController.createUpdate(hospital, hospitalDto);
        hospital = (Hospital) efector;

        hospital.setEsCabecera(hospitalDto.isEsCabecera());
        hospital.setAdmitePasiva(hospitalDto.isAdmitePasiva());
        hospital.setNivelComplejidad(hospitalDto.getNivelComplejidad());

        if (hospitalDto.getIdCaps() != null) {
            List<Long> idList = new ArrayList();
            if (hospital.getCaps() != null) {
                for (Caps caps : hospital.getCaps()) {
                    for (Long id : hospitalDto.getIdCaps()) {
                        if (!caps.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }
            List<Long> idsToAdd = idList.isEmpty() ? hospitalDto.getIdCaps() : idList;
            for (Long id : idsToAdd) {
                hospital.getCaps().add(capsService.findById(id).get());
                capsService.findById(id).get().setCabecera(hospital);
            }
        }

        return hospital;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody HospitalDto hospitalDto) {
        ResponseEntity<?> respuestaValidaciones = efectorController.validationsCreate(hospitalDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Hospital hospital = createUpdate(new Hospital(), hospitalDto);
            hospitalService.save(hospital);
            return new ResponseEntity(new Mensaje("Hospital creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody HospitalDto hospitalDto) {
        if (!hospitalService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el hospital"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = efectorController.validations(hospitalDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Hospital hospital = createUpdate(hospitalService.findById(id).get(), hospitalDto);
            hospitalService.save(hospital);
            return new ResponseEntity(new Mensaje("Hospital modificado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!hospitalService.activo(id))
            return new ResponseEntity(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);

        Hospital hospital = hospitalService.findById(id).get();
        hospital.setActivo(false);
        hospitalService.save(hospital);
        return new ResponseEntity(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!hospitalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el hospital"), HttpStatus.NOT_FOUND);
        hospitalService.deleteById(id);
        return new ResponseEntity(new Mensaje("Hospital eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
