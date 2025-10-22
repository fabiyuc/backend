package com.guardias.backend.controller;

import java.util.ArrayList;
import java.util.List;
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

import com.guardias.backend.dto.AsistencialDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asistencial.AsistencialDetailDto;
import com.guardias.backend.dto.asistencial.AsistencialEfectorDto;
import com.guardias.backend.dto.asistencial.AsistencialEfectorRegistroActividadDto;
import com.guardias.backend.dto.asistencial.AsistencialListDto;
import com.guardias.backend.dto.asistencial.AsistencialListForLegajosDto;
import com.guardias.backend.dto.asistencial.AsistencialSummaryDto;
import com.guardias.backend.dto.asistencial.AsistencialTiposGuardiasDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.service.AsistencialService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.RegistrosPendientesService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/asistencial")
@CrossOrigin(origins = "http://localhost:4200")
public class AsistencialController {

    @Autowired
    AsistencialService asistencialService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    RegistrosPendientesService registrosPendientesService;

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

    @GetMapping("/listAsistencialByEfector/{idEfector}")
    public ResponseEntity<List<AsistencialEfectorRegistroActividadDto>> listAsistencialByEfector(
            @PathVariable Long idEfector) {
        List<AsistencialEfectorRegistroActividadDto> asistencialList = asistencialService
                .findAsistencialByEfectorAndActivoTrue(idEfector);
        List<AsistencialEfectorRegistroActividadDto> listaFiltrada = new ArrayList<>();

        for (AsistencialEfectorRegistroActividadDto asistencial : asistencialList) {
            List<RegistroActividad> activeRegActividades = new ArrayList<>();
            for (RegistroActividad registroActividad : asistencial.getIdRegistrosActividades()) {
                if (registroActividad.isActivo()) {
                    activeRegActividades.add(registroActividad);
                }
            }
            asistencial.setIdRegistrosActividades(activeRegActividades);
            listaFiltrada.add(asistencial);
        }
        return new ResponseEntity<>(listaFiltrada, HttpStatus.OK);
    }

    @GetMapping("/listAllByEfector/{idEfector}")
    public ResponseEntity<List<Asistencial>> listAllByEfector(@PathVariable Long idEfector) {
        List<Asistencial> asistencial = asistencialService.findByEfectorAndActivoTrue(idEfector);
        return ResponseEntity.ok(asistencial);
    }

    @GetMapping("/listAsistencialByCargaHorariaAndCategoria/{idEfector}/{idCargaHoraria}/{idCategoria}")
    public ResponseEntity<List<AsistencialEfectorDto>> listAsistencialByCargaHorariaAndCategoria(
            @PathVariable Long idEfector,
            @PathVariable Long idCargaHoraria,
            @PathVariable Long idCategoria) {

        // Llamada al servicio para obtener la lista filtrada
        List<AsistencialEfectorDto> asistenciales = asistencialService
                .findAsistencialByCargaHorariaAndCategoria(idEfector, idCargaHoraria, idCategoria);

        return ResponseEntity.ok(asistenciales);
    }

