package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

import com.guardias.backend.dto.AsistencialDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asistencial.AsistencialListDto;
import com.guardias.backend.dto.asistencial.AsistencialListForLegajosDto;
import com.guardias.backend.dto.asistencial.AsistencialSummaryDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.AutoridadService;
import com.guardias.backend.service.TipoGuardiaService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/asistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class AsistencialController {

    @Autowired
    AsistencialService asistencialService;
    
    @GetMapping("/list")
    public ResponseEntity<List<Asistencial>> list() {
        List<Asistencial> asistencialList = asistencialService.findByActivoTrue().orElse(new ArrayList<>());
        List<Asistencial> filteredList = new ArrayList<>();

        for (Asistencial asistencial : asistencialList) {
            List<RegistroActividad> activeRegActividades = new ArrayList<>();
            for (RegistroActividad registroActividad : asistencial.getRegistrosActividades()) {
                if (registroActividad.isActivo()) {
                    activeRegActividades.add(registroActividad);
                }
            }
            asistencial.setRegistrosActividades(activeRegActividades);
            filteredList.add(asistencial);
        }

        return new ResponseEntity<List<Asistencial>>(filteredList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Asistencial>> listAll() {
        List<Asistencial> list = asistencialService.findAll();
        return new ResponseEntity<List<Asistencial>>(list, HttpStatus.OK);
    }

    // lista asistenciales con datos personales resumido

    @GetMapping("/listSummary")
    public ResponseEntity<List<AsistencialSummaryDto>> listSummary() {
        List<AsistencialSummaryDto> summaryList = asistencialService.getAsistencialSummaryList();

        return new ResponseEntity<List<AsistencialSummaryDto>>(summaryList,
                HttpStatus.OK);
    }
    // lista asistenciales con datos personales completos

    @GetMapping("/listDtos")
    public ResponseEntity<List<AsistencialListDto>> listDtos() {
        List<AsistencialListDto> asistencialListDtos = asistencialService.getAsistencialList();

        return new ResponseEntity<List<AsistencialListDto>>(asistencialListDtos,
                HttpStatus.OK);
    }
    // lista asistenciales habilitados para crear legajo segun tipoGuardia

    @GetMapping("/listForLegajosDtos")
    public ResponseEntity<List<AsistencialListForLegajosDto>> listForLegajosDtos() {
        List<AsistencialListForLegajosDto> asistencialListForLegajosDtos = asistencialService
                .getAsistencialListForLegajos();

        return new ResponseEntity<List<AsistencialListForLegajosDto>>(
                asistencialListForLegajosDtos, HttpStatus.OK);
    }

    // lista asistenciales habilitados de un hospital para crear distribuciones
    // horarias (CARGO Y AGRUP)

    @GetMapping("/listForDistHorariaDtos")
    public ResponseEntity<List<AsistencialListForLegajosDto>> listForDistHoraria() {
        List<AsistencialListForLegajosDto> asistencialListForLegajosDtos = asistencialService
                .getAsistencialListForLegajos();

        return new ResponseEntity<List<AsistencialListForLegajosDto>>(
                asistencialListForLegajosDtos, HttpStatus.OK);
    }

    // lista legajos segun id Asistencial

    @GetMapping("/legajos/{id}")
    public ResponseEntity<List<Legajo>> getLegajosByAsistencial(@PathVariable("id") Long id) {
        if (!asistencialService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la persona"),
                    HttpStatus.NOT_FOUND);

        Asistencial asistencial = asistencialService.findById(id).orElse(null);
        if (asistencial == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Filtramos los legajos que estén activos
        List<Legajo> legajosActivos = asistencial.getLegajos().stream()
                .filter(Legajo::isActivo) // Filtra por el campo activo
                .collect(Collectors.toList());

        return new ResponseEntity<>(legajosActivos, HttpStatus.OK);
    }

    // Lista asistenciales segun Udo con tipoGuardia CARGO Y/O AGRUPACION

    @GetMapping("/listByUdoAndTipoGuardia/{idUdo}")
    public ResponseEntity<List<AsistencialSummaryDto>> getAsistencialesByUdoAndTipoGuardia(@PathVariable Long idUdo) {
        List<AsistencialSummaryDto> asistenciales = asistencialService.getAsistencialesByUdoAndTipoGuardia(idUdo);

        if (asistenciales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(asistenciales, HttpStatus.OK);
    }

    // Lista asistenciales segun efector con tipoGuardia CARGO Y/O AGRUPACION

    @GetMapping("/listByEfectorAndTipoGuardia/{idEfector}")
    public ResponseEntity<List<AsistencialSummaryDto>> getAsistencialesByEfectorAndTipoGuardia(
            @PathVariable Long idEfector) {
        List<AsistencialSummaryDto> asistenciales = asistencialService
                .getAsistencialesByEfectorAndTipoGuardia(idEfector);

        if (asistenciales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(asistenciales, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Asistencial> getById(@PathVariable("id") Long id) {
        if (!asistencialService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la persona tipo asistencial"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findById(id).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);
    }

    @GetMapping("/detaildni/{dni}")
    public ResponseEntity<Asistencial> getByDni(@PathVariable("dni") int dni) {
        if (!asistencialService.activoDni(dni))
            return new ResponseEntity(new Mensaje("no existe asistencial con ese dni"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findByDni(dni).get();
        return new ResponseEntity<Asistencial>(asistencial, HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AsistencialDto asistencialDto) {

        ResponseEntity<?> respuestaValidaciones = asistencialService.validations(asistencialDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Asistencial asistencial = asistencialService.createUpdate(new Asistencial(), asistencialDto);
            asistencialService.save(asistencial);
            return new ResponseEntity(new Mensaje("asistencial creado"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody AsistencialDto asistencialDto) {
        if (!asistencialService.activo(id))
            return new ResponseEntity(new Mensaje("el profesional no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = asistencialService.validations(asistencialDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Asistencial asistencial = asistencialService.createUpdate(asistencialService.findById(id).get(), asistencialDto);
            asistencialService.save(asistencial);
            return new ResponseEntity(new Mensaje("asistencial modificado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!asistencialService.activo(id))
            return new ResponseEntity(new Mensaje("el profesional no existe"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findById(id).get();
        asistencial.setActivo(false);
        asistencialService.save(asistencial);
        return new ResponseEntity<>(new Mensaje("Asistencial eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!asistencialService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        asistencialService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Asistencial eliminado FISICAMENTE"), HttpStatus.OK);
    }

    /* @PostMapping("/{idAsistencial}/addTipoGuardia/{idTipoGuardia}")
    public ResponseEntity<?> agregarTipoGuardia(@PathVariable("idAsistencial") Long idAsistencial,

            @PathVariable("idTipoGuardia") Long idTipoGuardia) {

        try {
            asistencialService.agregarTipoGuardia(idAsistencial, idTipoGuardia);
            return new ResponseEntity<>(new Mensaje("Tipo DEGuardia agregado al asistencial correctamente"),
                    HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new Mensaje("No se encontró el asistencial con el ID proporcionado"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al agregar el tipo de Guardia al asistencial"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } */

    @GetMapping("/esPlanta/{idAsistencial}/{idEfector}")
    public boolean esPlanta(@PathVariable("idAsistencial") long idAsistencial, @PathVariable("idEfector") long idEfector) {
        return asistencialService.esPlanta(idAsistencial,idEfector);
    }

}