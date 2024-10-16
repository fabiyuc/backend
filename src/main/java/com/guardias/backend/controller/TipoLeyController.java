package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.TipoLeyDto;
import com.guardias.backend.entity.Ley;
import com.guardias.backend.entity.TipoLey;
import com.guardias.backend.service.LeyService;
import com.guardias.backend.service.TipoLeyService;

import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/tipoLey")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoLeyController {

    @Autowired
    TipoLeyService tipoLeyService;
    @Autowired
    LeyService leyService;

    @GetMapping("/list")
    public ResponseEntity<List<TipoLey>> list() {
        List<TipoLey> list = tipoLeyService.findByActivoTrue().get();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<TipoLey>> listAll() {
        List<TipoLey> list = tipoLeyService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<TipoLey>> getById(@PathVariable("id") Long id) {
        if (!tipoLeyService.activo(id))
            return new ResponseEntity(new Mensaje("Tipo de Ley no encontrada"), HttpStatus.NOT_FOUND);
        TipoLey tipoLey = tipoLeyService.findById(id).get();
        return new ResponseEntity(tipoLey, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(TipoLeyDto tipoLeyDto, Long id) {
        if (StringUtils.isBlank(tipoLeyDto.getDescripcion()))
            return new ResponseEntity<Mensaje>(new Mensaje("La descripcion es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (tipoLeyService.existsByDescripcion(tipoLeyDto.getDescripcion())
                && (tipoLeyService.findByDescripcion(tipoLeyDto.getDescripcion()).get().getId() != id))
            return new ResponseEntity<Mensaje>(new Mensaje("Esa descripcion ya existe"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private TipoLey createUpdate(TipoLey tipoLey, TipoLeyDto tipoLeyDto) {

        if (tipoLeyDto.getDescripcion() != null && !tipoLeyDto.getDescripcion().isEmpty()
                && !tipoLeyDto.getDescripcion().equals(tipoLey.getDescripcion()))
            tipoLey.setDescripcion(tipoLeyDto.getDescripcion());

        if (tipoLeyDto.getIdLeyes() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (tipoLey.getLeyes() != null) {
                for (Ley ley : tipoLey.getLeyes()) {
                    for (Long id : tipoLeyDto.getIdLeyes()) {
                        if (!ley.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                tipoLey.setLeyes(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? tipoLeyDto.getIdLeyes() : idList;
            for (Long id : idsToAdd) {
                tipoLey.getLeyes().add(leyService.findById(id));
                leyService.findById(id).setTipoLey(tipoLey);
            }

        }

        tipoLey.setActivo(true);
        return tipoLey;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoLeyDto tipoLeyDto) {

        ResponseEntity<?> respuestaValidaciones = validations(tipoLeyDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            TipoLey tipoLey = createUpdate(new TipoLey(), tipoLeyDto);
            tipoLeyService.save(tipoLey);
            return new ResponseEntity<Mensaje>(new Mensaje("Tipo de Ley creada correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TipoLeyDto tipoLeyDto) {
        if (!tipoLeyService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(tipoLeyDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            TipoLey tipoLey = createUpdate(tipoLeyService.findById(id).get(), tipoLeyDto);
            tipoLeyService.save(tipoLey);
            return new ResponseEntity<Mensaje>(new Mensaje("Tipo de Ley modificada correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!tipoLeyService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        TipoLey tipoLey = tipoLeyService.findById(id).get();
        tipoLey.setActivo(false);
        tipoLeyService.save(tipoLey);
        return new ResponseEntity<>(new Mensaje("Tipo Ley eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!tipoLeyService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        tipoLeyService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Tipo Ley eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
