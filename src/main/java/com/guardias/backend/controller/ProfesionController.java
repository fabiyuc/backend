package com.guardias.backend.controller;

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
import com.guardias.backend.entity.Profesion;
import com.guardias.backend.service.ProfesionService;

@RestController
@RequestMapping("/profesion")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfesionController {

    @Autowired
    ProfesionService profesionService;

    @GetMapping("/lista")
    public ResponseEntity<List<Profesion>> list() {
        List<Profesion> list = profesionService.list();
        return new ResponseEntity<List<Profesion>>(list, HttpStatus.OK);
    }

    @GetMapping("/listaasistenciales")
    public ResponseEntity<List<Profesion>> listAsistenciales() {
        List<Profesion> asistenciales = profesionService.listAsistenciales();
        return new ResponseEntity<>(asistenciales, HttpStatus.OK);
    }

    @GetMapping("/listanoasistenciales")
    public ResponseEntity<List<Profesion>> listNoAsistenciales() {
        List<Profesion> noAsistenciales = profesionService.listNoAsistenciales();
        return new ResponseEntity<>(noAsistenciales, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Profesion> getById(@PathVariable("id") Long id) {
        if (!profesionService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la profesion"), HttpStatus.NOT_FOUND);
        Profesion profesion = profesionService.getOne(id).get();
        return new ResponseEntity<Profesion>(profesion, HttpStatus.OK);
    }

    @GetMapping("/detallenombre/{nombre}")
    public ResponseEntity<Profesion> getByNombre(@PathVariable("nombre") String nombre) {
        if (!profesionService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe profesion con ese nombre"), HttpStatus.NOT_FOUND);
        Profesion profesion = profesionService.getByNombre(nombre).get();
        return new ResponseEntity<Profesion>(profesion, HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProfesionDto profesionDto) {
        if (StringUtils.isBlank(profesionDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (profesionService.existsByNombre(profesionDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);

        if (profesionDto.getAsistencial() == null)
            return new ResponseEntity(new Mensaje("indicar si es asistencial o no"),
                    HttpStatus.BAD_REQUEST);
        
        Profesion profesion = new Profesion();
        profesion.setNombre(profesionDto.getNombre());
        profesion.setAsistencial(profesionDto.getAsistencial());
       /*  profesion.setLegajos(profesionDto.getLegajos());
        profesion.setEspecialidades(profesionDto.getEspecialidades()); */

        profesionService.save(profesion);
        return new ResponseEntity(new Mensaje("Profesion creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ProfesionDto profesionDto) {
        // Busca por ID
        if (!profesionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la profesion"), HttpStatus.NOT_FOUND);

        // Verifica que el nombre no exista para el mismo ID
        if (profesionService.existsByNombre(profesionDto.getNombre()) &&
                profesionService.getByNombre(profesionDto.getNombre()).get().getId() != id)
            return new ResponseEntity(new Mensaje("esa profesion ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(profesionDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre de la profesion es obligatoria"), HttpStatus.BAD_REQUEST);

        if (profesionDto.getAsistencial() == null)
            return new ResponseEntity(new Mensaje("indicar si es asistencial o no"),
                    HttpStatus.BAD_REQUEST);

        Profesion profesion = profesionService.getOne(id).get();
        profesion.setNombre(profesionDto.getNombre());
        profesion.setAsistencial(profesionDto.getAsistencial());
        profesionService.save(profesion);
        return new ResponseEntity(new Mensaje("Profesion actualizada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!profesionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la profesion"), HttpStatus.NOT_FOUND);
        profesionService.delete(id);
        return new ResponseEntity(new Mensaje("Profesion eliminada"), HttpStatus.OK);
    }
}
