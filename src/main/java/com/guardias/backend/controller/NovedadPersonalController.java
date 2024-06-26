package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NovedadPersonalDto;
import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.service.ArticuloService;
import com.guardias.backend.service.IncisoService;
import com.guardias.backend.service.NovedadPersonalService;
import com.guardias.backend.service.PersonService;

@Controller
@RequestMapping("/novedadPersonal")
@CrossOrigin(origins = "http://localhost:4200")
public class NovedadPersonalController {

    @Autowired
    NovedadPersonalService novedadPersonalService;
    @Autowired
    PersonService personaService;
    @Autowired
    ArticuloService articuloService;
    @Autowired
    IncisoService incisoService;

    @GetMapping("/list")
    public ResponseEntity<List<NovedadPersonal>> list() {
        List<NovedadPersonal> list = novedadPersonalService.findByActivo();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<NovedadPersonal>> listAll() {
        List<NovedadPersonal> list = novedadPersonalService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<NovedadPersonal> getById(@PathVariable("id") Long id) {
        if (!novedadPersonalService.activo(id))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"), HttpStatus.NOT_FOUND);
        NovedadPersonal novedadPersonal = novedadPersonalService.getById(id).get();
        return new ResponseEntity(novedadPersonal, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{id}")
    public ResponseEntity<List<NovedadPersonal>> getByPersona(@PathVariable("id") Long id) {
        if (!novedadPersonalService.activoByPersona(id))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"),
                    HttpStatus.NOT_FOUND);
        List<NovedadPersonal> novedadesList = novedadPersonalService.findByPersona(id).get();
        return new ResponseEntity(novedadesList, HttpStatus.OK);
    }

    @GetMapping("/detailfecha/{fecha}")
    public ResponseEntity<List<NovedadPersonal>> getByFecha(@PathVariable("fecha") LocalDate fecha) {
        if (!novedadPersonalService.existsByFechaInicio(fecha))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"),
                    HttpStatus.NOT_FOUND);
        List<NovedadPersonal> novedadesList = novedadPersonalService.findByFechaInicio(fecha).get();
        return new ResponseEntity(novedadesList, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(NovedadPersonalDto novedadPersonalDto) {
        if (novedadPersonalDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (novedadPersonalDto.getFechaFinal() == null)
            return new ResponseEntity(new Mensaje("la fecha de final es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (novedadPersonalDto.getIdPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private NovedadPersonal createUpdate(NovedadPersonal novedadPersonal, NovedadPersonalDto novedadPersonalDto) {

        if (novedadPersonalDto.getFechaInicio() != null
                && !novedadPersonalDto.getFechaInicio().equals(novedadPersonal.getFechaInicio()))
            novedadPersonal.setFechaInicio(novedadPersonalDto.getFechaInicio());

        if (novedadPersonalDto.getFechaFinal() != null
                && !novedadPersonalDto.getFechaFinal().equals(novedadPersonal.getFechaFinal()))
            novedadPersonal.setFechaFinal(novedadPersonalDto.getFechaFinal());

        novedadPersonal.setPuedeRealizarGuardia(novedadPersonalDto.isPuedeRealizarGuardia());
        novedadPersonal.setCobraSueldo(novedadPersonalDto.isCobraSueldo());
        novedadPersonal.setNecesitaReemplazo(novedadPersonalDto.isNecesitaReemplazo());
        novedadPersonal.setActual(novedadPersonalDto.isActual());

        if (novedadPersonalDto.getDescripcion() != null
                && !novedadPersonalDto.getDescripcion().equals(novedadPersonal.getDescripcion())
                && !novedadPersonalDto.getDescripcion().isEmpty())
            novedadPersonal.setDescripcion(novedadPersonalDto.getDescripcion());

        if (novedadPersonal.getPersona() == null ||
                (novedadPersonalDto.getIdPersona() != null &&
                        !Objects.equals(novedadPersonal.getPersona().getId(),
                                novedadPersonalDto.getIdPersona()))) {
            novedadPersonal.setPersona(personaService.findById(novedadPersonalDto.getIdPersona()));
        }

        if (novedadPersonal.getSuplente() == null ||
                (novedadPersonalDto.getIdSuplente() != null &&
                        !Objects.equals(novedadPersonal.getSuplente().getId(),
                                novedadPersonalDto.getIdSuplente()))) {
            novedadPersonal.setSuplente(personaService.findById(novedadPersonalDto.getIdSuplente()));
        }

        if (novedadPersonal.getArticulo() == null ||
                (novedadPersonalDto.getIdArticulo() != null &&
                        !Objects.equals(novedadPersonal.getArticulo().getId(),
                                novedadPersonalDto.getIdArticulo()))) {
            novedadPersonal.setArticulo(articuloService.findById(novedadPersonalDto.getIdArticulo()).get());
        }

        if (novedadPersonal.getInciso() == null ||
                (novedadPersonalDto.getIdInciso() != null &&
                        !Objects.equals(novedadPersonal.getInciso().getId(),
                                novedadPersonalDto.getIdInciso()))) {
            novedadPersonal.setInciso(incisoService.findById(novedadPersonalDto.getIdInciso()).get());
        }
        novedadPersonal.setActivo(true);
        return novedadPersonal;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NovedadPersonalDto novedadPersonalDto) {

        ResponseEntity<?> respuestaValidaciones = validations(novedadPersonalDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            NovedadPersonal novedadPersonal = createUpdate(new NovedadPersonal(), novedadPersonalDto);
            novedadPersonalService.save(novedadPersonal);
            return new ResponseEntity(new Mensaje("Novedad creada correctamente"), HttpStatus.OK);

        } else {
            return respuestaValidaciones;
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody NovedadPersonalDto novedadPersonalDto) {
        if (!novedadPersonalService.activo(id))
            return new ResponseEntity(new Mensaje("Novedad no encontrada"),
                    HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(novedadPersonalDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            NovedadPersonal novedadPersonal = createUpdate(novedadPersonalService.getById(id).get(),
                    novedadPersonalDto);
            novedadPersonalService.save(novedadPersonal);
            return new ResponseEntity(new Mensaje("Novedad modificada correctamente"), HttpStatus.OK);

        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!novedadPersonalService.activo(id))
            return new ResponseEntity(new Mensaje("La novedad no exixte"), HttpStatus.NOT_FOUND);

        NovedadPersonal novedadPersonal = novedadPersonalService.findById(id).get();
        novedadPersonal.setActual(false);
        novedadPersonalService.save(novedadPersonal);
        return new ResponseEntity<>(new Mensaje("novedad eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!novedadPersonalService.existsById(id))
            return new ResponseEntity(new Mensaje("La novedad no exixte"), HttpStatus.NOT_FOUND);
        novedadPersonalService.deleteById(null);
        return new ResponseEntity<>(new Mensaje("novedad eliminada FISICAMENTE"), HttpStatus.OK);
    }

}