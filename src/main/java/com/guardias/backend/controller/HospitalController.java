package com.guardias.backend.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.service.HospitalService;

@Controller
@RequestMapping("/hospital")
@CrossOrigin(origins = "http://localhost:4200")
public class HospitalController {

    @Autowired
    HospitalService hospitalService;

    @GetMapping("/list")
    public ResponseEntity<List<Hospital>> list() {
        List<Hospital> list = hospitalService.findByActivo(true);
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

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody HospitalDto hospitalDto) {
        if (StringUtils.isBlank(hospitalDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(hospitalDto.getDomicilio()))
            return new ResponseEntity(new Mensaje("el domicilio es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (hospitalService.existsByNombre(hospitalDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);
        Hospital hospital = new Hospital();
        hospital.setNombre(hospitalDto.getNombre());
        hospital.setDomicilio(hospitalDto.getDomicilio());
        hospital.setTelefono(hospitalDto.getTelefono());
        hospital.setEstado(hospitalDto.isEstado());
        hospital.setRegion(hospitalDto.getRegion());
        hospital.setLocalidad(hospitalDto.getLocalidad());
        hospital.setObservacion(hospitalDto.getObservacion());
        hospital.setEsCabecera(hospitalDto.isEsCabecera());
        hospital.setAdmitePasiva(hospitalDto.isAdmitePasiva());

        hospitalService.save(hospital);
        return new ResponseEntity(new Mensaje("Hospital creado correctamente"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody HospitalDto hospitalDto) {
        if (!hospitalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el hospital"), HttpStatus.NOT_FOUND);

        // if (hospitalService.existsByNombre(hospitalDto.getNombre()) &&
        // hospitalService.getHospitalByNombre(hospitalDto.getNombre()).get().getId() ==
        // id)
        // return new ResponseEntity(new Mensaje("ese hospital ya existe"),
        // HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(hospitalDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        Hospital hospital = hospitalService.findById(id).get();

        if (hospital.getNombre() != hospitalDto.getNombre() && hospitalDto.getNombre() != null
                && !hospitalDto.getNombre().isEmpty())
            hospital.setNombre(hospitalDto.getNombre());

        if (hospital.getDomicilio() != hospitalDto.getDomicilio() && hospitalDto.getDomicilio() != null
                && !hospitalDto.getDomicilio().isEmpty())
            hospital.setDomicilio(hospitalDto.getDomicilio());

        if (hospital.getTelefono() != hospitalDto.getTelefono() && hospitalDto.getTelefono() != null
                && !hospitalDto.getTelefono().isEmpty())
            hospital.setTelefono(hospitalDto.getTelefono());

        if (hospital.isEstado() != hospitalDto.isEstado())
            hospital.setEstado(hospitalDto.isEstado());

        if (!hospitalDto.getRegion().equals(hospital.getRegion())) {
            hospital.setRegion(hospitalDto.getRegion());
        }
        if (!hospitalDto.getLocalidad().equals(hospital.getLocalidad())) {
            hospital.setLocalidad(hospitalDto.getLocalidad());
        }

        if (hospital.getObservacion() != hospitalDto.getObservacion() && hospitalDto.getObservacion() != null
                && !hospitalDto.getObservacion().isEmpty())
            hospital.setObservacion(hospitalDto.getObservacion());

        if (hospital.isEsCabecera() != hospitalDto.isEsCabecera())
            hospital.setEsCabecera(hospitalDto.isEsCabecera());

        if (hospital.isAdmitePasiva() != hospitalDto.isAdmitePasiva())
            hospital.setAdmitePasiva(hospitalDto.isAdmitePasiva());

        hospitalService.save(hospital);
        return new ResponseEntity(new Mensaje("Hospital actualizado"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!hospitalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el hospital"), HttpStatus.NOT_FOUND);

        Hospital hospital = hospitalService.findById(id).get();
        hospital.setActivo(false);
        hospitalService.save(hospital);
        return new ResponseEntity(new Mensaje("Hospital eliminado"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!hospitalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el hospital"), HttpStatus.NOT_FOUND);
        hospitalService.deleteById(id);
        return new ResponseEntity(new Mensaje("Hospital eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
