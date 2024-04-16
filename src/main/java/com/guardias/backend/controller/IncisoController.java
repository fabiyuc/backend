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

import com.guardias.backend.dto.IncisoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.entity.Ley;
import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.service.ArticuloService;
import com.guardias.backend.service.IncisoService;
import com.guardias.backend.service.NovedadPersonalService;

@Controller
@RequestMapping("/inciso")
@CrossOrigin(origins = "http://localhost:4200")
public class IncisoController {
    @Autowired
    IncisoService incisoService;
    @Autowired
    ArticuloService articuloService;
    @Autowired
    LeyController leyController;
    @Autowired
    NovedadPersonalService novedadPersonalService;

    @GetMapping("/list")
    public ResponseEntity<List<Inciso>> list() {
        List<Inciso> list = incisoService.findByActivoTrue();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Inciso>> listAll() {
        List<Inciso> list = incisoService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Inciso>> getById(@PathVariable("id") Long id) {
        if (!incisoService.activo(id))
            return new ResponseEntity(new Mensaje("Inciso no encontrado"), HttpStatus.NOT_FOUND);
        Inciso inciso = incisoService.findById(id).get();
        return new ResponseEntity(inciso, HttpStatus.OK);
    }

    private Inciso createUpdate(Inciso inciso, IncisoDto incisoDto) {

        Ley ley = leyController.createUpdate(inciso, incisoDto);
        inciso = (Inciso) ley;

        if (inciso.getArticulo() != null && (inciso.getArticulo().getId() != incisoDto.getIdArticulo())
                && incisoDto.getIdArticulo() != null) {
            inciso.setArticulo(articuloService.findById(incisoDto.getIdArticulo()).get());
        }

        if (incisoDto.getIdSubIncisos() != null) {
            List<Long> idList = new ArrayList();
            if (inciso.getSubIncisos() == null) {
                for (Inciso incisoList : inciso.getSubIncisos()) {
                    for (Long id : incisoDto.getIdSubIncisos()) {
                        if (!incisoList.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }

            List<Long> idsToAdd = idList.isEmpty() ? incisoDto.getIdSubIncisos() : idList;
            for (Long id : idsToAdd) {
                incisoService.findById(id).get().setIncisoPadre(inciso);
                inciso.getSubIncisos().add(incisoService.findById(id).get());
            }
        }

        if (incisoDto.getIdNovedadesPersonales() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (inciso.getNovedadesPersonales() != null) {
                for (NovedadPersonal novedad : inciso.getNovedadesPersonales()) {
                    for (Long id : incisoDto.getIdNovedadesPersonales()) {
                        if (!novedad.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                inciso.setNovedadesPersonales(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? incisoDto.getIdNovedadesPersonales() : idList;
            for (Long id : idsToAdd) {
                inciso.getNovedadesPersonales().add(novedadPersonalService.findById(id).get());
                novedadPersonalService.findById(id).get().setInciso(inciso);
            }
        }
        return inciso;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody IncisoDto incisoDto) {
        ResponseEntity<?> respuestaValidaciones = leyController.validationsCreate(incisoDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Inciso inciso = createUpdate(new Inciso(), incisoDto);
            incisoService.save(inciso);
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody IncisoDto incisoDto) {
        if (!incisoService.activo(id))
            return new ResponseEntity(new Mensaje("El inciso no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = leyController.validations(incisoDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Inciso inciso = createUpdate(incisoService.findById(id).get(), incisoDto);
            incisoService.save(inciso);
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso modificado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!incisoService.activo(id))
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso no encontrado"), HttpStatus.NOT_FOUND);

        Inciso inciso = incisoService.findById(id).get();
        inciso.setActivo(false);
        incisoService.save(inciso);
        return new ResponseEntity<Mensaje>(new Mensaje("Inciso  eliminado"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!incisoService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso no encontrado"), HttpStatus.NOT_FOUND);
        incisoService.deleteById(id);
        return new ResponseEntity<Mensaje>(new Mensaje("Inciso  eliminado FISICAMENTE"), HttpStatus.OK);
    }
}
