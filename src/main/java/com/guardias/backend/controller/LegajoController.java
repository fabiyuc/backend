package com.guardias.backend.controller;

import java.util.ArrayList;
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

import com.guardias.backend.dto.LegajoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Especialidad;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.Profesion;
import com.guardias.backend.service.CargoService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.EspecialidadService;
import com.guardias.backend.service.LegajoService;
import com.guardias.backend.service.PersonService;
import com.guardias.backend.service.ProfesionService;
import com.guardias.backend.service.RevistaService;
import com.guardias.backend.service.SuspencionService;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/legajo")
@CrossOrigin(origins = "http://localhost:4200")
public class LegajoController {

    @Autowired
    LegajoService legajoService;
    @Autowired
    PersonService personService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    ProfesionService profesionService;
    @Autowired
    SuspencionService suspencionService;
    @Autowired
    RevistaService revistaService;
    @Autowired
    CargoService cargoService;
    @Autowired
    EspecialidadService especialidadService;

    @GetMapping("/list")
    public ResponseEntity<List<Legajo>> list() {
        List<Legajo> list = legajoService.findByActivoTrue();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Legajo>> listAll() {
        List<Legajo> list = legajoService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Legajo>> getById(@PathVariable("id") Long id) {
        if (!legajoService.activo(id))
            return new ResponseEntity(new Mensaje("No existe el legajo"),
                    HttpStatus.NOT_FOUND);
        Legajo legajo = legajoService.findById(id).get();
        return new ResponseEntity(legajo, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(LegajoDto legajoDto) {
        if (legajoDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("La fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        if (legajoDto.getActual() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar si es actual o no"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getLegal() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar si es legal o no"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getIdPersona() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la persona"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getIdUdo() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la UdO"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getIdRevista() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la situacion de revista"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getIdProfesion() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la profesion"),
                    HttpStatus.BAD_REQUEST);
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Legajo createUpdate(Legajo legajo, LegajoDto legajoDto,  Long idEspecialidad) {

        if (legajoDto.getFechaInicio() != null && legajo.getFechaInicio() != legajoDto.getFechaInicio())
            legajo.setFechaInicio(legajoDto.getFechaInicio());

        if (legajoDto.getFechaFinal() != null && legajo.getFechaFinal() != legajoDto.getFechaFinal())
            legajo.setFechaFinal(legajoDto.getFechaFinal());

        if (legajoDto.getMatriculaNacional() != null
                && legajo.getMatriculaNacional() != legajoDto.getMatriculaNacional())
            legajo.setMatriculaNacional(legajoDto.getMatriculaNacional());

        if (legajoDto.getMatriculaProvincial() != null
                && legajo.getMatriculaProvincial() != legajoDto.getMatriculaProvincial())
            legajo.setMatriculaProvincial(legajoDto.getMatriculaProvincial());

        if (legajoDto.getIdPersona() != null) {
            if (legajo.getPersona() == null
                    || !Objects.equals(legajo.getPersona().getId(), legajoDto.getIdPersona())) {
                legajo.setPersona(personService.findById(legajoDto.getIdPersona()));
            }
        }

        if (legajoDto.getIdUdo() != null) {
            if (legajo.getUdo() == null
                    || !Objects.equals(legajo.getUdo().getId(), legajoDto.getIdUdo())) {
                legajo.setUdo(efectorService.findById(legajoDto.getIdUdo()));
            }
        }
        legajo.setActual(legajoDto.getActual());
        legajo.setLegal(legajoDto.getLegal());

        if (legajoDto.getIdProfesion() != null) {
            Optional<Profesion> profesionOptional = profesionService.findById(legajoDto.getIdProfesion());
            if (profesionOptional.isPresent()) {
                Profesion profesion = profesionOptional.get();
    
                // Filtrar la lista de especialidades para conservar solo la especialidad deseada
                List<Especialidad> especialidadesFiltradas = profesion.getEspecialidades().stream()
                    .filter(especialidad -> Objects.equals(especialidad.getId(), idEspecialidad))
                    .collect(Collectors.toList());
    
                if (!especialidadesFiltradas.isEmpty()) {
                    // Asignar solo la especialidad filtrada a la profesión
                    Especialidad especialidad = especialidadesFiltradas.get(0);
                    profesion.getEspecialidades().clear();
                    profesion.getEspecialidades().add(especialidad);
    
                    // Asociar la profesión filtrada al legajo
                    legajo.setProfesion(profesion);
    
                    // Guardar el legajo y la profesión con la especialidad especificada
                    legajoService.save(legajo);
    
                    // Verificación de especialidades
                    System.out.println("Especialidades después de asignar a la profesión:");
                    profesion.getEspecialidades().forEach(especialidadItem -> 
                        System.out.println("ID: " + especialidadItem.getId() + ", Nombre: " + especialidadItem.getNombre()));
                } else {
                    throw new RuntimeException("Especialidad no encontrada en la profesión");
                }
            } else {
                throw new RuntimeException("Profesion no encontrada");
            }
        }
        
        if (legajoDto.getIdSuspencion() != null) {
            if (legajo.getSuspencion() == null
                    || !Objects.equals(legajo.getSuspencion().getId(), legajoDto.getIdSuspencion())) {
                legajo.setSuspencion(suspencionService.findById(legajoDto.getIdSuspencion()).get());
            }
        }

        if (legajoDto.getIdRevista() != null) {
            if (legajo.getRevista() == null
                    || !Objects.equals(legajo.getRevista().getId(), legajoDto.getIdRevista())) {
                legajo.setRevista(revistaService.findById(legajoDto.getIdRevista()).get());
            }
        }

        if (legajoDto.getIdCargo() != null) {
            if (legajo.getCargo() == null
                    || !Objects.equals(legajo.getCargo().getId(), legajoDto.getIdCargo())) {
                legajo.setCargo(cargoService.findById(legajoDto.getIdCargo()).get());
            }
        }

        if (legajoDto.getIdEfectores() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (legajo.getEfectores() != null) {
                for (Efector efector : legajo.getEfectores()) {
                    for (Long id : legajoDto.getIdEfectores()) {
                        if (!efector.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                legajo.setEfectores(new ArrayList<Efector>());
            }

            List<Long> idsToAdd = idList.isEmpty() ? legajoDto.getIdEfectores() : idList;

            for (Long id : idsToAdd) {
                legajo.getEfectores().add(efectorService.findById(id));
                efectorService.findById(id).getLegajos().add(legajo);
            }
        }

        legajo.setActivo(true);

        return legajo;
    }

    @PostMapping("/create/{idEspecialidad}") 
    public ResponseEntity<?> create(@RequestBody LegajoDto legajoDto,@PathVariable("idEspecialidad") Long idEspecialidad) {
        ResponseEntity<?> respuestaValidaciones = validations(legajoDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Legajo legajo = createUpdate(new Legajo(), legajoDto, idEspecialidad);
            legajoService.save(legajo);

            return new ResponseEntity(new Mensaje("Legajo creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}/{idEspecialidad}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @PathVariable("idEspecialidad") Long idEspecialidad,@RequestBody LegajoDto legajoDto) {
        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(legajoDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Legajo legajo = createUpdate(legajoService.findById(id).get(), legajoDto,idEspecialidad);
            legajoService.save(legajo);

            return new ResponseEntity(new Mensaje("Legajo modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {

        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);

        Legajo legajo = legajoService.findById(id).get();
        legajo.setActivo(false);
        legajoService.save(legajo);
        return new ResponseEntity(new Mensaje("legajo eliminado"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);
        legajoService.deleteById(id);
        return new ResponseEntity(new Mensaje("legajo eliminado FISICAMENTE"), HttpStatus.OK);
    }

}