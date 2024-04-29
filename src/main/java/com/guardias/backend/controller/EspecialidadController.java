package com.guardias.backend.controller;

import java.util.List;
import java.util.Objects;
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

import com.guardias.backend.dto.EspecialidadDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Especialidad;
import com.guardias.backend.service.EspecialidadService;
import com.guardias.backend.service.ProfesionService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/especialidad")
@CrossOrigin(origins = "http://localhost:4200")
public class EspecialidadController {

    @Autowired
    EspecialidadService especialidadService;
    @Autowired
    ProfesionService profesionService;

    @GetMapping("/list")
    public ResponseEntity<List<Especialidad>> list() {
        List<Especialidad> list = especialidadService.findByActivo();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Especialidad>> listAll() {
        List<Especialidad> list = especialidadService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Especialidad>> getById(@PathVariable("id") Long id) {
        if (!especialidadService.activo(id))
            return new ResponseEntity(new Mensaje("especialidad no existe"), HttpStatus.NOT_FOUND);
        Especialidad especialidad = especialidadService.findById(id).get();
        return new ResponseEntity(especialidad, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Especialidad>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!especialidadService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("especialidad no existe"), HttpStatus.NOT_FOUND);
        Especialidad especialidad = especialidadService.findByNombre(nombre).get();
        return new ResponseEntity(especialidad, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(EspecialidadDto especialidadDto) {

        if (StringUtils.isBlank(especialidadDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Especialidad createUpdate(Especialidad especialidad, EspecialidadDto especialidadDto) {

        if (especialidadDto.getNombre() != null && !especialidadDto.getNombre().equals(especialidad.getNombre())
                && !especialidadDto.getNombre().isEmpty())
            especialidad.setNombre(especialidadDto.getNombre());

        /*
         * if (especialidadDto.getIdProfesion() != null &&
         * !especialidadDto.getIdProfesion().equals(especialidad.getId()))
         * especialidad.setProfesion(profesionService.findById(especialidadDto.
         * getIdProfesion()).get());
         */
        if (especialidadDto.getIdProfesion() != null) {
            if (especialidad.getProfesion() == null
                    || !Objects.equals(especialidad.getProfesion().getId(), especialidadDto.getIdProfesion())) {
                especialidad.setProfesion(profesionService.findById(especialidadDto.getIdProfesion()).get());
            }
        }
        especialidad.setEsPasiva(especialidadDto.getEsPasiva());
        especialidad.setActivo(true);
        return especialidad;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EspecialidadDto especialidadDto) {
        ResponseEntity<?> respuestaValidaciones = validations(especialidadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Especialidad especialidad = createUpdate(new Especialidad(), especialidadDto);
            especialidadService.save(especialidad);
            return new ResponseEntity(new Mensaje("Especialidad creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody EspecialidadDto especialidadDto) {
        if (!especialidadService.activo(id))
            return new ResponseEntity(new Mensaje("especialidad no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(especialidadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Especialidad especialidad = createUpdate(especialidadService.findById(id).get(), especialidadDto);
            especialidadService.save(especialidad);
            return new ResponseEntity(new Mensaje("Especialidad modificada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!especialidadService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la especialidad"), HttpStatus.NOT_FOUND);

        Especialidad especialidad = especialidadService.findById(id).get();
        especialidad.setActivo(false);
        especialidadService.save(especialidad);
        return new ResponseEntity(new Mensaje("especialidad eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!especialidadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la especialidad"), HttpStatus.NOT_FOUND);
        especialidadService.deleteById(id);
        return new ResponseEntity(new Mensaje("especialidad eliminada FISICAMENTE"), HttpStatus.OK);
    }

}
