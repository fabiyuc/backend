package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

import com.guardias.backend.dto.AsistencialDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.Person;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.RegistroActividadService;
import com.guardias.backend.service.TipoGuardiaService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/asistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class AsistencialController {

    @Autowired
    AsistencialService asistencialService;
    @Autowired
    RegistroActividadService registroActividadService;
    @Autowired
    TipoGuardiaService tipoGuardiaService;

    @Autowired
    @Lazy
    PersonController personController;

    @GetMapping("/list")
    public ResponseEntity<List<Asistencial>> list() {
        List<Asistencial> list = asistencialService.findByActivoTrue();
        return new ResponseEntity<List<Asistencial>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Asistencial>> listAll() {
        List<Asistencial> list = asistencialService.findAll();
        return new ResponseEntity<List<Asistencial>>(list, HttpStatus.OK);
    }

    @GetMapping("/legajos/{id}")
    public ResponseEntity<?> getLegajosByAsistencial(@PathVariable("id") Long id) {
        if (!asistencialService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la persona"), HttpStatus.NOT_FOUND);

        Asistencial asistencial = asistencialService.findById(id).get();
        List<Legajo> legajos = asistencial.getLegajos();

        return new ResponseEntity<>(legajos, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Asistencial> getById(@PathVariable("id") Long id) {
        if (!asistencialService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la persona tipo asistencial"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findById(id).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);
    }

    @GetMapping("/detaildni/{dni}")
    public ResponseEntity<Asistencial> getByDni(@PathVariable("dni") int dni) {
        if (!asistencialService.activoDni(dni))
            return new ResponseEntity(new Mensaje("no existe asistencial con ese dni"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findByDni(dni).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);

    }

    private Asistencial createUpdate(Asistencial asistencial, AsistencialDto asistencialDto) {
        Person person = personController.createUpdate(asistencial, asistencialDto);
        asistencial = (Asistencial) person;
        /* if (!asistencialDto.getTipoGuardia().equals(asistencial.getTipoGuardia()))
            asistencial.setTipoGuardia(asistencialDto.getTipoGuardia()); */

        if (asistencialDto.getIdRegistrosActividades() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (asistencial.getRegistrosActividades() != null) {
                for (RegistroActividad registro : asistencial.getRegistrosActividades()) {
                    for (Long id : asistencialDto.getIdRegistrosActividades()) {
                        if (!registro.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }
            List<Long> idsToAdd = idList.isEmpty() ? asistencialDto.getIdRegistrosActividades() : idList;
            for (Long id : idsToAdd) {
                asistencial.getRegistrosActividades().add(registroActividadService.findById(id).get());
                registroActividadService.findById(id).get().setAsistencial(asistencial);
            }
        }

        if (asistencialDto.getIdTiposGuardias() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (asistencial.getTiposGuardias() != null) {
                for (TipoGuardia tipoGuardia : asistencial.getTiposGuardias()) {
                    for (Long id : asistencialDto.getIdRegistrosActividades()) {
                        if (!tipoGuardia.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }

            List<Long> idsToAdd = idList.isEmpty() ? asistencialDto.getIdTiposGuardias() : idList;

            for (Long id : idsToAdd) {
                asistencial.getTiposGuardias().add(tipoGuardiaService.findById(id).get());
                tipoGuardiaService.findById(id).get().getAsistenciales().add(asistencial);
            }
        }

        asistencial.setActivo(true);
        return asistencial;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AsistencialDto asistencialDto) {

        ResponseEntity<?> respuestaValidaciones = personController.validations(asistencialDto, 0L);

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
        if (!asistencialService.activo(id))
            return new ResponseEntity(new Mensaje("el profesional no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = personController.validations(asistencialDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Asistencial asistencial = createUpdate(asistencialService.findById(id).get(), asistencialDto);
            asistencialService.save(asistencial);
            return new ResponseEntity(new Mensaje("asistencial modificado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!asistencialService.activo(id))
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

    @PostMapping("/{idAsistencial}/addTipoGuardia/{idTipoGuardia}")
    public ResponseEntity<?> agregarTipoGuardia(@PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idTipoGuardia") Long idTipoGuardia) {
        
        try {
            asistencialService.agregarTipoGuardia(idAsistencial, idTipoGuardia);
            return new ResponseEntity<>(new Mensaje("Tipo DEGuardia agregado al asistencial correctamente"),
                    HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se encontró el asistencial con el ID proporcionado"),
                    HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al agregar el tipo de Guardia al asistencial"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}