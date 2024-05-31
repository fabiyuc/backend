package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.DdjjService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistroActividadService;
import com.guardias.backend.service.RegistroMensualService;
import com.guardias.backend.service.SumaHorasService;

@RestController
@RequestMapping("/registroMensual")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroMensualController {
    @Autowired
    RegistroMensualService registroMensualService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    RegistroActividadService registroActividadService;
    @Autowired
    DdjjService ddjjService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    SumaHorasService sumaHorasService;

    @GetMapping("/list")
    public ResponseEntity<List<RegistroMensual>> list() {
        List<RegistroMensual> list = registroMensualService.findByActivoTrue().get();
        return new ResponseEntity<List<RegistroMensual>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<RegistroMensual>> listAll() {
        List<RegistroMensual> list = registroMensualService.findAll();
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

            return new ResponseEntity<List<RegistroMensual>>(registrosMensuales, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registros mensuales de Cargo y reagrupacion no encontrados"), HttpStatus.BAD_REQUEST);
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

            return new ResponseEntity<List<RegistroMensual>>(registrosMensuales, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Registros mensuales extra no encontrados"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<RegistroMensual>> getById(@PathVariable("id") Long id) {
        if (!registroMensualService.activo(id))
            return new ResponseEntity(new Mensaje("El registro mensual no existe"), HttpStatus.NOT_FOUND);
        RegistroMensual registroMensual = registroMensualService.findById(id).get();
        return new ResponseEntity(registroMensual, HttpStatus.OK);
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

    // @GetMapping("/detailId/{idAsistencial}/{idEfector}/{mes}/{anio}")
    // public ResponseEntity<Long>
    // idByIdAsistencialAndMes(@PathVariable("idAsistencial") Long idAsistencial,
    // @PathVariable("idEfector") Long idEfector,
    // @PathVariable("mes") String mes, @PathVariable("anio") int anio) {
    // MesesEnum mesEnum = MesesEnum.valueOf(mes);

    // try {
    // Long idRegistroMensual = registroMensualService
    // .idByIdAsistencialAndMes(idAsistencial, idEfector, mesEnum, anio)
    // .get();
    // return new ResponseEntity<Long>(idRegistroMensual, HttpStatus.OK);
    // } catch (Exception e) {
    // return new ResponseEntity(new Mensaje("Registro no encontrado"),
    // HttpStatus.BAD_REQUEST);
    // }
    // }

    public ResponseEntity<?> validations(RegistroMensualDto registroMensualDto) {

        if (registroMensualDto.getMes() == null)
            return new ResponseEntity(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);

        if (registroMensualDto.getAnio() < 1991)
            return new ResponseEntity(new Mensaje("El año es incorrecto"), HttpStatus.BAD_REQUEST);

        if (registroMensualDto.getIdAsistencial() < 1)
            return new ResponseEntity(new Mensaje("El id de la persona es incorrecto"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    private RegistroMensual createUpdate(RegistroMensual registroMensual,
            RegistroMensualDto registroMensualDto) {

        if (registroMensualDto.getMes() != null && !registroMensualDto.getMes().equals(registroMensual.getMes()))
            registroMensual.setMes(registroMensualDto.getMes());

        if (registroMensualDto.getAnio() != registroMensual.getAnio())
            registroMensual.setAnio(registroMensualDto.getAnio());

        // if (registroMensualDto.getIdAsistencial() !=
        // registroMensual.getIdAsistencial())
        // registroMensual.setIdAsistencial(registroMensualDto.getIdAsistencial());

        if (registroMensualDto.getIdAsistencial() != null && (registroMensual.getAsistencial() == null
                || !Objects.equals(registroMensual.getAsistencial().getId(), registroMensualDto.getIdAsistencial()))) {
            registroMensual.setAsistencial(asistencialService.findById(registroMensualDto.getIdAsistencial()).get());
        }

        if (registroMensualDto.getIdRegistroActividad() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (registroMensual.getRegistroActividad() != null) {
                for (RegistroActividad registroActividad : registroMensual.getRegistroActividad()) {
                    for (Long id : registroMensualDto.getIdRegistroActividad()) {
                        if (!registroActividad.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                registroMensual.setRegistroActividad(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? registroMensualDto.getIdRegistroActividad() : idList;
            for (Long id : idsToAdd) {
                registroMensual.getRegistroActividad().add(registroActividadService.findById(id).get());
                registroActividadService.findById(id).get().setRegistroMensual(registroMensual);
            }
        }

        if (registroMensualDto.getIdEfector() != null && (registroMensual.getEfector() == null
                || !Objects.equals(registroMensual.getEfector().getId(), registroMensualDto.getIdEfector()))) {
            registroMensual.setEfector(efectorService.findById(registroMensualDto.getIdEfector()));
        }

        if (registroMensualDto.getIdSumaHoras() != null && (registroMensual.getSumaHoras() == null
                || !Objects.equals(registroMensual.getSumaHoras().getId(), registroMensualDto.getIdSumaHoras()))) {
            registroMensual.setSumaHoras(sumaHorasService.findById(registroMensualDto.getIdSumaHoras()).get());
        }

        if (registroMensualDto.getIdDdjj() != null && (registroMensual.getDdjj() == null
                || !Objects.equals(registroMensual.getDdjj().getId(), registroMensualDto.getIdDdjj()))) {
            registroMensual.setDdjj(ddjjService.findById(registroMensualDto.getIdDdjj()).get());
        }

        registroMensual.setActivo(true);
        return registroMensual;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RegistroMensualDto registroMensualDto) {
        ResponseEntity<?> respuestaValidaciones = validations(registroMensualDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroMensual registroMensual = createUpdate(new RegistroMensual(), registroMensualDto);
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

        ResponseEntity<?> respuestaValidaciones = validations(registroMensualDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            RegistroMensual registroMensual = createUpdate(registroMensualService.findById(id).get(),
                    registroMensualDto);
            registroMensualService.save(registroMensual);
            return new ResponseEntity(new Mensaje("Registro de Actividad modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    public Long createRegistroMensual(Long idAsistencial, Long idEfector, MesesEnum mesEnum, int anio) {
        RegistroMensualDto registroMensualDto = new RegistroMensualDto();

        registroMensualDto.setMes(mesEnum);
        registroMensualDto.setAnio(anio);
        registroMensualDto.setIdAsistencial(idAsistencial);
        registroMensualDto.setIdEfector(idEfector);

        ResponseEntity<?> respuesta = create(registroMensualDto);

        if (respuesta.getStatusCode() == HttpStatus.OK) {

            RegistroMensual registroMensual = registroMensualService
                    .findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial, idEfector, mesEnum, anio)
                    .get();

            return registroMensual.getId();
        } else {
            return null;
        }
    }

    public void setRegistroMensual(RegistroActividad registroActividad) {

        Long idAsistencial = registroActividad.getAsistencial().getId();
        Long idEfector = registroActividad.getEfector().getId();
        int mes = registroActividad.getFechaIngreso().getMonth().getValue();
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(mes);
        int anio = registroActividad.getFechaIngreso().getYear();
        Long id;

        try {
            RegistroMensual registroMensual = registroMensualService
                    .findByAsistencialIdAndEfectorIdAndMesAndAnio(idAsistencial, idEfector, mesEnum, anio)
                    .get();
            id = registroMensual.getId();
        } catch (Exception exception) {
            System.out.println("id no encontrado");
            id = createRegistroMensual(idAsistencial, idEfector, mesEnum, anio);
        }

        try {
            registroActividad.setRegistroMensual(registroMensualService.findById(id).get());
            registroActividadService.save(registroActividad);
        } catch (Exception e) {
            System.out.println("error: idRegistroMensual nulo -- " + e.getMessage());
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
