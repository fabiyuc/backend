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
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.CargoService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.EspecialidadService;
import com.guardias.backend.service.LegajoService;
import com.guardias.backend.service.PersonService;
import com.guardias.backend.service.ProfesionService;
import com.guardias.backend.service.RevistaService;
import com.guardias.backend.service.SuspencionService;

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
    @Autowired
    AsistencialService asistencialService;

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

        //////// verifica que es contra factura /////////////
        boolean esContraFactura = asistencialService.esContraFactura(legajoDto.getIdPersona());
        if (!esContraFactura && legajoDto.getIdUdo() == null) {
            return new ResponseEntity<>(new Mensaje("indicar la UdO"), HttpStatus.BAD_REQUEST);
        }

        /*
         * if (legajoDto.getIdUdo() == null)
         * return new ResponseEntity<Mensaje>(new Mensaje("indicar la UdO"),
         * HttpStatus.BAD_REQUEST);
         */

        if (legajoDto.getIdRevista() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la situacion de revista"),
                    HttpStatus.BAD_REQUEST);

        if (legajoDto.getIdProfesion() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la profesion"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Legajo createUpdate(Legajo legajo, LegajoDto legajoDto) {

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

        if (legajoDto.getIdEspecialidades() != null) {
            if (legajo.getEspecialidades() == null) {
                legajo.setEspecialidades(new ArrayList<>());
            }

            List<Especialidad> especialidadesParaEliminar = new ArrayList<>();
            for (Especialidad especialidad : legajo.getEspecialidades()) {
                if (!legajoDto.getIdEspecialidades().contains(especialidad.getId())) {
                    especialidadesParaEliminar.add(especialidad);
                }
            }

            for (Especialidad especialidad : especialidadesParaEliminar) {
                legajo.getEspecialidades().remove(especialidad);
                especialidad.getLegajos().remove(legajo);
            }

            // Agrego nuevas especialidades al legajo si no están presentes
            for (Long id : legajoDto.getIdEspecialidades()) {
                boolean found = false;
                for (Especialidad especialidad : legajo.getEspecialidades()) {
                    if (especialidad.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Especialidad especialidadToAdd = especialidadService.findById(id).orElse(null);
                    if (especialidadToAdd != null) {
                        legajo.getEspecialidades().add(especialidadToAdd);
                        especialidadToAdd.getLegajos().add(legajo);
                    }
                }
            }
        }

        if (legajoDto.getIdEfectores() != null) {
            if (legajo.getEfectores() == null) {
                legajo.setEfectores(new ArrayList<>());
            }

            // Crea una nueva lista para almacenar los efectores actualizados
            List<Efector> efectoresActualizados = new ArrayList<>();
            for (Efector efector : legajo.getEfectores()) {
                if (legajoDto.getIdEfectores().contains(efector.getId())) {
                    efectoresActualizados.add(efector);
                } else {
                    // Remover el legajo de los efectores que se eliminarán
                    efector.getLegajos().remove(legajo);
                }
            }
            legajo.setEfectores(efectoresActualizados);

            // agrega nuevos efectores si no estan presentes
            for (Long id : legajoDto.getIdEfectores()) {
                boolean found = false;
                for (Efector efector : legajo.getEfectores()) {
                    if (efector.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Efector efectorToAdd = efectorService.findById(id);
                    if (efectorToAdd != null) {
                        legajo.getEfectores().add(efectorToAdd);
                        efectorToAdd.getLegajos().add(legajo);
                    } else {
                        throw new RuntimeException("No se encontró el efector con ID: " + id);
                    }
                }
            }
        } /*
           * else {
           * // Si legajoDto.getIdEfectores() es null, eliminamos todos los efectores
           * System.out.println("3.  si legajoDto.getIdEfectores(): es  null " +
           * legajoDto.getIdEfectores());
           * 
           * // donde controlo si legajo.getEfectores() es null?????
           * if (legajo.getEfectores() != null) {
           * for (Efector efector : legajo.getEfectores()) {
           * efector.getLegajos().remove(legajo);
           * System.out.println("3.1 legajo borrado de efector");
           * 
           * }
           * 
           * legajo.getEfectores().clear();
           * 
           * System.out.println("3.2 efectores borrados de legajo" +
           * legajo.getEfectores());
           * 
           * }
           * 
           * }
           */

        if (legajoDto.getIdProfesion() != null) {
            if (legajo.getProfesion() == null
                    || !Objects.equals(legajo.getProfesion().getId(), legajoDto.getIdProfesion())) {
                legajo.setProfesion(profesionService.findById(legajoDto.getIdProfesion()).get());
            }
        }

        legajo.setActual(legajoDto.getActual());
        legajo.setLegal(legajoDto.getLegal());
        legajo.setActivo(true);

        return legajo;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LegajoDto legajoDto) {
        ResponseEntity<?> respuestaValidaciones = validations(legajoDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Legajo legajo = createUpdate(new Legajo(), legajoDto);
            legajoService.save(legajo);

            return new ResponseEntity<>(new Mensaje("Legajo creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LegajoDto legajoDto) {
        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(legajoDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Legajo legajo = createUpdate(legajoService.findById(id).get(), legajoDto);
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