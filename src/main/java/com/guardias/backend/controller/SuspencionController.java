package com.guardias.backend.controller;

import java.time.LocalDate;
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
import com.guardias.backend.entity.Suspencion;
import com.guardias.backend.service.SuspencionService;

@RestController
@RequestMapping("/suspencion")
@CrossOrigin(origins = "http://localhost:4200")
public class SuspencionController {

    @Autowired
    SuspencionService suspencionService;

    @GetMapping("/lista")
    public ResponseEntity<List<Suspencion>> list() {
        List<Suspencion> list = suspencionService.list();
        return new ResponseEntity<List<Suspencion>>(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Suspencion> getById(@PathVariable("id") Long id) {
        if (!suspencionService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la suspención con ese ID"), HttpStatus.NOT_FOUND);
        Suspencion suspencion = suspencionService.getOne(id).get();
        return new ResponseEntity<Suspencion>(suspencion, HttpStatus.OK);
    }

    //**** ESTO DEBERIA SER UN LISTA CON LAS SUSPENCIONES DE LAS FEC DE INICIO? */
    @GetMapping("/detalleFechaInicio/{fechaInicio}")
    public ResponseEntity<Suspencion> getByFechaInicio(@PathVariable("fechaInicio") LocalDate fechaInicio) {
        if (!suspencionService.existsByFechaInicio(fechaInicio))
            return new ResponseEntity(new Mensaje("no existe con esta fecha de inicio"), HttpStatus.NOT_FOUND);
        Suspencion suspencion = suspencionService.getByFechaInicio(fechaInicio).get();
        return new ResponseEntity<Suspencion>(suspencion, HttpStatus.OK);
    }

    @GetMapping("/detalleFechaFin/{fechaFin}")
    public ResponseEntity<Suspencion> getByFechaFin(@PathVariable("fechaFin") LocalDate fechaFin) {
        if (!suspencionService.existsByFechaFin(fechaFin))
            return new ResponseEntity(new Mensaje("no existe con esta fecha de fin"), HttpStatus.NOT_FOUND);
        Suspencion suspencion = suspencionService.getByFechaFin(fechaFin).get();
        return new ResponseEntity<Suspencion>(suspencion, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SuspencionDto suspencionDto) {
        if (StringUtils.isBlank(suspencionDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (suspencionDto.getFechaInicio() == null) {
                return new ResponseEntity(new Mensaje("La fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);
            }    
        if (suspencionDto.getFechaFin() == null) {
                return new ResponseEntity(new Mensaje("La fecha de fin es obligatoria"), HttpStatus.BAD_REQUEST);
            }   
        Suspencion suspencion = new Suspencion();
        suspencion.setDescripcion(suspencionDto.getDescripcion());
        suspencion.setFechaInicio(suspencionDto.getFechaInicio());
        suspencion.setFechaFin(suspencionDto.getFechaFin());
        suspencion.setLegajos(suspencionDto.getLegajos());
        suspencionService.save(suspencion);
        return new ResponseEntity(new Mensaje("servicio creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody SuspencionDto suspencionDto) {
        if (!suspencionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la suspención"), HttpStatus.NOT_FOUND);

        if (StringUtils.isBlank(suspencionDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"), HttpStatus.BAD_REQUEST);

        if (suspencionDto.getFechaInicio() == null) {
                return new ResponseEntity(new Mensaje("La fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);
            }    
        if (suspencionDto.getFechaFin() == null) {
                return new ResponseEntity(new Mensaje("La fecha de fin es obligatoria"), HttpStatus.BAD_REQUEST);
            }    
        Suspencion suspencion = suspencionService.getOne(id).get();
        suspencion.setDescripcion(suspencionDto.getDescripcion());
        suspencion.setFechaInicio(suspencionDto.getFechaInicio());
        suspencion.setFechaFin(suspencionDto.getFechaFin());
        suspencionService.save(suspencion);
        return new ResponseEntity(new Mensaje("La suspensión ha sido actualizada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!suspencionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la suspención"), HttpStatus.NOT_FOUND);
        suspencionService.delete(id);
        return new ResponseEntity(new Mensaje("suspención eliminada"), HttpStatus.OK);
    }

}
