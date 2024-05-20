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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.PaisDto;
import com.guardias.backend.entity.Pais;
import com.guardias.backend.entity.Provincia;
import com.guardias.backend.service.PaisService;
import com.guardias.backend.service.ProvinciaService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/pais")
@CrossOrigin(origins = "http://localhost:4200")
public class PaisController {

    @Autowired
    PaisService paisService;

    @Autowired
    ProvinciaService provinciaService;

    @GetMapping("/list")
    public ResponseEntity<List<Pais>> list() {
        List<Pais> list = paisService.findByActivo();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Pais>> listAll() {
        List<Pais> list = paisService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Pais>> getById(@PathVariable("id") Long id) {
        if (!paisService.activo(id))
            return new ResponseEntity(new Mensaje("pais no existe"), HttpStatus.NOT_FOUND);
        Pais pais = paisService.findById(id).get();
        return new ResponseEntity(pais, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Pais> getByNombre(@PathVariable("nombre") String nombre) {
        if (!paisService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe el nombre del pais"), HttpStatus.NOT_FOUND);
        Pais pais = paisService.findByNombre(nombre).get();
        return new ResponseEntity<Pais>(pais, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(PaisDto paisDto, Long id) {
        if (StringUtils.isBlank(paisDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(paisDto.getNacionalidad()))
            return new ResponseEntity(new Mensaje("la nacionalidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(paisDto.getCodigo()))
            return new ResponseEntity(new Mensaje("el codigo es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (paisService.activoByNombre(paisDto.getNombre())
                && (paisService.findByNombre(paisDto.getNombre()).get().getId() != id))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);

        if (paisService.existsByNacionalidad(paisDto.getNacionalidad())
                && (paisService.findByNacionalidad(paisDto.getNacionalidad()).get().getId() != id))
            return new ResponseEntity(new Mensaje("esa nacionalidad ya existe"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    private Pais createUpdate(Pais pais, PaisDto paisDto) {
        if (pais.getNombre() != paisDto.getNombre() && paisDto.getNombre() != null && !paisDto.getNombre().isEmpty())
            pais.setNombre(paisDto.getNombre());

        if (pais.getCodigo() != paisDto.getCodigo() && paisDto.getCodigo() != null && !paisDto.getCodigo().isEmpty())
            pais.setCodigo(paisDto.getCodigo());

        if (pais.getNacionalidad() != paisDto.getNacionalidad() && paisDto.getNacionalidad() != null
                && !paisDto.getNacionalidad().isEmpty())
            pais.setNacionalidad(paisDto.getNacionalidad());

        if (paisDto.getIdProvincias() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (pais.getProvincias() != null) {
                for (Provincia provincia : pais.getProvincias()) {
                    for (Long id : paisDto.getIdProvincias()) {
                        if (!provincia.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                pais.setProvincias(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? paisDto.getIdProvincias() : idList;
            for (Long id : idsToAdd) {
                pais.getProvincias().add(provinciaService.findById(id).get());
                provinciaService.findById(id).get().setPais(pais);
            }
        }

        pais.setActivo(true);
        return pais;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PaisDto paisDto) {

        ResponseEntity<?> respuestaValidaciones = validations(paisDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Pais pais = createUpdate(new Pais(), paisDto);
            paisService.save(pais);
            return new ResponseEntity(new Mensaje("pais creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody PaisDto paisDto) {
        if (!paisService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el pais"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(paisDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Pais pais = createUpdate(paisService.findById(id).get(), paisDto);
            paisService.save(pais);
            return new ResponseEntity(new Mensaje("pais modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!paisService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el pais"), HttpStatus.NOT_FOUND);

        Pais pais = paisService.findById(id).get();
        pais.setActivo(false);
        paisService.save(pais);
        return new ResponseEntity(new Mensaje("pais eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!paisService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el pais"), HttpStatus.NOT_FOUND);
        paisService.deleteById(id);
        return new ResponseEntity(new Mensaje("pais eliminado FISICAMENTE"), HttpStatus.OK);
    }
}