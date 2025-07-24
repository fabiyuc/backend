package com.guardias.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

import com.guardias.backend.dto.LegajoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.legajo.LegajoActualDto;
import com.guardias.backend.dto.legajo.LegajoBajaDto;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.service.LegajoService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/legajo")
@CrossOrigin(origins = "http://localhost:4200")
public class LegajoController {

    // Configuración del directorio donde se guardarán las imágenes
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    LegajoService legajoService;

    @GetMapping("/list")
    public ResponseEntity<List<Legajo>> list() {
        List<Legajo> list = legajoService.findByActivoTrue();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Legajo>> listAll() {
        List<Legajo> list = legajoService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Legajo>> getById(@PathVariable("id") Long id) {
        if (!legajoService.activo(id))
            return new ResponseEntity(new Mensaje("No existe el legajo"),
                    HttpStatus.NOT_FOUND);
        Legajo legajo = legajoService.findById(id).get();
        return new ResponseEntity(legajo, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody LegajoDto legajoDto) {
        ResponseEntity<?> respuestaValidaciones = legajoService.validations(legajoDto, 0L);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Legajo legajo = legajoService.createUpdate(new Legajo(), legajoDto);
            legajoService.save(legajo);

            return new ResponseEntity<>(legajo, HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody LegajoDto legajoDto) {
        if (!legajoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = legajoService.validations(legajoDto, id);
        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Legajo legajo = legajoService.createUpdate(legajoService.findById(id).get(), legajoDto);
            legajoService.save(legajo);

            return new ResponseEntity<>(new Mensaje("Legajo modificado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id, @RequestBody @Valid LegajoBajaDto legajoBajaDto) {

        try {
            // Verifica que los valores requeridos estén presentes
            if (legajoBajaDto.getMotivoBaja() == null || legajoBajaDto.getMotivoBaja().isBlank()) {
                return new ResponseEntity<>(new Mensaje("El motivo de la baja es obligatorio"), HttpStatus.BAD_REQUEST);
            }
            if (legajoBajaDto.getFechaFinal() == null) {
                return new ResponseEntity<>(new Mensaje("La fecha final es obligatoria"), HttpStatus.BAD_REQUEST);
            }

            legajoService.logicDelete(id, legajoBajaDto);
            return new ResponseEntity<>(new Mensaje("Legajo dado de baja lógicamente"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!legajoService.existsById(id))
            return new ResponseEntity<>(new Mensaje("no existe el legajo"), HttpStatus.NOT_FOUND);
        legajoService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("legajo eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/tieneTipoGuardiaPermitido/{idPersona}")
    public ResponseEntity<Boolean> tieneTipoGuardiaPermitido(@PathVariable Long idPersona) {
        boolean tipoGuardiaValido = legajoService.tieneTipoGuardiaPermitido(idPersona);
        return new ResponseEntity<>(tipoGuardiaValido, HttpStatus.OK);
    }

    @GetMapping("/listByAsistencial/{idAsistencial}")
    public ResponseEntity<List<LegajoActualDto>> listByAsistencial(@PathVariable("idAsistencial") Long idAsistencial) {
        List<LegajoActualDto> legajos = legajoService.getLegajosByAsistencial(idAsistencial);
        return new ResponseEntity<>(legajos, HttpStatus.OK);
    }

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long id,
            @RequestParam("image") MultipartFile file) {

        // Validar que el legajo existe
        if (!legajoService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("Legajo no encontrado"), HttpStatus.NOT_FOUND);
        }

        // Validar que se envió un archivo
        if (file.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("No se seleccionó ningún archivo"), HttpStatus.BAD_REQUEST);
        }

        // Validar tipo de archivo (solo imágenes)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return new ResponseEntity<>(new Mensaje("El archivo debe ser una imagen"), HttpStatus.BAD_REQUEST);
        }

        // Validar tamaño del archivo (máximo 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            return new ResponseEntity<>(new Mensaje("El archivo no puede ser mayor a 5MB"), HttpStatus.BAD_REQUEST);
        }

        try {
            Legajo legajo = legajoService.findById(id).get();
            String apellido = legajo.getPersona().getApellido();
            String nombre = legajo.getPersona().getNombre();
            int dni = legajo.getPersona().getDni();

            String mainFolderName = (apellido + "_" + nombre + "_" + dni)
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim();

            // 🔥 CALCULAR HASH MD5 DEL ARCHIVO PARA VERIFICACIÓN GLOBAL
            String fileHash = legajoService.calculateMD5(file.getInputStream());
            System.out.println("🔍 Hash del archivo a subir: " + fileHash);

            // 🔥 1. VERIFICAR SI EL LEGAJO YA TIENE ESTA IMAGEN (MISMA URL)
            if (legajo.getUrl() != null && !legajo.getUrl().isEmpty()) {
                String existingImagePath = uploadDir + legajo.getUrl().replace("/uploads/", "");
                Path existingFile = Paths.get(existingImagePath);

                if (Files.exists(existingFile)) {
                    try {
                        String existingHash = legajoService.calculateMD5(Files.newInputStream(existingFile));
                        if (existingHash.equals(fileHash)) {
                            System.out.println("✅ La imagen es idéntica a la actual del legajo");

                            final String currentFilename = existingFile.getFileName().toString();
                            final String currentFolderName = existingFile.getParent().getFileName().toString();
                            final String currentUrl = legajo.getUrl();

                            return new ResponseEntity<>(new Object() {
                                public final String mensaje = "La imagen ya está asociada a este legajo";
                                public final String url = currentUrl;
                                public final String filename = currentFilename;
                                public final String folderName = currentFolderName;
                                public final boolean isDuplicate = true;
                                public final boolean isSameImage = true;
                                public final String existingFile = currentFilename;
                            }, HttpStatus.OK);
                        }
                    } catch (Exception e) {
                        System.err.println("⚠️ Error al verificar imagen actual: " + e.getMessage());
                    }
                }
            }

            // 🔥 2. BUSCAR IMAGEN EXISTENTE EN TODAS LAS CARPETAS DE LA PERSONA
            Path mainDir = Paths.get(uploadDir, "legajos", mainFolderName);
            String existingImageUrl = findExistingImageByHash(mainDir, fileHash, mainFolderName);

            if (existingImageUrl != null) {
                System.out.println("🔍 Imagen encontrada en carpeta existente: " + existingImageUrl);

                // 🔥 ACTUALIZAR URL EN BD SIN DUPLICAR ARCHIVO
                legajo.setUrl(existingImageUrl);
                legajoService.save(legajo);

                System.out.println("✅ URL reutilizada y actualizada en BD: " + existingImageUrl);

                final String reusedFilename = Paths.get(existingImageUrl).getFileName().toString();
                final String reusedUrl = existingImageUrl;
                final String reusedHash = fileHash;

                return new ResponseEntity<>(new Object() {
                    public final String mensaje = "Imagen reutilizada de carpeta existente";
                    public final String url = reusedUrl;
                    public final String filename = reusedFilename;
                    public final String folderName = "reutilizada";
                    public final boolean isDuplicate = true;
                    public final boolean isReused = true;
                    public final String hash = reusedHash;
                }, HttpStatus.OK);
            }

            // 🔥 3. SI NO EXISTE, CREAR NUEVA CARPETA Y SUBIR IMAGEN
            String personaName = nombre + "_" + apellido;
            String legajoFolderName = personaName
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim()
                    + "_legajo_" + id;

            Path legajosDir = Paths.get(uploadDir, "legajos");
            Path legajoDir = mainDir.resolve(legajoFolderName);

            // Crear directorios necesarios
            if (!Files.exists(legajosDir)) {
                Files.createDirectories(legajosDir);
            }
            if (!Files.exists(mainDir)) {
                Files.createDirectories(mainDir);
            }
            if (!Files.exists(legajoDir)) {
                Files.createDirectories(legajoDir);
            }

            // 🔥 CREAR NOMBRE ÚNICO PARA EL ARCHIVO
            String originalFilename = file.getOriginalFilename();
            String extension = ".png";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String timestamp = String.valueOf(System.currentTimeMillis());
            String cleanPersonaName = personaName
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim();

            String uniqueFilename = "sello_" + cleanPersonaName + "_" + timestamp + extension;

            // 🔥 GUARDAR ARCHIVO NUEVO
            Path filePath = legajoDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("💾 Nuevo archivo guardado en: " + filePath.toString());

            // 🔥 ACTUALIZAR URL EN BD
            String imageUrl = "/uploads/legajos/" + mainFolderName + "/" + legajoFolderName + "/" + uniqueFilename;
            legajo.setUrl(imageUrl);
            legajoService.save(legajo);

            System.out.println("✅ Nueva URL guardada en BD: " + imageUrl);

            final String finalImageUrl = imageUrl;
            final String finalUniqueFilename = uniqueFilename;
            final String finalLegajoFolderName = legajoFolderName;
            final String finalFilePath = filePath.toString();
            final String finalFileHash = fileHash;

            return new ResponseEntity<>(new Object() {
                public final String mensaje = "Imagen subida exitosamente";
                public final String url = finalImageUrl;
                public final String filename = finalUniqueFilename;
                public final String folderName = finalLegajoFolderName;
                public final String fullPath = finalFilePath;
                public final boolean isDuplicate = false;
                public final boolean isNew = true;
                public final String hash = finalFileHash;
            }, HttpStatus.OK);

        } catch (IOException e) {
            System.err.println("❌ Error de IO: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(new Mensaje("Error al guardar el archivo: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            System.err.println("❌ Error general: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(new Mensaje("Error inesperado: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/checkDuplicate/{id}")
    public ResponseEntity<?> checkDuplicate(@PathVariable("id") Long id,
            @RequestParam("image") MultipartFile file) {

        if (!legajoService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("Legajo no encontrado"), HttpStatus.NOT_FOUND);
        }

        if (file.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("No se seleccionó ningún archivo"), HttpStatus.BAD_REQUEST);
        }

        try {
            Legajo legajo = legajoService.findById(id).get();
            String apellido = legajo.getPersona().getApellido();
            String nombre = legajo.getPersona().getNombre();
            int dni = legajo.getPersona().getDni();

            String mainFolderName = (apellido + "_" + nombre + "_" + dni)
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim();

            // 🔥 CALCULAR HASH DEL ARCHIVO
            String fileHash = legajoService.calculateMD5(file.getInputStream());
            System.out.println("🔍 Hash del archivo para verificación: " + fileHash);

            // 🔥 1. VERIFICAR SI ES LA IMAGEN ACTUAL DEL LEGAJO
            if (legajo.getUrl() != null && !legajo.getUrl().isEmpty()) {
                String existingImagePath = uploadDir + legajo.getUrl().replace("/uploads/", "");
                Path existingFile = Paths.get(existingImagePath);

                if (Files.exists(existingFile)) {
                    try {
                        String existingHash = legajoService.calculateMD5(Files.newInputStream(existingFile));
                        if (existingHash.equals(fileHash)) {
                            final String currentFilename = existingFile.getFileName().toString();
                            final String currentFolderName = existingFile.getParent().getFileName().toString();
                            final String currentUrl = legajo.getUrl();

                            return new ResponseEntity<>(new Object() {
                                public final String mensaje = "Esta imagen ya está asociada a este legajo";
                                public final String url = currentUrl;
                                public final String filename = currentFilename;
                                public final String folderName = currentFolderName;
                                public final boolean isDuplicate = true;
                                public final boolean isSameImage = true;
                                public final String existingFile = currentFilename;
                            }, HttpStatus.OK);
                        }
                    } catch (Exception e) {
                        System.err.println("⚠️ Error al verificar imagen actual: " + e.getMessage());
                    }
                }
            }

            // 🔥 2. BUSCAR EN TODAS LAS CARPETAS DE LA PERSONA
            Path mainDir = Paths.get(uploadDir, "legajos", mainFolderName);
            String existingImageUrl = findExistingImageByHash(mainDir, fileHash, mainFolderName);

            if (existingImageUrl != null) {
                Path existingPath = Paths.get(uploadDir + existingImageUrl.replace("/uploads/", ""));
                final String duplicateFolderName = existingPath.getParent().getFileName().toString();
                final String duplicateFileName = existingPath.getFileName().toString();
                final String duplicateUrl = existingImageUrl;

                return new ResponseEntity<>(new Object() {
                    public final String mensaje = "Esta imagen ya existe en otra carpeta de legajo";
                    public final String url = duplicateUrl;
                    public final String filename = duplicateFileName;
                    public final String folderName = duplicateFolderName;
                    public final boolean isDuplicate = true;
                    public final boolean isInOtherFolder = true;
                    public final String existingFile = duplicateFileName;
                }, HttpStatus.OK);
            }

            // 🔥 3. NO HAY DUPLICADOS
            return new ResponseEntity<>(new Object() {
                public final boolean isDuplicate = false;
                public final String message = "Imagen nueva, se puede subir";
            }, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("❌ Error al verificar duplicado: " + e.getMessage());
            return new ResponseEntity<>(new Mensaje("Error al verificar duplicado: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") Long id) {

        if (!legajoService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("Legajo no encontrado"), HttpStatus.NOT_FOUND);
        }

        try {
            Legajo legajo = legajoService.findById(id).get();

            if (legajo.getUrl() != null && !legajo.getUrl().isEmpty()) {
                String urlPath = legajo.getUrl();
                if (urlPath.startsWith("/uploads/legajos/")) {
                    // 🔥 EXTRAER EL PATH DESDE uploads/
                    String filePath = urlPath.substring("/uploads/".length());
                    Path file = Paths.get(uploadDir, filePath);

                    if (Files.exists(file)) {
                        Files.deleteIfExists(file);
                        System.out.println("🗑️ Archivo eliminado: " + file.toString());
                    }

                    // 🔥 INTENTAR ELIMINAR LA CARPETA SI ESTÁ VACÍA
                    Path legajoDir = file.getParent();
                    if (legajoDir != null && Files.exists(legajoDir)) {
                        try (var stream = Files.list(legajoDir)) {
                            if (stream.findAny().isEmpty()) {
                                Files.deleteIfExists(legajoDir);
                                System.out.println("📁 Carpeta eliminada: " + legajoDir.toString());
                            }
                        } catch (Exception e) {
                            System.err.println("No se pudo eliminar la carpeta: " + e.getMessage());
                        }
                    }
                }

                // 🔥 LIMPIAR URL EN LA BASE DE DATOS
                legajo.setUrl(null);
                legajoService.save(legajo);

                return new ResponseEntity<>(new Object() {
                    public final String mensaje = "Imagen eliminada exitosamente";
                }, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Mensaje("El legajo no tiene imagen"), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar imagen: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(new Mensaje("Error al eliminar la imagen: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listImages/{id}")
    public ResponseEntity<?> listImages(@PathVariable("id") Long id) {
        try {
            if (!legajoService.activo(id)) {
                return new ResponseEntity<>(new Mensaje("Legajo no encontrado"), HttpStatus.NOT_FOUND);
            }

            Legajo legajo = legajoService.findById(id).get();
            String personaName = legajo.getPersona().getNombre() + "_" + legajo.getPersona().getApellido();
            String apellido = legajo.getPersona().getApellido();
            String nombre = legajo.getPersona().getNombre();
            int dni = legajo.getPersona().getDni();

            // 🔥 CREAR NOMBRE DE CARPETA PRINCIPAL (apellido_nombre_dni)
            final String mainFolderName = (apellido + "_" + nombre + "_" + dni)
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim();

            // 🔥 CREAR NOMBRE DE SUBCARPETA (apellido_nombre_legajo_id)
            String legajoFolderName = personaName
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim()
                    + "_legajo_" + id;

            Path legajoDir = Paths.get(uploadDir, "legajos", mainFolderName, legajoFolderName);

            if (!Files.exists(legajoDir)) {
                return new ResponseEntity<>(new Object() {
                    public final String mensaje = "No hay imágenes para este legajo";
                    public final String[] imagenes = new String[0];
                }, HttpStatus.OK);
            }

            // 🔥 LISTAR TODAS LAS IMÁGENES EN LA CARPETA
            List<String> imageFiles = new ArrayList<>();
            try (var stream = Files.list(legajoDir)) {
                stream.filter(Files::isRegularFile)
                        .filter(path -> {
                            String fileName = path.getFileName().toString().toLowerCase();
                            return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                                    fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                                    fileName.endsWith(".bmp");
                        })
                        .sorted((a, b) -> {
                            // Ordenar por fecha de modificación (más reciente primero)
                            try {
                                return Files.getLastModifiedTime(b).compareTo(Files.getLastModifiedTime(a));
                            } catch (IOException e) {
                                return 0;
                            }
                        })
                        .forEach(path -> {
                            String imageUrl = "/uploads/legajos/" + mainFolderName + "/" + legajoFolderName + "/"
                                    + path.getFileName().toString();
                            imageFiles.add(imageUrl);
                        });
            }

            return new ResponseEntity<>(new Object() {
                public final String mensaje = "Imágenes encontradas: " + imageFiles.size();
                public final String personaName = legajo.getPersona().getNombre() + " "
                        + legajo.getPersona().getApellido();
                public final String[] imagenes = imageFiles.toArray(new String[0]);
                public final String currentImage = legajo.getUrl();
            }, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al listar imágenes: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 🔥 MÉTODO AUXILIAR PARA BUSCAR IMAGEN EXISTENTE POR HASH
    private String findExistingImageByHash(Path mainDir, String targetHash, String mainFolderName) {
        if (!Files.exists(mainDir)) {
            return null;
        }

        try (var legajoDirStream = Files.list(mainDir)) {
            return legajoDirStream
                    .filter(Files::isDirectory)
                    .filter(dir -> dir.getFileName().toString().contains("_legajo_"))
                    .map(legajoDir -> {
                        try (var fileStream = Files.list(legajoDir)) {
                            return fileStream
                                    .filter(Files::isRegularFile)
                                    .filter(file -> {
                                        String fileName = file.getFileName().toString().toLowerCase();
                                        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                                                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                                                fileName.endsWith(".bmp");
                                    })
                                    .filter(file -> {
                                        try {
                                            String existingHash = legajoService
                                                    .calculateMD5(Files.newInputStream(file));
                                            return existingHash.equals(targetHash);
                                        } catch (Exception e) {
                                            return false;
                                        }
                                    })
                                    .map(file -> {
                                        String legajoFolderName = legajoDir.getFileName().toString();
                                        String fileName = file.getFileName().toString();
                                        return "/uploads/legajos/" + mainFolderName + "/" + legajoFolderName + "/"
                                                + fileName;
                                    })
                                    .findFirst()
                                    .orElse(null);
                        } catch (IOException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            System.err.println("❌ Error al buscar imagen existente: " + e.getMessage());
            return null;
        }
    }

}