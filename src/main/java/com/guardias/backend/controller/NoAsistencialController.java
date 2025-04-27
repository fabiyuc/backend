package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NoAsistencialDto;
import com.guardias.backend.dto.noAsistencial.NoAsistencialListDto;
import com.guardias.backend.dto.noAsistencial.NoAsistencialSummaryDto;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.entity.Person;
import com.guardias.backend.service.NoAsistencialService;

@RestController
@RequestMapping("/noasistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class NoAsistencialController {

    @Autowired
    NoAsistencialService noAsistencialService;

    @Autowired
    PersonController personController;

    @GetMapping("/list")
    public ResponseEntity<List<NoAsistencial>> list() {
        List<NoAsistencial> list = noAsistencialService.findByActivoTrue().orElse(new ArrayList<>());
        return new ResponseEntity<List<NoAsistencial>>(list, HttpStatus.OK);
    }

    @GetMapping("/listNoAsistencialByEfector/{idEfector}")
    public ResponseEntity<List<NoAsistencial>> listByEfector(@PathVariable Long idEfector) {
        List<NoAsistencial> list = noAsistencialService.findByEfectorByActivoTrue(idEfector);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/listAllByEfector/{idEfector}")
    public ResponseEntity<List<NoAsistencial>> listAllByEfector(@PathVariable Long idEfector) {
        List<NoAsistencial> list = noAsistencialService.findByEfectorAndActivoTrue(idEfector);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/listDtos")
    public ResponseEntity<List<NoAsistencialListDto>> listDtos() {
        List<NoAsistencialListDto> noAsistencialListDtos = noAsistencialService.getNoAsistencialList();

        return new ResponseEntity<List<NoAsistencialListDto>>(noAsistencialListDtos, HttpStatus.OK);
    }

    // Lista de noAsistenciales que son autoridades, con sus datos personales
    @GetMapping("/listAutoridadesByEfector/{idEfector}")
    public ResponseEntity<List<NoAsistencialListDto>> listAutoridadesByEfector(@PathVariable Long idEfector) {
        List<NoAsistencialListDto> autoridades = noAsistencialService
                .getNoAsistencialListAutoridadesByEfector(idEfector);
        return ResponseEntity.ok(autoridades);
    }

    // Lista de no asistenciales que son autoridades regionales
    @GetMapping("/listAutoridadesRegionalesByEfector/{idEfector}")
    public ResponseEntity<List<NoAsistencialListDto>> listAutoridadesRegionalesByEfector(@PathVariable Long idEfector) {
        List<NoAsistencialListDto> autoridades = noAsistencialService
                .getNoAsistencialListAutoridadesRegionalesByEfector(idEfector);
        return ResponseEntity.ok(autoridades);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<NoAsistencial>> listAll() {
        List<NoAsistencial> list = noAsistencialService.findAll();
        return new ResponseEntity<List<NoAsistencial>>(list, HttpStatus.OK);
    }

    @GetMapping("/legajos/{id}")
    public ResponseEntity<List<Legajo>> getLegajosByNoAsistencial(@PathVariable("id") Long id) {
        if (!noAsistencialService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la persona"), HttpStatus.NOT_FOUND);

        NoAsistencial noAsistencial = noAsistencialService.findById(id).get();

        if (noAsistencial == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        // Filtramos los legajos que estén activos
        List<Legajo> legajosActivos = noAsistencial.getLegajos().stream()
                .filter(Legajo::isActivo) // Filtra por el campo activo
                .collect(Collectors.toList());

        return new ResponseEntity<>(legajosActivos, HttpStatus.OK);
    }

    // Lista de no asistenciales por efector
    @GetMapping("/listByEfector/{idEfector}")
    public ResponseEntity<List<NoAsistencialSummaryDto>> getNoAsistencialesByEfector(
            @PathVariable("idEfector") Long idEfector) {
        List<NoAsistencialSummaryDto> noAsistenciales = noAsistencialService.getNoAsistencialesByEfector(idEfector);
        if (noAsistenciales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(noAsistenciales, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<NoAsistencial> getById(@PathVariable("id") Long id) {
        if (!noAsistencialService.activo(id))
            return new ResponseEntity(new Mensaje("Profesional no entontrado"), HttpStatus.NOT_FOUND);
        NoAsistencial noAsistencial = noAsistencialService.findById(id).get();
        return new ResponseEntity<NoAsistencial>(noAsistencial, HttpStatus.OK);
    }

    @GetMapping("/detailDni/{dni}")
    public ResponseEntity<NoAsistencial> getById(@PathVariable("dni") int dni) {
        if (!noAsistencialService.existsByDniAndActivoTrue(dni))
            return new ResponseEntity(new Mensaje("Profesional no entontrado"), HttpStatus.NOT_FOUND);
        NoAsistencial noAsistencial = noAsistencialService.findByDniAndActivoTrue(dni).get();
        return new ResponseEntity<NoAsistencial>(noAsistencial, HttpStatus.OK);
    }

    public NoAsistencial createUpdate(NoAsistencial noAsistencial, NoAsistencialDto noAsistencialDto) {

        Person person = personController.createUpdate(noAsistencial, noAsistencialDto);
        noAsistencial = (NoAsistencial) person;

        if (noAsistencial.getDescripcion() == null || (noAsistencialDto.getDescripcion() != null
                && !Objects.equals(noAsistencial.getDescripcion(), noAsistencialDto.getDescripcion())))
            noAsistencial.setDescripcion(noAsistencialDto.getDescripcion());

        noAsistencial.setActivo(true);
        return noAsistencial;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NoAsistencialDto noAsistencialDto) {

        ResponseEntity<?> respuestaValidaciones = personController.validations(noAsistencialDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            NoAsistencial noAsistencial = createUpdate(new NoAsistencial(), noAsistencialDto);

            noAsistencialService.save(noAsistencial);
            return new ResponseEntity(new Mensaje("No asistencial creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody NoAsistencialDto noAsistencialDto) {
        if (!noAsistencialService.activo(id))
            return new ResponseEntity(new Mensaje("La persona no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = personController.validations(noAsistencialDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            NoAsistencial noAsistencial = createUpdate(noAsistencialService.findById(id).get(), noAsistencialDto);
            noAsistencialService.save(noAsistencial);
            return new ResponseEntity(new Mensaje("Persona modificada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!noAsistencialService.activo(id))
            return new ResponseEntity(new Mensaje("el profesional no existe"), HttpStatus.NOT_FOUND);
        NoAsistencial noNoAsistencial = noAsistencialService.findById(id).get();
        noNoAsistencial.setActivo(false);
        noAsistencialService.save(noNoAsistencial);
        return new ResponseEntity<>(new Mensaje("NoAsistencial eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!noAsistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        noAsistencialService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("NoAsistencial eliminado FISICAMENTE"), HttpStatus.OK);
    }
}