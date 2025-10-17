package com.guardias.backend.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.guardias.backend.dto.FacturaDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.factura.FacturaDetailDto;
import com.guardias.backend.dto.factura.FacturaSummaryDto;
import com.guardias.backend.entity.Factura;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;
import com.guardias.backend.service.FacturaService;

@RestController
@RequestMapping("/factura")
@CrossOrigin(origins = "http://localhost:4200")
public class FacturaController {

    @Autowired
    FacturaService facturaService;

    @Value("${app.upload.dir}")
    private String uploadDir;

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

        ResponseEntity<?> respuestaValidaciones = facturaService
                .validations(facturaDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            // PRIMERO: Validar completitud de facturas
            ResponseEntity<?> validacionCompletitud = facturaService.validarCompletitudAntesDeGuardar(facturaDto);
            if (validacionCompletitud.getStatusCode() != HttpStatus.OK) {
                return validacionCompletitud;
            }

            // SEGUNDO: Crear y guardar
            Factura factura = facturaService.createUpdate(new Factura(), facturaDto);
            facturaService.save(factura);

            // TERCERO: Actualizar estado de registros
            facturaService.actualizarEstadoFacturasDespuesDeGuardar(factura);

            return new ResponseEntity(new Mensaje("Factura creada"), HttpStatus.OK);
        }
        return respuestaValidaciones;
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

    @PostMapping("/uploadPdf/{facturaId}")
    public ResponseEntity<?> uploadPdf(@PathVariable("facturaId") Long facturaId,
            @RequestParam("pdf") MultipartFile file) {

        if (!facturaService.existsById(facturaId)) {
            return new ResponseEntity<>(new Mensaje("Factura no encontrada"), HttpStatus.NOT_FOUND);
        }

        if (file.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("No se seleccionó ningún archivo"), HttpStatus.BAD_REQUEST);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            return new ResponseEntity<>(new Mensaje("El archivo debe ser un PDF"), HttpStatus.BAD_REQUEST);
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            return new ResponseEntity<>(new Mensaje("El archivo no puede ser mayor a 10MB"), HttpStatus.BAD_REQUEST);
        }

        try {
            Factura factura = facturaService.findById(facturaId).get();

            // Fecha a usar (fechaEmision de la factura o hoy)
            java.time.LocalDate fecha = factura.getFechaEmision() != null ? factura.getFechaEmision()
                    : java.time.LocalDate.now();
            int año = fecha.getYear();
            int mes = fecha.getMonthValue();

            // Nombre del mes en español en mayúsculas
            String[] meses = new DateFormatSymbols(new Locale("es")).getMonths();
            String nombreMes = meses[mes - 1].toUpperCase();

            // Nombre y DNI para la carpeta (limpio)
            String dni = String.valueOf(factura.getDniTitular());
            String rawNombre = factura.getNombreTitular() != null ? factura.getNombreTitular().trim() : "";
            String rawApellido = factura.getApellidoTitular() != null ? factura.getApellidoTitular().trim() : "";

            String primerNombre = "Titular";
            if (!rawNombre.isBlank()) {
                String[] tokens = rawNombre.split("\\s+");
                if (tokens.length > 0 && !tokens[0].isBlank()) {
                    primerNombre = capitalize(tokens[0]);
                }
            }
            String apellido = !rawApellido.isBlank() ? capitalize(rawApellido) : "Titular";

            // Construir como Apellido + PrimerNombre - DNI (ej: SotoJose-23931731)
            String personaFolder = (apellido + primerNombre + "-" + dni)
                    .replaceAll("[^A-Za-z0-9\\-]", ""); // conservar letras, números y guión
            if (personaFolder.isBlank()) {
                personaFolder = "Titular-" + dni;
            }

            // Rutas: uploadDir/Facturas/{año}/{personaFolder}/{MES}/
            Path facturasDir = Paths.get(uploadDir, "Facturas");
            Path añoDir = facturasDir.resolve(String.valueOf(año));
            Path personaDir = añoDir.resolve(personaFolder);
            Path mesDir = personaDir.resolve(nombreMes);

            if (!Files.exists(facturasDir))
                Files.createDirectories(facturasDir);
            if (!Files.exists(añoDir))
                Files.createDirectories(añoDir);
            if (!Files.exists(personaDir))
                Files.createDirectories(personaDir);
            if (!Files.exists(mesDir))
                Files.createDirectories(mesDir);

            // Nombre único del archivo
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            String baseName = "factura";
            if (originalFilename != null && !originalFilename.isBlank()) {
                int dot = originalFilename.lastIndexOf('.');
                if (dot > 0) {
                    baseName = originalFilename.substring(0, dot);
                    extension = originalFilename.substring(dot);
                } else {
                    baseName = originalFilename;
                }
            }
            baseName = baseName.replaceAll("[^a-zA-Z0-9_\\-]", "_").replaceAll("_+", "_").replaceAll("^_|_$", "");
            if (baseName.isBlank())
                baseName = "factura";
            String timestamp = String.valueOf(System.currentTimeMillis());
            String uniqueFilename = baseName + "_" + timestamp + extension;

            Path filePath = mesDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // URL relativa para almacenar en BD
            String pdfUrl = "/uploads/Facturas/" + año + "/" + personaFolder + "/" + nombreMes + "/" + uniqueFilename;
            factura.setUrl(pdfUrl);
            facturaService.save(factura);

            final String finalPdfUrl = pdfUrl;
            final String finalUniqueFilename = uniqueFilename;
            final String finalFullPath = filePath.toString();

            return new ResponseEntity<>(new Object() {
                public final String mensaje = "PDF subido y factura actualizada exitosamente";
                public final String url = finalPdfUrl;
                public final String filename = finalUniqueFilename;
                public final String fullPath = finalFullPath;
            }, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(new Mensaje("Error de IO al guardar el archivo: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error inesperado: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Pequeña función local para capitalizar correctamente
    // (primera letra mayúscula, resto minúsculas)
    // Nota: Java no permite funciones locales con declaración; usamos expresión
    // inline:
    // implementado arriba con capitalize(...) estilo in-place:
    // definir helper simple:
    // (si prefieres, extraer a método privado de la clase)
    // --- helper ---
    // private static String capitalize(String s) { ... }
    // Pero por simplicidad lo implementamos en línea abajo:
    // (Reemplazos ya aplicados arriba)
    private String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        s = s.toLowerCase();
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

}
