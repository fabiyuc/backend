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
import com.guardias.backend.dto.CategoriaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Categoria;
import com.guardias.backend.service.CategoriaService;

@RestController
@RequestMapping("/categoria")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    @GetMapping("/list")
    public ResponseEntity<List<Categoria>> list() {
        List<Categoria> list = categoriaService.list();
        return new ResponseEntity<List<Categoria>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable("id") Long id) {
        if (!categoriaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe categoria con ese ID"), HttpStatus.NOT_FOUND);
        Categoria categoria = categoriaService.findById(id).get();
        return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Categoria> getByNombre(@PathVariable("nombre") String nombre) {
        if (!categoriaService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe categoria con ese nombre"), HttpStatus.NOT_FOUND);
        Categoria categoria = categoriaService.getByNombre(nombre).get();
        return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CategoriaDto categoriaDto) {
        if (StringUtils.isBlank(categoriaDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (categoriaService.existsByNombre(categoriaDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDto.getNombre());
        categoriaService.save(categoria);
        return new ResponseEntity<>(new Mensaje("Categoria creada"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CategoriaDto categoriaDto) {
        if (!categoriaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe categoria con ese ID"), HttpStatus.NOT_FOUND);

        if (categoriaService.existsByNombre(categoriaDto.getNombre())
                && categoriaService.getByNombre(categoriaDto.getNombre()).get().getId() == id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(categoriaDto.getNombre()))
            return new ResponseEntity<>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        Categoria categoria = categoriaService.findById(id).get();
        categoria.setNombre(categoriaDto.getNombre());
        categoriaService.save(categoria);

        return new ResponseEntity<>(new Mensaje("Categoria Actualizada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (!categoriaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe categoria con ese ID"), HttpStatus.NOT_FOUND);
        categoriaService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Categoria eliminada"), HttpStatus.OK);

    }

}
