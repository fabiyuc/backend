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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.MinisterioDto;
import com.guardias.backend.dto.efector.EfectorMinisterioDto;
import com.guardias.backend.dto.efector.EfectorSummaryDto;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Ministerio;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.Servicio;
import com.guardias.backend.service.MinisterioService;
import com.guardias.backend.service.ServicioService;

@Controller
@RequestMapping("/ministerio")
@CrossOrigin(origins = "http://localhost:4200")
public class MinisterioController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    MinisterioService ministerioService;
    @Autowired
    EfectorController efectorController;

    @Autowired
    ServicioService servicioService;

    @GetMapping("/list")
    public ResponseEntity<List<Ministerio>> list() {
        List<Ministerio> ministerioList = ministerioService.findByActivoTrue().orElse(new ArrayList<>());
        List<Ministerio> filteredList = new ArrayList<>();

        for (Ministerio ministerio : ministerioList) {
            List<RegistroActividad> activeRegActividades = new ArrayList<>();
            for (RegistroActividad registroActividad : ministerio.getRegistrosActividades()) {
                if (registroActividad.isActivo()) {
                    activeRegActividades.add(registroActividad);
                }
            }
            ministerio.setRegistrosActividades(activeRegActividades);
            filteredList.add(ministerio);
        }

        return new ResponseEntity<List<Ministerio>>(filteredList, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Ministerio>> listAll() {
        List<Ministerio> list = ministerioService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listSelection")
    public ResponseEntity<List<EfectorSummaryDto>> listActivos() {
        List<EfectorSummaryDto> list = ministerioService.findActiveEfectors();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detailNombreAll/{id}")
    public ResponseEntity<?> getByIdNombre(@PathVariable Long id) {
        Optional<EfectorMinisterioDto> efectorMinisterioDto = ministerioService.findByIdNombre(id);
        return efectorMinisterioDto
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build()); // Retorna 200 OK con cuerpo vacío
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Ministerio>> getById(@PathVariable("id") Long id) {
        if (!ministerioService.activo(id))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Ministerio ministerio = ministerioService.findById(id).get();
        return new ResponseEntity(ministerio, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Ministerio>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!ministerioService.activoByNombre(nombre))
            return new ResponseEntity(new Mensaje("Efector no encontrado"), HttpStatus.NOT_FOUND);
        Ministerio ministerio = ministerioService.findByNombre(nombre).get();
        return new ResponseEntity(ministerio, HttpStatus.OK);
    }

    @GetMapping("/isMinisterio/{id}")
    public ResponseEntity<Boolean> isMinisterio(@PathVariable("id") Long id) {
        boolean esMinisterio = ministerioService.isMinisterio(id);
        return new ResponseEntity<>(esMinisterio, HttpStatus.OK);
    }

    private Ministerio createUpdate(Ministerio ministerio, MinisterioDto ministerioDto) {
        Efector efector = efectorController.createUpdate(ministerio, ministerioDto);
        ministerio = (Ministerio) efector;

        // Asignamos la
        if (ministerioDto.getIdCabecera() != null) {
            Ministerio cabecera = ministerioService.findById(ministerioDto.getIdCabecera())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No se encontró un Ministerio con id: " + ministerioDto.getIdCabecera()));
            ministerio.setCabecera(cabecera);
        } else {
            // Si idCabecera es nulo, asignamos null a la cabecera
            ministerio.setCabecera(null);
        }

        if (ministerioDto.getIdMinisterios() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (ministerio.getMinisterios() != null) {
                for (Ministerio ministerios : ministerio.getMinisterios()) {
                    for (Long id : ministerioDto.getIdMinisterios()) {
                        if (!ministerios.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                ministerio.setMinisterios(new ArrayList<Ministerio>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? ministerioDto.getIdMinisterios() : idList;
            for (Long id : idsToAdd) {
                ministerio.getMinisterios().add(ministerioService.findById(id).get());
                ministerioService.findById(id).get().setCabecera(ministerio);
            }
        }

        ministerio.setActivo(true);
        return ministerio;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MinisterioDto ministerioDto) {
        ResponseEntity<?> respuestaValidaciones = efectorController.validations(ministerioDto, 0L);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Ministerio ministerio = createUpdate(new Ministerio(), ministerioDto);
            ministerioService.save(ministerio);

            // 🔗 Actualizar el lado propietario (Servicio) después de guardar el ministerio
            if (ministerio.getServicios() != null) {
                for (Servicio s : ministerio.getServicios()) {
                    Servicio servicio = servicioService.findById(s.getId()).get();
                    if (servicio != null) {
                        if (servicio.getEfectores() == null) {
                            servicio.setEfectores(new java.util.ArrayList<>());
                        }
                        if (!servicio.getEfectores().contains(ministerio)) {
                            servicio.getEfectores().add(ministerio);
                            servicioService.save(servicio);
                        }
                    }
                }
            }

            return new ResponseEntity<>(ministerio, HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody MinisterioDto ministerioDto) {
        if (!ministerioService.activo(id))
            return new ResponseEntity(new Mensaje("no existe el efector"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = efectorController.validations(ministerioDto, id);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Ministerio ministerio = createUpdate(ministerioService.findById(id).get(), ministerioDto);
            ministerioService.save(ministerio);

            // 🔗 Actualizar el lado propietario (Servicio) después de guardar el ministerio
            if (ministerio.getServicios() != null) {
                for (Servicio s : ministerio.getServicios()) {
                    Servicio servicio = servicioService.findById(s.getId()).get();
                    if (servicio != null) {
                        if (servicio.getEfectores() == null) {
                            servicio.setEfectores(new java.util.ArrayList<>());
                        }
                        if (!servicio.getEfectores().contains(ministerio)) {
                            servicio.getEfectores().add(ministerio);
                            servicioService.save(servicio);
                        }
                    }
                }
            }

            return new ResponseEntity(new Mensaje("Ministerio creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!ministerioService.activo(id))
            return new ResponseEntity(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);

        Ministerio ministerio = ministerioService.findById(id).get();
        ministerio.setActivo(false);
        ministerioService.save(ministerio);
        return new ResponseEntity(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {
        if (!ministerioService.existsById(id))
            return new ResponseEntity<Mensaje>(new Mensaje("efector no encontrado"), HttpStatus.NOT_FOUND);
        ministerioService.deleteById(id);
        return new ResponseEntity<Mensaje>(new Mensaje("Efector eliminado FISICAMENTE"), HttpStatus.OK);
    }

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long id,
            @RequestParam("image") MultipartFile file) {

        // Validar que el ministerio existe
        if (!ministerioService.activo(id)) {
            return new ResponseEntity<>(new Mensaje("Ministerio no encontrado"), HttpStatus.NOT_FOUND);
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
            // 🔥 OBTENER EL MINISTERIO PARA USAR SU NOMBRE
            Ministerio ministerio = ministerioService.findById(id).get();

            // 🔥 CREAR NOMBRE DE CARPETA SEGURO
            String ministerioFolderName = ministerio.getNombre()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim()
                    + "_" + id;

            // 🔥 CREAR DIRECTORIOS
            Path ministeriosDir = Paths.get(uploadDir, "ministerios");
            Path ministerioDir = ministeriosDir.resolve(ministerioFolderName);

            if (!Files.exists(ministeriosDir)) {
                Files.createDirectories(ministeriosDir);
            }

            if (!Files.exists(ministerioDir)) {
                Files.createDirectories(ministerioDir);
            }

            // 🔥 CALCULAR HASH MD5 DEL ARCHIVO PARA DETECTAR DUPLICADOS
            String fileHash = ministerioService.calculateMD5(file.getInputStream());
            System.out.println("🔍 Hash del archivo: " + fileHash);

            // 🔥 VERIFICAR SI YA EXISTE UN ARCHIVO CON EL MISMO HASH
            if (Files.exists(ministerioDir)) {
                try (var stream = Files.list(ministerioDir)) {
                    Optional<Path> duplicateFile = stream
                            .filter(Files::isRegularFile)
                            .filter(path -> {
                                try {
                                    String existingHash = ministerioService.calculateMD5(Files.newInputStream(path));
                                    return existingHash.equals(fileHash);
                                } catch (Exception e) {
                                    return false;
                                }
                            })
                            .findFirst();

                    if (duplicateFile.isPresent()) {
                        String existingFileName = duplicateFile.get().getFileName().toString();
                        String existingUrl = "/uploads/ministerios/" + ministerioFolderName + "/" + existingFileName;

                        System.out.println("⚠️ Archivo duplicado detectado: " + existingFileName);

                        return new ResponseEntity<>(new Object() {
                            public final String mensaje = "Esta imagen ya existe en el ministerio";
                            public final String url = existingUrl;
                            public final String filename = existingFileName;
                            public final String folderName = ministerioFolderName;
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
            if (Files.exists(ministerioDir)) {
                String cleanOriginalName = originalFilename != null
                        ? originalFilename.replaceAll("[^a-zA-Z0-9.]", "_").toLowerCase()
                        : "imagen";

                Path possibleExistingFile = ministerioDir.resolve("sello_" + cleanOriginalName);
                if (Files.exists(possibleExistingFile)) {
                    String existingUrl = "/uploads/ministerios/" + ministerioFolderName + "/"
                            + possibleExistingFile.getFileName().toString();

                    System.out
                            .println("⚠️ Archivo con nombre similar ya existe: " + possibleExistingFile.getFileName());

                    return new ResponseEntity<>(new Object() {
                        public final String mensaje = "Ya existe una imagen con nombre similar. ¿Desea reemplazarla?";
                        public final String url = existingUrl;
                        public final String filename = possibleExistingFile.getFileName().toString();
                        public final String folderName = ministerioFolderName;
                        public final boolean isDuplicateName = true;
                        public final String existingFile = possibleExistingFile.getFileName().toString();
                        public final String originalName = originalFilename;
                    }, HttpStatus.CONFLICT); // 409 Conflict para indicar duplicado
                }
            }

            // 🔥 SI NO HAY DUPLICADOS, PROCEDER CON LA SUBIDA
            String timestamp = String.valueOf(System.currentTimeMillis());
            String cleanMinisterioName = ministerio.getNombre()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .replaceAll("\\s+", "_")
                    .toLowerCase()
                    .trim();

            String uniqueFilename = "sello_" + cleanMinisterioName + "_" + timestamp + extension;

            // 🔥 GUARDAR ARCHIVO
            Path filePath = ministerioDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("💾 Archivo guardado en: " + filePath.toString());

            // 🔥 ACTUALIZAR URL EN BD
            String imageUrl = "/uploads/ministerios/" + ministerioFolderName + "/" + uniqueFilename;
            ministerio.setUrl(imageUrl);
            ministerioService.save(ministerio);

            System.out.println("✅ URL guardada en BD: " + imageUrl);

            return new ResponseEntity<>(new Object() {
                public final String mensaje = "Imagen subida exitosamente";
                public final String url = imageUrl;
                public final String filename = uniqueFilename;
                public final String folderName = ministerioFolderName;
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

    @GetMapping("/imageByUser/{idUsuario}")
    public ResponseEntity<?> getImageByUserId(@PathVariable("idUsuario") Long idUsuario) {
        try {
            // Buscar el ministerio asignado al usuario a través de sus legajos
            Optional<Ministerio> ministerioOpt = ministerioService.findMinisterioByUsuarioId(idUsuario);

            if (!ministerioOpt.isPresent()) {
                return new ResponseEntity<>(new Mensaje("Usuario no tiene ministerio asignado"), HttpStatus.NOT_FOUND);
            }

            Ministerio ministerio = ministerioOpt.get();

            if (ministerio.getUrl() == null || ministerio.getUrl().isEmpty()) {
                return new ResponseEntity<>(new Object() {
                    public final String mensaje = "El ministerio no tiene imagen asignada";
                    public final String ministerioNombre = ministerio.getNombre();
                    public final String url = null;
                }, HttpStatus.OK);
            }

            return new ResponseEntity<>(new Object() {
                public final String mensaje = "Imagen encontrada";
                public final String ministerioNombre = ministerio.getNombre();
                public final String url = ministerio.getUrl();
            }, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("❌ Error al obtener imagen por usuario: " + e.getMessage());
            return new ResponseEntity<>(new Mensaje("Error al obtener la imagen: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
