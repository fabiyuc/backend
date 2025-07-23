package com.guardias.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.guardias.backend.dto.CapsDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.efector.EfectorCapsDto;
import com.guardias.backend.dto.efector.EfectorSummaryDto;
import com.guardias.backend.entity.Caps;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.service.CapsService;
import com.guardias.backend.service.HospitalService;

@Controller
@RequestMapping("/caps")
@CrossOrigin(origins = "http://localhost:4200")
public class CapsController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    HospitalService hospitalService;
    @Autowired
    CapsService capsService;

    @Autowired
    EfectorController efectorController;

    @GetMapping("/list")
    public ResponseEntity<List<Caps>> list() {
        List<Caps> capsList = capsService.findByActivoTrue().orElse(new ArrayList<>());
        List<Caps> filteredList = new ArrayList<>();

        for (Caps caps : capsList) {
            List<RegistroActividad> activeRegActividades = new ArrayList<>();
            for (RegistroActividad registroActividad : caps.getRegistrosActividades()) {
                if (registroActividad.isActivo()) {
                    activeRegActividades.add(registroActividad);
                }
            }
            caps.setRegistrosActividades(activeRegActividades);
            filteredList.add(caps);
        }

        return new ResponseEntity<List<Caps>>(filteredList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Caps>> listAll() {
        List<Caps> list = capsService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listSelection")
    public ResponseEntity<List<EfectorSummaryDto>> listActivos() {
        List<EfectorSummaryDto> list = capsService.findActiveEfectors();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detailNombreAll/{id}")
    public ResponseEntity<?> getByIdNombre(@PathVariable Long id) {
        Optional<EfectorCapsDto> efectorCapsDto = capsService.findByIdNombre(id);
        return efectorCapsDto
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build()); // Retorna 200 OK con cuerpo vacío
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Caps>> getById(@PathVariable("id") Long id) {
        if (!capsService.activo(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Caps caps = capsService.findById(id).get();
        return new ResponseEntity(caps, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Caps>> getByName(@PathVariable("nombre") String nombre) {
        if (!capsService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Caps caps = capsService.findByNombre(nombre).get();
        return new ResponseEntity(caps, HttpStatus.OK);
    }

    @GetMapping("/isCaps/{id}")
    public ResponseEntity<Boolean> isCaps(@PathVariable("id") Long id) {
        boolean esCaps = capsService.isCaps(id);
        return new ResponseEntity<>(esCaps, HttpStatus.OK);
    }

    @GetMapping("/getCabecera/{id}")
    public ResponseEntity<String> getCabeceraNameByCapsId(@PathVariable Long id) {
        Optional<String> cabeceraName = capsService.findCabeceraNameByCapsId(id);
        return cabeceraName
                .map(name -> ResponseEntity.ok(name))
                .orElse(ResponseEntity.notFound().build());
    }

    private Caps createUpdate(Caps caps, CapsDto capsDto) {
        Efector efector = efectorController.createUpdate(caps, capsDto);
        caps = (Caps) efector;

        if (caps.getCabecera() == null ||
                (capsDto.getIdCabecera() != null &&
                        !Objects.equals(caps.getCabecera().getId(),
                                capsDto.getIdRegion()))) {
            caps.setCabecera(hospitalService.findById(capsDto.getIdCabecera()).get());
        }
        if (!Objects.equals(caps.getAreaProgramatica(), capsDto.getAreaProgramatica()))
            caps.setAreaProgramatica(capsDto.getAreaProgramatica());

        if (caps.getTipoCaps() == null
                || (capsDto.getTipoCaps() != null && !Objects.equals(caps.getTipoCaps(), capsDto.getTipoCaps())))
            caps.setTipoCaps(capsDto.getTipoCaps());

        return caps;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CapsDto capsDto) {
        ResponseEntity<?> respuestaValidaciones = efectorController.validations(capsDto, 0L);

        if (capsDto.getTipoCaps() == null)
            return new ResponseEntity(new Mensaje("es obligatorio indicar el tipo de Caps"),
                    HttpStatus.BAD_REQUEST);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Caps caps = createUpdate(new Caps(), capsDto);
            caps.setActivo(true);
            capsService.save(caps);
            return new ResponseEntity<>(caps, HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody CapsDto capsDto) {
        if (!capsService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = efectorController.validations(capsDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Caps caps = createUpdate(capsService.findById(id).get(), capsDto);
            capsService.save(caps);
            return new ResponseEntity(new Mensaje("Caps creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!capsService.activo(id))
            return new ResponseEntity(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);

        Caps caps = capsService.findById(id).get();
        caps.setActivo(false);
        capsService.save(caps);
        return new ResponseEntity(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!capsService.existsById(id))
            return new ResponseEntity(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);
        capsService.deleteById(id);
        return new ResponseEntity(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long id,
            @RequestParam("image") MultipartFile file) {

        // Validar que el caps existe
        if (!capsService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("Caps no encontrado"), HttpStatus.NOT_FOUND);
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
            // 🔥 OBTENER EL CAPS PARA USAR SU NOMBRE
            Caps caps = capsService.findById(id).get();

            // 🔥 CREAR NOMBRE DE CARPETA SEGURO
            String capsFolderName = caps.getNombre()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim()
                    + "_" + id;

            // 🔥 CREAR DIRECTORIOS
            Path capsDir = Paths.get(uploadDir, "caps");
            Path capsFolder = capsDir.resolve(capsFolderName);

            if (!Files.exists(capsDir)) {
                Files.createDirectories(capsDir);
            }

            if (!Files.exists(capsFolder)) {
                Files.createDirectories(capsFolder);
            }

            // 🔥 CALCULAR HASH MD5 DEL ARCHIVO PARA DETECTAR DUPLICADOS
            String fileHash = capsService.calculateMD5(file.getInputStream());
            System.out.println("🔍 Hash del archivo: " + fileHash);

            // 🔥 VERIFICAR SI YA EXISTE UN ARCHIVO CON EL MISMO HASH
            if (Files.exists(capsFolder)) {
                try (var stream = Files.list(capsFolder)) {
                    Optional<Path> duplicateFile = stream
                            .filter(Files::isRegularFile)
                            .filter(path -> {
                                try {
                                    String existingHash = capsService.calculateMD5(Files.newInputStream(path));
                                    return existingHash.equals(fileHash);
                                } catch (Exception e) {
                                    return false;
                                }
                            })
                            .findFirst();

                    if (duplicateFile.isPresent()) {
                        String existingFileName = duplicateFile.get().getFileName().toString();
                        String existingUrl = "/uploads/caps/" + capsFolderName + "/" + existingFileName;

                        System.out.println("⚠️ Archivo duplicado detectado: " + existingFileName);

                        return new ResponseEntity<>(new Object() {
                            public final String mensaje = "Esta imagen ya existe en el CAPS";
                            public final String url = existingUrl;
                            public final String filename = existingFileName;
                            public final String folderName = capsFolderName;
                            public final boolean isDuplicate = true;
                            public final String existingFile = existingFileName;
                        }, HttpStatus.OK);
                    }
                }
            }

            // 🔥 VERIFICAR TAMBIÉN POR NOMBRE DE ARCHIVO ORIGINAL
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 🔥 BUSCAR SI YA EXISTE UN ARCHIVO CON EL MISMO NOMBRE ORIGINAL
            if (Files.exists(capsFolder)) {
                String cleanOriginalName = originalFilename != null
                        ? originalFilename.replaceAll("[^a-zA-Z0-9.]", "_").toLowerCase()
                        : "imagen";

                Path possibleExistingFile = capsFolder.resolve("sello_" + cleanOriginalName);
                if (Files.exists(possibleExistingFile)) {
                    String existingUrl = "/uploads/caps/" + capsFolderName + "/"
                            + possibleExistingFile.getFileName().toString();

                    System.out
                            .println("⚠️ Archivo con nombre similar ya existe: " + possibleExistingFile.getFileName());

                    return new ResponseEntity<>(new Object() {
                        public final String mensaje = "Ya existe una imagen con nombre similar. ¿Desea reemplazarla?";
                        public final String url = existingUrl;
                        public final String filename = possibleExistingFile.getFileName().toString();
                        public final String folderName = capsFolderName;
                        public final boolean isDuplicateName = true;
                        public final String existingFile = possibleExistingFile.getFileName().toString();
                        public final String originalName = originalFilename;
                    }, HttpStatus.CONFLICT); // 409 Conflict para indicar duplicado
                }
            }

            // 🔥 SI NO HAY DUPLICADOS, PROCEDER CON LA SUBIDA
            String timestamp = String.valueOf(System.currentTimeMillis());
            String cleanCapsName = caps.getNombre()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim();

            String uniqueFilename = "sello_" + cleanCapsName + "_" + timestamp + extension;

            // 🔥 GUARDAR ARCHIVO
            Path filePath = capsFolder.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("💾 Archivo guardado en: " + filePath.toString());

            // 🔥 ACTUALIZAR URL EN BD
            String imageUrl = "/uploads/caps/" + capsFolderName + "/" + uniqueFilename;
            caps.setUrl(imageUrl);
            capsService.save(caps);

            System.out.println("✅ URL guardada en BD: " + imageUrl);

            return new ResponseEntity<>(new Object() {
                public final String mensaje = "Imagen subida exitosamente";
                public final String url = imageUrl;
                public final String filename = uniqueFilename;
                public final String folderName = capsFolderName;
                public final String fullPath = filePath.toString();
                public final boolean isDuplicate = false;
                public final String hash = fileHash;
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
    public ResponseEntity<?> checkImageDuplicate(@PathVariable("id") Long id,
            @RequestParam("image") MultipartFile file) {

        // Validar que el caps existe
        if (!capsService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("Caps no encontrado"), HttpStatus.NOT_FOUND);
        }

        // Validar que se envió un archivo
        if (file.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("No se seleccionó ningún archivo"), HttpStatus.BAD_REQUEST);
        }

        try {
            // 🔥 OBTENER EL CAPS PARA USAR SU NOMBRE
            Caps caps = capsService.findById(id).get();

            // 🔥 CREAR NOMBRE DE CARPETA SEGURO
            String capsFolderName = caps.getNombre()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim()
                    + "_" + id;

            Path capsDir = Paths.get(uploadDir, "caps", capsFolderName);

            // 🔥 SOLO VERIFICAR, NO CREAR DIRECTORIOS NI SUBIR
            if (!Files.exists(capsDir)) {
                // Si no existe la carpeta, no hay duplicados
                return new ResponseEntity<>(new Object() {
                    public final boolean isDuplicate = false;
                    public final String message = "No hay duplicados";
                }, HttpStatus.OK);
            }

            // 🔥 CALCULAR HASH MD5 DEL ARCHIVO PARA DETECTAR DUPLICADOS
            String fileHash = capsService.calculateMD5(file.getInputStream());
            System.out.println("🔍 Verificando hash del archivo: " + fileHash);

            // 🔥 VERIFICAR SI YA EXISTE UN ARCHIVO CON EL MISMO HASH
            try (var stream = Files.list(capsDir)) {
                Optional<Path> duplicateFile = stream
                        .filter(Files::isRegularFile)
                        .filter(path -> {
                            try {
                                String existingHash = capsService.calculateMD5(Files.newInputStream(path));
                                return existingHash.equals(fileHash);
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .findFirst();

                if (duplicateFile.isPresent()) {
                    String existingFileName = duplicateFile.get().getFileName().toString();
                    String existingUrl = "/uploads/caps/" + capsFolderName + "/" + existingFileName;

                    System.out.println("⚠️ Duplicado detectado en verificación: " + existingFileName);

                    return new ResponseEntity<>(new Object() {
                        public final String mensaje = "Esta imagen ya existe en el caps";
                        public final String url = existingUrl;
                        public final String filename = existingFileName;
                        public final String folderName = capsFolderName;
                        public final boolean isDuplicate = true;
                        public final String existingFile = existingFileName;
                    }, HttpStatus.OK);
                }
            }

            // 🔥 NO HAY DUPLICADOS
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

        if (!capsService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("Caps no encontrado"), HttpStatus.NOT_FOUND);
        }

        try {
            Caps caps = capsService.findById(id).get();

            if (caps.getUrl() != null && !caps.getUrl().isEmpty()) {
                String urlPath = caps.getUrl();
                if (urlPath.startsWith("/uploads/caps/")) {
                    // 🔥 EXTRAER EL PATH DESDE uploads/
                    String filePath = urlPath.substring("/uploads/".length());
                    Path file = Paths.get(uploadDir, filePath);

                    if (Files.exists(file)) {
                        Files.deleteIfExists(file);
                        System.out.println("🗑️ Archivo eliminado: " + file.toString());
                    }

                    // 🔥 INTENTAR ELIMINAR LA CARPETA SI ESTÁ VACÍA
                    Path capsDir = file.getParent();
                    if (capsDir != null && Files.exists(capsDir)) {
                        try (var stream = Files.list(capsDir)) {
                            if (stream.findAny().isEmpty()) {
                                Files.deleteIfExists(capsDir);
                                System.out.println("📁 Carpeta eliminada: " + capsDir.toString());
                            }
                        } catch (Exception e) {
                            System.err.println("No se pudo eliminar la carpeta: " + e.getMessage());
                        }
                    }
                }

                // 🔥 LIMPIAR URL EN LA BASE DE DATOS
                caps.setUrl(null);
                capsService.save(caps);

                return new ResponseEntity<>(new Object() {
                    public final String mensaje = "Imagen eliminada exitosamente";
                }, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Mensaje("El caps no tiene imagen"), HttpStatus.BAD_REQUEST);
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
            if (!capsService.activo(id)) {
                return new ResponseEntity<>(new Mensaje("Caps no encontrado"), HttpStatus.NOT_FOUND);
            }

            Caps caps = capsService.findById(id).get();

            String capsFolderName = caps.getNombre()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim()
                    + "_" + id;

            Path capsDir = Paths.get(uploadDir, "caps", capsFolderName);

            if (!Files.exists(capsDir)) {
                return new ResponseEntity<>(new Object() {
                    public final String mensaje = "No hay imágenes para este caps";
                    public final String[] imagenes = new String[0];
                }, HttpStatus.OK);
            }

            // 🔥 LISTAR TODAS LAS IMÁGENES EN LA CARPETA
            List<String> imageFiles = new ArrayList<>();
            try (var stream = Files.list(capsDir)) {
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
                            String imageUrl = "/uploads/caps/" + capsFolderName + "/"
                                    + path.getFileName().toString();
                            imageFiles.add(imageUrl);
                        });
            }

            return new ResponseEntity<>(new Object() {
                public final String mensaje = "Imágenes encontradas: " + imageFiles.size();
                public final String capsName = caps.getNombre();
                public final String[] imagenes = imageFiles.toArray(new String[0]);
                public final String currentImage = caps.getUrl();
            }, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al listar imágenes: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
