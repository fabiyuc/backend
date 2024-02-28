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

    @GetMapping("/list")
    public ResponseEntity<List<CargaHoraria>> list() {
        List<CargaHoraria> list = cargaHorariaService.findByActivo(true);
        return new ResponseEntity<List<CargaHoraria>>(list, HttpStatus.OK);
    }
    
    @GetMapping("/listAll")
    public ResponseEntity<List<CargaHoraria>> listAll() {
        List<CargaHoraria> list = cargaHorariaService.findAll();
        return new ResponseEntity<List<CargaHoraria>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<CargaHoraria> getById(@PathVariable("id") Long id) {
        if (!cargaHorariaService.existsById(id))
            return new ResponseEntity(new Mensaje("No existe la carga horaria"), HttpStatus.NOT_FOUND);
        CargaHoraria cargaHoraria = cargaHorariaService.findById(id).get();
        return new ResponseEntity<CargaHoraria>(cargaHoraria, HttpStatus.OK);
    }

    @GetMapping("/detailcantidad/{cantidad}")
    public ResponseEntity<CargaHoraria> getByCantidad(@PathVariable("cantidad") int cantidad) {
        if (!cargaHorariaService.existsByCantidad(cantidad))
            return new ResponseEntity(new Mensaje("no existe esa carga horaria"), HttpStatus.NOT_FOUND);
        CargaHoraria cargaHoraria = cargaHorariaService.getByCantidad(cantidad).get();
        return new ResponseEntity<CargaHoraria>(cargaHoraria, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CargaHorariaDto cargaHorariaDto) {

        if (cargaHorariaDto.getCantidad() < 1)
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"),
                    HttpStatus.BAD_REQUEST);
        if (cargaHorariaService.existsByCantidad(cargaHorariaDto.getCantidad()))
            return new ResponseEntity(new Mensaje("esa cantidad ya existe"),
                    HttpStatus.BAD_REQUEST);
        CargaHoraria cargaHoraria = new CargaHoraria();
        cargaHoraria.setCantidad(cargaHorariaDto.getCantidad());
        cargaHorariaService.save(cargaHoraria);
        return new ResponseEntity(new Mensaje("Carga horaria creada"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CargaHorariaDto cargaHorariaDto) {

        // Busca por ID
        if (!cargaHorariaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la carga horaria"), HttpStatus.NOT_FOUND);

        // Verifica que la cantidad no exista para el mismo ID
        if (cargaHorariaService.existsByCantidad(cargaHorariaDto.getCantidad()) &&
                cargaHorariaService.getByCantidad(cargaHorariaDto.getCantidad()).get().getId() == id)
            return new ResponseEntity(new Mensaje("esa carga horaria ya existe"), HttpStatus.BAD_REQUEST);

        // ***********Verificar que no permita indicar vacio o 0*********************
        String cantidadStr = Integer.toString(cargaHorariaDto.getCantidad());
        if (StringUtils.isBlank(cantidadStr))
            return new ResponseEntity(new Mensaje("la cantidad es obligatoria"), HttpStatus.BAD_REQUEST);

        CargaHoraria cargaHoraria = cargaHorariaService.findById(id).get();
        cargaHoraria.setCantidad(cargaHorariaDto.getCantidad());
        cargaHorariaService.save(cargaHoraria);
        return new ResponseEntity(new Mensaje("Carga horaria actualizada"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!cargaHorariaService.existsById(id))
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