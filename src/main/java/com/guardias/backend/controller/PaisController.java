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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.PaisDto;
import com.guardias.backend.entity.Pais;
import com.guardias.backend.service.PaisService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/pais")
@CrossOrigin(origins = "http://localhost:4200")
public class PaisController {

    @Autowired
    PaisService paisService;

    @GetMapping("/list")
    public ResponseEntity<List<Pais>> list() {
        List<Pais> list = paisService.findByActivoTrue().get();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Pais>> listAll() {
        List<Pais> list = paisService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Pais>> getById(@PathVariable("id") Long id) {
        if (!paisService.activo(id))
            return new ResponseEntity(new Mensaje("pais no existe"), HttpStatus.NOT_FOUND);
        Pais pais = paisService.findById(id).get();
        return new ResponseEntity(pais, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Pais> getByNombre(@PathVariable("nombre") String nombre) {
        if (!paisService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe el nombre del pais"), HttpStatus.NOT_FOUND);
        Pais pais = paisService.findByNombre(nombre).get();
        return new ResponseEntity<Pais>(pais, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PaisDto paisDto) {
        if (StringUtils.isBlank(paisDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (paisService.existsByNombre(paisDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(paisDto.getNacionalidad()))
            return new ResponseEntity(new Mensaje("la nacionalidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (paisService.existsByNacionalidad(paisDto.getNacionalidad()))
            return new ResponseEntity(new Mensaje("esa nacionalidad ya existe"),
                    HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(paisDto.getCodigo()))
            return new ResponseEntity(new Mensaje("el codigo es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        Pais pais = new Pais();
        pais.setNombre(paisDto.getNombre());
        pais.setNacionalidad(paisDto.getNacionalidad());
        pais.setCodigo(paisDto.getCodigo());

        // ******* no necesito guardar ni modificar la listas */
        // pais.setProvincias(paisDto.getProvincias());
        paisService.save(pais);
        return new ResponseEntity(new Mensaje("pais creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody PaisDto paisDto) {
        if (!paisService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el pais"), HttpStatus.NOT_FOUND);

        if (paisService.existsByNombre(paisDto.getNombre()) &&
                paisService.findByNombre(paisDto.getNombre()).get().getId() != id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(paisDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(paisDto.getNacionalidad()))
            return new ResponseEntity(new Mensaje("la nacionalidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (paisService.existsByNacionalidad(paisDto.getNacionalidad()))
            return new ResponseEntity(new Mensaje("esa nacionalidad ya existe"),
                    HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(paisDto.getCodigo()))
            return new ResponseEntity(new Mensaje("el codigo es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        Pais pais = paisService.findById(id).get();
        // ******* La validacion antes de setear los valores me gusta que sea en la
        // misma linea pero no muestra mensajes de error

        // ******* Ahora está mostrando los msjs de error por la validacion previa, ver
        // como queda para limpiar el codigo */
        if (pais.getNombre() != paisDto.getNombre() && paisDto.getNombre() != null && !paisDto.getNombre().isEmpty())
            pais.setNombre(paisDto.getNombre());

        if (pais.getCodigo() != paisDto.getCodigo() && paisDto.getCodigo() != null && !paisDto.getCodigo().isEmpty())
            pais.setCodigo(paisDto.getCodigo());

        if (pais.getNacionalidad() != paisDto.getNacionalidad() && paisDto.getNacionalidad() != null
                && !paisDto.getNacionalidad().isEmpty())
            pais.setNacionalidad(paisDto.getNacionalidad());

        // ******* no necesito guardar ni modificar la listas */
        // if (!pais.getProvincias().equals(paisDto.getProvincias()))
        // pais.setProvincias(paisDto.getProvincias());

        paisService.save(pais);
        return new ResponseEntity(new Mensaje("pais actualizado"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!paisService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el pais"), HttpStatus.NOT_FOUND);

        Pais pais = paisService.findById(id).get();
        pais.setActivo(false);
        paisService.save(pais);
        return new ResponseEntity(new Mensaje("pais eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!paisService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el pais"), HttpStatus.NOT_FOUND);
        paisService.deleteById(id);
        return new ResponseEntity(new Mensaje("pais eliminado FISICAMENTE"), HttpStatus.OK);
    }

}