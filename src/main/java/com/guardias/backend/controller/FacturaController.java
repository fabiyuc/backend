package com.guardias.backend.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.guardias.backend.dto.FacturaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.factura.FacturaDetailDto;
import com.guardias.backend.dto.factura.FacturaSummaryDto;
import com.guardias.backend.entity.Factura;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;
import com.guardias.backend.service.FacturaService;
import com.guardias.backend.service.RegistroMensualService;

@RestController
@RequestMapping("/factura")
@CrossOrigin(origins = "http://localhost:4200")
public class FacturaController {

    @Autowired
    FacturaService facturaService;
    @Autowired
    RegistroMensualService registroMensualService;

    @GetMapping("/list")
    public ResponseEntity<List<Factura>> list() {
        List<Factura> facturasList = facturaService.findByActivoTrue()
                .orElse(new ArrayList<>());

        return new ResponseEntity<List<Factura>>(facturasList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Factura>> listAll() {
        List<Factura> list = facturaService.findAll();
        return new ResponseEntity<List<Factura>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Factura> getById(@PathVariable("id") Long id) {
        if (!facturaService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la factura con ese id"),
                    HttpStatus.NOT_FOUND);
        Factura factura = facturaService.findById(id).get();
        return new ResponseEntity<Factura>(factura, HttpStatus.OK);
    }

    @GetMapping("/detailAsistencial/{idAsistencial}")
    public ResponseEntity<Factura> getByAsistencial(@PathVariable("idAsistencial") Long idAsistencial) {
        if (!facturaService.activoByAsistencial(idAsistencial))
            return new ResponseEntity(new Mensaje("no existe la factura de este asistencial"),
                    HttpStatus.NOT_FOUND);
        Factura factura = facturaService.findByAsistencial(idAsistencial).get();
        return new ResponseEntity<Factura>(factura, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody FacturaDto facturaDto) {
        try {
            System.out.println("=== INICIANDO CREACIÓN DE FACTURA ===");

            // 1. Validaciones básicas
            ResponseEntity<?> validacionesBasicas = facturaService.validations(facturaDto);
            if (validacionesBasicas.getStatusCode() != HttpStatus.OK) {
                System.out.println("Validaciones básicas fallaron");
                return validacionesBasicas;
            }
            System.out.println("✓ Validaciones básicas OK");

            // 2. Determinar periodoCarga (PRIMERA, SEGUNDA o FUERA_DE_TERMINO)
            QuincenaEnum periodoCarga = facturaService.determinarPeriodoCarga(facturaDto);
            System.out.println("✓ Periodo carga determinado: " + periodoCarga);

            // 3. Validar cantidad de facturas según periodoCarga
            ResponseEntity<?> validacionCantidad = facturaService.validarCantidadFacturas(facturaDto, periodoCarga);
            if (validacionCantidad.getStatusCode() != HttpStatus.OK) {
                System.out.println("Validación cantidad falló");
                return validacionCantidad;
            }
            System.out.println("✓ Validación cantidad OK");

            // 4. Validar montos según periodoCarga
            ResponseEntity<?> validacionMontos = facturaService.validarMontosFacturas(facturaDto, periodoCarga);
            if (validacionMontos.getStatusCode() != HttpStatus.OK) {
                System.out.println("Validación montos falló");
                return validacionMontos;
            }
            System.out.println("✓ Validación montos OK");

            // 5. Crear y guardar factura
            Factura factura = facturaService.createUpdate(new Factura(), facturaDto);
            facturaService.save(factura);
            System.out.println("✓ Factura creada y guardada - ID: " + factura.getId());

            // 6. Actualizar estado de facturación según periodoCarga
            facturaService.actualizarEstadoFacturacion(factura, periodoCarga);
            System.out.println("✓ Estado de facturación actualizado");

            String mensaje = "Factura creada exitosamente (" + periodoCarga + ")";
            return new ResponseEntity(new Mensaje(mensaje), HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("❌ Error creando factura: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity(new Mensaje("Error creando factura: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id,
            @RequestBody FacturaDto facturaDto) {
        if (!facturaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe la factura"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = facturaService
                .validations(facturaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Factura factura = facturaService
                    .createUpdate(facturaService.findById(id).get(), facturaDto);
            facturaService.save(factura);
            return new ResponseEntity(new Mensaje("factura modificada"), HttpStatus.OK);
        }
        return respuestaValidaciones;
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!facturaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        // 1. Obtener la factura antes de eliminarla
        Factura factura = facturaService.findById(id).get();

        // 2. Guardar referencia a los registros mensuales afectados
        List<RegistroMensual> registrosAfectados = factura.getRegistrosMensuales();

        // 3. Realizar el borrado lógico
        factura.setActivo(false);
        facturaService.save(factura);

        // 4. Actualizar el estado de facturasCompletas para los registros afectados
        facturaService.actualizarEstadoFacturasDespuesDeEliminar(registrosAfectados);

        return new ResponseEntity<>(new Mensaje("factura  eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!facturaService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        // 1. Obtener la factura antes de eliminarla
        Factura factura = facturaService.findById(id).get();

        // 2. Guardar referencia a los registros mensuales afectados
        List<RegistroMensual> registrosAfectados = factura.getRegistrosMensuales();

        // 3. Realizar el borrado
        facturaService.deleteById(id);

        // 4. Actualizar el estado de facturasCompletas para los registros afectados
        facturaService.actualizarEstadoFacturasDespuesDeEliminar(registrosAfectados);

        return new ResponseEntity<>(new Mensaje("factura eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/getMontoByQuincena/{idAsistencial}/{idEfector}/{quincena}/{mes}/{anio}")
    public ResponseEntity<?> getMontoByQuincena(
            @PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("quincena") String quincena,
            @PathVariable("mes") String mes,
            @PathVariable("anio") int anio) {

        QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());
        MesesEnum mesEnum = MesesEnum.valueOf(mes.toUpperCase());

        try {
            BigDecimal monto = facturaService.getMontoByQuincena(idAsistencial, idEfector, quincenaEnum, mesEnum, anio);

            return new ResponseEntity<>(monto, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Mensaje("Error al obtener el monto " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getByAsistencialAndFiltros/{idAsistencial}")
    public ResponseEntity<Factura> ByAsistencial(
            @PathVariable("idAsistencial") Long idAsistencial) {
        if (!facturaService.activoByAsistencial(idAsistencial))
            return new ResponseEntity(new Mensaje("no existe la factura de este asistencial"),
                    HttpStatus.NOT_FOUND);
        Factura factura = facturaService.findByAsistencial(idAsistencial).get();
        return new ResponseEntity<Factura>(factura, HttpStatus.OK);
    }

    @GetMapping("/listSummary/{idEfector}/{anio}/{mes}/{quincena}")
    public ResponseEntity<?> listSummary(
            @PathVariable("idEfector") int idEfector,
            @PathVariable int anio,
            @PathVariable("mes") String mes,
            @PathVariable("quincena") String quincena) {

        try {
            QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());
            MesesEnum mesEnum = MesesEnum.valueOf(mes.toUpperCase());
            List<FacturaSummaryDto> facturas = facturaService.getFacturasByAnioMesQuincena(idEfector, anio, mesEnum,
                    quincenaEnum);
            return ResponseEntity.ok(facturas);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new Mensaje("Error al obtener facturas: " + e.getMessage()));
        }
    }

    @GetMapping("/getByFiltros/{idAsistencial}/{idEfector}/{anio}/{mes}/{quincena}")
    public ResponseEntity<?> getFacturasByFiltros(
            @PathVariable("idAsistencial") Long idAsistencial,
            @PathVariable("idEfector") Long idEfector,
            @PathVariable("anio") int anio,
            @PathVariable("mes") String mes,
            @PathVariable("quincena") String quincena) {

        try {
            QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());
            MesesEnum mesEnum = MesesEnum.valueOf(mes.toUpperCase());
            List<FacturaDetailDto> facturas = facturaService.getByFiltros(idAsistencial, idEfector, anio, mesEnum,
                    quincenaEnum);
            return ResponseEntity.ok(facturas);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new Mensaje("Error al obtener facturas: " + e.getMessage()));
        }
    }

    @GetMapping("/existeFactura/{idAsistencial}/{idEfector}/{anio}/{mes}/{quincena}")
    public ResponseEntity<?> existeFactura(
            @PathVariable Long idAsistencial,
            @PathVariable Long idEfector,
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable String quincena) {

        try {
            QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());
            MesesEnum mesEnum = MesesEnum.valueOf(mes.toUpperCase());

            boolean existe = facturaService.existeFacturaByFiltros(idAsistencial, idEfector, anio, mesEnum,
                    quincenaEnum);
            return ResponseEntity.ok(existe);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new Mensaje("Parámetro no válido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new Mensaje("Error al verificar factura: " + e.getMessage()));
        }
    }

    @GetMapping("/existenDosFacturas/{idAsistencial}/{idEfector}/{anio}/{mes}/{quincena}")
    public ResponseEntity<?> existenDosFacturas(
            @PathVariable Long idAsistencial,
            @PathVariable Long idEfector,
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable String quincena) {

        try {
            QuincenaEnum quincenaEnum = QuincenaEnum.valueOf(quincena.toUpperCase());
            MesesEnum mesEnum = MesesEnum.valueOf(mes.toUpperCase());

            boolean existenDosFacturas = facturaService.existenDosFacturasByFiltros(
                    idAsistencial, idEfector, anio, mesEnum, quincenaEnum);

            return ResponseEntity.ok(existenDosFacturas);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new Mensaje("Parámetro no válido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new Mensaje("Error al verificar facturas: " + e.getMessage()));
        }
    }

    @GetMapping("/existeFacturaSinQuincena/{idAsistencial}/{idEfector}/{anio}/{mes}")
    public ResponseEntity<?> existeFacturaSinQuincena(
            @PathVariable Long idAsistencial,
            @PathVariable Long idEfector,
            @PathVariable int anio,
            @PathVariable String mes) {

        try {
            MesesEnum mesEnum = MesesEnum.valueOf(mes.toUpperCase());

            boolean existe = facturaService.existeFacturaByFiltrosSinQuincena(idAsistencial, idEfector, anio, mesEnum);
            return ResponseEntity.ok(existe);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new Mensaje("Parámetro no válido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new Mensaje("Error al verificar factura: " + e.getMessage()));
        }
    }

    @GetMapping("/existenDosFacturasSinQuincena/{idAsistencial}/{idEfector}/{anio}/{mes}/{quincena}")
    public ResponseEntity<?> existenDosFacturasSinQuincena(
            @PathVariable Long idAsistencial,
            @PathVariable Long idEfector,
            @PathVariable int anio,
            @PathVariable String mes) {

        try {
            MesesEnum mesEnum = MesesEnum.valueOf(mes.toUpperCase());

            boolean existenDosFacturas = facturaService.existenDosFacturasByFiltrosSinQuincena(
                    idAsistencial, idEfector, anio, mesEnum);

            return ResponseEntity.ok(existenDosFacturas);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new Mensaje("Parámetro no válido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new Mensaje("Error al verificar facturas: " + e.getMessage()));
        }
    }

}
