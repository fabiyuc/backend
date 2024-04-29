package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import com.guardias.backend.dto.RevistaDto;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.service.AdicionalService;
import com.guardias.backend.service.CargaHorariaService;
import com.guardias.backend.service.CategoriaService;
import com.guardias.backend.service.LegajoService;
import com.guardias.backend.service.RevistaService;
import com.guardias.backend.service.TipoRevistaService;

@RestController
@RequestMapping("/revista")
@CrossOrigin(origins = "http://localhost:4200")
public class RevistaController {

    @Autowired
    RevistaService revistaService;
    @Autowired
    TipoRevistaService tipoRevistaService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    AdicionalService adicionalService;
    @Autowired
    CargaHorariaService cargaHorariaService;
    @Autowired
    LegajoService legajoService;

    @GetMapping("/list")
    public ResponseEntity<List<Revista>> list() {
        List<Revista> list = revistaService.findByActivoTrue();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Revista>> listAll() {
        List<Revista> list = revistaService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Revista>> getById(@PathVariable("id") Long id) {
        if (!revistaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la revista con ese ID"), HttpStatus.NOT_FOUND);
        Revista revista = revistaService.findById(id).get();
        return new ResponseEntity(revista, HttpStatus.OK);
    }

    public ResponseEntity<?> validations(RevistaDto revistaDto) {

        if (revistaDto.getAgrupacion() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La agrupacion es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (revistaDto.getIdTipoRevista() == null)
            return new ResponseEntity(new Mensaje("El tipo de revista es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (revistaDto.getIdCategoria() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La categoria es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (revistaDto.getIdAdicional() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El adicional es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (revistaDto.getIdCargaHoraria() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La carga horaria es obligatoria"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    public Revista createUpdate(Revista revista, RevistaDto revistaDto) {
        if (revistaDto.getAgrupacion() != null && revista.getAgrupacion() != revistaDto.getAgrupacion())
            revista.setAgrupacion(revistaDto.getAgrupacion());

        if (revistaDto.getIdTipoRevista() != null) {
            if (revista.getTipoRevista() == null
                    || !Objects.equals(revista.getTipoRevista().getId(), revistaDto.getIdTipoRevista())) {
                revista.setTipoRevista(tipoRevistaService.findById(revistaDto.getIdTipoRevista()).get());
            }
        }

        // Verificar si la categoría se proporciona en el RevistaDto
        if (revistaDto.getIdCategoria() != null) {
            // Si la categoría ya está establecida o es diferente a la proporcionada,
            // actualizar la categoría
            if (revista.getCategoria() == null
                    || !Objects.equals(revista.getCategoria().getId(), revistaDto.getIdCategoria())) {
                revista.setCategoria(categoriaService.findById(revistaDto.getIdCategoria()).get());
            }
        }

        if (revistaDto.getIdAdicional() != null) {
            // Si el adicional ya está establecido o es diferente al proporcionado,
            // actualizar
            // el adicional
            if (revista.getAdicional() == null
                    || !Objects.equals(revista.getAdicional().getId(), revistaDto.getIdAdicional())) {
                revista.setAdicional(adicionalService.findById(revistaDto.getIdAdicional()).get());
            }
        }
        if (revistaDto.getIdCargaHoraria() != null) {
            if (revista.getCargaHoraria() == null
                    || !Objects.equals(revista.getCargaHoraria().getId(), revistaDto.getIdCargaHoraria())) {
                revista.setCargaHoraria(cargaHorariaService.findById(revistaDto.getIdCargaHoraria()).get());
            }
        }

        if (revistaDto.getIdLegajos() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (revista.getLegajos() != null) {
                for (Legajo legajo : revista.getLegajos()) {
                    for (Long id : revistaDto.getIdLegajos()) {
                        if (!legajo.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                revista.setLegajos(new ArrayList<Legajo>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? revistaDto.getIdLegajos() : idList;
            for (Long id : idsToAdd) {
                revista.getLegajos().add(legajoService.findById(id).get());
                legajoService.findById(id).get().setRevista(revista);
            }

        }
        revista.setActivo(true);
        return revista;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RevistaDto revistaDto) {
        ResponseEntity<?> respuestaValidaciones = validations(revistaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Revista revista = createUpdate(new Revista(), revistaDto);
            revistaService.save(revista);
            return new ResponseEntity<>(new Mensaje("Revista creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody RevistaDto revistaDto) {
        if (!revistaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la revista"), HttpStatus.NOT_FOUND);
        ResponseEntity<?> respuestaValidaciones = validations(revistaDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Revista revista = createUpdate(revistaService.findById(id).get(), revistaDto);
            revistaService.save(revista);
            return new ResponseEntity(new Mensaje("revista actualizada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!revistaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la revista"), HttpStatus.NOT_FOUND);

        Revista revista = revistaService.findById(id).get();
        revista.setActivo(false);
        revistaService.save(revista);
        return new ResponseEntity(new Mensaje("revista eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!revistaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la revista"), HttpStatus.NOT_FOUND);
        revistaService.deleteById(id);
        return new ResponseEntity(new Mensaje("revista eliminada FISICAMENTE"), HttpStatus.OK);
    }
}
