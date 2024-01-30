package com.guardias.backend.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.AsistencialDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.service.AsistencialService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/asistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class AsistencialController {

    @Autowired
    AsistencialService asistencialService;

    @GetMapping("/lista")
    public ResponseEntity<List<Asistencial>> list() {
        List<Asistencial> list = asistencialService.list();
        return new ResponseEntity<List<Asistencial>>(list, HttpStatus.OK);
    }

    // *** POSTMAN: /asistencial/listaestado?estado=true o
    // /asistencial/lista?estado=false
    @GetMapping("/listaestado")
    public ResponseEntity<List<Asistencial>> list(@RequestParam("estado") Boolean estado) {
        List<Asistencial> list = asistencialService.findByEstado(estado);
        return new ResponseEntity<List<Asistencial>>(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Asistencial> getById(@PathVariable("id") Long id) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la persona tipo asistencial"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findById(id).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);
    }

    @GetMapping("/detalledni/{dni}")
    public ResponseEntity<Asistencial> getByDni(@PathVariable("dni") int dni) {
        if (!asistencialService.existsByDni(dni))
            return new ResponseEntity(new Mensaje("no existe asistencial con ese dni"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findByDni(dni).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AsistencialDto asistencialDto) {

        if (StringUtils.isBlank(asistencialDto.getNombre())) {
            return new ResponseEntity<>(new Mensaje("El Nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getApellido())) {
            return new ResponseEntity<>(new Mensaje("El Apellido es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (asistencialDto.getDni() < 1000000) 
            return new ResponseEntity<>(new Mensaje("DNI es incorrecto"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(asistencialDto.getCuil())) {
            return new ResponseEntity<>(new Mensaje("El Cuil es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (asistencialDto.getFechaNacimiento() == null) {
            return new ResponseEntity(new Mensaje("La fecha de nacimiento es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getSexo())) {
            return new ResponseEntity<>(new Mensaje("Es obligatorio indicar el sexo"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(asistencialDto.getTelefono())) {
            return new ResponseEntity<>(new Mensaje("Es obligatorio indicar el telefono"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(asistencialDto.getEmail())) {
            return new ResponseEntity<>(new Mensaje("Es obligatorio indicar el email"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(asistencialDto.getDomicilio())) {
            return new ResponseEntity<>(new Mensaje("Es obligatorio indicar el domicilio"), HttpStatus.BAD_REQUEST);
        }

        if (asistencialDto.getEstado() == null)
            return new ResponseEntity(new Mensaje("indicar si el estado es True o False"), HttpStatus.BAD_REQUEST);

        Asistencial asistencial = new Asistencial();

        asistencial.setApellido(asistencialDto.getApellido());
        asistencial.setNombre(asistencialDto.getNombre());
        asistencial.setDni(asistencialDto.getDni());
        asistencial.setCuil(asistencialDto.getCuil());
        asistencial.setFechaNacimiento(asistencialDto.getFechaNacimiento());
        asistencial.setSexo(asistencialDto.getSexo());
        asistencial.setTelefono(asistencialDto.getTelefono());
        asistencial.setEmail(asistencialDto.getEmail());
        asistencial.setDomicilio(asistencialDto.getDomicilio());
        asistencial.setEstado(asistencialDto.getEstado());
        
        
        asistencial.setLegajos(asistencialDto.getLegajos());
        asistencial.setTipoGuardia(asistencialDto.getTipoGuardia());
        
        /* asistencial.setDistribucionesHorarias(asistencialDto.getDistribucionesHorarias()); */ // MAPEAR AL TIPO DE
                                                                                           // DISTRIBUCION HORARIA
                                                                                           // CORRECTO

        asistencialService.save(asistencial);
        return new ResponseEntity(new Mensaje("asistencial creado"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody AsistencialDto asistencialDto) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        if (asistencialDto.getDni() < 1000000)
            return new ResponseEntity<>(new Mensaje("DNI es incorrecto"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(asistencialDto.getApellido())) {
            return new ResponseEntity<>(new Mensaje("El Apellido es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getNombre())) {
            return new ResponseEntity<>(new Mensaje("El Nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getCuil())) {
            return new ResponseEntity<>(new Mensaje("El Cuil es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        Asistencial asistencial = asistencialService.findById(id).get();

        asistencial.setApellido(asistencialDto.getApellido());
        asistencial.setNombre(asistencialDto.getNombre());
        asistencial.setDni(asistencialDto.getDni());
        asistencial.setCuil(asistencialDto.getCuil());
        asistencial.setFechaNacimiento(asistencialDto.getFechaNacimiento());
        asistencial.setSexo(asistencialDto.getSexo());
        asistencial.setTelefono(asistencialDto.getTelefono());
        asistencial.setEmail(asistencialDto.getEmail());
        asistencial.setDomicilio(asistencialDto.getDomicilio());
        asistencial.setEstado(asistencialDto.getEstado());
        asistencial.setLegajos(asistencialDto.getLegajos());
        asistencial.setDistribucionesHorarias(asistencialDto.getDistribucionesHorarias());

        asistencialService.save(asistencial);

        return new ResponseEntity<>(new Mensaje("Asistencial Actualizada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        asistencialService.delete(id);
        return new ResponseEntity<>(new Mensaje("Asistencial eliminado"), HttpStatus.OK);

    }
}
