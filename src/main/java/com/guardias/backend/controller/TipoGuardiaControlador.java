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
import com.guardias.backend.dto.TipoGuardiaDto;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.service.TipoGuardiaService;

@RestController
@RequestMapping("/tipoGuardia")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoGuardiaControlador {
    @Autowired
    TipoGuardiaService tipoGuardiaServicio;

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<TipoGuardia> getByNombre(@PathVariable("nombre") String nombre) {
        if (!tipoGuardiaServicio.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe el tipo de guardia"), HttpStatus.NOT_FOUND);
        TipoGuardia tipoGuardia = tipoGuardiaServicio.getByNombre(nombre).get();
        return new ResponseEntity<TipoGuardia>(tipoGuardia, HttpStatus.OK);
    }

    @GetMapping("/detaildescripcion/{descripcion}")
    public ResponseEntity<TipoGuardia> getByDescripcion(@PathVariable("descripcion") String descripcion) {
        if (!tipoGuardiaServicio.existsByDescripcion(descripcion))
            return new ResponseEntity(new Mensaje("no existe el tipo de guardia"), HttpStatus.NOT_FOUND);
        TipoGuardia tipoGuardia = tipoGuardiaServicio.getByDescripcion(descripcion).get();
        return new ResponseEntity<TipoGuardia>(tipoGuardia, HttpStatus.OK);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<TipoGuardia>> list() {
        List<TipoGuardia> list = tipoGuardiaServicio.list();
        return new ResponseEntity<List<TipoGuardia>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TipoGuardia> getById(@PathVariable("id") Long id) {
        if (!tipoGuardiaServicio.existsById(id))
            return new ResponseEntity(new Mensaje("No existe el tipo de guardia"), HttpStatus.NOT_FOUND);
        TipoGuardia tipoGuardia = tipoGuardiaServicio.getOne(id).get();
        return new ResponseEntity<TipoGuardia>(tipoGuardia, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoGuardiaDto tipoGuardiaDto) {
        if (StringUtils.isBlank(tipoGuardiaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(tipoGuardiaDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (tipoGuardiaServicio.existsByNombre(tipoGuardiaDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);
        if (tipoGuardiaServicio.existsByDescripcion(tipoGuardiaDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("esa descripcion ya existe"),
                    HttpStatus.BAD_REQUEST);
        TipoGuardia tipoGuardia = new TipoGuardia();
        tipoGuardia.setNombre(tipoGuardiaDto.getNombre());
        tipoGuardia.setDescripcion(tipoGuardiaDto.getDescripcion());
        tipoGuardiaServicio.save(tipoGuardia);
        return new ResponseEntity(new Mensaje("tipo de guardia creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TipoGuardiaDto tipoGuardiaDto) {
        if (!tipoGuardiaServicio.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el tipo de guardia"), HttpStatus.NOT_FOUND);

        if (tipoGuardiaServicio.existsByNombre(tipoGuardiaDto.getNombre()) &&
                tipoGuardiaServicio.getByNombre(tipoGuardiaDto.getNombre()).get().getId() == id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(tipoGuardiaDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(tipoGuardiaDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"), HttpStatus.BAD_REQUEST);

        TipoGuardia tipoGuardia = tipoGuardiaServicio.getOne(id).get();
        tipoGuardia.setNombre(tipoGuardiaDto.getNombre());
        tipoGuardia.setDescripcion(tipoGuardiaDto.getDescripcion());
        tipoGuardiaServicio.save(tipoGuardia);
        return new ResponseEntity(new Mensaje("servicio actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (!tipoGuardiaServicio.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el tipo de guardia"), HttpStatus.NOT_FOUND);
        tipoGuardiaServicio.delete(id);
        return new ResponseEntity(new Mensaje("tipo de guardia eliminado"), HttpStatus.OK);
    }

}