package com.guardias.backend.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ProfesionDto;
import com.guardias.backend.entity.Especialidad;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.Profesion;
import com.guardias.backend.service.EspecialidadService;
import com.guardias.backend.service.LegajoService;
import com.guardias.backend.service.ProfesionService;

@RestController
@RequestMapping("/profesion")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfesionController {

    @Autowired
    ProfesionService profesionService;
    @Autowired
    LegajoService legajoService;
    @Autowired
    EspecialidadService especialidadService;

    @GetMapping("/list")
    public ResponseEntity<List<Profesion>> list() {
        List<Profesion> list = profesionService.findByActivo();
        return new ResponseEntity<List<Profesion>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Profesion>> listAll() {
        List<Profesion> list = profesionService.findAll();
        return new ResponseEntity<List<Profesion>>(list, HttpStatus.OK);
    }

    @GetMapping("/listasistenciales")
    public ResponseEntity<List<Profesion>> listAsistenciales() {
        List<Profesion> asistenciales = profesionService.findByAsistencialTrue();
        return new ResponseEntity<>(asistenciales, HttpStatus.OK);
    }

    @GetMapping("/listnoasistenciales")
    public ResponseEntity<List<Profesion>> listNoAsistenciales() {
        List<Profesion> noAsistenciales = profesionService.findByAsistencialFalse();
        return new ResponseEntity<>(noAsistenciales, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Profesion> getById(@PathVariable("id") Long id) {
        if (!profesionService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la profesion"), HttpStatus.NOT_FOUND);
        Profesion profesion = profesionService.findById(id).get();
        return new ResponseEntity<Profesion>(profesion, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Profesion> getByNombre(@PathVariable("nombre") String nombre) {
        if (!profesionService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe profesion con ese nombre"), HttpStatus.NOT_FOUND);
        Profesion profesion = profesionService.findByNombre(nombre).get();
        return new ResponseEntity<Profesion>(profesion, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(ProfesionDto profesionDto, Long id) {
        if (StringUtils.isBlank(profesionDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (profesionDto.getAsistencial() == null)
            return new ResponseEntity(new Mensaje("indicar si es asistencial o no"),
                    HttpStatus.BAD_REQUEST);

        if (profesionService.existsByNombre(profesionDto.getNombre())
                && (profesionService.findByNombre(profesionDto.getNombre()).get().getId() != id))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    // private Pais createUpdate(Pais pais, PaisDto paisDto) {

    private Profesion createUpdate(Profesion profesion, ProfesionDto profesionDto) {
        if (profesion.getNombre() != profesionDto.getNombre() && profesionDto.getNombre() != null
                && !profesionDto.getNombre().isEmpty())
            profesion.setNombre(profesionDto.getNombre());

        if (profesionDto.getIdLegajos() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (profesion.getLegajos() != null) {
                for (Legajo legajo : profesion.getLegajos()) {
                    for (Long id : profesionDto.getIdLegajos()) {
                        if (!legajo.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                profesion.setLegajos(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? profesionDto.getIdLegajos() : idList;
            for (Long id : idsToAdd) {
                profesion.getLegajos().add(legajoService.findById(id).get());
                legajoService.findById(id).get().setProfesion(profesion);
            }
        }

        if (profesionDto.getIdEspecialidades() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (profesion.getEspecialidades() != null) {
                for (Especialidad especialidad : profesion.getEspecialidades()) {
                    for (Long id : profesionDto.getIdEspecialidades()) {
                        if (!especialidad.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                profesion.setEspecialidades(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? profesionDto.getIdEspecialidades() : idList;
            for (Long id : idsToAdd) {
                profesion.getEspecialidades().add(especialidadService.findById(id).get());
                especialidadService.findById(id).get().setProfesion(profesion);
            }
        }

        profesion.setAsistencial(profesionDto.getAsistencial());
        profesion.setActivo(true);

        return profesion;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProfesionDto profesionDto) {

        ResponseEntity<?> respuestaValidaciones = validations(profesionDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Profesion profesion = createUpdate(new Profesion(), profesionDto);
            profesionService.save(profesion);
            return new ResponseEntity(new Mensaje("Profesion creada"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ProfesionDto profesionDto) {
        // Busca por ID
        if (!profesionService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la profesion"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(profesionDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Profesion profesion = createUpdate(profesionService.findById(id).get(), profesionDto);
            profesionService.save(profesion);
            return new ResponseEntity(new Mensaje("Profesion creada"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!profesionService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la profesion"), HttpStatus.NOT_FOUND);
        Profesion profesion = profesionService.findById(id).get();
        profesion.setActivo(false);
        profesionService.save(profesion);
        return new ResponseEntity(new Mensaje("Profesion eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!profesionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la profesion"), HttpStatus.NOT_FOUND);
        profesionService.deleteById(id);
        return new ResponseEntity(new Mensaje("Profesion eliminada FISICAMENTE"), HttpStatus.OK);
    }
}
