package com.guardias.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
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

import com.guardias.backend.dto.HospitalDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.caps.CapsNameDto;
import com.guardias.backend.dto.efector.EfectorHospitalDto;
import com.guardias.backend.dto.efector.EfectorSummaryDto;
import com.guardias.backend.dto.servicio.ServicioSummaryDto;
import com.guardias.backend.entity.Caps;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.service.CapsService;
import com.guardias.backend.service.HospitalService;

@Controller
@RequestMapping("/hospital")
@CrossOrigin(origins = "http://localhost:4200")
public class HospitalController {

    // Configuración del directorio donde se guardarán las imágenes
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    HospitalService hospitalService;

    @Autowired
    CapsService capsService;

    @Autowired
    EfectorController efectorController;

    @GetMapping("/list")
    public ResponseEntity<List<Hospital>> list() {
        List<Hospital> hospitalList = hospitalService.findByActivoTrue().orElse(new ArrayList<>());
        List<Hospital> filteredList = new ArrayList<>();

        for (Hospital hospital : hospitalList) {
            List<RegistroActividad> activeRegActividades = new ArrayList<>();
            for (RegistroActividad registroActividad : hospital.getRegistrosActividades()) {
                if (registroActividad.isActivo()) {
                    activeRegActividades.add(registroActividad);
                }
            }
            hospital.setRegistrosActividades(activeRegActividades);
            filteredList.add(hospital);
        }

        return new ResponseEntity<List<Hospital>>(filteredList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Hospital>> listAll() {
        List<Hospital> list = hospitalService.findAll();
        return new ResponseEntity<List<Hospital>>(list, HttpStatus.OK);
    }

    @GetMapping("/listSelection")
    public ResponseEntity<List<EfectorSummaryDto>> listActivos() {
        List<EfectorSummaryDto> list = hospitalService.findActiveEfectors();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listPasivas")
    public ResponseEntity<List<Hospital>> listPasivas() {
        List<Hospital> list = hospitalService.findByAdmitePasiva();
        return new ResponseEntity<List<Hospital>>(list, HttpStatus.OK);
    }

    @GetMapping("/listCaps/{hospitalId}")
    public ResponseEntity<?> listActiveCapsByHospitalId(@PathVariable Long hospitalId) {
        List<CapsNameDto> capsList = hospitalService.findActiveCapsByHospitalId(hospitalId);

        if (capsList != null) {
            return new ResponseEntity<>(capsList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Hospital no encontrado o no está activo", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/detailNombreAll/{id}")
    public ResponseEntity<?> getByIdNombre(@PathVariable Long id) {
        Optional<EfectorHospitalDto> efectorHospitalDto = hospitalService.findByIdNombre(id);
        return efectorHospitalDto
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build()); // Retorna 200 OK con cuerpo vacío
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Hospital> getById(@PathVariable("id") Long id) {
        if (!hospitalService.activo(id))
            return new ResponseEntity(new Mensaje("Hospital no encontrado"), HttpStatus.NOT_FOUND);
        Hospital hospital = hospitalService.findById(id).get();
        return new ResponseEntity<Hospital>(hospital, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Hospital> getByNombre(@PathVariable("nombre") String nombre) {
        if (!hospitalService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("Hospital no encontrado"), HttpStatus.NOT_FOUND);
        Hospital hospital = hospitalService.findByNombre(nombre).get();
        return new ResponseEntity(hospital, HttpStatus.OK);
    }

    @GetMapping("/isHospital/{id}")
    public ResponseEntity<Boolean> isHospital(@PathVariable("id") Long id) {
        boolean esHospital = hospitalService.isHospital(id);
        return new ResponseEntity<>(esHospital, HttpStatus.OK);
    }

    private Hospital createUpdate(Hospital hospital, HospitalDto hospitalDto) {
        Efector efector = efectorController.createUpdate(hospital, hospitalDto);
        hospital = (Hospital) efector;

        hospital.setEsCabecera(hospitalDto.getEsCabecera());
        hospital.setAdmitePasiva(hospitalDto.getAdmitePasiva());
        hospital.setNivelComplejidad(hospitalDto.getNivelComplejidad());

        if (hospitalDto.getIdCaps() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (hospital.getCaps() != null) {
                for (Caps caps : hospital.getCaps()) {
                    for (Long id : hospitalDto.getIdCaps()) {
                        if (!caps.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                hospital.setCaps(new ArrayList<Caps>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? hospitalDto.getIdCaps() : idList;
            for (Long id : idsToAdd) {
                hospital.getCaps().add(capsService.findById(id).get());
                capsService.findById(id).get().setCabecera(hospital);
            }
        }

        return hospital;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody HospitalDto hospitalDto) {
        ResponseEntity<?> respuestaValidaciones = efectorController.validations(hospitalDto, 0L);

        if (hospitalDto.getEsCabecera() == null) {
            return new ResponseEntity<>(new Mensaje("Indicar si es cabecera o no"), HttpStatus.BAD_REQUEST);
        }

        if (hospitalDto.getAdmitePasiva() == null) {
            return new ResponseEntity<>(new Mensaje("Indicar si admite pasiva o no"), HttpStatus.BAD_REQUEST);
        }

        if (hospitalDto.getNivelComplejidad() == null) {
            return new ResponseEntity<>(new Mensaje("Indicar el nivel de complejidad"), HttpStatus.BAD_REQUEST);
        }

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Hospital hospital = createUpdate(new Hospital(), hospitalDto);
            hospital.setActivo(true);

            // 🔥 GUARDAR Y DEVOLVER EL HOSPITAL CREADO CON SU ID
            hospitalService.save(hospital);
            return new ResponseEntity<>(hospital, HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody HospitalDto hospitalDto) {
        if (!hospitalService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el hospital"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = efectorController.validations(hospitalDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Hospital hospital = createUpdate(hospitalService.findById(id).get(), hospitalDto);
            hospitalService.save(hospital);
            return new ResponseEntity(new Mensaje("Hospital modificado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!hospitalService.activo(id))
            return new ResponseEntity(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);

        Hospital hospital = hospitalService.findById(id).get();
        hospital.setActivo(false);
        hospitalService.save(hospital);
        return new ResponseEntity(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!hospitalService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el hospital"), HttpStatus.NOT_FOUND);
        hospitalService.deleteById(id);
        return new ResponseEntity(new Mensaje("Hospital eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @GetMapping("/serviciosActivos/{idHospital}")
    public ResponseEntity<List<ServicioSummaryDto>> getServiciosActivos(@PathVariable Long idHospital) {
        List<ServicioSummaryDto> servicios = hospitalService.getActiveServiciosByHospitalId(idHospital);
        return ResponseEntity.ok(servicios);
    }

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long id,
            @RequestParam("image") MultipartFile file) {

        // Validar que el hospital existe
        if (!hospitalService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("Hospital no encontrado"), HttpStatus.NOT_FOUND);
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
            // 🔥 OBTENER EL HOSPITAL PARA USAR SU NOMBRE
            Hospital hospital = hospitalService.findById(id).get();

            // 🔥 CREAR NOMBRE DE CARPETA SEGURO
            String hospitalFolderName = hospital.getNombre()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim()
                    + "_" + id;

            // 🔥 CREAR DIRECTORIOS
            Path hospitalesDir = Paths.get(uploadDir, "hospitales");
            Path hospitalDir = hospitalesDir.resolve(hospitalFolderName);

            if (!Files.exists(hospitalesDir)) {
                Files.createDirectories(hospitalesDir);
            }

            if (!Files.exists(hospitalDir)) {
                Files.createDirectories(hospitalDir);
            }

            // 🔥 CALCULAR HASH MD5 DEL ARCHIVO PARA DETECTAR DUPLICADOS
            String fileHash = hospitalService.calculateMD5(file.getInputStream());
            System.out.println("🔍 Hash del archivo: " + fileHash);

            // 🔥 VERIFICAR SI YA EXISTE UN ARCHIVO CON EL MISMO HASH
            if (Files.exists(hospitalDir)) {
                try (var stream = Files.list(hospitalDir)) {
                    Optional<Path> duplicateFile = stream
                            .filter(Files::isRegularFile)
                            .filter(path -> {
                                try {
                                    String existingHash = hospitalService.calculateMD5(Files.newInputStream(path));
                                    return existingHash.equals(fileHash);
                                } catch (Exception e) {
                                    return false;
                                }
                            })
                            .findFirst();

                    if (duplicateFile.isPresent()) {
                        String existingFileName = duplicateFile.get().getFileName().toString();
                        String existingUrl = "/uploads/hospitales/" + hospitalFolderName + "/" + existingFileName;

                        System.out.println("⚠️ Archivo duplicado detectado: " + existingFileName);

                        return new ResponseEntity<>(new Object() {
                            public final String mensaje = "Esta imagen ya existe en el hospital";
                            public final String url = existingUrl;
                            public final String filename = existingFileName;
                            public final String folderName = hospitalFolderName;
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
            if (Files.exists(hospitalDir)) {
                String cleanOriginalName = originalFilename != null
                        ? originalFilename.replaceAll("[^a-zA-Z0-9.]", "_").toLowerCase()
                        : "imagen";

                Path possibleExistingFile = hospitalDir.resolve("sello_" + cleanOriginalName);
                if (Files.exists(possibleExistingFile)) {
                    String existingUrl = "/uploads/hospitales/" + hospitalFolderName + "/"
                            + possibleExistingFile.getFileName().toString();

                    System.out
                            .println("⚠️ Archivo con nombre similar ya existe: " + possibleExistingFile.getFileName());

                    return new ResponseEntity<>(new Object() {
                        public final String mensaje = "Ya existe una imagen con nombre similar. ¿Desea reemplazarla?";
                        public final String url = existingUrl;
                        public final String filename = possibleExistingFile.getFileName().toString();
                        public final String folderName = hospitalFolderName;
                        public final boolean isDuplicateName = true;
                        public final String existingFile = possibleExistingFile.getFileName().toString();
                        public final String originalName = originalFilename;
                    }, HttpStatus.CONFLICT); // 409 Conflict para indicar duplicado
                }
            }

            // 🔥 SI NO HAY DUPLICADOS, PROCEDER CON LA SUBIDA
            String timestamp = String.valueOf(System.currentTimeMillis());
            String cleanHospitalName = hospital.getNombre()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim();

            String uniqueFilename = "sello_" + cleanHospitalName + "_" + timestamp + extension;

            // 🔥 GUARDAR ARCHIVO
            Path filePath = hospitalDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("💾 Archivo guardado en: " + filePath.toString());

            // 🔥 ACTUALIZAR URL EN BD
            String imageUrl = "/uploads/hospitales/" + hospitalFolderName + "/" + uniqueFilename;
            hospital.setUrl(imageUrl);
            hospitalService.save(hospital);

            System.out.println("✅ URL guardada en BD: " + imageUrl);

            return new ResponseEntity<>(new Object() {
                public final String mensaje = "Imagen subida exitosamente";
                public final String url = imageUrl;
                public final String filename = uniqueFilename;
                public final String folderName = hospitalFolderName;
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

        // Validar que el hospital existe
        if (!hospitalService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("Hospital no encontrado"), HttpStatus.NOT_FOUND);
        }

        // Validar que se envió un archivo
        if (file.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("No se seleccionó ningún archivo"), HttpStatus.BAD_REQUEST);
        }

        try {
            // 🔥 OBTENER EL HOSPITAL PARA USAR SU NOMBRE
            Hospital hospital = hospitalService.findById(id).get();

            // 🔥 CREAR NOMBRE DE CARPETA SEGURO
            String hospitalFolderName = hospital.getNombre()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim()
                    + "_" + id;

            Path hospitalDir = Paths.get(uploadDir, "hospitales", hospitalFolderName);

            // 🔥 SOLO VERIFICAR, NO CREAR DIRECTORIOS NI SUBIR
            if (!Files.exists(hospitalDir)) {
                // Si no existe la carpeta, no hay duplicados
                return new ResponseEntity<>(new Object() {
                    public final boolean isDuplicate = false;
                    public final String message = "No hay duplicados";
                }, HttpStatus.OK);
            }

            // 🔥 CALCULAR HASH MD5 DEL ARCHIVO PARA DETECTAR DUPLICADOS
            String fileHash = hospitalService.calculateMD5(file.getInputStream());
            System.out.println("🔍 Verificando hash del archivo: " + fileHash);

            // 🔥 VERIFICAR SI YA EXISTE UN ARCHIVO CON EL MISMO HASH
            try (var stream = Files.list(hospitalDir)) {
                Optional<Path> duplicateFile = stream
                        .filter(Files::isRegularFile)
                        .filter(path -> {
                            try {
                                String existingHash = hospitalService.calculateMD5(Files.newInputStream(path));
                                return existingHash.equals(fileHash);
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .findFirst();

                if (duplicateFile.isPresent()) {
                    String existingFileName = duplicateFile.get().getFileName().toString();
                    String existingUrl = "/uploads/hospitales/" + hospitalFolderName + "/" + existingFileName;

                    System.out.println("⚠️ Duplicado detectado en verificación: " + existingFileName);

                    return new ResponseEntity<>(new Object() {
                        public final String mensaje = "Esta imagen ya existe en el hospital";
                        public final String url = existingUrl;
                        public final String filename = existingFileName;
                        public final String folderName = hospitalFolderName;
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

        if (!hospitalService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("Hospital no encontrado"), HttpStatus.NOT_FOUND);
        }

        try {
            Hospital hospital = hospitalService.findById(id).get();

            if (hospital.getUrl() != null && !hospital.getUrl().isEmpty()) {
                String urlPath = hospital.getUrl();
                if (urlPath.startsWith("/uploads/hospitales/")) {
                    // 🔥 EXTRAER EL PATH DESDE uploads/
                    String filePath = urlPath.substring("/uploads/".length());
                    Path file = Paths.get(uploadDir, filePath);

                    if (Files.exists(file)) {
                        Files.deleteIfExists(file);
                        System.out.println("🗑️ Archivo eliminado: " + file.toString());
                    }

                    // 🔥 INTENTAR ELIMINAR LA CARPETA SI ESTÁ VACÍA
                    Path hospitalDir = file.getParent();
                    if (hospitalDir != null && Files.exists(hospitalDir)) {
                        try (var stream = Files.list(hospitalDir)) {
                            if (stream.findAny().isEmpty()) {
                                Files.deleteIfExists(hospitalDir);
                                System.out.println("📁 Carpeta eliminada: " + hospitalDir.toString());
                            }
                        } catch (Exception e) {
                            System.err.println("No se pudo eliminar la carpeta: " + e.getMessage());
                        }
                    }
                }

                // 🔥 LIMPIAR URL EN LA BASE DE DATOS
                hospital.setUrl(null);
                hospitalService.save(hospital);

                return new ResponseEntity<>(new Object() {
                    public final String mensaje = "Imagen eliminada exitosamente";
                }, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Mensaje("El hospital no tiene imagen"), HttpStatus.BAD_REQUEST);
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
            if (!hospitalService.activo(id)) {
                return new ResponseEntity<>(new Mensaje("Hospital no encontrado"), HttpStatus.NOT_FOUND);
            }

            Hospital hospital = hospitalService.findById(id).get();

            String hospitalFolderName = hospital.getNombre()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim()
                    + "_" + id;

            Path hospitalDir = Paths.get(uploadDir, "hospitales", hospitalFolderName);

            if (!Files.exists(hospitalDir)) {
                return new ResponseEntity<>(new Object() {
                    public final String mensaje = "No hay imágenes para este hospital";
                    public final String[] imagenes = new String[0];
                }, HttpStatus.OK);
            }

            // 🔥 LISTAR TODAS LAS IMÁGENES EN LA CARPETA
            List<String> imageFiles = new ArrayList<>();
            try (var stream = Files.list(hospitalDir)) {
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
                            String imageUrl = "/uploads/hospitales/" + hospitalFolderName + "/"
                                    + path.getFileName().toString();
                            imageFiles.add(imageUrl);
                        });
            }

            return new ResponseEntity<>(new Object() {
                public final String mensaje = "Imágenes encontradas: " + imageFiles.size();
                public final String hospitalName = hospital.getNombre();
                public final String[] imagenes = imageFiles.toArray(new String[0]);
                public final String currentImage = hospital.getUrl();
            }, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al listar imágenes: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
