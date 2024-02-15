package com.guardias.backend.controller;

import java.util.List;
import java.util.Set;

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
import com.guardias.backend.entity.Legajo;
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

    @GetMapping("/legajos/{id}")
    public ResponseEntity<?> getLegajosByAsistencial(@PathVariable("id") Long id) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la persona"), HttpStatus.NOT_FOUND);

        Asistencial asistencial = asistencialService.findById(id).get();
        Set<Legajo> legajos = asistencial.getLegajos();

        return new ResponseEntity<>(legajos, HttpStatus.OK);
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

    private ResponseEntity<?> validations(AsistencialDto asistencialDto) {
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

        if (asistencialDto.getEstado() == null)
            return new ResponseEntity(new Mensaje("indicar si el estado es True o False"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Asistencial createUpdate(Asistencial asistencial, AsistencialDto asistencialDto) {
        if (!asistencialDto.getTipoGuardia().equals(asistencial.getTipoGuardia()))
            asistencial.setTipoGuardia(asistencialDto.getTipoGuardia());
        if (!asistencialDto.getTelefono().equals(asistencial.getTelefono()))
            asistencial.setTelefono(asistencialDto.getTelefono());
        if (!asistencialDto.getSuplentes().equals(asistencial.getSuplentes()))
            asistencial.setSuplentes(asistencialDto.getSuplentes());
        if (!asistencialDto.getSexo().equals(asistencial.getSexo()))
            asistencial.setSexo(asistencialDto.getSexo());
        if (!asistencialDto.getNovedadesPersonales().equals(asistencial.getNovedadesPersonales()))
            asistencial.setNovedadesPersonales(asistencialDto.getNovedadesPersonales());
        if (!asistencialDto.getNombre().equals(asistencial.getNombre()))
            asistencial.setNombre(asistencialDto.getNombre());
        if (!asistencialDto.getLegajos().equals(asistencial.getLegajos()))
            asistencial.setLegajos(asistencialDto.getLegajos());
        if (!asistencialDto.getFechaNacimiento().equals(asistencial.getFechaNacimiento()))
            asistencial.setFechaNacimiento(asistencialDto.getFechaNacimiento());
        if (!asistencialDto.getEstado().equals(asistencial.getEstado()))
            asistencial.setEstado(asistencialDto.getEstado());
        if (!asistencialDto.getEmail().equals(asistencial.getEmail()))
            asistencial.setEmail(asistencialDto.getEmail());
        if (!asistencialDto.getDomicilio().equals(asistencial.getDomicilio()))
            asistencial.setDomicilio(asistencialDto.getDomicilio());
        if (asistencialDto.getDni() != asistencial.getDni())
            asistencial.setDni(asistencialDto.getDni());
        if (!asistencialDto.getDistribucionesHorarias().equals(asistencial.getDistribucionesHorarias()))
            asistencial.setDistribucionesHorarias(asistencialDto.getDistribucionesHorarias());
        if (!asistencialDto.getCuil().equals(asistencial.getCuil()))
            asistencial.setCuil(asistencialDto.getCuil());
        if (!asistencialDto.getApellido().equals(asistencial.getApellido()))
            asistencial.setApellido(asistencialDto.getApellido());
        if (!asistencialDto.getRegistrosActividades().equals(asistencial.getRegistrosActividades()))
            asistencial.setRegistrosActividades(asistencialDto.getRegistrosActividades());
        return asistencial;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AsistencialDto asistencialDto) {

        ResponseEntity<?> respuestaValidaciones = validations(asistencialDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Asistencial asistencial = createUpdate(new Asistencial(), asistencialDto);
            asistencialService.save(asistencial);
            return new ResponseEntity(new Mensaje("asistencial creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody AsistencialDto asistencialDto) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("el profesional no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(asistencialDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Asistencial asistencial = createUpdate(asistencialService.findById(id).get(), asistencialDto);
            asistencialService.save(asistencial);
            return new ResponseEntity(new Mensaje("asistencial modificado correctamente"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Mensaje("Error al crear el elemento"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        asistencialService.delete(id);
        return new ResponseEntity<>(new Mensaje("Asistencial eliminado"), HttpStatus.OK);
    }
}
