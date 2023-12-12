package com.guardias.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.guardias.backend.dto.AsistencialDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Profesion;
import com.guardias.backend.service.AsistencialService;

@RestController
@RequestMapping("/asistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class AsistencialController {
    
    @Autowired
    AsistencialService asistencialService;

     @GetMapping("/lista")
    public ResponseEntity<List<Asistencial>> list() {
        List<Asistencial> list = asistencialService.list();
        return new ResponseEntity<List<Asistencial>>(list, HttpStatus.OK);
    }

    //*** POSTMAN: /asistencial/listaestado?estado=true o /asistencial/lista?estado=false
    @GetMapping("/listaestado")
    public ResponseEntity<List<Asistencial>> list(@RequestParam("estado") Boolean estado) {
        List<Asistencial> list = asistencialService.findByEstado(estado);
        return new ResponseEntity<List<Asistencial>>(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Asistencial> getById(@PathVariable("id") Long id) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la persona tipo asistencial"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.getOne(id).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);
    }

    @GetMapping("/detalledni/{dni}")
    public ResponseEntity<Asistencial> getByDni(@PathVariable("dni") String dni) {
        if (!asistencialService.existsByDni(dni))
            return new ResponseEntity(new Mensaje("no existe asistencial con ese dni"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.getByDni(dni).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);
    
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AsistencialDto asistencialDto) {
        /* if (StringUtils.isBlank(servicioDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (serviceServicio.existsByDescripcion(servicioDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("esa descripcion ya existe"),
                    HttpStatus.BAD_REQUEST); */
        Asistencial asistencial = new Asistencial();

        asistencial.setApellido(asistencialDto.getApellido());
        asistencial.setNombre(asistencialDto.getNombre());
        asistencial.setDni(asistencialDto.getDni());
        asistencial.setCuil(asistencialDto.getCuil());
        asistencial.setFechaNacimiento(asistencialDto.getFechaNacimiento());
        asistencial.setSexo(asistencialDto.getSexo());
        asistencial.setNumCelular(asistencialDto.getNumCelular());
        asistencial.setEmail(asistencialDto.getEmail());
        asistencial.setDomicilio(asistencialDto.getDomicilio());
        asistencial.setEstado(asistencialDto.getEstado());
        asistencial.setLegajos(asistencialDto.getLegajos());

        asistencialService.save(asistencial);
        return new ResponseEntity(new Mensaje("asistencial creado"), HttpStatus.OK);
    }
}
