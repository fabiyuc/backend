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
import com.guardias.backend.dto.LegajoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.service.LegajoService;

@RestController
@RequestMapping("/legajo")
@CrossOrigin(origins = "http://localhost:4200")
public class LegajoController {

    @Autowired
    LegajoService legajoService;

    @GetMapping("/lista")
    public ResponseEntity<List<Legajo>> list() {
        List<Legajo> list = legajoService.list();
        return new ResponseEntity<List<Legajo>>(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Legajo> getById(@PathVariable("id") Long id) {
        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el legajo"), HttpStatus.NOT_FOUND);
        Legajo legajo = legajoService.getOne(id).get();
        return new ResponseEntity<Legajo>(legajo, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LegajoDto legajoDto) {
       
        if (legajoDto.getFechaInicio() == null) {
            return new ResponseEntity(new Mensaje("La fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);
        }    
        if (legajoDto.getFechaFinal() == null) {
            return new ResponseEntity(new Mensaje("La fecha de fin es obligatoria"), HttpStatus.BAD_REQUEST);
        } 
        if (legajoDto.getActual()==null)
            return new ResponseEntity(new Mensaje("indicar si es actual o no"),
                    HttpStatus.BAD_REQUEST);
        if (legajoDto.getLegal()==null)
            return new ResponseEntity(new Mensaje("indicar si es legal o no"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(legajoDto.getMatriculaNacional()))
            return new ResponseEntity(new Mensaje("la matricula nacional es obligatoria"),
                    HttpStatus.BAD_REQUEST);  
        if (legajoDto.getProfesion()==null)
            return new ResponseEntity(new Mensaje("indicar la profesion"),
                    HttpStatus.BAD_REQUEST);
        if (legajoDto.getRevista()==null)
            return new ResponseEntity(new Mensaje("indicar la revista"),
                    HttpStatus.BAD_REQUEST);
        if (legajoDto.getAsistencial()==null && legajoDto.getNoAsistencial()== null )
            return new ResponseEntity(new Mensaje("indicar si es asistencial o no"),
                    HttpStatus.BAD_REQUEST);          
             
        Legajo legajo = new Legajo();
        legajo.setFechaInicio(legajoDto.getFechaInicio());
        legajo.setFechaFinal(legajoDto.getFechaFinal());
        legajo.setActual(legajoDto.getActual());
        legajo.setLegal(legajoDto.getActual());
        legajo.setMatriculaNacional(legajoDto.getMatriculaNacional());
        legajo.setMatriculaProvincial(legajoDto.getMatriculaProvincial());
        legajo.setProfesion(legajoDto.getProfesion());
        legajo.setSuspencion(legajoDto.getSuspencion());
        legajo.setRevista(legajoDto.getRevista());
        legajo.setAsistencial(legajoDto.getAsistencial());
        legajo.setNoAsistencial(legajoDto.getNoAsistencial());

        legajoService.save(legajo);
        return new ResponseEntity(new Mensaje("Legajo creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LegajoDto legajoDto) {
        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);

        if (legajoDto.getFechaInicio() == null) {
            return new ResponseEntity(new Mensaje("La fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);
        }    
        if (legajoDto.getFechaFinal() == null) {
            return new ResponseEntity(new Mensaje("La fecha de fin es obligatoria"), HttpStatus.BAD_REQUEST);
        } 
        if (legajoDto.getActual()==null)
            return new ResponseEntity(new Mensaje("indicar si es actual o no"),
                    HttpStatus.BAD_REQUEST);
        if (legajoDto.getLegal()==null)
            return new ResponseEntity(new Mensaje("indicar si es legal o no"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(legajoDto.getMatriculaNacional()))
            return new ResponseEntity(new Mensaje("la matricula nacional es obligatoria"),
                    HttpStatus.BAD_REQUEST); 

        if (legajoDto.getProfesion()==null)
            return new ResponseEntity(new Mensaje("indicar la profesion"),
                    HttpStatus.BAD_REQUEST);
        if (legajoDto.getRevista()==null)
            return new ResponseEntity(new Mensaje("indicar la revista"),
                    HttpStatus.BAD_REQUEST);
        if (legajoDto.getAsistencial()==null && legajoDto.getNoAsistencial()== null )
            return new ResponseEntity(new Mensaje("indicar si es asistencial o no"),
                    HttpStatus.BAD_REQUEST);  
        
        Legajo legajo = legajoService.getOne(id).get();
        legajo.setFechaInicio(legajoDto.getFechaInicio());
        legajo.setFechaFinal(legajoDto.getFechaFinal());
        legajo.setActual(legajoDto.getActual());
        legajo.setLegal(legajoDto.getActual());
        legajo.setMatriculaNacional(legajoDto.getMatriculaNacional());
        legajo.setMatriculaProvincial(legajoDto.getMatriculaProvincial());
        legajo.setProfesion(legajoDto.getProfesion());
        legajo.setSuspencion(legajoDto.getSuspencion());
        legajo.setRevista(legajoDto.getRevista());
        legajo.setAsistencial(legajoDto.getAsistencial());
        legajo.setNoAsistencial(legajoDto.getNoAsistencial());
        
        legajoService.save(legajo);
        return new ResponseEntity(new Mensaje("El legajo ha sido actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);
        legajoService.delete(id);
        return new ResponseEntity(new Mensaje("legajo eliminado"), HttpStatus.OK);
    }

}
