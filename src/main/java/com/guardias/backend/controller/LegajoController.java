package com.guardias.backend.controller;

import java.util.List;
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

    @GetMapping("/detail/{id}")
    public ResponseEntity<Legajo> getById(@PathVariable("id") Long id) {
        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el legajo"), HttpStatus.NOT_FOUND);
        Legajo legajo = legajoService.getOne(id).get();
        return new ResponseEntity<Legajo>(legajo, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LegajoDto legajoDto) {
       
/* ############  validar campos en front o ver como hacerlo aqui con bool y date */

        /* if (StringUtils.isBlank(legajoDto.isEsActual()))
            return new ResponseEntity(new Mensaje("el establecimiento es obligatorio"),HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(legajoDto.isEsLegal()))
            return new ResponseEntity(new Mensaje("el servicio es obligatorio"),HttpStatus.BAD_REQUEST); */

        
        Legajo legajo = new Legajo(legajoDto.getFechaInicio(),
                legajoDto.getFechaFinal(), 
                legajoDto.isEsActual(),
                legajoDto.isEsLegal(),
                legajoDto.getMatriculaNacional(),
                legajoDto.getMatriculaProvincial(),
                legajoDto.getProfesion(),
                legajoDto.getSuspencion(),
                legajoDto.getRevista());
        legajoService.save(legajo);
        return new ResponseEntity(new Mensaje("Legajo creado"), HttpStatus.OK);
    } 

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LegajoDto legajoDto) {
        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);
        
        //validar las fechas y los datos booleanos en front que no sea campo vacio hasta poder validar en back

        Legajo legajo = legajoService.getOne(id).get();
        legajo.setFechaInicio(legajoDto.getFechaInicio());
        legajo.setFechaFinal(legajoDto.getFechaFinal());
        legajo.setEsActual(legajoDto.isEsActual());
        legajo.setEsLegal(legajoDto.isEsLegal());
        legajo.setMatriculaNacional(legajoDto.getMatriculaNacional());
        legajo.setMatriculaProvincial(legajoDto.getMatriculaProvincial());
        legajo.setProfesion(legajoDto.getProfesion());
        legajo.setSuspencion(legajoDto.getSuspencion());
        legajo.setRevista(legajoDto.getRevista());
        legajoService.save(legajo);
        return new ResponseEntity(new Mensaje("El legajo ha sido actualizada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        
        if (!legajoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);
        legajoService.delete(id);
        return new ResponseEntity(new Mensaje("legajo eliminado"), HttpStatus.OK);
    }
    
}
