package com.guardias.backend.controller;

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
import com.guardias.backend.entity.Ministerio;
import com.guardias.backend.service.MinisterioService;
import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/ministerio")
@CrossOrigin(origins = "http://localhost:4200")
public class MinisterioController {

    @Autowired
    MinisterioService ministerioService;

    @GetMapping("/list")
    public ResponseEntity<List<Ministerio>> list() {
        List<Ministerio> list = ministerioService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Ministerio>> getById(@PathVariable("id") Long id) {
        if (!ministerioService.existsById(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Ministerio ministerio = ministerioService.findById(id).get();
        return new ResponseEntity(ministerio, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Ministerio>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!ministerioService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Ministerio ministerio = ministerioService.findByNombre(nombre).get();
        return new ResponseEntity(ministerio, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MinisterioDto ministerioDto) {
        if (StringUtils.isBlank(ministerioDto.getNombre()))
            return new ResponseEntity<Mensaje>(new Mensaje("el nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(ministerioDto.getDomicilio()))
            return new ResponseEntity<Mensaje>(new Mensaje("el domicilio es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (ministerioService.existsByNombre(ministerioDto.getNombre()))
            return new ResponseEntity<Mensaje>(new Mensaje("ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);

        Ministerio ministerio = new Ministerio();
        ministerio.setNombre(ministerioDto.getNombre());
        ministerio.setDomicilio(ministerioDto.getDomicilio());
        ministerio.setTelefono(ministerioDto.getTelefono());
        ministerio.setEstado(ministerioDto.isEstado());
        ministerio.setRegion(ministerioDto.getRegion());
        ministerio.setLocalidad(ministerioDto.getLocalidad());
        ministerio.setObservacion(ministerioDto.getObservacion());

        ministerio.setCabecera(ministerioDto.getCabecera());

        ministerioService.save(ministerio);
        return new ResponseEntity<Mensaje>(new Mensaje("Ministerio creado correctamente"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody MinisterioDto ministerioDto) {
        if (!ministerioService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        // if (ministerioService.existsByNombre(ministerioDto.getNombre()) &&
        // ministerioService.getMinisterioByNombre(ministerioDto.getNombre()).get().getId()
        // ==
        // id)
        // return new ResponseEntity<Mensaje>(new Mensaje("ese ministerio ya existe"),
        // HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(ministerioDto.getNombre()))
            return new ResponseEntity<Mensaje>(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        Ministerio ministerio = ministerioService.findById(id).get();

        if (ministerio.getNombre() != ministerioDto.getNombre() && ministerioDto.getNombre() != null
                && !ministerioDto.getNombre().isEmpty())
            ministerio.setNombre(ministerioDto.getNombre());

        if (ministerio.getDomicilio() != ministerioDto.getDomicilio() && ministerioDto.getDomicilio() != null
                && !ministerioDto.getDomicilio().isEmpty())
            ministerio.setDomicilio(ministerioDto.getDomicilio());

        if (ministerio.getTelefono() != ministerioDto.getTelefono() && ministerioDto.getTelefono() != null
                && !ministerioDto.getTelefono().isEmpty())
            ministerio.setTelefono(ministerioDto.getTelefono());

        if (ministerio.isEstado() != ministerioDto.isEstado())
            ministerio.setEstado(ministerioDto.isEstado());

        if (!ministerioDto.getRegion().equals(ministerio.getRegion())) {
            ministerio.setRegion(ministerioDto.getRegion());
        }
        if (!ministerioDto.getLocalidad().equals(ministerio.getLocalidad())) {
            ministerio.setLocalidad(ministerioDto.getLocalidad());
        }

        if (ministerio.getObservacion() != ministerioDto.getObservacion() && ministerioDto.getObservacion() != null
                && !ministerioDto.getObservacion().isEmpty())
            ministerio.setObservacion(ministerioDto.getObservacion());

        if (ministerio.getCabecera() != ministerioDto.getCabecera() && ministerioDto.getCabecera() != null)
            ministerio.setCabecera(ministerioDto.getCabecera());

        ministerioService.save(ministerio);
        return new ResponseEntity<Mensaje>(new Mensaje("Ministerio actualizado"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (!ministerioService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);
        ministerioService.deleteById(id);
        return new ResponseEntity<Mensaje>(new Mensaje("Efector eliminado"), HttpStatus.OK);
    }

}
