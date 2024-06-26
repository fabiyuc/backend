package com.guardias.backend.controller;

import java.io.IOException;
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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.RegistroMensualDto;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.service.JsonFileService;
import com.guardias.backend.service.RegistroMensualService;

@RestController
@RequestMapping("/registroMensual")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroMensualController {
    @Autowired
    RegistroMensualService registroMensualService;
    @Autowired
    JsonFileService jsonFileService;

    @GetMapping("/list")
    public ResponseEntity<List<RegistroMensual>> list() {
        List<RegistroMensual> list = registroMensualService.findByActivoTrue().get();

        for (RegistroMensual registroMensual : list) {
            try {
                String jsonContent = jsonFileService.decodeToString(registroMensual.getJsonFile());
                registroMensual.setRegistroActividad(registroMensualService.mapFromJson(jsonContent));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<List<RegistroMensual>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<RegistroMensual>> listAll() {
        List<RegistroMensual> list = registroMensualService.findAll();
        for (RegistroMensual registroMensual : list) {
            try {
                String jsonContent = jsonFileService.decodeToString(registroMensual.getJsonFile());
                registroMensual.setRegistroActividad(registroMensualService.mapFromJson(jsonContent));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<List<RegistroMensual>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAMEcargoyagrup/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<RegistroMensual>> listByYearMonthEfectorAndTipoGuardiaCargoReagrupacion(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensual> registrosMensuales = registroMensualService
                    .findByAnioMesEfectorAndTipoGuardiaCargoReagrupacion(anio, mesEnum, idEfector);
            for (RegistroMensual registroMensual : registrosMensuales) {
                try {
                    String jsonContent = jsonFileService.decodeToString(registroMensual.getJsonFile());
                    registroMensual.setRegistroActividad(registroMensualService.mapFromJson(jsonContent));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return new ResponseEntity<List<RegistroMensual>>(registrosMensuales, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registros mensuales de Cargo y reagrupacion no encontrados"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAMEextra/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<RegistroMensual>> listByYearMonthEfectorAndTipoGuardiaExtra(
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("idEfector") Long idEfector) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            List<RegistroMensual> registrosMensuales = registroMensualService
                    .findByAnioMesEfectorAndTipoGuardiaExtra(anio, mesEnum, idEfector);
            for (RegistroMensual registroMensual : registrosMensuales) {
                try {
                    String jsonContent = jsonFileService.decodeToString(registroMensual.getJsonFile());
                    registroMensual.setRegistroActividad(registroMensualService.mapFromJson(jsonContent));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return new ResponseEntity<List<RegistroMensual>>(registrosMensuales, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registros mensuales extra no encontrados"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        if (!registroMensualService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("El registro mensual no existe"), HttpStatus.NOT_FOUND);
        }

        RegistroMensual registroMensual = registroMensualService.findById(id).orElse(null);
        if (registroMensual == null) {
            return new ResponseEntity<>(new Mensaje("El registro mensual no existe"), HttpStatus.NOT_FOUND);
        }

        try {
            String jsonContent = jsonFileService.decodeToString(registroMensual.getJsonFile());
            registroMensual.setRegistroActividad(registroMensualService.mapFromJson(jsonContent));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Mensaje("Error al mapear el JSON"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(registroMensual, HttpStatus.OK);
    }

    @GetMapping("/listMes/{idAsistencial}/{idEfector}/{mes}/{anio}")
    public ResponseEntity<List<RegistroActividad>> getByMes(@PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("mes") String mes, @PathVariable("anio") int anio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        try {
            RegistroMensual registroMensual = registroMensualService
                    .findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial, idEfector, mesEnum, anio)
                    .get();
            List<RegistroActividad> list = registroMensual.getRegistroActividad();
            return new ResponseEntity<List<RegistroActividad>>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registro no encontrado"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegistroMensualDto registroMensualDto) {
        ResponseEntity<?> respuestaValidaciones = registroMensualService.validations(registroMensualDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroMensual registroMensual = registroMensualService.createUpdate(new RegistroMensual(),
                    registroMensualDto);
            registroMensualService.save(registroMensual);
            return new ResponseEntity(new Mensaje("Registro de Actividad creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody RegistroMensualDto registroMensualDto) {
        if (!registroMensualService.activo(id))
            return new ResponseEntity(new Mensaje("Registro de actividad no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = registroMensualService.validations(registroMensualDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroMensual registroMensual = registroMensualService.createUpdate(
                    registroMensualService.findById(id).get(),
                    registroMensualDto);
            registroMensualService.save(registroMensual);
            return new ResponseEntity(new Mensaje("Registro de Actividad modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!registroMensualService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        RegistroMensual registroMensual = registroMensualService.findById(id).get();
        registroMensual.setActivo(false);
        registroMensualService.save(registroMensual);
        return new ResponseEntity<>(new Mensaje("Registro mensual eliminado correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!registroMensualService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        registroMensualService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Registro mensual eliminado FISICAMENTEE"), HttpStatus.OK);
    }
}
