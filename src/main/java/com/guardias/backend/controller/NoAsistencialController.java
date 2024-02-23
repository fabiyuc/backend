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
import org.springframework.web.bind.annotation.RestController;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NoAsistencialDto;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.service.NoAsistencialService;
import com.guardias.backend.service.PersonService;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/noasistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class NoAsistencialController {

    @Autowired
    NoAsistencialService noAsistencialService;
    @Autowired
    PersonService personservice;

    @GetMapping("/list")
    public ResponseEntity<List<NoAsistencial>> list() {
        List<NoAsistencial> list = noAsistencialService.list();
        return new ResponseEntity<List<NoAsistencial>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<NoAsistencial> getById(@PathVariable("id") Long id) {
        if (!noAsistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("Profesional no entontrado"), HttpStatus.NOT_FOUND);
        NoAsistencial noAsistencial = noAsistencialService.findById(id).get();
        return new ResponseEntity<NoAsistencial>(noAsistencial, HttpStatus.OK);
    }

    @GetMapping("/detailDni/{dni}")
    public ResponseEntity<NoAsistencial> getById(@PathVariable("dni") int dni) {
        if (!noAsistencialService.existsByDni(dni))
            return new ResponseEntity(new Mensaje("Profesional no entontrado"), HttpStatus.NOT_FOUND);
        NoAsistencial noAsistencial = noAsistencialService.findByDni(dni).get();
        return new ResponseEntity<NoAsistencial>(noAsistencial, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(NoAsistencialDto noNoAsistencialDto) {
        if (StringUtils.isBlank(noNoAsistencialDto.getNombre())) {
            return new ResponseEntity<>(new Mensaje("El Nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(noNoAsistencialDto.getApellido())) {
            return new ResponseEntity<>(new Mensaje("El Apellido es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (noNoAsistencialDto.getDni() < 1000000)
            return new ResponseEntity<>(new Mensaje("DNI es incorrecto"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(noNoAsistencialDto.getCuil())) {
            return new ResponseEntity<>(new Mensaje("El Cuil es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (noNoAsistencialDto.getFechaNacimiento() == null) {
            return new ResponseEntity(new Mensaje("La fecha de nacimiento es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(noNoAsistencialDto.getSexo())) {
            return new ResponseEntity<>(new Mensaje("Es obligatorio indicar el sexo"), HttpStatus.BAD_REQUEST);
        }

        if (noNoAsistencialDto.getEstado() == null)
            return new ResponseEntity(new Mensaje("indicar si el estado es True o False"), HttpStatus.BAD_REQUEST);
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private NoAsistencial createUpdate(NoAsistencial noNoAsistencial, NoAsistencialDto noNoAsistencialDto) {

        if (!noNoAsistencialDto.getTelefono().equals(noNoAsistencial.getTelefono()))
            noNoAsistencial.setTelefono(noNoAsistencialDto.getTelefono());
        if (!noNoAsistencialDto.getSuplentes().equals(noNoAsistencial.getSuplentes()))
            noNoAsistencial.setSuplentes(noNoAsistencialDto.getSuplentes());
        if (!noNoAsistencialDto.getSexo().equals(noNoAsistencial.getSexo()))
            noNoAsistencial.setSexo(noNoAsistencialDto.getSexo());
        if (!noNoAsistencialDto.getNovedadesPersonales().equals(noNoAsistencial.getNovedadesPersonales()))
            noNoAsistencial.setNovedadesPersonales(noNoAsistencialDto.getNovedadesPersonales());
        if (!noNoAsistencialDto.getNombre().equals(noNoAsistencial.getNombre()))
            noNoAsistencial.setNombre(noNoAsistencialDto.getNombre());
        if (!noNoAsistencialDto.getLegajos().equals(noNoAsistencial.getLegajos()))
            noNoAsistencial.setLegajos(noNoAsistencialDto.getLegajos());
        if (!noNoAsistencialDto.getFechaNacimiento().equals(noNoAsistencial.getFechaNacimiento()))
            noNoAsistencial.setFechaNacimiento(noNoAsistencialDto.getFechaNacimiento());
        if (!noNoAsistencialDto.getEstado().equals(noNoAsistencial.getEstado()))
            noNoAsistencial.setEstado(noNoAsistencialDto.getEstado());
        if (!noNoAsistencialDto.getEmail().equals(noNoAsistencial.getEmail()))
            noNoAsistencial.setEmail(noNoAsistencialDto.getEmail());
        if (!noNoAsistencialDto.getDomicilio().equals(noNoAsistencial.getDomicilio()))
            noNoAsistencial.setDomicilio(noNoAsistencialDto.getDomicilio());
        if (noNoAsistencialDto.getDni() != noNoAsistencial.getDni())
            noNoAsistencial.setDni(noNoAsistencialDto.getDni());
        if (!noNoAsistencialDto.getDistribucionesHorarias().equals(noNoAsistencial.getDistribucionesHorarias()))
            noNoAsistencial.setDistribucionesHorarias(noNoAsistencialDto.getDistribucionesHorarias());
        if (!noNoAsistencialDto.getCuil().equals(noNoAsistencial.getCuil()))
            noNoAsistencial.setCuil(noNoAsistencialDto.getCuil());
        if (!noNoAsistencialDto.getApellido().equals(noNoAsistencial.getApellido()))
            noNoAsistencial.setApellido(noNoAsistencialDto.getApellido());
        if (!noNoAsistencialDto.getDescripcion().equals(noNoAsistencial.getDescripcion()))
            noNoAsistencial.setDescripcion(noNoAsistencialDto.getDescripcion());
        return noNoAsistencial;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NoAsistencialDto noNoAsistencialDto) {

        ResponseEntity<?> respuestaValidaciones = validations(noNoAsistencialDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            NoAsistencial noNoAsistencial = createUpdate(new NoAsistencial(), noNoAsistencialDto);
            noAsistencialService.save(noNoAsistencial);
            return new ResponseEntity(new Mensaje("Presona creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody NoAsistencialDto noNoAsistencialDto) {
        if (!noAsistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("La persona no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(noNoAsistencialDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            NoAsistencial noNoAsistencial = createUpdate(noAsistencialService.findById(id).get(), noNoAsistencialDto);
            noAsistencialService.save(noNoAsistencial);
            return new ResponseEntity(new Mensaje("Persona modificada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PostMapping("/{idPersona}/addLegajo/{idLegajo}")
    public ResponseEntity<?> agregarLegajo(@PathVariable("idPersona") Long idPersona,
            @PathVariable("idLegajo") Long idLegajo) {
        try {
            personservice.agregarLegajo(idPersona, idLegajo);
            return new ResponseEntity<>(new Mensaje("Legajo agregado al articulo correctamente"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se encontró la persona con el ID proporcionado"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al agregar el Legajo al articulo "),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // utilizar para la novedad y para el suplente
    @PostMapping("/{idPersona}/addNovedadPersonal/{idNovedadPersonal}")
    public ResponseEntity<?> agregarNovedadPersonal(@PathVariable("idPersona") Long idPersona,
            @PathVariable("idNovedadPersonal") Long idNovedadPersonal) {
        try {
            personservice.agregarNovedadPersonal(idPersona, idNovedadPersonal);
            return new ResponseEntity<>(new Mensaje("Novedad agregada al articulo correctamente"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se encontró la persona con el ID proporcionado"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al agregar la Novedad al articulo "),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{idPersona}/addDistribucionHoraria/{idNovedadPersonal}")
    public ResponseEntity<?> agregarDistribucionHoraria(@PathVariable("idPersona") Long idPersona,
            @PathVariable("idDistribucionHoraria") Long idDistribucionHoraria) {
        try {
            personservice.agregarDistribucionHoraria(idPersona, idDistribucionHoraria);
            return new ResponseEntity<>(new Mensaje("Distribucion horaria agregada al articulo correctamente"),
                    HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se encontró la persona con el ID proporcionado"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al agregar la Distribucion horaria al articulo "),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!noAsistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        noAsistencialService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Persona eliminada correctamente"), HttpStatus.OK);
    }
}
