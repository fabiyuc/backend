package com.guardias.backend.controller;

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

import com.guardias.backend.dto.ArticuloDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Articulo;
import com.guardias.backend.service.ArticuloService;

import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/articulo")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticuloController {
    @Autowired
    ArticuloService articuloService;

    @GetMapping("/lista")
    public ResponseEntity<List<Articulo>> list() {
        List<Articulo> list = articuloService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<List<Articulo>> getById(@PathVariable("id") Long id) {
        if (!articuloService.existsById(id))
            return new ResponseEntity(new Mensaje("Articulo no encontrado"), HttpStatus.NOT_FOUND);
        Articulo articulo = articuloService.findById(id).get();
        return new ResponseEntity(articulo, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(ArticuloDto articuloDto) {

        if (StringUtils.isBlank(articuloDto.getNumero()))
            return new ResponseEntity<Mensaje>(new Mensaje("El numero es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(articuloDto.getDenominacion()))
            return new ResponseEntity<Mensaje>(new Mensaje("La denominacion es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (articuloDto.getEstado() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El estado es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (articuloDto.getFechaAlta() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La fecha de alta es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (articuloService.existsByNumero(articuloDto.getNumero()))
            return new ResponseEntity<Mensaje>(new Mensaje("Ese numero ya existe"),
                    HttpStatus.BAD_REQUEST);

        if (articuloService.existsByDenominacion(articuloDto.getDenominacion()))
            return new ResponseEntity<Mensaje>(new Mensaje("Esa denominacion ya existe"),
                    HttpStatus.BAD_REQUEST);
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Articulo createUpdate(Articulo articulo, ArticuloDto articuloDto) {
        if (!articuloDto.getNumero().equals(articulo.getNumero()))
            articulo.setNumero(articuloDto.getNumero());
        if (!articuloDto.getDenominacion().equals(articulo.getDenominacion()))
            articulo.setDenominacion(articuloDto.getDenominacion());
        if (!articuloDto.getDetalle().equals(articulo.getDetalle()))
            articulo.setDetalle(articuloDto.getDetalle());
        if (!articuloDto.getEstado().equals(articulo.getEstado()))
            articulo.setEstado(articuloDto.getEstado());
        if (!articuloDto.getFechaAlta().equals(articulo.getFechaAlta()))
            articulo.setFechaAlta(articuloDto.getFechaAlta());
        if (!articuloDto.getFechaBaja().equals(articulo.getFechaBaja()))
            articulo.setFechaBaja(articuloDto.getFechaBaja());
        if (!articuloDto.getFechaModificacion().equals(articulo.getFechaModificacion()))
            articulo.setFechaModificacion(articuloDto.getFechaModificacion());
        if (!articuloDto.getMotivoModificacion().equals(articulo.getMotivoModificacion()))
            articulo.setMotivoModificacion(articuloDto.getMotivoModificacion());
        if (!articuloDto.getSubArticulos().equals(articulo.getSubArticulos()))
            articulo.setSubArticulos(articuloDto.getSubArticulos());
        if (!articuloDto.getIncisos().equals(articulo.getIncisos()))
            articulo.setIncisos(articuloDto.getIncisos());
        if (!articuloDto.getNovedadPersonal().equals(articulo.getNovedadPersonal()))
            articulo.setNovedadPersonal(articuloDto.getNovedadPersonal());
        return articulo;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ArticuloDto articuloDto) {

        ResponseEntity<?> respuestaValidaciones = validations(articuloDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Articulo articulo = createUpdate(new Articulo(), articuloDto);
            articuloService.save(articulo);
            return new ResponseEntity<Mensaje>(new Mensaje("Articulo creado correctamente"), HttpStatus.OK);
        } else {
            return new ResponseEntity<Mensaje>(new Mensaje("Error al crear el elemento"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ArticuloDto articuloDto) {
        if (!articuloService.existsById(id))
            return new ResponseEntity(new Mensaje("El articulo no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(articuloDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Articulo articulo = createUpdate(articuloService.findById(id).get(), articuloDto);
            articuloService.save(articulo);
            return new ResponseEntity<Mensaje>(new Mensaje("Articulo modificado correctamente"), HttpStatus.OK);
        } else {
            return new ResponseEntity<Mensaje>(new Mensaje("Error al crear el elemento"),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (!articuloService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("Articulo no encontrado"), HttpStatus.NOT_FOUND);
        articuloService.deleteById(id);
        return new ResponseEntity<Mensaje>(new Mensaje("Articulo eliminado"), HttpStatus.OK);
    }
}
