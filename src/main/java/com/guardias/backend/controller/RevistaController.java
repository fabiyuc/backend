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
import com.guardias.backend.entity.CargaHoraria;
import com.guardias.backend.entity.Categoria;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.enums.AgrupacionEnum;
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
        List<Revista> list = revistaService.findByActivoTrue().get();
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

    @PostMapping("/check")
    public ResponseEntity<Revista> checkRevista(@RequestBody RevistaDto revistaDto) {
        // Convertir el String de agrupación a AgrupacionEnum
        AgrupacionEnum agrupacionEnum = AgrupacionEnum.fromDisplayName(revistaDto.getAgrupacion());

        // Verificar que el enum no sea nulo

        Revista existingRevista = revistaService.findByAttributes(
                revistaDto.getIdTipoRevista(),
                revistaDto.getIdCategoria(),
                revistaDto.getIdAdicional(),
                revistaDto.getIdCargaHoraria(),
                agrupacionEnum);
        if (existingRevista != null) {
            return ResponseEntity.ok(existingRevista);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> validations(RevistaDto revistaDto, Long id) {
        if (revistaDto.getAgrupacion() == null)
            return new ResponseEntity<>(new Mensaje("La agrupación es obligatoria"), HttpStatus.BAD_REQUEST);

        // verificar que la revista que se va a crear no exista
        if (id == 0) {
            AgrupacionEnum agrupacionEnum = AgrupacionEnum.fromDisplayName(revistaDto.getAgrupacion());
            Revista existingRevista = revistaService.findByAttributes(
                    revistaDto.getIdTipoRevista(),
                    revistaDto.getIdCategoria(),
                    revistaDto.getIdAdicional(),
                    revistaDto.getIdCargaHoraria(),
                    agrupacionEnum);
            if (existingRevista != null) {
                return new ResponseEntity<>(new Mensaje("La revista ya existe"), HttpStatus.BAD_REQUEST);
            }
        }

        /*
         * if (revistaDto.getIdTipoRevista() == null)
         * return new ResponseEntity<>(new Mensaje("El tipo de revista es obligatorio"),
         * HttpStatus.BAD_REQUEST);
         */
        if (revistaDto.getIdCategoria() == null)
            return new ResponseEntity<>(new Mensaje("La categoría es obligatoria"), HttpStatus.BAD_REQUEST);

        if (revistaDto.getIdCargaHoraria() == null)
            return new ResponseEntity<>(new Mensaje("La carga horaria es obligatoria"), HttpStatus.BAD_REQUEST);

        Categoria categoria = categoriaService.findById(revistaDto.getIdCategoria()).orElse(null);
        CargaHoraria cargaHoraria = cargaHorariaService.findById(revistaDto.getIdCargaHoraria()).orElse(null);

        if (categoria == null || cargaHoraria == null) {
            return new ResponseEntity<>(new Mensaje("Categoría o carga horaria no encontrada"), HttpStatus.BAD_REQUEST);
        }

        if (cargaHoraria.getCantidad() == 24 || cargaHoraria.getCantidad() == 30) {
            if ("24 HS".equals(categoria.getNombre())) {
                // La categoría '24 HS' no permite adicionales
                if (revistaDto.getIdAdicional() != null) {
                    return new ResponseEntity<>(
                            new Mensaje(
                                    "La categoría '24 HS' con carga horaria de 24 o 30 horas no permite adicionales"),
                            HttpStatus.BAD_REQUEST);
                }
                revistaDto.setIdAdicional(null); // Asegurarse de que sea null
            }
        } else {
            // Validaciones para otras combinaciones
            if (revistaDto.getIdAdicional() == null) {
                return new ResponseEntity<>(
                        new Mensaje("El adicional es obligatorio para esta categoría y carga horaria"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(new Mensaje("Válido"), HttpStatus.OK);
    }

    public Revista createUpdate(Revista revista, RevistaDto revistaDto) {

        // Convertir el displayName de agrupación a su correspondiente enum
        if (revistaDto.getAgrupacion() != null) {
            AgrupacionEnum agrupacionEnum = AgrupacionEnum.fromDisplayName(revistaDto.getAgrupacion());
            if (agrupacionEnum != null) {
                revista.setAgrupacion(agrupacionEnum);
            } else {
                // Log o lanzar excepción si no se encuentra la agrupación
                System.out.println("Agrupación no encontrada para: " + revistaDto.getAgrupacion());

            }
        }

        if (revistaDto.getIdTipoRevista() != null) {
            if (revista.getTipoRevista() == null
                    || !Objects.equals(revista.getTipoRevista().getId(), revistaDto.getIdTipoRevista())) {
                revista.setTipoRevista(tipoRevistaService.findById(revistaDto.getIdTipoRevista()).get());
            }
        }

        if (revistaDto.getIdCategoria() != null) {
            if (revista.getCategoria() == null
                    || !Objects.equals(revista.getCategoria().getId(), revistaDto.getIdCategoria())) {
                revista.setCategoria(categoriaService.findById(revistaDto.getIdCategoria()).get());
            }
        }

        if (revistaDto.getIdAdicional() != null) {
            if (revista.getAdicional() == null
                    || !Objects.equals(revista.getAdicional().getId(), revistaDto.getIdAdicional())) {
                revista.setAdicional(adicionalService.findById(revistaDto.getIdAdicional()).get());
            }
        } else {
            // Si se ha validado que el adicional debe ser nulo
            revista.setAdicional(null);
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
                revista.setLegajos(new ArrayList<>());
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
        ResponseEntity<?> respuestaValidaciones = validations(revistaDto, 0L);

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
            return new ResponseEntity<>(new Mensaje("No existe la revista"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(revistaDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            // Obtener la revista existente para la comparación
            Revista existingRevista = revistaService.findById(id).get();

            // Crear/actualizar la revista sin modificar el adicional si no está permitido
            Revista revista = createUpdate(existingRevista, revistaDto);
            revistaService.save(revista);
            return new ResponseEntity<>(new Mensaje("Revista actualizada correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!revistaService.activo(id))
            return new ResponseEntity<>(new Mensaje("No existe la revista"), HttpStatus.NOT_FOUND);

        Revista revista = revistaService.findById(id).get();
        revista.setActivo(false);
        revistaService.save(revista);
        return new ResponseEntity<>(new Mensaje("Revista eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!revistaService.existsById(id))
            return new ResponseEntity<>(new Mensaje("No existe la revista"), HttpStatus.NOT_FOUND);
        revistaService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Revista eliminada físicamente"), HttpStatus.OK);
    }
}