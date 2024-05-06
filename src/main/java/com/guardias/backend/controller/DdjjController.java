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

import com.guardias.backend.dto.DdjjDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.service.DdjjService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistroMensualService;
import com.guardias.backend.service.ValorGmiService;

@Controller
@RequestMapping("/ddjj")
@CrossOrigin(origins = "http://localhost:4200")
public class DdjjController {
    @Autowired
    DdjjService ddjjService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    ValorGmiService valorGmiService;
    @Autowired
    RegistroMensualService registroMensualService;

    @GetMapping("/list")
    public ResponseEntity<List<Ddjj>> list() {
        List<Ddjj> list = ddjjService.findByActivoTrue();
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Ddjj>> listAll() {
        List<Ddjj> list = ddjjService.findAll();
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Ddjj> getById(@PathVariable("id") Long id) {
        if (!ddjjService.activo(id))
            return new ResponseEntity(new Mensaje("Valor no encontrado"), HttpStatus.NOT_FOUND);
        Ddjj ddjj = ddjjService.findById(id).get();
        return new ResponseEntity(ddjj, HttpStatus.OK);
    }

    @GetMapping("/listEfectorMes/{idEfector}/{mes}/{anio}")
    public ResponseEntity<List<Ddjj>> listEfectorMes(@PathVariable("idEfector") Long idEfector,
            @PathVariable("mes") String mes, @PathVariable("anio") int anio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        if (!ddjjService.existsByAnioAndMes(anio, mesEnum))
            return new ResponseEntity(new Mensaje("La DDJJ no existe"), HttpStatus.NOT_FOUND);

        List<Ddjj> list = ddjjService.findByEfectorIdAndMesAndAnio(idEfector, mesEnum, anio);
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);

    }

    @GetMapping("/listAnioMes/{mes}/{anio}")
    public ResponseEntity<List<Ddjj>> listAnioMes(@PathVariable("mes") String mes, @PathVariable("anio") int anio) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        if (!ddjjService.existsByAnioAndMes(anio, mesEnum))
            return new ResponseEntity(new Mensaje("La DDJJ no existe"), HttpStatus.NOT_FOUND);

        List<Ddjj> list = ddjjService.findByByAnioAndMes(anio, mesEnum);
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAnio/{anio}")
    public ResponseEntity<List<Ddjj>> listAnio(@PathVariable("anio") int anio) {

        if (!ddjjService.existsByAnio(anio))
            return new ResponseEntity(new Mensaje("La DDJJ no existe"), HttpStatus.NOT_FOUND);

        List<Ddjj> list = ddjjService.findByByAnio(anio);
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    public ResponseEntity<?> validations(DdjjDto ddjjDto) {

        if (ddjjDto.getMes() == null)
            return new ResponseEntity(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getAnio() < 1991)
            return new ResponseEntity(new Mensaje("El año es incorrecto"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getIdEfector() < 1)
            return new ResponseEntity(new Mensaje("El id del efector es incorrecto"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getEstadoDdjj() == null)
            return new ResponseEntity(new Mensaje("El estado es obligatorio"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Ddjj createUpdate(Ddjj ddjj, DdjjDto ddjjDto) {

        if (ddjjDto.getMes() != null && !ddjjDto.getMes().equals(ddjj.getMes()))
            ddjj.setMes(ddjjDto.getMes());

        if (ddjjDto.getAnio() != ddjj.getAnio())
            ddjj.setAnio(ddjjDto.getAnio());

        if (ddjjDto.getIdEfector() != null && (ddjj.getEfector() == null
                || !Objects.equals(ddjj.getEfector().getId(), ddjjDto.getIdEfector()))) {
            ddjj.setEfector(efectorService.findById(ddjjDto.getIdEfector()));
        }

        if (ddjjDto.getIdValorGmi() != null && (ddjj.getValorGmi() == null
                || !Objects.equals(ddjj.getValorGmi().getId(), ddjjDto.getIdValorGmi()))) {
            ddjj.setValorGmi(valorGmiService.findById(ddjjDto.getIdValorGmi()).get());
        }

        if (ddjjDto.getEstadoDdjj() != null && !ddjjDto.getEstadoDdjj().equals(ddjj.getEstadoDdjj()))
            ddjj.setEstadoDdjj(ddjjDto.getEstadoDdjj());

        if (ddjjDto.getSubtotal() != null && !ddjjDto.getSubtotal().equals(ddjj.getSubtotal()))
            ddjj.setSubtotal(ddjjDto.getSubtotal());

        if (ddjjDto.getTotal() != null && !ddjjDto.getTotal().equals(ddjj.getTotal()))
            ddjj.setTotal(ddjjDto.getTotal());

        if (ddjjDto.getIdRegistrosMensuales() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (ddjj.getRegistrosMensuales() != null) {
                for (RegistroMensual registroMensual : ddjj.getRegistrosMensuales()) {
                    for (Long id : ddjjDto.getIdRegistrosMensuales()) {
                        if (!registroMensual.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                ddjj.setRegistrosMensuales(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? ddjjDto.getIdRegistrosMensuales() : idList;
            for (Long id : idsToAdd) {
                ddjj.getRegistrosMensuales().add(registroMensualService.findById(id).get());
                registroMensualService.findById(id).get().setDdjj(ddjj);
            }
        }

        ddjj.setActivo(true);
        return ddjj;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DdjjDto ddjjDto) {
        ResponseEntity<?> respuestaValidaciones = validations(ddjjDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Ddjj ddjj = createUpdate(new Ddjj(), ddjjDto);
            ddjjService.save(ddjj);
            return new ResponseEntity(new Mensaje("Declaracion Jurada creada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody DdjjDto ddjjDto) {

        if (!ddjjService.activo(id))
            return new ResponseEntity(new Mensaje("DDJJ no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(ddjjDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Ddjj ddjj = createUpdate(ddjjService.findById(id).get(), ddjjDto);
            ddjjService.save(ddjj);
            return new ResponseEntity(new Mensaje("Declaracion Jurada modificada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!ddjjService.activo(id))
            return new ResponseEntity(new Mensaje("DDJJ no existe"), HttpStatus.NOT_FOUND);
        Ddjj ddjj = ddjjService.findById(id).get();
        ddjj.setActivo(false);
        ddjjService.save(ddjj);
        return new ResponseEntity(new Mensaje("Declaracion Jurada eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!ddjjService.existsById(id))
            return new ResponseEntity(new Mensaje("DDJJ no existe"), HttpStatus.NOT_FOUND);

        ddjjService.deleteById(id);
        return new ResponseEntity(new Mensaje("Declaracion Jurada eliminada FISICAMENTE"), HttpStatus.OK);
    }
}
