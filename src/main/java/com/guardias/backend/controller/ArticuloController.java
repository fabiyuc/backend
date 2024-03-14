package com.guardias.backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

import com.guardias.backend.dto.ArticuloDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Articulo;
import com.guardias.backend.entity.Ley;
import com.guardias.backend.service.ArticuloService;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/articulo")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticuloController {
    @Autowired
    ArticuloService articuloService;

    @Autowired
    LeyController leyController;

    @GetMapping("/list")
    public ResponseEntity<List<Articulo>> list() {
        List<Articulo> list = articuloService.findByActivoTrue();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Articulo>> listAll() {
        List<Articulo> list = articuloService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Articulo>> getById(@PathVariable("id") Long id) {
        if (!articuloService.activo(id))
            return new ResponseEntity(new Mensaje("Articulo no encontrado"), HttpStatus.NOT_FOUND);
        Articulo articulo = articuloService.findById(id).get();
        return new ResponseEntity(articulo, HttpStatus.OK);
    }

    private Articulo createUpdate(Articulo articulo, ArticuloDto articuloDto) {

        Ley ley = leyController.createUpdate(articulo, articuloDto);
        articulo = (Articulo) ley;

        /*
         * if (ley.getTipoLey() == null ||
         * (leyDto.getIdTipoLey() != null &&
         * !Objects.equals(ley.getTipoLey().getId(),
         * leyDto.getIdTipoLey()))) {
         * ley.setTipoLey(tipoLeyService.findById(leyDto.getIdTipoLey()).get());
         * }
         */

        if (articulo.getArticulo() == null || (articuloDto.getIdArticulo() == null
                && !Objects.equals(articulo.getArticulo().getId(), articuloDto.getIdArticulo()))) {
            articulo.setArticulo(articuloService.findById(articuloDto.getIdArticulo()).get());
        }

        if (articulo.getSubArticulos() != null) {
            for (Articulo suarArticulo : articulo.getSubArticulos()) {
                Set<Articulo> listArticulos = new HashSet<Articulo>();

                // Recorrer el DTO ver que no esten repetidos, si no estan, agregarlos a una
                // lista auxiliar para
                // luego meterlos en la lista de getSubArticulos
            }
        } else if (articuloDto.getIdArticulo() != null) {
            for (iterable_type iterable_element : iterable) {
                // recorrer el getIdArticulo agregando los elementos a getSubArticulos
            }
        }

        if (articulo.getSubArticulos() != articuloDto.getSubArticulos() && articuloDto.getSubArticulos() != null
                && !articuloDto.getSubArticulos().isEmpty())
            articulo.setSubArticulos(articuloDto.getSubArticulos());

        if (articulo.getIncisos() != articuloDto.getIncisos() && articuloDto.getIncisos() != null
                && !articuloDto.getIncisos().isEmpty())
            articulo.setIncisos(articuloDto.getIncisos());

        return articulo;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ArticuloDto articuloDto) {

        ResponseEntity<?> respuestaValidaciones = leyController.validationsCreate(articuloDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Articulo articulo = createUpdate(new Articulo(), articuloDto);
            articuloService.save(articulo);
            return new ResponseEntity<Mensaje>(new Mensaje("Articulo creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ArticuloDto articuloDto) {
        if (!articuloService.activo(id))
            return new ResponseEntity(new Mensaje("El articulo no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = leyController.validations(articuloDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Articulo articulo = createUpdate(articuloService.findById(id).get(), articuloDto);
            articuloService.save(articulo);
            return new ResponseEntity<Mensaje>(new Mensaje("Articulo modificado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PostMapping("/{idArticulo}/agregarInciso/{idInciso}")
    public ResponseEntity<?> agregarInciso(@PathVariable("idArticulo") Long idArticulo,
            @PathVariable("idInciso") Long idInciso) {
        try {
            articuloService.agregarInciso(idArticulo, idInciso);

            return new ResponseEntity<>(new Mensaje("Inciso agregado al articulo correctamente"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se encontró el articlo con el ID proporcionado"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al agregar el inciso agregado al articulo "),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{idArticulo}/addSubArticulo/{idSubArticulo}")
    public ResponseEntity<?> agregarSubArticulo(@PathVariable("idArticulo") Long idArticulo,
            @PathVariable("idSubArticulo") Long idSubArticulo) {
        try {
            articuloService.agregarSubArticulo(idArticulo, idSubArticulo);

            return new ResponseEntity<>(new Mensaje("SubArticulo agregado al articulo correctamente"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se encontró el articlo con el ID proporcionado"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al agregar el SubArticulo al articulo "),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!articuloService.activo(id))
            return new ResponseEntity(new Mensaje("El articulo no existe"), HttpStatus.NOT_FOUND);
        Articulo articulo = articuloService.findById(id).get();
        articulo.setActivo(false);
        articuloService.save(articulo);
        return new ResponseEntity<>(new Mensaje("Articulo eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!articuloService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("Articulo no encontrado"), HttpStatus.NOT_FOUND);
        articuloService.deleteById(id);
        return new ResponseEntity<Mensaje>(new Mensaje("Articulo eliminado FISICAMENTE"), HttpStatus.OK);
    }
}
