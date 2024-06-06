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
import com.guardias.backend.dto.TipoCargoDto;
import com.guardias.backend.entity.TipoCargo;
import com.guardias.backend.service.TipoCargoService;

@RestController
@RequestMapping("/tipoCargo")
@CrossOrigin(origins = "http://localhost:4200")

public class TipoCargoController {
    @Autowired
    TipoCargoService tipoCargoService;

    @GetMapping("/list")
    public ResponseEntity<List<TipoCargo>> list() {
        List<TipoCargo> list = tipoCargoService.findByActivoTrue().get();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<TipoCargo>> listAll() {
        List<TipoCargo> list = tipoCargoService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TipoCargo> getById(@PathVariable("id") long id) {
        if (!tipoCargoService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        TipoCargo tipoCargo = tipoCargoService.findById(id).get();
        return new ResponseEntity<TipoCargo>(tipoCargo, HttpStatus.OK);

    }

    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<TipoCargo> getByNombre(@PathVariable("nombre") String nombre) {
        if (!tipoCargoService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        TipoCargo tipoCargo = tipoCargoService.findByNombre(nombre).get();
        return new ResponseEntity<TipoCargo>(tipoCargo, HttpStatus.OK);

    }

    private ResponseEntity<?> validations(TipoCargoDto tipoCargoDto, Long id) {
        if (StringUtils.isBlank(tipoCargoDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if (tipoCargoDto.getDescripcion() == null)
            return new ResponseEntity(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private TipoCargo createUpdate(TipoCargo tipoCargo, TipoCargoDto tipoCargoDto) {

        if (tipoCargoDto.getNombre() != null && !tipoCargoDto.getNombre().isEmpty()
                && !tipoCargoDto.getNombre().equals(tipoCargo.getNombre()))
            tipoCargo.setNombre(tipoCargoDto.getNombre());

        if (tipoCargoDto.getDescripcion() != null && !tipoCargoDto.getDescripcion().isEmpty()
                && !tipoCargoDto.getDescripcion().equals(tipoCargo.getDescripcion()))
            tipoCargo.setDescripcion(tipoCargoDto.getDescripcion());

        tipoCargo.setEshospitalario(tipoCargoDto.isEshospitalario());

        tipoCargo.setActivo(true);

        return tipoCargo;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoCargoDto tipoCargoDto) {

        ResponseEntity<?> respuestaValidaciones = validations(tipoCargoDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            TipoCargo tipoCargo = createUpdate(new TipoCargo(), tipoCargoDto);
            tipoCargoService.save(tipoCargo);
            return new ResponseEntity<>(new Mensaje("Tipo cargo creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody TipoCargoDto tipoCargoDto) {
        if (!tipoCargoService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(tipoCargoDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            TipoCargo tipoCargo = createUpdate(tipoCargoService.findById(id).get(), tipoCargoDto);
            tipoCargoService.save(tipoCargo);
            return new ResponseEntity<>(new Mensaje("Tipo cargo modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!tipoCargoService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        TipoCargo tipoCargo = tipoCargoService.findById(id).get();
        tipoCargo.setActivo(false);
        tipoCargoService.save(tipoCargo);
        return new ResponseEntity<>(new Mensaje("Tipo Cargo eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!tipoCargoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        tipoCargoService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Tipo Cargo eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
