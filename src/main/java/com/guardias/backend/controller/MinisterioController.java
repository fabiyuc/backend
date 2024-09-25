package com.guardias.backend.controller;

import java.util.ArrayList;
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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.MinisterioDto;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Ministerio;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.service.MinisterioService;

@Controller
@RequestMapping("/ministerio")
@CrossOrigin(origins = "http://localhost:4200")
public class MinisterioController {

    @Autowired
    MinisterioService ministerioService;
    @Autowired
    EfectorController efectorController;

    @GetMapping("/list")
    public ResponseEntity<List<Ministerio>> list() {
        List<Ministerio> ministerioList = ministerioService.findByActivoTrue().orElse(new ArrayList<>());
        List<Ministerio> filteredList = new ArrayList<>();

        for (Ministerio ministerio : ministerioList) {
            List<RegistroActividad> activeRegActividades = new ArrayList<>();
            for (RegistroActividad registroActividad : ministerio.getRegistrosActividades()) {
                if (registroActividad.isActivo()) {
                    activeRegActividades.add(registroActividad);
                }
            }
            ministerio.setRegistrosActividades(activeRegActividades);
            filteredList.add(ministerio);
        }

        return new ResponseEntity<List<Ministerio>>(filteredList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Ministerio>> listAll() {
        List<Ministerio> list = ministerioService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Ministerio>> getById(@PathVariable("id") Long id) {
        if (!ministerioService.activo(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Ministerio ministerio = ministerioService.findById(id).get();
        return new ResponseEntity(ministerio, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Ministerio>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!ministerioService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Ministerio ministerio = ministerioService.findByNombre(nombre).get();
        return new ResponseEntity(ministerio, HttpStatus.OK);
    }

    private Ministerio createUpdate(Ministerio ministerio, MinisterioDto ministerioDto) {
        Efector efector = efectorController.createUpdate(ministerio, ministerioDto);
        ministerio = (Ministerio) efector;

        // Asignamos la cabecera solo si idCabecera no es nulo
        if (ministerioDto.getIdCabecera() != null) {
            Ministerio cabecera = ministerioService.findById(ministerioDto.getIdCabecera())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No se encontró un Ministerio con id: " + ministerioDto.getIdCabecera()));
            ministerio.setCabecera(cabecera);
        } else {
            // Si idCabecera es nulo, asignamos null a la cabecera
            ministerio.setCabecera(null);
        }

        if (ministerioDto.getIdMinisterios() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (ministerio.getMinisterios() != null) {
                for (Ministerio ministerios : ministerio.getMinisterios()) {
                    for (Long id : ministerioDto.getIdMinisterios()) {
                        if (!ministerios.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                ministerio.setMinisterios(new ArrayList<Ministerio>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? ministerioDto.getIdMinisterios() : idList;
            for (Long id : idsToAdd) {
                ministerio.getMinisterios().add(ministerioService.findById(id).get());
                ministerioService.findById(id).get().setCabecera(ministerio);
            }
        }

        ministerio.setActivo(true);
        return ministerio;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MinisterioDto ministerioDto) {
        ResponseEntity<?> respuestaValidaciones = efectorController.validations(ministerioDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Ministerio ministerio = createUpdate(new Ministerio(), ministerioDto);
            ministerio.setId(58L);
            ministerioService.save(ministerio);
            return new ResponseEntity(new Mensaje("Ministerio creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody MinisterioDto ministerioDto) {
        if (!ministerioService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = efectorController.validations(ministerioDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Ministerio ministerio = createUpdate(ministerioService.findById(id).get(), ministerioDto);
            ministerioService.save(ministerio);
            return new ResponseEntity(new Mensaje("Ministerio creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!ministerioService.activo(id))
            return new ResponseEntity(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);

        Ministerio ministerio = ministerioService.findById(id).get();
        ministerio.setActivo(false);
        ministerioService.save(ministerio);
        return new ResponseEntity(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!ministerioService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);
        ministerioService.deleteById(id);
        return new ResponseEntity<Mensaje>(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
