package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ServicioDto;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.Servicio;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistroActividadService;
import com.guardias.backend.service.ServicioService;

@RestController
@RequestMapping("/servicio")
@CrossOrigin(origins = "http://localhost:4200")
public class ServicioController {

    @Autowired
    ServicioService servicioService;
    @Autowired
    RegistroActividadService registroActividadService;
    @Autowired
    EfectorService efectorService;

    @GetMapping("/list")
    public ResponseEntity<List<Servicio>> list() {
        List<Servicio> servicioList = servicioService.findByActivoTrue().orElse(new ArrayList<>());
        List<Servicio> filteredList = new ArrayList<>();

        for (Servicio servicio : servicioList) {
            List<RegistroActividad> activeRegActividades = new ArrayList<>();
            for (RegistroActividad registroActividad : servicio.getRegistrosActividades()) {
                if (registroActividad.isActivo()) {
                    activeRegActividades.add(registroActividad);
                }
            }
            servicio.setRegistrosActividades(activeRegActividades);
            filteredList.add(servicio);
        }

        return new ResponseEntity<List<Servicio>>(filteredList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Servicio>> listAll() {
        List<Servicio> list = servicioService.findAll();
        return new ResponseEntity<List<Servicio>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Servicio> getById(@PathVariable("id") Long id) {
        if (!servicioService.activo(id))
            return new ResponseEntity(new Mensaje("No existe el servicio"), HttpStatus.NOT_FOUND);
        Servicio servicio = servicioService.findById(id).get();
        return new ResponseEntity<Servicio>(servicio, HttpStatus.OK);
    }

    @GetMapping("/detaildescripcion/{descripcion}")
    public ResponseEntity<Servicio> getByDescripcion(@PathVariable("descripcion") String descripcion) {
        if (!servicioService.activoByDescripcion(descripcion))
            return new ResponseEntity(new Mensaje("no existe el servicio"),
                    HttpStatus.NOT_FOUND);
        Servicio servicio = servicioService.findByDescripcion(descripcion).get();
        return new ResponseEntity<Servicio>(servicio, HttpStatus.OK);
    }

    @GetMapping("/detailnivel/{nivel}")
    public ResponseEntity<List<Servicio>> getByNivel(@PathVariable("nivel") int nivel) {
        List<Servicio> servicios = servicioService.findByNivel(nivel);
        if (!servicioService.existsByNivel(nivel))
            return new ResponseEntity(new Mensaje("no existe el nivel"), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(servicios, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(ServicioDto servicioDto) {

        if (StringUtils.isBlank(servicioDto.getDescripcion()))
            return new ResponseEntity(new Mensaje("la descripcion es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (servicioDto.getNivel() <= 0)
            return new ResponseEntity(new Mensaje("el nivel debe ser mayor que 0"),
                    HttpStatus.BAD_REQUEST);

        if (servicioDto.getCritico() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar si es critico o no"),
                    HttpStatus.BAD_REQUEST);
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private Servicio createUpdate(Servicio servicio, ServicioDto servicioDto) {

        if (servicio.getDescripcion() != servicioDto.getDescripcion() && servicioDto.getDescripcion() != null
                && !servicioDto.getDescripcion().isEmpty())
            servicio.setDescripcion(servicioDto.getDescripcion());

        if (servicio.getNivel() != servicioDto.getNivel())
            servicio.setNivel(servicioDto.getNivel());

        if (servicioDto.getIdRegistrosActividades() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (servicio.getRegistrosActividades() != null) {
                for (RegistroActividad registro : servicio.getRegistrosActividades()) {
                    for (Long id : servicioDto.getIdRegistrosActividades()) {
                        if (!registro.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                servicio.setRegistrosActividades(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? servicioDto.getIdRegistrosActividades() : idList;
            for (Long id : idsToAdd) {
                servicio.getRegistrosActividades().add(registroActividadService.findById(id).get());
                registroActividadService.findById(id).get().setServicio(servicio);
            }
        }

        if (servicioDto.getIdEfectores() != null) {
            if (servicio.getEfectores() == null) {
                servicio.setEfectores(new ArrayList<Efector>());
            }


            List<Long> idList = new ArrayList<Long>();
            for (Long id : servicioDto.getIdEfectores()) {
                boolean exists = false;
                for (Efector efector : servicio.getEfectores()) {
                    if (efector.getId().equals(id)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    idList.add(id);
                }
            }

            for (Long id : idList) {
                Efector efector = efectorService.findById(id);
                servicio.getEfectores().add(efector);
                efector.getServicios().add(servicio);
            }
        } /* else {
            if (servicio.getEfectores() == null) {
                servicio.setEfectores(new ArrayList<Efector>());
            }
        }

        servicio.setServicioCritico(servicioDto.isServicioCritico());
        servicio.setActivo(true);
        return servicio;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ServicioDto servicioDto) {

        ResponseEntity<?> respuestaValidaciones = validations(servicioDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Servicio servicio = createUpdate(new Servicio(), servicioDto);
            servicioService.save(servicio);
            return new ResponseEntity(new Mensaje("servicio creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ServicioDto servicioDto) {
        if (!servicioService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el servicio"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(servicioDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Servicio servicio = createUpdate(servicioService.findById(id).get(), servicioDto);
            servicioService.save(servicio);
            return new ResponseEntity(new Mensaje("servicio modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!servicioService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el servicio"), HttpStatus.NOT_FOUND);
        Servicio servicio = servicioService.findById(id).get();
        servicio.setActivo(false);
        servicioService.save(servicio);
        return new ResponseEntity(new Mensaje("servicio eliminado"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!servicioService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el servicio"), HttpStatus.NOT_FOUND);
        servicioService.deleteById(id);
        return new ResponseEntity(new Mensaje("servicio eliminado"), HttpStatus.OK);
    }

}
