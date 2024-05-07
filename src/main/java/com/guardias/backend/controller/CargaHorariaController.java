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

import com.guardias.backend.dto.CargaHorariaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.CargaHoraria;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.service.CargaHorariaService;
import com.guardias.backend.service.RevistaService;

@RestController
@RequestMapping("/cargaHoraria")
@CrossOrigin(origins = "http://localhost:4200")
public class CargaHorariaController {

    @Autowired
    CargaHorariaService cargaHorariaService;

    @Autowired
    RevistaService revistaService;

    @GetMapping("/list")
    public ResponseEntity<List<CargaHoraria>> list() {
        List<CargaHoraria> list = cargaHorariaService.findByActivoTrue().get();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<CargaHoraria>> listAll() {
        List<CargaHoraria> list = cargaHorariaService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<CargaHoraria>> getById(@PathVariable("id") Long id) {
        if (!cargaHorariaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);
        CargaHoraria cargaHoraria = cargaHorariaService.findById(id).get();
        return new ResponseEntity(cargaHoraria, HttpStatus.OK);
    }

    @GetMapping("/detailcantidad/{cantidad}")
    public ResponseEntity<List<CargaHoraria>> getByCantidad(@PathVariable("cantidad") int cantidad) {
        if (!cargaHorariaService.activoByCantidad(cantidad))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);
        CargaHoraria cargaHoraria = cargaHorariaService.findByCantidad(cantidad).get();
        return new ResponseEntity(cargaHoraria, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(CargaHorariaDto cargaHorariaDto, Long id) {

        if (cargaHorariaDto.getCantidad() < 1)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"), HttpStatus.BAD_REQUEST);

        /*
         * if (cargaHorariaDto.getIdRevistas() == null)
         * return new ResponseEntity(new Mensaje("La revista es obligatoria"),
         * HttpStatus.BAD_REQUEST);
         */

        if (cargaHorariaService.existsByCantidad(cargaHorariaDto.getCantidad())
                && (cargaHorariaService.findByCantidad(cargaHorariaDto.getCantidad()).get().getId() != id))
            return new ResponseEntity(new Mensaje("esa cantidad ya existe"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private CargaHoraria createUpdate(CargaHoraria cargaHoraria, CargaHorariaDto cargaHorariaDto) {
        if (cargaHorariaDto.getCantidad() > 0 && cargaHorariaDto.getCantidad() != cargaHoraria.getCantidad()) {
            cargaHoraria.setCantidad(cargaHorariaDto.getCantidad());
        }

        if (cargaHorariaDto.getDescripcion() != null && !cargaHorariaDto.getDescripcion().isEmpty()
                && !cargaHorariaDto.getDescripcion().equals(cargaHoraria.getDescripcion())) {
            cargaHoraria.setDescripcion(cargaHorariaDto.getDescripcion());
        }

        if (cargaHorariaDto.getIdRevistas() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (cargaHoraria.getRevistas() != null) {
                for (Revista revista : cargaHoraria.getRevistas()) {
                    for (Long id : cargaHorariaDto.getIdRevistas()) {
                        if (!revista.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                cargaHoraria.setRevistas(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? cargaHorariaDto.getIdRevistas() : idList;
            for (Long id : idsToAdd) {
                cargaHoraria.getRevistas().add(revistaService.findById(id).get());
                revistaService.findById(id).get().setCargaHoraria(cargaHoraria);
            }
        }
        cargaHoraria.setActivo(true);
        return cargaHoraria;

    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CargaHorariaDto cargaHorariaDto) {
        ResponseEntity<?> respuestaValidaciones = validations(cargaHorariaDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            CargaHoraria cargaHoraria = createUpdate(new CargaHoraria(), cargaHorariaDto);

            cargaHorariaService.save(cargaHoraria);
            return new ResponseEntity(new Mensaje("Carga horaria creada correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CargaHorariaDto cargaHorariaDto) {

        // Busca por ID
        if (!cargaHorariaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(cargaHorariaDto, id);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            CargaHoraria cargaHoraria = createUpdate(cargaHorariaService.findById(id).get(), cargaHorariaDto);
            cargaHorariaService.save(cargaHoraria);
            return new ResponseEntity(new Mensaje("Carga horaria modificada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!cargaHorariaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);

        CargaHoraria cargaHoraria = cargaHorariaService.findById(id).get();
        cargaHoraria.setActivo(false);
        cargaHorariaService.save(cargaHoraria);
        return new ResponseEntity(new Mensaje("carga horaria eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!cargaHorariaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);
        cargaHorariaService.deleteById(id);
        return new ResponseEntity(new Mensaje("carga horaria eliminada FISICAMENTE"), HttpStatus.OK);
    }
}