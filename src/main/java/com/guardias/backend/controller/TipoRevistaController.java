package com.guardias.backend.controller;

import java.util.ArrayList;
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
import com.guardias.backend.dto.TipoRevistaDto;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.entity.TipoRevista;
import com.guardias.backend.service.RevistaService;
import com.guardias.backend.service.TipoRevistaService;

@RestController
@RequestMapping("/tipoRevista")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoRevistaController {

    @Autowired
    TipoRevistaService tipoRevistaService;

    @Autowired
    RevistaService revistaService;

    @GetMapping("/list")
    public ResponseEntity<List<TipoRevista>> list() {
        List<TipoRevista> list = tipoRevistaService.findByActivoTrue();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<TipoRevista>> listAll() {
        List<TipoRevista> list = tipoRevistaService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TipoRevista> getById(@PathVariable("id") Long id) {
        if (!tipoRevistaService.activo(id))
            return new ResponseEntity(new Mensaje("No existe el tipo de revista"), HttpStatus.NOT_FOUND);
        TipoRevista tipoRevista = tipoRevistaService.findById(id).get();
        return new ResponseEntity(tipoRevista, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<TipoRevista>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!tipoRevistaService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe el tipo de revista con ese nombre"), HttpStatus.NOT_FOUND);
        TipoRevista tipoRevista = tipoRevistaService.findByNombre(nombre).get();
        return new ResponseEntity(tipoRevista, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(TipoRevistaDto tipoRevistaDto, Long id) {
        if (tipoRevistaDto.getNombre() == null)
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (tipoRevistaService.existsByNombre(tipoRevistaDto.getNombre())
                && (tipoRevistaService.findByNombre(tipoRevistaDto.getNombre()).get().getId() != id))
            return new ResponseEntity<>(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private TipoRevista createUpdate(TipoRevista tipoRevista, TipoRevistaDto tipoRevistaDto) {
        if (StringUtils.isNotBlank(tipoRevistaDto.getNombre()))
            tipoRevista.setNombre(tipoRevistaDto.getNombre());
        if (tipoRevistaDto.getNombre() != null && tipoRevista.getNombre() != tipoRevistaDto.getNombre())
            tipoRevista.setNombre(tipoRevistaDto.getNombre());

        if (tipoRevistaDto.getIdRevista() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (tipoRevista.getRevistas() != null) {
                for (Revista revista : tipoRevista.getRevistas()) {
                    for (Long id : tipoRevistaDto.getIdRevista()) {
                        if (!revista.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }
            List<Long> idsToAdd = idList.isEmpty() ? tipoRevistaDto.getIdRevista() : idList;
            for (Long id : idsToAdd) {
                tipoRevista.getRevistas().add(revistaService.findById(id).get());
                revistaService.findById(id).get().setTipoRevista(tipoRevista);
            }
        }
        tipoRevista.setActivo(true);
        return tipoRevista;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoRevistaDto tipoRevistaDto) {
        ResponseEntity<?> respuestaValidaciones = validations(tipoRevistaDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            TipoRevista tipoRevista = createUpdate(new TipoRevista(), tipoRevistaDto);
            tipoRevistaService.save(tipoRevista);
            return new ResponseEntity<>(new Mensaje("Tipo de revista creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TipoRevistaDto tipoRevistaDto) {
        if (!tipoRevistaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el tipo de revista"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(tipoRevistaDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            TipoRevista tipoRevista = createUpdate(tipoRevistaService.findById(id).get(), tipoRevistaDto);
            tipoRevistaService.save(tipoRevista);
            return new ResponseEntity<>(new Mensaje("Tipo de revista modificado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!tipoRevistaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        TipoRevista tipoRevista = tipoRevistaService.findById(id).get();
        tipoRevista.setActivo(false);
        tipoRevistaService.save(tipoRevista);
        return new ResponseEntity<>(new Mensaje("Tipo Revista eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!tipoRevistaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        tipoRevistaService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Tipo Revista eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
