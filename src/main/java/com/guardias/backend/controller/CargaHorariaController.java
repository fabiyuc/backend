package com.guardias.backend.controller;

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
import com.guardias.backend.dto.CargaHorariaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.CargaHoraria;
import com.guardias.backend.service.CargaHorariaService;

@RestController
@RequestMapping("/cargaHoraria")
@CrossOrigin(origins = "http://localhost:4200")
public class CargaHorariaController {
    
    @Autowired
    CargaHorariaService cargaHorariaService;

    @GetMapping("/lista")
    public ResponseEntity<List<CargaHoraria>> list() {
        List<CargaHoraria> list = cargaHorariaService.list();
        return new ResponseEntity<List<CargaHoraria>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<CargaHoraria> getById(@PathVariable("id") Long id) {
        if (!cargaHorariaService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        CargaHoraria cargaHoraria = cargaHorariaService.getOne(id).get();
        return new ResponseEntity<CargaHoraria>(cargaHoraria, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{cantidad}")
    public ResponseEntity<CargaHoraria> getByCantidad(@PathVariable("cantidad") int cantidad) {
        if (!cargaHorariaService.existsByCantidad(cantidad))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);
        CargaHoraria cargaHoraria = cargaHorariaService.getByCantidad(cantidad).get();
        return new ResponseEntity<CargaHoraria>(cargaHoraria, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CargaHorariaDto cargaHorariaDto) {
        
        String cantidadStr = Integer.toString(cargaHorariaDto.getCantidad());
        if (StringUtils.isBlank(cantidadStr))
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (cargaHorariaService.existsByCantidad(cargaHorariaDto.getCantidad()))
            return new ResponseEntity(new Mensaje("esa cantidad ya existe"),
                    HttpStatus.BAD_REQUEST);
        CargaHoraria cargaHoraria = new CargaHoraria(cargaHorariaDto.getCantidad());
        cargaHorariaService.save(cargaHoraria);
        return new ResponseEntity(new Mensaje("Carga horaria creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CargaHorariaDto cargaHorariaDto) {
        if (!cargaHorariaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);

        if (cargaHorariaService.existsByCantidad(cargaHorariaDto.getCantidad()) &&
                cargaHorariaService.getByCantidad(cargaHorariaDto.getCantidad()).get().getId() == id)
            return new ResponseEntity(new Mensaje("esa carga horaria ya existe"), HttpStatus.BAD_REQUEST);

        String cantidadStr = Integer.toString(cargaHorariaDto.getCantidad());
        if (StringUtils.isBlank(cantidadStr))
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"), HttpStatus.BAD_REQUEST);

        CargaHoraria cargaHoraria = cargaHorariaService.getOne(id).get();
        cargaHoraria.setCantidad(cargaHorariaDto.getCantidad());
        cargaHorariaService.save(cargaHoraria);
        return new ResponseEntity(new Mensaje("Carga horaria actualizada"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        
        if (!cargaHorariaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);
        cargaHorariaService.delete(id);
        return new ResponseEntity(new Mensaje("carga horaria eliminada"), HttpStatus.OK);
    }
}
