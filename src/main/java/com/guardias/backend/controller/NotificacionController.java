package com.guardias.backend.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.NotificacionDto;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Notificacion;
import com.guardias.backend.enums.TipoNotificacionEnum;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.NotificacionService;

@RestController
@RequestMapping("/notificacion")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificacionController {

    @Autowired
    NotificacionService notificacionService;
    @Autowired
    EfectorService efectorService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping("/list")
    public ResponseEntity<List<Notificacion>> list() {
        List<Notificacion> list = notificacionService.findByActivoTrue().get();
        return new ResponseEntity<List<Notificacion>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Notificacion>> listAll() {
        List<Notificacion> list = notificacionService.findAll();
        return new ResponseEntity<List<Notificacion>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Notificacion> getById(@PathVariable("id") Long id) {
        if (!notificacionService.activo(id))
            return new ResponseEntity(new Mensaje("no existe una notificación con ese nombre"), HttpStatus.NOT_FOUND);
        Notificacion notificacion = notificacionService.findById(id).get();
        return new ResponseEntity<Notificacion>(notificacion, HttpStatus.OK);
    }

    @GetMapping("/detailtipo/{tipo},{activo}")
    public ResponseEntity<List<Notificacion>> getByTipoAndActivo(@PathVariable("tipo") TipoNotificacionEnum tipo,
            @PathVariable("activo") boolean activo) {
        List<Notificacion> notificaciones = notificacionService.findByTipoAndActivo(tipo, activo);

        if (notificaciones.isEmpty()) {
            return new ResponseEntity(new Mensaje("No se encontraron notificaciones para ese tipo"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(notificaciones, HttpStatus.OK);
    }

    private ResponseEntity<?> validations(NotificacionDto notificacionDto) {
        if (notificacionDto.getTipo() == null)
            return new ResponseEntity<>(new Mensaje("El Tipo es obligatorio"), HttpStatus.BAD_REQUEST);

        if (notificacionDto.getCategoria() == null)
            return new ResponseEntity(new Mensaje("La Categoria es obligatoria"), HttpStatus.BAD_REQUEST);

        if (notificacionDto.getDetalle() == null)
            return new ResponseEntity(new Mensaje("El Detalle es obligatorio"), HttpStatus.BAD_REQUEST);

        if (notificacionDto.getFechaNotificacion() == null)
            return new ResponseEntity(new Mensaje("La Fecha de Notificacion es obligatoria"), HttpStatus.BAD_REQUEST);

        /*
         * if (notificacionDto.getIdEfectores() == null)
         * return new ResponseEntity(new Mensaje("El efector es obligatorio"),
         * HttpStatus.BAD_REQUEST);
         */

        return new ResponseEntity(new Mensaje("Valido"), HttpStatus.OK);
    }

    private Notificacion createUpdate(Notificacion notificacion, NotificacionDto notificacionDto) {

        if (notificacionDto.getTipo() != null && notificacion.getTipo() != notificacionDto.getTipo())
            notificacion.setTipo(notificacionDto.getTipo());

        if (notificacionDto.getCategoria() != null
                && !notificacionDto.getCategoria().equals(notificacion.getCategoria())
                && !notificacionDto.getCategoria().isEmpty())
            notificacion.setCategoria(notificacionDto.getCategoria());

        if (notificacionDto.getDetalle() != null && !notificacionDto.getDetalle().equals(notificacion.getDetalle())
                && !notificacionDto.getDetalle().isEmpty())
            notificacion.setDetalle(notificacionDto.getDetalle());

        if (notificacionDto.getUrl() != null && !notificacionDto.getUrl().equals(notificacion.getUrl())
                && !notificacionDto.getUrl().isEmpty())
            notificacion.setUrl(notificacionDto.getUrl());

        if (notificacionDto.getFechaNotificacion() != null
                && notificacionDto.getFechaNotificacion() != notificacion.getFechaNotificacion())
            notificacion.setFechaNotificacion(notificacionDto.getFechaNotificacion());

        if (notificacionDto.getFechaBaja() != null && notificacionDto.getFechaBaja() != notificacion.getFechaBaja())
            notificacion.setFechaBaja(notificacionDto.getFechaBaja());

        // NUEVO: reemplazo total de efectores (lista completa recibida)
        if (notificacionDto.getIdEfectores() != null) {
            // Limpiar relaciones previas si es update
            if (notificacion.getEfectores() != null) {
                for (Efector efPrev : notificacion.getEfectores()) {
                    if (efPrev.getNotificaciones() != null) {
                        efPrev.getNotificaciones().remove(notificacion);
                    }
                }
            }
            Set<Long> únicos = new HashSet<>();
            List<Efector> nuevos = new ArrayList<>();
            for (Long id : notificacionDto.getIdEfectores()) {
                if (id == null)
                    continue;
                if (únicos.add(id)) {
                    Efector ef = efectorService.findById(id);
                    if (ef != null && ef.isActivo()) {
                        nuevos.add(ef);
                    }
                }
            }
            notificacion.setEfectores(nuevos);
            for (Efector ef : nuevos) {
                if (ef.getNotificaciones() == null) {
                    ef.setNotificaciones(new ArrayList<>());
                }
                if (!ef.getNotificaciones().contains(notificacion)) {
                    ef.getNotificaciones().add(notificacion);
                }
            }
        }

        notificacion.setActivo(true);
        return notificacion;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody NotificacionDto notificacionDto) {
        ResponseEntity<?> respuestaValidaciones = validations(notificacionDto);
        if (respuestaValidaciones.getStatusCode() != HttpStatus.OK) {
            return respuestaValidaciones;
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setTipo(notificacionDto.getTipo());
        notificacion.setCategoria(notificacionDto.getCategoria());
        notificacion.setFechaNotificacion(notificacionDto.getFechaNotificacion());
        notificacion.setDetalle(notificacionDto.getDetalle());
        notificacion.setUrl(notificacionDto.getUrl());
        notificacion.setActivo(true);
        notificacion.setFechaBaja(notificacionDto.getFechaBaja());

        List<Efector> efectores = new ArrayList<>();
        if (notificacionDto.getIdEfectores() != null && !notificacionDto.getIdEfectores().isEmpty()) {
            Set<Long> únicos = new HashSet<>();
            for (Long id : notificacionDto.getIdEfectores()) {
                if (id == null)
                    continue;
                if (únicos.add(id)) {
                    Efector ef = efectorService.findById(id);
                    if (ef != null && ef.isActivo()) {
                        efectores.add(ef);
                    }
                }
            }
        }
        notificacion.setEfectores(efectores);
        notificacionService.save(notificacion);
        for (Efector ef : efectores) {
            if (ef.getNotificaciones() == null) {
                ef.setNotificaciones(new ArrayList<>());
            }
            if (!ef.getNotificaciones().contains(notificacion)) {
                ef.getNotificaciones().add(notificacion);
            }
        }

        return new ResponseEntity<>(notificacion, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody NotificacionDto notificacionDto) {
        if (!notificacionService.existsById(id))
            return new ResponseEntity(new Mensaje("La notificación no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = validations(notificacionDto);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Notificacion notificacion = createUpdate(notificacionService.findById(id).get(), notificacionDto);
            notificacionService.save(notificacion);
            return new ResponseEntity(new Mensaje("Notificación actualizada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!notificacionService.activo(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);

        Notificacion notificacion = notificacionService.findById(id).get();
        notificacion.setActivo(false);
        notificacionService.save(notificacion);
        return new ResponseEntity<>(new Mensaje("Notificación eliminada"), HttpStatus.OK);

    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!notificacionService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        notificacionService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Notificación eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @PostMapping("/uploadPdf/{efectorId}")
    public ResponseEntity<?> uploadPdf(@PathVariable("efectorId") Long efectorId,
            @RequestParam("pdf") MultipartFile file,
            @RequestParam("notificacion") String notificacionJson) {
        Efector efectorPrincipal = efectorService.findById(efectorId);
        if (efectorPrincipal == null || !efectorPrincipal.isActivo()) {
            return new ResponseEntity<>(new Mensaje("Efector no encontrado o inactivo"), HttpStatus.NOT_FOUND);
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
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            NotificacionDto notificacionDto = mapper.readValue(notificacionJson, NotificacionDto.class);

            ResponseEntity<?> validaciones = validations(notificacionDto);
            if (validaciones.getStatusCode() != HttpStatus.OK) {
                return validaciones;
            }

            // Obtener fecha actual o la de la notificación
            LocalDate fecha = notificacionDto.getFechaNotificacion() != null
                    ? notificacionDto.getFechaNotificacion()
                    : LocalDate.now();
            int año = fecha.getYear();
            int mes = fecha.getMonthValue();

            // Obtener nombre del mes en español y en mayúsculas
            String[] meses = new DateFormatSymbols(new java.util.Locale("es")).getMonths();
            String nombreMes = meses[mes - 1].toUpperCase();
            String carpetaMes = nombreMes + "-" + año;

            // Carpeta: /uploads/notificaciones/{año}/{MES-AÑO}/
            Path notificacionesDir = Paths.get(uploadDir, "notificaciones");
            Path añoDir = notificacionesDir.resolve(String.valueOf(año));
            Path mesDir = añoDir.resolve(carpetaMes);

            if (!Files.exists(notificacionesDir))
                Files.createDirectories(notificacionesDir);
            if (!Files.exists(añoDir))
                Files.createDirectories(añoDir);
            if (!Files.exists(mesDir))
                Files.createDirectories(mesDir);

            // Nombre único para el PDF
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            String baseName = "archivo";
            if (originalFilename != null && !originalFilename.isBlank()) {
                int dot = originalFilename.lastIndexOf('.');
                if (dot > 0) {
                    baseName = originalFilename.substring(0, dot);
                    extension = originalFilename.substring(dot);
                } else {
                    baseName = originalFilename;
                }
            }
            baseName = baseName
                    .replaceAll("[^a-zA-Z0-9_\\-]", "_")
                    .replaceAll("_+", "_")
                    .replaceAll("^_|_$", "");
            if (baseName.isBlank())
                baseName = "archivo";
            String timestamp = String.valueOf(System.currentTimeMillis());
            String uniqueFilename = baseName + "_" + timestamp + extension;

            Path filePathTmp = mesDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePathTmp, StandardCopyOption.REPLACE_EXISTING);

            // URL para guardar en la notificación
            String pdfUrlTmp = "/uploads/notificaciones/" + año + "/" + carpetaMes + "/" + uniqueFilename;

            Notificacion notificacion = new Notificacion();
            notificacion.setTipo(notificacionDto.getTipo());
            notificacion.setCategoria(notificacionDto.getCategoria());
            notificacion.setFechaNotificacion(notificacionDto.getFechaNotificacion());
            notificacion.setDetalle(notificacionDto.getDetalle());
            notificacion.setUrl(pdfUrlTmp);
            notificacion.setActivo(true);
            notificacion.setFechaBaja(notificacionDto.getFechaBaja());

            List<Efector> efectores = new ArrayList<>();
            Set<Long> unicos = new HashSet<>();
            if (notificacionDto.getIdEfectores() != null && !notificacionDto.getIdEfectores().isEmpty()) {
                for (Long id : notificacionDto.getIdEfectores()) {
                    if (id == null)
                        continue;
                    if (unicos.add(id)) {
                        Efector ef = efectorService.findById(id);
                        if (ef != null && ef.isActivo()) {
                            efectores.add(ef);
                        }
                    }
                }
            }
            if (unicos.add(efectorId)) {
                efectores.add(efectorPrincipal);
            }

            notificacion.setEfectores(efectores);
            notificacionService.save(notificacion);
            for (Efector ef : efectores) {
                if (ef.getNotificaciones() == null) {
                    ef.setNotificaciones(new ArrayList<>());
                }
                if (!ef.getNotificaciones().contains(notificacion)) {
                    ef.getNotificaciones().add(notificacion);
                }
            }

            final String finalCarpetaMes = carpetaMes;
            final String finalUniqueFilename = uniqueFilename;
            final String finalPdfUrl = pdfUrlTmp;
            final String finalFilePath = filePathTmp.toString();

            return new ResponseEntity<>(new Object() {
                public final String mensaje = "PDF subido y notificación creada exitosamente";
                public final String url = finalPdfUrl;
                public final String filename = finalUniqueFilename;
                public final String mesFolder = finalCarpetaMes;
                public final String fullPath = finalFilePath;
                public final Long notificacionId = notificacion.getId();
                public final int efectoresAsociados = efectores.size();
            }, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al guardar el PDF y la notificación: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
