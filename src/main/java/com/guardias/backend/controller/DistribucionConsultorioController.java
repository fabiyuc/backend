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
import com.guardias.backend.dto.DistribucionConsultorioDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.DistribucionConsultorio;
import com.guardias.backend.service.DistribucionConsultorioService;

@RestController
@RequestMapping("/distribucionConsultorio")
@CrossOrigin(origins = "http://localhost:4200")
public class DistribucionConsultorioController {

    @Autowired
    DistribucionConsultorioService distribucionConsultorioService;

    @GetMapping("/list")
    public ResponseEntity<List<DistribucionConsultorio>> list() {
        List<DistribucionConsultorio> list = distribucionConsultorioService.findByActivo(true);
        return new ResponseEntity<List<DistribucionConsultorio>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<DistribucionConsultorio>> listAll() {
        List<DistribucionConsultorio> list = distribucionConsultorioService.findAll();
        return new ResponseEntity<List<DistribucionConsultorio>>(list, HttpStatus.OK);
    }

    @GetMapping("/list/{fechaInicio}")
    public ResponseEntity<List<DistribucionConsultorio>> getByFechainicio(
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<DistribucionConsultorio> list = distribucionConsultorioService.findByFechaInicio(fechaInicio);
        return new ResponseEntity<List<DistribucionConsultorio>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<DistribucionConsultorio> getById(@PathVariable("id") Long id) {
        if (!distribucionConsultorioService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        DistribucionConsultorio distribucionConsultorio = distribucionConsultorioService.findById(id).get();
        return new ResponseEntity<DistribucionConsultorio>(distribucionConsultorio, HttpStatus.OK);
    }

    @GetMapping("/detailefector/{idEfector}")
    public ResponseEntity<List<DistribucionConsultorio>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!distribucionConsultorioService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionConsultorio> distribucionConsultorio = distribucionConsultorioService
                .findByEfectorId(idEfector).get();
        return new ResponseEntity<>(distribucionConsultorio, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<DistribucionConsultorio>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!distribucionConsultorioService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"),
                    HttpStatus.NOT_FOUND);
        List<DistribucionConsultorio> distribucionConsultorio = distribucionConsultorioService
                .findByPersonaId(idPersona).get();
        return new ResponseEntity<>(distribucionConsultorio, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DistribucionConsultorioDto distribucionConsultorioDto) {

        if (distribucionConsultorioDto.getDia() == null)
            return new ResponseEntity(new Mensaje("El dia es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionConsultorioDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionConsultorioDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionConsultorioDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionConsultorioDto.getEfector() == null)
            return new ResponseEntity(new Mensaje("El efector es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionConsultorioDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionConsultorio distribucionConsultorio = new DistribucionConsultorio();
        distribucionConsultorio.setDia(distribucionConsultorioDto.getDia());
        distribucionConsultorio.setFechaInicio(distribucionConsultorioDto.getFechaInicio());
        distribucionConsultorio.setFechaFinalizacion(distribucionConsultorioDto.getFechaFinalizacion());
        distribucionConsultorio.setHoraIngreso(distribucionConsultorioDto.getHoraIngreso());
        distribucionConsultorio.setPersona(distribucionConsultorioDto.getPersona());
        distribucionConsultorio.setEfector(distribucionConsultorioDto.getEfector());
        distribucionConsultorio.setCantidadHoras(distribucionConsultorioDto.getCantidadHoras());

        distribucionConsultorio.setLugar(distribucionConsultorioDto.getLugar());
        distribucionConsultorio.setEspecialidad(distribucionConsultorioDto.getEspecialidad());
        distribucionConsultorio.setCantidadTurnos(distribucionConsultorioDto.getCantidadTurnos());

        distribucionConsultorioService.save(distribucionConsultorio);
        return new ResponseEntity(new Mensaje("Carga horaria creada"),
                HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody DistribucionConsultorioDto distribucionConsultorioDto) {

        if (!distribucionConsultorioService.existsById(id))
            return new ResponseEntity(new Mensaje("La distribucion no existe"), HttpStatus.NOT_FOUND);

        if (distribucionConsultorioDto.getDia() == null)
            return new ResponseEntity(new Mensaje("El dia es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionConsultorioDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionConsultorioDto.getHoraIngreso() == null)
            return new ResponseEntity(new Mensaje("la hora de ingreso es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionConsultorioDto.getCantidadHoras() == null)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionConsultorioDto.getEfector() == null)
            return new ResponseEntity(new Mensaje("El efector es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (distribucionConsultorioDto.getPersona() == null)
            return new ResponseEntity(new Mensaje("la persona es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        DistribucionConsultorio distribucionConsultorio = distribucionConsultorioService.findById(id).get();

        if (!distribucionConsultorioDto.getDia().equals(distribucionConsultorio.getDia()))
            distribucionConsultorio.setDia(distribucionConsultorioDto.getDia());
        if (!distribucionConsultorioDto.getFechaInicio().equals(distribucionConsultorio.getFechaInicio()))
            distribucionConsultorio.setFechaInicio(distribucionConsultorioDto.getFechaInicio());
        if (!distribucionConsultorioDto.getFechaFinalizacion().equals(distribucionConsultorio.getFechaFinalizacion()))
            distribucionConsultorio.setFechaFinalizacion(distribucionConsultorioDto.getFechaFinalizacion());
        if (!distribucionConsultorioDto.getHoraIngreso().equals(distribucionConsultorio.getHoraIngreso()))
            distribucionConsultorio.setHoraIngreso(distribucionConsultorioDto.getHoraIngreso());
        if (!distribucionConsultorioDto.getPersona().equals(distribucionConsultorio.getPersona()))
            distribucionConsultorio.setPersona(distribucionConsultorioDto.getPersona());
        if (!distribucionConsultorioDto.getEfector().equals(distribucionConsultorio.getEfector()))
            distribucionConsultorio.setEfector(distribucionConsultorioDto.getEfector());
        if (!distribucionConsultorioDto.getCantidadHoras().equals(distribucionConsultorio.getCantidadHoras()))
            distribucionConsultorio.setCantidadHoras(distribucionConsultorioDto.getCantidadHoras());

        if (!distribucionConsultorioDto.getLugar().equals(distribucionConsultorio.getLugar()))
            distribucionConsultorio.setLugar(distribucionConsultorioDto.getLugar());
        if (!distribucionConsultorioDto.getEspecialidad().equals(distribucionConsultorio.getEspecialidad()))
            distribucionConsultorio.setEspecialidad(distribucionConsultorioDto.getEspecialidad());
        if (distribucionConsultorioDto.getCantidadTurnos() != distribucionConsultorio.getCantidadTurnos())
            distribucionConsultorio.setCantidadTurnos(distribucionConsultorioDto.getCantidadTurnos());

        distribucionConsultorioService.save(distribucionConsultorio);

        return new ResponseEntity(new Mensaje("Carga horaria modificada"),
                HttpStatus.OK);
    }

}