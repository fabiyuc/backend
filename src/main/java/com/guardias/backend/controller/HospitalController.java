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

import com.guardias.backend.dto.HospitalDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.service.HospitalService;

@Controller
@RequestMapping("/hospital")
@CrossOrigin(origins = "http://localhost:4200")
public class HospitalController {

    @Autowired
    HospitalService hospitalService;

    @Autowired
    EfectorController efectorController;

    @GetMapping("/list")
    public ResponseEntity<List<Hospital>> list() {
        List<Hospital> list = hospitalService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listPasivas")
    public ResponseEntity<List<Hospital>> listPasivas() {
        List<Hospital> list = hospitalService.findByAdmitePasiva();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Hospital>> getById(@PathVariable("id") Long id) {
        if (!hospitalService.existsById(id))
            return new ResponseEntity(new Mensaje("Hospital no encontrado"), HttpStatus.NOT_FOUND);
        Hospital hospital = hospitalService.findById(id).get();
        return new ResponseEntity(hospital, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Hospital>> getById(@PathVariable("nombre") String nombre) {
        if (!hospitalService.existsByNombre(nombre))
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

        return hospital;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody HospitalDto hospitalDto) {
        ResponseEntity<?> respuestaValidaciones = efectorController.validations(hospitalDto);

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
        if (!hospitalService.existsById(id))
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

    // @PostMapping("/{idEfector}/addAutoridad/{idAutoridad}")
    // public ResponseEntity<?> agregarAutoridad(@PathVariable("idEfector") Long
    // idEfector,
    // @PathVariable("idAutoridad") Long idAutoridad) {
    // ResponseEntity<?> respuestaValidaciones =
    // efectorController.agregarAutoridad(idEfector, idAutoridad);
    // return respuestaValidaciones;
    // }

    // @PostMapping("/{idEfector}/addNotificacion/{idNotificacion}")
    // public ResponseEntity<?> agregarNotificacion(@PathVariable("idEfector") Long
    // idEfector,
    // @PathVariable("idNotificacion") Long idNotificacion) {
    // ResponseEntity<?> respuestaValidaciones =
    // efectorController.agregarNotificacion(idEfector, idNotificacion);
    // return respuestaValidaciones;
    // }

    // @PostMapping("/{idEfector}/addLegajo/{idLegajo}")
    // public ResponseEntity<?> agregarLegajo(@PathVariable("idEfector") Long
    // idEfector,
    // @PathVariable("idLegajo") Long idLegajo) {
    // ResponseEntity<?> respuestaValidaciones =
    // efectorController.agregarLegajo(idEfector, idLegajo);
    // return respuestaValidaciones;
    // }

    // @PostMapping("/{idEfector}/addUdo/{idLegajoUdo}")
    // public ResponseEntity<?> agregarLegajoUdo(@PathVariable("idEfector") Long
    // idEfector,
    // @PathVariable("idLegajoUdo") Long idLegajoUdo) {
    // ResponseEntity<?> respuestaValidaciones =
    // efectorController.agregarLegajoUdo(idEfector, idLegajoUdo);
    // return respuestaValidaciones;
    // }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        ResponseEntity<?> respuestaValidaciones = efectorController.logicDelete(id);
        return respuestaValidaciones;
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!hospitalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el hospital"), HttpStatus.NOT_FOUND);
        hospitalService.deleteById(id);
        return new ResponseEntity(new Mensaje("Hospital eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
