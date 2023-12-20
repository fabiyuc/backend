package com.guardias.backend.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
        Asistencial asistencial = asistencialService.getone(id).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);
    }

    @GetMapping("/detalledni/{dni}")
    public ResponseEntity<Asistencial> getByDni(@PathVariable("dni") String dni) {
        if (!asistencialService.existsByDni(dni))
            return new ResponseEntity(new Mensaje("no existe asistencial con ese dni"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.getByDni(dni).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AsistencialDto asistencialDto) {

        if (StringUtils.isBlank(asistencialDto.getDni()))
            return new ResponseEntity<>(new Mensaje("El DNI es obligatorio"), HttpStatus.BAD_REQUEST);

        if (asistencialService.existsByDni(asistencialDto.getDni()))
            return new ResponseEntity(new Mensaje("El DNI ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(asistencialDto.getApellido())) {
            return new ResponseEntity<>(new Mensaje("El Apellido es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getNombre())) {
            return new ResponseEntity<>(new Mensaje("El Nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getCuil())) {
            return new ResponseEntity<>(new Mensaje("El Cuil es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (asistencialDto.getFechaNacimiento() == null)
            return new ResponseEntity<>(new Mensaje("La Fecha de Nacimiento es obligatoria"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(asistencialDto.getSexo())) {
            return new ResponseEntity<>(new Mensaje("El Sexo es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getNumCelular())) {
            return new ResponseEntity<>(new Mensaje("El Numero de Celular es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getEmail())) {
            return new ResponseEntity<>(new Mensaje("El E-mail es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getDomicilio())) {
            return new ResponseEntity<>(new Mensaje("El Domicilio es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (asistencialDto.getEstado() == null)
            return new ResponseEntity<>(new Mensaje("El Estado es obligatorio"), HttpStatus.BAD_REQUEST);

        // if (StringUtils.isBlank(asistencialDto.getLegajo())) {
        // return new ResponseEntity<>(new Mensaje("El Legajo es obligatorio"),
        // HttpStatus.BAD_REQUEST);
        // }

        // if (asistencialDto.getLegajos() == null)
        // return new ResponseEntity<>(new Mensaje("El Legajo es obligatorio"),
        // HttpStatus.BAD_REQUEST);

        /*
         * if (StringUtils.isBlank(servicioDto.getDescripcion()))
         * return new ResponseEntity(new Mensaje("la descripcion es obligatoria"),
         * HttpStatus.BAD_REQUEST);
         * if (serviceServicio.existsByDescripcion(servicioDto.getDescripcion()))
         * return new ResponseEntity(new Mensaje("esa descripcion ya existe"),
         * HttpStatus.BAD_REQUEST);
         */
        Asistencial asistencial = new Asistencial();

        asistencial.setApellido(asistencialDto.getApellido());
        asistencial.setNombre(asistencialDto.getNombre());
        asistencial.setDni(asistencialDto.getDni());
        asistencial.setCuil(asistencialDto.getCuil());
        asistencial.setFechaNacimiento(asistencialDto.getFechaNacimiento());
        asistencial.setSexo(asistencialDto.getSexo());
        asistencial.setNumCelular(asistencialDto.getNumCelular());
        asistencial.setEmail(asistencialDto.getEmail());
        asistencial.setDomicilio(asistencialDto.getDomicilio());
        asistencial.setEstado(asistencialDto.getEstado());
        // asistencial.setLegajos(asistencialDto.getLegajos());

        asistencialService.save(asistencial);
        return new ResponseEntity(new Mensaje("asistencial creado"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody AsistencialDto asistencialDto) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        if (asistencialService.existsByDni(asistencialDto.getDni())
                && asistencialService.getByDni(asistencialDto.getDni()).get().getId() != id)
            return new ResponseEntity(new Mensaje("ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(asistencialDto.getDni()))
            return new ResponseEntity<>(new Mensaje("El DNI es obligatorio"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(asistencialDto.getApellido())) {
            return new ResponseEntity<>(new Mensaje("El Apellido es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getNombre())) {
            return new ResponseEntity<>(new Mensaje("El Nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getCuil())) {
            return new ResponseEntity<>(new Mensaje("El Cuil es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (asistencialDto.getFechaNacimiento() == null)
            return new ResponseEntity<>(new Mensaje("La Fecha de Nacimiento es obligatoria"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(asistencialDto.getSexo())) {
            return new ResponseEntity<>(new Mensaje("El Sexo es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getNumCelular())) {
            return new ResponseEntity<>(new Mensaje("El Numero de Celular es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getEmail())) {
            return new ResponseEntity<>(new Mensaje("El E-mail es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(asistencialDto.getDomicilio())) {
            return new ResponseEntity<>(new Mensaje("El Domicilio es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (asistencialDto.getEstado() == null)
            return new ResponseEntity<>(new Mensaje("El Estado es obligatorio"), HttpStatus.BAD_REQUEST);

        // if (asistencialDto.getLegajo() == null)
        // return new ResponseEntity<>(new Mensaje("El Legajo es obligatorio"),
        // HttpStatus.BAD_REQUEST);

        Asistencial asistencial = asistencialService.getone(id).get();

        asistencial.setApellido(asistencialDto.getApellido());
        asistencial.setNombre(asistencialDto.getNombre());
        asistencial.setDni(asistencialDto.getDni());
        asistencial.setCuil(asistencialDto.getCuil());
        asistencial.setFechaNacimiento(asistencialDto.getFechaNacimiento());
        asistencial.setSexo(asistencialDto.getSexo());
        asistencial.setNumCelular(asistencialDto.getNumCelular());
        asistencial.setEmail(asistencialDto.getEmail());
        asistencial.setDomicilio(asistencialDto.getDomicilio());
        asistencial.setEstado(asistencialDto.getEstado());
        // asistencial.setLegajo(asistencialDto.getLegajo());

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