    @GetMapping("/listAsistencialSinLegajo")
    public ResponseEntity<List<AsistencialEfectorDto>> listAsistencialSinLegajo() {
        List<AsistencialEfectorDto> asistenciales = asistencialService.findAsistencialSinLegajo();
        return ResponseEntity.ok(asistenciales);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Asistencial>> listAll() {
        List<Asistencial> list = asistencialService.findAll();
        return new ResponseEntity<List<Asistencial>>(list, HttpStatus.OK);
    }

    // habilitaciones de guardias con el tipo de guardia EXTRA O CF
    @GetMapping("/listAsistencialesAndEfectorAndTG")
    public ResponseEntity<List<AsistencialSummaryDto>> getAsistencialesAndEfectorAndTG() {
        List<AsistencialSummaryDto> asistenciales = asistencialService.getAsistencialSummaryListTG();
        return new ResponseEntity<List<AsistencialSummaryDto>>(asistenciales, HttpStatus.OK);
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

    // Lista de asistenciales que son autoridades, con sus datos personales
    @GetMapping("/listAutoridadesByEfector/{idEfector}")
    public ResponseEntity<List<AsistencialListDto>> listAutoridadesByEfector(@PathVariable Long idEfector) {
        List<AsistencialListDto> autoridades = asistencialService.getAsistencialListAutoridadesByEfector(idEfector);
        return ResponseEntity.ok(autoridades);
    }

    // Lista de asistenciales que son autoridades regionales
    @GetMapping("/listAutoridadesRegionalesByEfector/{idEfector}")
    public ResponseEntity<List<AsistencialListDto>> listAutoridadesRegionalesByEfector(@PathVariable Long idEfector) {
        List<AsistencialListDto> autoridades = asistencialService
                .getAsistencialListAutoridadesRegionalesByEfector(idEfector);
        return ResponseEntity.ok(autoridades);
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

    // Lista asistenciales segun efector que tengan tipoGuardia CARGO Y/O AGRUPACION
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

    // Lista asistenciales segun efector y tipoGuardia
    @GetMapping("/listByEfectorAndTG/{idEfector}/{tipoGuardia}")
    public ResponseEntity<List<AsistencialSummaryDto>> getAsistencialesByEfectorAndTG(
            @PathVariable Long idEfector,
            @PathVariable String tipoGuardia) {

        List<AsistencialSummaryDto> asistenciales = asistencialService.getAsistencialesByEfectorAndTG(idEfector,
                tipoGuardia);
        return new ResponseEntity<>(asistenciales, HttpStatus.OK);
    }

    @GetMapping("/ConPendientes/{idEfector}/{tipoGuardia}")
    public ResponseEntity<List<AsistencialSummaryDto>> getConPendientes(
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("tipoGuardia") String tipoGuardia) {

        List<AsistencialSummaryDto> asistenciales = registrosPendientesService.findConPendientes(idEfector,
                tipoGuardia);

        return new ResponseEntity<>(asistenciales, HttpStatus.OK);
    }

    @GetMapping("/asistencialesConPendientes/{idEfector}/{mes}/{anio}/{idTipoGuardia}")
    public ResponseEntity<List<AsistencialSummaryDto>> getAsistencialesConPendientes(
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("mes") @Min(1) @Max(12) int mes,
            @PathVariable("anio") @Min(1900) @Max(2200) int anio,
            @PathVariable("idTipoGuardia") Long idTipoGuardia) {

        List<AsistencialSummaryDto> asistenciales = registrosPendientesService.findAsistencialesConPendientes(idEfector,
                mes, anio, idTipoGuardia);

        return new ResponseEntity<>(asistenciales, HttpStatus.OK);
    }

    // NO VA, NO VIENE AL CASO PORQUE NO HAY Q BUSCAR EN LA LISTA DE ASISTENCIALES
    // SINO EN LA LISTA DE HABILIT EN GUARDIAS EXTRAS
    @GetMapping("/listByEfectorAndTipoGuardiaExtraHabilitado/{idEfector}")
    public ResponseEntity<List<AsistencialSummaryDto>> listByEfectorAndTipoGuardiaExtraHabilitado(
            @PathVariable Long idEfector) {
        List<AsistencialSummaryDto> asistenciales = asistencialService
                .getAsistencialesByEfectorAndTipoGuardiaExtraHabilitado(idEfector);

        if (asistenciales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(asistenciales, HttpStatus.OK);
    }

    @GetMapping("/listByEfector/{idEfector}")
    public ResponseEntity<List<AsistencialEfectorDto>> getAsistencialesByEfector(
            @PathVariable("idEfector") Long idEfector) {
        List<AsistencialEfectorDto> asistenciales = asistencialService.getAsistencialesByEfector(idEfector);
        if (asistenciales.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(asistenciales, HttpStatus.OK);
    }

    //
    @GetMapping("/listAsistencialDetailByEfector/{idEfector}")
    public ResponseEntity<List<AsistencialDetailDto>> getAsistencialesDetailByEfector(
            @PathVariable("idEfector") Long idEfector) {
        List<AsistencialDetailDto> asistenciales = asistencialService.getAsistencialesDetailByEfector(idEfector);
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

    @GetMapping("/detailAsistencial/{id}")
    public ResponseEntity<AsistencialDetailDto> getByIdDetail(@PathVariable("id") Long id) {
        if (!asistencialService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la persona tipo asistencial"), HttpStatus.NOT_FOUND);
        List<AsistencialDetailDto> asistencial = asistencialService.findByIdDetail(id);
        return new ResponseEntity(asistencial, HttpStatus.OK);
    }

    @GetMapping("/detaildni/{dni}")
    public ResponseEntity<Asistencial> getByDni(@PathVariable("dni") int dni) {
        if (!asistencialService.existsByDniAndActivoTrue(dni))
            return new ResponseEntity(new Mensaje("no existe asistencial con ese dni"), HttpStatus.NOT_FOUND);
        Asistencial asistencial = asistencialService.findByDniAndActivoTrue(dni).get();
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
            Asistencial asistencial = asistencialService.createUpdate(asistencialService.findById(id).get(),
                    asistencialDto);
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

    @GetMapping("/esPlanta/{idAsistencial}/{idEfector}")
    public boolean esPlanta(@PathVariable("idAsistencial") long idAsistencial,
            @PathVariable("idEfector") long idEfector) {
        return asistencialService.esPlanta(idAsistencial, idEfector);
    }

    @GetMapping("/es-cargo-o-agrupacion/{idAsistencial}")
    public boolean esCargoOAgrupacion(@PathVariable Long idAsistencial) {
        return asistencialService.esCargoOAgrupacion(idAsistencial);
    }

    @GetMapping("/getTiposGuardias/{idAsistencial}")
    public ResponseEntity<List<AsistencialTiposGuardiasDto>> getTiposGuardias(
            @PathVariable("idAsistencial") Long idAsistencial) {

        List<AsistencialTiposGuardiasDto> tiposGuardias = asistencialService.obtenerTiposGuardia(idAsistencial);

        return new ResponseEntity<>(tiposGuardias, HttpStatus.OK);
    }

}