package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.guardias.backend.dto.DistribucionGuardiaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionGuardia;
import com.guardias.backend.service.DistribucionGuardiaService;

@RestController
@RequestMapping("/distribucionGuardia")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionGuardiaController {

    @Autowired
    DistribucionGuardiaService distribucionGuardiaService;

    @GetMapping("/list")
    public ResponseEntity<List<DistribucionGuardia>> list() {
        List<DistribucionGuardia> list = distribucionGuardiaService.list();
        return new ResponseEntity<List<DistribucionGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionGuardia> getById(@PathVariable("id") Long id) {
        if (!distribucionGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionGuardia distribucionGuardia = distribucionGuardiaService.findById(id).get();
        return new ResponseEntity<DistribucionGuardia>(distribucionGuardia, HttpStatus.OK);
    }

    @GetMapping("/list/{fechaInicio}")
    public ResponseEntity<List<DistribucionGuardia>> getByFechainicio(
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<DistribucionGuardia> list = distribucionGuardiaService.findByFechaInicio(fechaInicio);
        return new ResponseEntity<List<DistribucionGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detailefector/{idEfector}")
    public ResponseEntity<List<DistribucionGuardia>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionGuardiaService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionGuardia> distribucionGuardia = distribucionGuardiaService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionGuardia, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<DistribucionGuardia>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionGuardiaService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionGuardia> distribucionGuardia = distribucionGuardiaService.findByPersonaId(idPersona).get();
        return new ResponseEntity<>(distribucionGuardia, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionGuardiaDto distribucionGuardiaDto) {

        if (distribucionGuardiaDto.getDia() == null)
            return new ResponseEntity(new Mensaje("El dia es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGuardiaDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGuardiaDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGuardiaDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGuardiaDto.getEfector() == null)
            return new ResponseEntity(new Mensaje("El efector es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGuardiaDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionGuardia distribucionGuardia = new DistribucionGuardia();
        distribucionGuardia.setDia(distribucionGuardiaDto.getDia());
        distribucionGuardia.setFechaInicio(distribucionGuardiaDto.getFechaInicio());
        distribucionGuardia.setFechaFinalizacion(distribucionGuardiaDto.getFechaFinalizacion());
        distribucionGuardia.setHoraIngreso(distribucionGuardiaDto.getHoraIngreso());
        distribucionGuardia.setPersona(distribucionGuardiaDto.getPersona());
        distribucionGuardia.setEfector(distribucionGuardiaDto.getEfector());
        distribucionGuardia.setCantidadHoras(distribucionGuardiaDto.getCantidadHoras());

        distribucionGuardia.setTipoGuardia(distribucionGuardiaDto.getTipoGuardia());

        distribucionGuardiaService.save(distribucionGuardia);
        return new ResponseEntity(new Mensaje("Carga horaria creada"),
                HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,

            @RequestBody DistribucionGuardiaDto distribucionGuardiaDto) {

        if (!distribucionGuardiaService.existsById(id))
            return new ResponseEntity(new Mensaje("La distribucion no existe"), HttpStatus.NOT_FOUND);

        if (distribucionGuardiaDto.getDia() == null)
            return new ResponseEntity(new Mensaje("El dia es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGuardiaDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGuardiaDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGuardiaDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGuardiaDto.getEfector() == null)
            return new ResponseEntity(new Mensaje("El efector es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionGuardiaDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionGuardia distribucionGuardia = distribucionGuardiaService.findById(id).get();

        if (!distribucionGuardiaDto.getDia().equals(distribucionGuardia.getDia()))
            distribucionGuardia.setDia(distribucionGuardiaDto.getDia());
        if (!distribucionGuardiaDto.getFechaInicio().equals(distribucionGuardia.getFechaInicio()))
            distribucionGuardia.setFechaInicio(distribucionGuardiaDto.getFechaInicio());
        if (!distribucionGuardiaDto.getFechaFinalizacion().equals(distribucionGuardia.getFechaFinalizacion()))
            distribucionGuardia.setFechaFinalizacion(distribucionGuardiaDto.getFechaFinalizacion());
        if (!distribucionGuardiaDto.getHoraIngreso().equals(distribucionGuardia.getHoraIngreso()))
            distribucionGuardia.setHoraIngreso(distribucionGuardiaDto.getHoraIngreso());
        if (!distribucionGuardiaDto.getPersona().equals(distribucionGuardia.getPersona()))
            distribucionGuardia.setPersona(distribucionGuardiaDto.getPersona());
        if (!distribucionGuardiaDto.getEfector().equals(distribucionGuardia.getEfector()))
            distribucionGuardia.setEfector(distribucionGuardiaDto.getEfector());
        if (!distribucionGuardiaDto.getCantidadHoras().equals(distribucionGuardia.getCantidadHoras()))
            distribucionGuardia.setCantidadHoras(distribucionGuardiaDto.getCantidadHoras());

        if (!distribucionGuardiaDto.getTipoGuardia().equals(distribucionGuardia.getTipoGuardia()))
            distribucionGuardia.setTipoGuardia(distribucionGuardiaDto.getTipoGuardia());

        distribucionGuardiaService.save(distribucionGuardia);

        return new ResponseEntity(new Mensaje("Carga horaria modificada"),
                HttpStatus.OK);
    }

}