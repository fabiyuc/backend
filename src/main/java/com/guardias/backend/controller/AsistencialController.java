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
import com.guardias.backend.entity.Person;
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.PersonService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/asistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class AsistencialController {

    @Autowired
    AsistencialService asistencialService;
    @Autowired
    PersonService personservice;
    @Autowired
    PersonController personController;

    @GetMapping("/list")
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
    @GetMapping("/listestado")
    public ResponseEntity<List<Asistencial>> list(@RequestParam("estado") Boolean estado) {
        List<Asistencial> list = asistencialService.findByEstado(estado);
        return new ResponseEntity<List<Asistencial>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Asistencial> getById(@PathVariable("id") Long id) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la persona tipo asistencial"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findById(id).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);
    }

    @GetMapping("/detaildni/{dni}")
    public ResponseEntity<Asistencial> getByDni(@PathVariable("dni") int dni) {
        if (!asistencialService.existsByDni(dni))
            return new ResponseEntity(new Mensaje("no existe asistencial con ese dni"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findByDni(dni).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);

    }

    private Asistencial createUpdate(Asistencial asistencial, AsistencialDto asistencialDto) {
        Person person = personController.createUpdate(asistencial, asistencialDto);
        asistencial = (Asistencial) person;
        if (!asistencialDto.getTipoGuardia().equals(asistencial.getTipoGuardia()))
            asistencial.setTipoGuardia(asistencialDto.getTipoGuardia());

        if (!asistencialDto.getRegistrosActividades().equals(asistencial.getRegistrosActividades()))
            asistencial.setRegistrosActividades(asistencialDto.getRegistrosActividades());
        return asistencial;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AsistencialDto asistencialDto) {

        ResponseEntity<?> respuestaValidaciones = personController.validations(asistencialDto);

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

        ResponseEntity<?> respuestaValidaciones = personController.validations(asistencialDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Asistencial asistencial = createUpdate(asistencialService.findById(id).get(), asistencialDto);
            asistencialService.save(asistencial);
            return new ResponseEntity(new Mensaje("asistencial modificado correctamente"), HttpStatus.OK);
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

    @PostMapping("/{idPersona}/addAutoridad/{idNovedadPersonal}")
    public ResponseEntity<?> agregarAutoridad(@PathVariable("idPersona") Long idPersona,
            @PathVariable("idDistribucionHoraria") Long idAutoridad) {
        try {
            personservice.agregarAutoridad(idPersona, idAutoridad);
            return new ResponseEntity<>(new Mensaje("Autoridad agregada a la persona correctamente"),
                    HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se encontró la persona con el ID proporcionado"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al agregar la Autoridad a la persona"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("el profesional no existe"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findById(id).get();
        asistencial.setActivo(false);
        asistencialService.save(asistencial);
        return new ResponseEntity<>(new Mensaje("Asistencial eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        asistencialService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Asistencial eliminado FISICAMENTE"), HttpStatus.OK);
    }
}
