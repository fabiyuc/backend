package com.guardias.backend.controller;

import java.time.LocalDate;
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
import com.guardias.backend.dto.SuspencionDto;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.Suspencion;
import com.guardias.backend.service.LegajoService;
import com.guardias.backend.service.SuspencionService;

@RestController
@RequestMapping("/suspencion")
@CrossOrigin(origins = "http://localhost:4200")
public class SuspencionController {

    @Autowired
    SuspencionService suspencionService;
    @Autowired
    LegajoService legajoService;

    @GetMapping("/list")
    public ResponseEntity<List<Suspencion>> list() {
        List<Suspencion> list = suspencionService.findByActivoTrue().get();
        return new ResponseEntity<List<Suspencion>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Suspencion>> listAll() {
        List<Suspencion> list = suspencionService.findAll();
        return new ResponseEntity<List<Suspencion>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Suspencion> getById(@PathVariable("id") Long id) {
        if (!suspencionService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la suspención con ese ID"), HttpStatus.NOT_FOUND);
        Suspencion suspencion = suspencionService.findById(id).get();
        return new ResponseEntity<Suspencion>(suspencion, HttpStatus.OK);
    }

    // **** ESTO DEBERIA SER UN LISTA CON LAS SUSPENCIONES DE LAS FEC DE INICIO? */
    @GetMapping("/detailFechaInicio/{fechaInicio}")
    public ResponseEntity<Suspencion> getByFechaInicio(@PathVariable("fechaInicio") LocalDate fechaInicio) {
        if (!suspencionService.activoByFechaInicio(fechaInicio))
            return new ResponseEntity(new Mensaje("no existe con esta fecha de inicio"), HttpStatus.NOT_FOUND);
        Suspencion suspencion = suspencionService.findByFechaInicio(fechaInicio).get();
        return new ResponseEntity<Suspencion>(suspencion, HttpStatus.OK);
    }

    @GetMapping("/detailFechaFin/{fechaFin}")
    public ResponseEntity<Suspencion> getByFechaFin(@PathVariable("fechaFin") LocalDate fechaFin) {
        if (!suspencionService.activoByFechaFin(fechaFin))
            return new ResponseEntity(new Mensaje("no existe con esta fecha de fin"), HttpStatus.NOT_FOUND);
        Suspencion suspencion = suspencionService.findByFechaFin(fechaFin).get();
        return new ResponseEntity<Suspencion>(suspencion, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(SuspencionDto suspencionDto) {
        if (StringUtils.isBlank(suspencionDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (suspencionDto.getFechaInicio() == null) {
            return new ResponseEntity(new Mensaje("La fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if (suspencionDto.getFechaFin() == null) {
            return new ResponseEntity(new Mensaje("La fecha de fin es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Suspencion createUpdate(Suspencion suspencion, SuspencionDto suspencionDto) {

        if (suspencion.getDescripcion() != suspencionDto.getDescripcion() && suspencionDto.getDescripcion() != null
                && !suspencionDto.getDescripcion().isEmpty())
            suspencion.setDescripcion(suspencionDto.getDescripcion());

        if (suspencion.getFechaInicio() != suspencionDto.getFechaInicio() && suspencionDto.getFechaInicio() != null)
            suspencion.setFechaInicio(suspencionDto.getFechaInicio());

        if (suspencion.getFechaFin() != suspencionDto.getFechaFin() && suspencionDto.getFechaFin() != null)
            suspencion.setFechaFin(suspencionDto.getFechaFin());

        if (suspencionDto.getIdLegajos() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (suspencion.getLegajos() != null) {
                for (Legajo legajo : suspencion.getLegajos()) {
                    for (Long id : suspencionDto.getIdLegajos()) {
                        if (!legajo.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                suspencion.setLegajos(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? suspencionDto.getIdLegajos() : idList;
            for (Long id : idsToAdd) {
                suspencion.getLegajos().add(legajoService.findById(id).get());
                legajoService.findById(id).get().setSuspencion(suspencion);
            }
        }
        suspencion.setActivo(true);
        return suspencion;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SuspencionDto suspencionDto) {

        ResponseEntity<?> respuestaValidaciones = validations(suspencionDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Suspencion suspencion = createUpdate(new Suspencion(), suspencionDto);
            suspencionService.save(suspencion);
            return new ResponseEntity(new Mensaje("suspencion creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody SuspencionDto suspencionDto) {
        if (!suspencionService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la suspención"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(suspencionDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Suspencion suspencion = createUpdate(suspencionService.findById(id).get(), suspencionDto);
            suspencionService.save(suspencion);
            return new ResponseEntity(new Mensaje("suspencion modificada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!suspencionService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la suspención"), HttpStatus.NOT_FOUND);

        Suspencion suspencion = suspencionService.findById(id).get();
        suspencion.setActivo(false);
        suspencionService.save(suspencion);
        return new ResponseEntity(new Mensaje("suspención eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!suspencionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la suspención"), HttpStatus.NOT_FOUND);
        suspencionService.delete(id);
        return new ResponseEntity(new Mensaje("suspención eliminada FISICAMENTE"), HttpStatus.OK);
    }

}
