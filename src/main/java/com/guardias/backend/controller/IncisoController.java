package com.guardias.backend.controller;

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

import com.guardias.backend.dto.IncisoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.entity.Ley;
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
        List<Inciso> list = incisoService.findByActivoTrue().get();
        return new ResponseEntity<List<Inciso>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Inciso>> listAll() {
        List<Inciso> list = incisoService.findAll();
        return new ResponseEntity<List<Inciso>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Inciso> getById(@PathVariable("id") Long id) {
        if (!incisoService.activo(id))
            return new ResponseEntity(new Mensaje("Inciso no encontrado"), HttpStatus.NOT_FOUND);
        Inciso inciso = incisoService.findById(id).get();
        return new ResponseEntity<Inciso>(inciso, HttpStatus.OK);
    }

    private Inciso createUpdate(Inciso inciso, IncisoDto incisoDto) {

        Ley ley = leyController.createUpdate(inciso, incisoDto);
        inciso = (Inciso) ley;

        if (inciso.getArticulo() == null ||
                (incisoDto.getIdArticulo() != null &&
                        !Objects.equals(inciso.getArticulo().getId(),
                                incisoDto.getIdArticulo()))) {
            inciso.setArticulo(articuloService.findById(incisoDto.getIdArticulo()).get());
        }

        /*
         * if (incisoDto.getIdNovedadesPersonales() != null) {
         * if (inciso.getNovedadesPersonales() == null) {
         * inciso.setNovedadesPersonales(new ArrayList<>()); // Initialize
         * novedadesPersonales list if null
         * }
         * for (Long id : incisoDto.getIdNovedadesPersonales()) {
         * NovedadPersonal novedad = novedadPersonalService.findById(id)
         * .orElseThrow(() -> new
         * RuntimeException("Novedad personal no encontrada con ID: " + id));
         * novedad.setInciso(inciso);
         * inciso.getNovedadesPersonales().add(novedad);
         * novedadPersonalService.save(novedad);
         * }
         * }
         */

        return inciso;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody IncisoDto incisoDto) {
        ResponseEntity<?> respuestaValidaciones = leyController.validations(incisoDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            // Crear el inciso principal y guardarlo en la base de datos
            Inciso inciso = new Inciso();
            inciso = createUpdate(inciso, incisoDto);
            inciso.setActivo(true);
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

        ResponseEntity<?> respuestaValidaciones = leyController.validations(incisoDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Inciso inciso = createUpdate(incisoService.findById(id).get(), incisoDto);
            incisoService.save(inciso);
            return new ResponseEntity<Mensaje>(new Mensaje("Inciso modificado correctamente"), HttpStatus.OK);
        } else {
            return new ResponseEntity<Mensaje>(new Mensaje("Error al crear el elemento"),
                    HttpStatus.BAD_REQUEST);
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