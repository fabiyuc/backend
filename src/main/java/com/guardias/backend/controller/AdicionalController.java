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

import com.guardias.backend.dto.AdicionalDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Adicional;
import com.guardias.backend.service.AdicionalService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/adicional")
@CrossOrigin(origins = "http://localhost:4200")
public class AdicionalController {

    @Autowired
    AdicionalService adicionalService;

    @GetMapping("/list")
    public ResponseEntity<List<Adicional>> list() {
        List<Adicional> list = adicionalService.list();
        return new ResponseEntity<List<Adicional>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Adicional> getById(@PathVariable("id") Long id) {
        if (!adicionalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe adicional con ese nombre"), HttpStatus.NOT_FOUND);
        Adicional adicional = adicionalService.findById(id).get();
        return new ResponseEntity<Adicional>(adicional, HttpStatus.OK);

    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Adicional> getByNombre(@PathVariable("nombre") String nombre) {
        if (!adicionalService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe adicional con ese nombre"), HttpStatus.NOT_FOUND);
        Adicional adicional = adicionalService.getByNombre(nombre).get();
        return new ResponseEntity<Adicional>(adicional, HttpStatus.OK);

    }

    private ResponseEntity<?> validations(AdicionalDto adicionalDto) {
        if (StringUtils.isBlank(adicionalDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (adicionalService.existsByNombre(adicionalDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Adicional createUpdate(Adicional adicional, AdicionalDto adicionalDto) {
        if (!adicionalDto.getNombre().equals(adicional.getNombre()))
            adicional.setNombre(adicionalDto.getNombre());

        if (!adicionalDto.getRevistas().equals(adicional.getRevistas()))
            adicional.setRevistas(adicionalDto.getRevistas());
        return adicional;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AdicionalDto adicionalDto) {

        ResponseEntity<?> respuestaValidaciones = validations(adicionalDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Adicional adicional = createUpdate(new Adicional(), adicionalDto);
            adicionalService.save(adicional);
            return new ResponseEntity<>(new Mensaje("Adicional creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody AdicionalDto adicionalDto) {
        if (!adicionalService.existsById(id))
            return new ResponseEntity(new Mensaje("El adicional no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(adicionalDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Adicional adicional = createUpdate(adicionalService.findById(id).get(), adicionalDto);
            adicionalService.save(adicional);
            return new ResponseEntity<>(new Mensaje("Adicional actualizado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PostMapping("/{idAdicional}/addRevista/{idRevista}")
    public ResponseEntity<?> agregarRevista(@PathVariable("idAdicional") Long idAdicional,
            @PathVariable("idRevista") Long idRevista) {
        try {
            adicionalService.agregarRevista(idAdicional, idRevista);
            return new ResponseEntity<>(new Mensaje("Revista agregada al adicional correctamente"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se encontró el adicional con el ID proporcionado"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al agregar la revista al adicional"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // BORRADO LOGICO
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") long id) {
        if (!adicionalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Adicional adicional = adicionalService.findById(id).get();
        adicional.setActivo(false);
        adicionalService.save(adicional);
        return new ResponseEntity<>(new Mensaje("Adicional eliminado correctamente"), HttpStatus.OK);
    }

    // BORRADO FISICO
    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!adicionalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        adicionalService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Adicional eliminado FISICAMENTE"), HttpStatus.OK);
    }

}