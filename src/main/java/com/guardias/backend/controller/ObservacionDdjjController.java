package com.guardias.backend.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ObservacionDdjjDto;
import com.guardias.backend.dto.ObservacionDdjj.ObservacionDdjjUltimoDto;
import com.guardias.backend.entity.ObservacionDdjj;
import com.guardias.backend.service.ObservacionDdjjService;

@RestController
@RequestMapping("/observacionDdjj")
@CrossOrigin(origins = "http://localhost:4200")
public class ObservacionDdjjController {

    @Autowired
    ObservacionDdjjService observacionDdjjService;

    @GetMapping("/list")
    public ResponseEntity<List<ObservacionDdjj>> list() {
        List<ObservacionDdjj> list = observacionDdjjService.findByActivoTrue();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ObservacionDdjj>> listAll() {
        List<ObservacionDdjj> list = observacionDdjjService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<ObservacionDdjj>> getById(@PathVariable("id") Long id) {
        if (!observacionDdjjService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la observacion de ddjj"),
                    HttpStatus.NOT_FOUND);
        ObservacionDdjj observacionDdjj = observacionDdjjService.findById(id).get();
        return new ResponseEntity(observacionDdjj, HttpStatus.OK);
    }

    /*
     * @PostMapping("/create")
     * public ResponseEntity<?> create(@RequestBody ObservacionDdjjDto
     * observacionDdjjDto) {
     * ResponseEntity<?> respuestaValidaciones =
     * observacionDdjjService.validations(observacionDdjjDto, 0L);
     * if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
     * ObservacionDdjj observacionDdjj = observacionDdjjService.createUpdate(new
     * ObservacionDdjj(),
     * observacionDdjjDto);
     * observacionDdjjService.save(observacionDdjj);
     * 
     * return new ResponseEntity(new Mensaje("Observacion de ddjj creada"),
     * HttpStatus.OK);
     * } else {
     * return respuestaValidaciones;
     * }
     * }
     */

    @PostMapping(value = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> create(
            @RequestPart("observacion") String observacionStr,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {

        try {
            // 1. Convertir JSON a DTO
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            ObservacionDdjjDto dto = mapper.readValue(observacionStr, ObservacionDdjjDto.class);

            // 2. Validaciones básicas de negocio (campos obligatorios)
            ResponseEntity<?> respuestaValidaciones = observacionDdjjService.validations(dto, 0L);
            if (respuestaValidaciones.getStatusCode() != HttpStatus.OK) {
                return respuestaValidaciones;
            }

            // 3. Llamar al servicio que hace TODO el trabajo duro
            ObservacionDdjj observacion = observacionDdjjService.crearConAdjunto(dto, archivo);

            return new ResponseEntity<>(new Mensaje("Observación creada con éxito"), HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(new Mensaje("Error al procesar el archivo: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error inesperado: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ObservacionDdjjDto observacionDdjjDto) {
        if (!observacionDdjjService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la observacion de ddjj"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = observacionDdjjService.validations(observacionDdjjDto, id);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            ObservacionDdjj observacionDdjj = observacionDdjjService
                    .createUpdate(observacionDdjjService.findById(id).get(), observacionDdjjDto);
            observacionDdjjService.save(observacionDdjj);

            return new ResponseEntity(new Mensaje("Observacion de ddjj modificada"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!observacionDdjjService.activo(id))
            return new ResponseEntity(new Mensaje("observacion de ddjj no encontrada"), HttpStatus.NOT_FOUND);

        ObservacionDdjj observacionDdjj = observacionDdjjService.findById(id).get();
        observacionDdjj.setActivo(false);
        observacionDdjjService.save(observacionDdjj);
        return new ResponseEntity(new Mensaje("observacion eliminada LOGICAMENTE"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!observacionDdjjService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la observacion de ddjj"), HttpStatus.NOT_FOUND);
        observacionDdjjService.deleteById(id);
        return new ResponseEntity(new Mensaje("Observacion eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/ultimaPorDdjj/{idDdjj}/{tipoDph}")
    public ResponseEntity<ObservacionDdjjUltimoDto> getUltimaObservacionByDdjjAndTipoDph(
            @PathVariable("idDdjj") Long idDdjj,
            @PathVariable("tipoDph") Boolean tipoDph) {

        System.out.println("=== INICIO LLAMADA AL CONTROLLER ===");
        System.out.println("ID DDJJ recibido: " + idDdjj);
        System.out.println("Tipo DPH recibido: " + tipoDph);
        ObservacionDdjjUltimoDto observacion = observacionDdjjService
                .getUltimaObservacionByDdjjAndTipoDph(idDdjj, tipoDph);

        if (observacion == null) {
            System.out.println("=== FIN CONTROLLER: No se encontraron resultados ===");
            // Crear un objeto vacío en lugar de retornar 404
            ObservacionDdjjUltimoDto observacionVacia = new ObservacionDdjjUltimoDto();
            return ResponseEntity.ok(observacionVacia);
        }
        System.out.println("=== FIN CONTROLLER: Resultado encontrado ===");
        return ResponseEntity.ok(observacion);
    }

    @GetMapping("/todasActivasPorDdjj/{idDdjj}/{tipoDph}")
    public ResponseEntity<List<ObservacionDdjjUltimoDto>> getAllObservacionesActivasByDdjjAndTipoDph(
            @PathVariable("idDdjj") Long idDdjj,
            @PathVariable("tipoDph") Boolean tipoDph) {

        List<ObservacionDdjjUltimoDto> observaciones = observacionDdjjService
                .getAllObservacionesActivasByDdjjAndTipoDph(idDdjj, tipoDph);

        return ResponseEntity.ok(observaciones);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String path) {
        try {
            // 1. Obtener el archivo (puede lanzar FileNotFoundException)
            Resource recurso = observacionDdjjService.cargarArchivoComoRecurso(path);

            // 2. Determinar tipo de archivo (Opción simplificada y segura)
            String contentType = "application/octet-stream";

            // 3. Devolver respuesta exitosa (200 OK)
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                    .body(recurso);

        } catch (FileNotFoundException e) {
            // CASO A: El archivo no existe -> Devolvemos 404 (Not Found)
            // Esto es correcto tanto en Desarrollo como en Producción
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            // CASO B: Error inesperado (ej: fallo de disco, memoria) -> Devolvemos 500
            e.printStackTrace(); // Muestra el error en la consola para que tú lo veas
            return ResponseEntity.internalServerError().build();
        }
    }

}
