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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.TipoLicenciaDto;
import com.guardias.backend.entity.Articulo;
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.entity.TipoLicencia;
import com.guardias.backend.service.ArticuloService;
import com.guardias.backend.service.IncisoService;
import com.guardias.backend.service.TipoLeyService;
import com.guardias.backend.service.TipoLicenciaService;

@Controller
@RequestMapping("/tipoLicencia")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoLicenciaController {

    @Autowired
    TipoLicenciaService tipoLicenciaService;
    @Autowired
    ArticuloService articuloService;
    @Autowired
    IncisoService incisoService;
    @Autowired
    TipoLeyService tipoLeyService;

    @GetMapping("/list")
    public ResponseEntity<List<TipoLicencia>> list() {
        List<TipoLicencia> list = tipoLicenciaService.findByActivoTrue().get();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<TipoLicencia>> listAll() {
        List<TipoLicencia> list = tipoLicenciaService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<TipoLicencia>> getById(@PathVariable("id") Long id) {
        if (!tipoLicenciaService.activo(id))
            return new ResponseEntity(new Mensaje("Tipo de licencia no encontrada"), HttpStatus.NOT_FOUND);
        TipoLicencia tipoLicencia = tipoLicenciaService.findById(id).get();
        return new ResponseEntity(tipoLicencia, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<TipoLicencia>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!tipoLicenciaService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("Tipo de licencia no encontrada"), HttpStatus.NOT_FOUND);
        TipoLicencia tipoLicencia = tipoLicenciaService.findByNombre(nombre).get();
        return new ResponseEntity(tipoLicencia, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(TipoLicenciaDto tipoLicenciaDto, Long id) {
        if (tipoLicenciaDto.getNombre() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El nombre es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        if (tipoLicenciaDto.getIdTipoLey() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La ley es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (tipoLicenciaDto.getIdArticulos() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El articulo es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        /*
         * if (tipoLicenciaDto.getIdIncisos() == null)
         * return new ResponseEntity<Mensaje>(new Mensaje("El inciso es obligatorio"),
         * HttpStatus.BAD_REQUEST);
         */

        if (tipoLicenciaService.existsByNombre(tipoLicenciaDto.getNombre())
                && (!tipoLicenciaService.findByNombre(tipoLicenciaDto.getNombre()).get().getId().equals(id)))
            return new ResponseEntity<Mensaje>(new Mensaje("Ese nombre ya existe"),
                    HttpStatus.BAD_REQUEST);
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private TipoLicencia createUpdate(TipoLicencia tipoLicencia, TipoLicenciaDto tipoLicenciaDto) {

        if (tipoLicenciaDto.getNombre() != null && !tipoLicenciaDto.getNombre().isEmpty()
                && !tipoLicenciaDto.getNombre().equals(tipoLicencia.getNombre())) {
            tipoLicencia.setNombre(tipoLicenciaDto.getNombre());
        }

        if (tipoLicenciaDto.getIdArticulos() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (tipoLicencia.getArticulos() != null) {
                for (Articulo articulo : tipoLicencia.getArticulos()) {
                    for (Long id : tipoLicenciaDto.getIdArticulos()) {
                        if (!articulo.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                tipoLicencia.setArticulos(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? tipoLicenciaDto.getIdArticulos() : idList;
            for (Long id : idsToAdd) {
                tipoLicencia.getArticulos().add(articuloService.findById(id).get());
                articuloService.findById(id).get().setTipoLicencia(tipoLicencia);
            }
        }

        if (tipoLicenciaDto.getIdIncisos() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (tipoLicencia.getIncisos() != null) {
                for (Inciso inciso : tipoLicencia.getIncisos()) {
                    for (Long id : tipoLicenciaDto.getIdIncisos()) {
                        if (!inciso.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                tipoLicencia.setIncisos(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? tipoLicenciaDto.getIdIncisos() : idList;
            for (Long id : idsToAdd) {
                tipoLicencia.getIncisos().add(incisoService.findById(id).get());
                incisoService.findById(id).get().setTipoLicencia(tipoLicencia);
            }
        }

        if (tipoLicencia.getTipoLey() == null ||
                (tipoLicenciaDto.getIdTipoLey() != null &&
                        !Objects.equals(tipoLicencia.getTipoLey().getId(),
                                tipoLicenciaDto.getIdTipoLey()))) {
            tipoLicencia.setTipoLey(tipoLeyService.findById(tipoLicenciaDto.getIdTipoLey()).get());
        }

        tipoLicencia.setActivo(true);
        return tipoLicencia;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TipoLicenciaDto tipoLicenciaDto) {

        ResponseEntity<?> respuestaValidaciones = validations(tipoLicenciaDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            TipoLicencia tipoLicencia = createUpdate(new TipoLicencia(), tipoLicenciaDto);
            tipoLicenciaService.save(tipoLicencia);
            return new ResponseEntity<Mensaje>(new Mensaje("Tipo de licencia creado correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TipoLicenciaDto tipoLicenciaDto) {

        if (!tipoLicenciaService.activo(id))
            return new ResponseEntity<>(new Mensaje("El tipo de licencia no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(tipoLicenciaDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            TipoLicencia tipoLicencia = createUpdate(tipoLicenciaService.findById(id).get(), tipoLicenciaDto);
            tipoLicenciaService.save(tipoLicencia);
            return new ResponseEntity<Mensaje>(new Mensaje("Tipo de licencia modificado correctamente"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!tipoLicenciaService.activo(id)) {
            return new ResponseEntity(new Mensaje("Tipo de licencia no encontrada"), HttpStatus.NOT_FOUND);
        }
        TipoLicencia tipoLicencia = tipoLicenciaService.findById(id).orElse(null);

        if (tipoLicencia != null && !tipoLicencia.getArticulos().isEmpty()) {
            return new ResponseEntity(new Mensaje("No se puede eliminar el tipo licencia, esta en uso"),
                    HttpStatus.BAD_REQUEST);
        }
        tipoLicencia.setActivo(false);
        tipoLicenciaService.save(tipoLicencia);
        return new ResponseEntity<>(new Mensaje("Tipo de licencia eliminado logicamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!tipoLicenciaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        tipoLicenciaService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Tipo Licencia eliminado FISICAMENTE"), HttpStatus.OK);
    }

}
