package com.guardias.backend.controller;

import java.util.List;
import java.util.Objects;

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
import com.guardias.backend.dto.SumaHorasDto;
import com.guardias.backend.entity.SumaHoras;
import com.guardias.backend.service.RegistroMensualService;
import com.guardias.backend.service.SumaHorasService;

@RestController
@RequestMapping("/SumaHoras")
@CrossOrigin(origins = "http://localhost:4200")
public class SumaHorasController {
    @Autowired
    SumaHorasService sumaHorasService;
    @Autowired
    RegistroMensualService registroMensualService;

    @GetMapping("/list")
    public ResponseEntity<List<SumaHoras>> list() {
        List<SumaHoras> list = sumaHorasService.findByActivoTrue().get();
        return new ResponseEntity<List<SumaHoras>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<SumaHoras>> listAll() {
        List<SumaHoras> list = sumaHorasService.findAll();
        return new ResponseEntity<List<SumaHoras>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<SumaHoras> getById(@PathVariable("id") Long id) {

        if (!sumaHorasService.activo(id))
            return new ResponseEntity(new Mensaje("Valor no encontrado"), HttpStatus.NOT_FOUND);
        SumaHoras sumaHoras = sumaHorasService.findById(id).get();
        return new ResponseEntity(sumaHoras, HttpStatus.OK);
    }

    @GetMapping("/findByRegistro/{idRegistroMensual}/{idAsistencial}")
    public ResponseEntity<List<SumaHoras>> findByRegistro(@PathVariable("idRegistroMensual") Long idRegistroMensual,
            @PathVariable("idAsistencial") Long idAsistencial) {

        if (!sumaHorasService.existByRegistroMensual(idRegistroMensual))
            return new ResponseEntity(new Mensaje("La suma no existe"),
                    HttpStatus.NOT_FOUND);

        List<SumaHoras> list = sumaHorasService.findByRegistroMensual(idRegistroMensual, idAsistencial);
        return new ResponseEntity<List<SumaHoras>>(list, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(SumaHorasDto sumaHorasDto) {

        if (sumaHorasDto.getIdRegistroMensual() <= 0)
            return new ResponseEntity(new Mensaje("El registro mensual no existe"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private SumaHoras createUpdate(SumaHoras sumaHoras, SumaHorasDto sumaHorasDto) {

        if (sumaHorasDto.getHorasLav() != sumaHoras.getHorasLav() && (sumaHorasDto.getHorasLav() > 0))
            sumaHoras.setHorasLav(sumaHorasDto.getHorasLav());

        if (sumaHorasDto.getHorasSdf() != sumaHoras.getHorasSdf() && (sumaHorasDto.getHorasSdf() > 0))
            sumaHoras.setHorasSdf(sumaHorasDto.getHorasSdf());

        if (sumaHorasDto.getIdRegistroMensual() != null && (sumaHoras.getRegistroMensual() == null
                || !Objects.equals(sumaHoras.getRegistroMensual().getId(), sumaHorasDto.getIdRegistroMensual()))) {
            sumaHoras.setRegistroMensual(registroMensualService.findById(sumaHorasDto.getIdRegistroMensual()).get());
        }

        /*
         * private Long idRegistroMensual;
         */

        sumaHoras.setActivo(true);
        return sumaHoras;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SumaHorasDto sumaHorasDto) {
        ResponseEntity<?> respuestaValidaciones = validations(sumaHorasDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            SumaHoras sumaHoras = createUpdate(new SumaHoras(), sumaHorasDto);
            sumaHorasService.save(sumaHoras);
            return new ResponseEntity(new Mensaje("Suma de horas creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody SumaHorasDto sumaHorasDto) {
        if (!sumaHorasService.activo(id))
            return new ResponseEntity(new Mensaje("La suma no existe"),
                    HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(sumaHorasDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            SumaHoras sumaHoras = createUpdate(sumaHorasService.findById(id).get(), sumaHorasDto);
            sumaHorasService.save(sumaHoras);
            return new ResponseEntity(new Mensaje("Suma de horas modificadas"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!sumaHorasService.activo(id))
            return new ResponseEntity(new Mensaje("La suma no existe"),
                    HttpStatus.NOT_FOUND);
        SumaHoras sumaHoras = sumaHorasService.findById(id).get();
        sumaHoras.setActivo(false);
        sumaHorasService.save(sumaHoras);
        return new ResponseEntity(new Mensaje("suma de hroas eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!sumaHorasService.existsById(id))
            return new ResponseEntity(new Mensaje("La suma no existe"),
                    HttpStatus.NOT_FOUND);
        sumaHorasService.deleteById(id);
        return new ResponseEntity(new Mensaje("suma de horas eliminadas FISICAMENTE"), HttpStatus.OK);
    }

}
