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

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ArticuloDto articuloDto) {
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

        Articulo articulo = new Articulo();
        articulo.setNumero(articuloDto.getNumero());
        articulo.setDenominacion(articuloDto.getDenominacion());
        articulo.setDetalle(articuloDto.getDetalle());
        articulo.setEstado(articuloDto.getEstado());
        articulo.setFechaAlta(articuloDto.getFechaAlta());
        articulo.setFechaBaja(articuloDto.getFechaBaja());
        articulo.setFechaModificacion(articuloDto.getFechaModificacion());
        articulo.setMotivoModificacion(articuloDto.getMotivoModificacion());
        articulo.setSubArticulos(articuloDto.getSubArticulos());
        articulo.setIncisos(articuloDto.getIncisos());

        articuloService.save(articulo);
        return new ResponseEntity<Mensaje>(new Mensaje("Articulo creado correctamente"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ArticuloDto articuloDto) {
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

        Articulo articulo = articuloService.findById(id).get();

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

        articuloService.save(articulo);
        return new ResponseEntity<Mensaje>(new Mensaje("Articulo modificado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (!articuloService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("Articulo no encontrado"), HttpStatus.NOT_FOUND);
        articuloService.deleteById(id);
        return new ResponseEntity<Mensaje>(new Mensaje("Articulo eliminado"), HttpStatus.OK);
    }
}
