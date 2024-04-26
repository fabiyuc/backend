package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.entity.Ley;
import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.service.ArticuloService;
import com.guardias.backend.service.IncisoService;
import com.guardias.backend.service.NovedadPersonalService;

@Controller
@RequestMapping("/articulo")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticuloController {
    @Autowired
    ArticuloService articuloService;

    @Autowired
    IncisoService incisoService;

    @Autowired
    LeyController leyController;

    @Autowired
    NovedadPersonalService novedadPersonalService;

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

        try {
            if (articuloDto.getIdArticuloPadre() != null && (articulo.getArticuloPadre() == null
                    || !Objects.equals(articulo.getArticuloPadre().getId(), articuloDto.getIdArticuloPadre()))) {
                articulo.setArticuloPadre(articuloService.findById(articuloDto.getIdArticuloPadre()).get());
            }
        } catch (Exception ex) {
            String mensajePersonalizado = "Error en getIdArticulo: " + articuloDto.getIdArticuloPadre()
                    + " - Exception:"
                    + ex.getMessage();
            System.out.println(mensajePersonalizado);
            ex.printStackTrace();
        }

        try {
            if (articuloDto.getIdIncisos() != null) {
                List<Long> idList = new ArrayList<Long>();
                if (articulo.getIncisos() != null) {
                    for (Inciso incisoList : articulo.getIncisos()) {
                        for (Long id : articuloDto.getIdIncisos()) {
                            if (!incisoList.getId().equals(id)) {
                                idList.add(id);
                            }
                        }
                    }
                } else {
                    articulo.setIncisos(new ArrayList<>());
                }

                List<Long> idsToAdd = idList.isEmpty() ? articuloDto.getIdIncisos() : idList;
                for (Long id : idsToAdd) {
                    try {
                        if (incisoService.existsById(id)) {
                            Inciso inciso = incisoService.findById(id).get();
                            articulo.getIncisos().add(inciso);
                        }
                        incisoService.findById(id).get().setArticulo(articulo);
                    } catch (Exception e) {
                        String mensajePersonalizado = "linea 130 - Error en getIdIncisos: "
                                + articuloDto.getIdIncisos();
                        System.out.println(mensajePersonalizado);
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception ex) {
            String mensajePersonalizado = "linea 139 - Error en getIdIncisos: " + articuloDto.getIdIncisos()
                    + " - Exception:"
                    + ex.getMessage() + " " + ex.getCause() + "-------------------------------------------------";
            System.out.println(mensajePersonalizado);
            ex.printStackTrace();
        }

        try {
            if (articuloDto.getIdNovedadesPersonales() != null) {
                List<Long> idList = new ArrayList<Long>();
                if (articulo.getNovedadesPersonales() != null) {
                    for (NovedadPersonal novedad : articulo.getNovedadesPersonales()) {
                        for (Long id : articuloDto.getIdNovedadesPersonales()) {
                            if (!novedad.getId().equals(id)) {
                                idList.add(id);
                            }
                        }
                    }
                } else {
                    articulo.setNovedadesPersonales(new ArrayList<>());
                }
                List<Long> idsToAdd = idList.isEmpty() ? articuloDto.getIdNovedadesPersonales() : idList;
                for (Long id : idsToAdd) {
                    articulo.getNovedadesPersonales().add(novedadPersonalService.findById(id).get());
                    novedadPersonalService.findById(id).get().setArticulo(articulo);
                }
            }
        } catch (Exception ex) {
            String mensajePersonalizado = "Error en getIdNovedadesPersonales: " + articuloDto.getIdNovedadesPersonales()
                    + " - Exception:" + ex.getMessage();
            System.out.println(mensajePersonalizado);
            ex.printStackTrace();
        }

        return articulo;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ArticuloDto articuloDto) {

        ResponseEntity<?> respuestaValidaciones = leyController.validations(articuloDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            try {
                Articulo articulo = createUpdate(new Articulo(), articuloDto);
                articuloService.save(articulo);
            } catch (Exception ex) {
                String mensajePersonalizado = "Se produjo un error al guardar el artículo: " + ex.getMessage();
                System.out.println(mensajePersonalizado);
                ex.printStackTrace();
            }

            return new ResponseEntity<Mensaje>(new Mensaje("Articulo creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ArticuloDto articuloDto) {
        if (!articuloService.activo(id))
            return new ResponseEntity(new Mensaje("El articulo no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = leyController.validations(articuloDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Articulo articulo = createUpdate(articuloService.findById(id).get(), articuloDto);
            articuloService.save(articulo);
            return new ResponseEntity<Mensaje>(new Mensaje("Articulo modificado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
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
