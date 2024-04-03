package com.guardias.backend.controller;

import java.util.ArrayList;
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

import com.guardias.backend.dto.CategoriaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Categoria;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.service.CategoriaService;
import com.guardias.backend.service.RevistaService;

@RestController
@RequestMapping("/categoria")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    RevistaService revistaService;

    @GetMapping("/list")
    public ResponseEntity<List<Categoria>> list() {
        List<Categoria> list = categoriaService.findByActivoTrue();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Categoria>> listAll() {
        List<Categoria> list = categoriaService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable("id") Long id) {
        if (!categoriaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe categoria"), HttpStatus.NOT_FOUND);
        Categoria categoria = categoriaService.findById(id).get();
        return new ResponseEntity(categoria, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Categoria> getByNombre(@PathVariable("nombre") String nombre) {
        if (!categoriaService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe categoria con ese nombre"), HttpStatus.NOT_FOUND);
        Categoria categoria = categoriaService.findByNombre(nombre).get();
        return new ResponseEntity(categoria, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(CategoriaDto categoriaDto) {
        if (categoriaDto.getNombre() == null)
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Categoria createUpdate(Categoria categoria, CategoriaDto categoriaDto) {

        if (categoriaDto.getNombre() != null && categoria.getNombre() != categoriaDto.getNombre())
            categoria.setNombre(categoriaDto.getNombre());

        if (categoriaDto.getIdRevista() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (categoria.getRevistas() != null) {
                for (Revista revista : categoria.getRevistas()) {
                    for (Long id : categoriaDto.getIdRevista()) {
                        if (!revista.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }
            List<Long> idsToAdd = idList.isEmpty() ? categoriaDto.getIdRevista() : idList;
            for (Long id : idsToAdd) {
                categoria.getRevistas().add(revistaService.findById(id).get());
                revistaService.findById(id).get().setCategoria(categoria);
            }
        }
        categoria.setActivo(true);
        return categoria;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CategoriaDto categoriaDto) {
        ResponseEntity<?> respuestaValidaciones = validations(categoriaDto);

        if (categoriaService.existsByNombre(categoriaDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Categoria categoria = createUpdate(new Categoria(), categoriaDto);
            categoriaService.save(categoria);
            return new ResponseEntity<>(new Mensaje("Categoria creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CategoriaDto categoriaDto) {
        if (!categoriaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe categoria"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(categoriaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Categoria categoria = createUpdate(categoriaService.findById(id).get(), categoriaDto);
            categoriaService.save(categoria);
            return new ResponseEntity<>(new Mensaje("Categoria actualizada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!categoriaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe categoria con ese ID"), HttpStatus.NOT_FOUND);
        Categoria categoria = categoriaService.findById(id).get();
        categoria.setActivo(false);
        categoriaService.save(categoria);
        return new ResponseEntity<>(new Mensaje("Categoria eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!categoriaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe categoria con ese ID"), HttpStatus.NOT_FOUND);
        categoriaService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Categoria eliminada FISICAMENTE"), HttpStatus.OK);

    }

}
