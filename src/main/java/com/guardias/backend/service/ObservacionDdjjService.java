package com.guardias.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ObservacionDdjjDto;
import com.guardias.backend.dto.ObservacionDdjj.ObservacionDdjjUltimoDto;
import com.guardias.backend.entity.ObservacionDdjj;
import com.guardias.backend.repository.ObservacionDdjjRepository;
import com.guardias.backend.security.service.UsuarioService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ObservacionDdjjService {

    @Value("${app.upload.dir:uploads/ddjj_observaciones}") // Configurable en properties
    private String rootUploadDir;

    @Autowired
    ObservacionDdjjRepository observacionDdjjRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    DdjjService ddjjService;

    public List<ObservacionDdjj> findByActivoTrue() {
        return observacionDdjjRepository.findByActivoTrue();
    }

    public List<ObservacionDdjj> findAll() {
        return observacionDdjjRepository.findAll();
    }

    public boolean activo(Long id) {
        return (observacionDdjjRepository.existsById(id) && observacionDdjjRepository.findById(id).get().isActivo());
    }

    public Optional<ObservacionDdjj> findById(Long id) {
        return observacionDdjjRepository.findById(id);
    }

    public ResponseEntity<?> validations(ObservacionDdjjDto observacionDdjjDto, Long id) {

        if (observacionDdjjDto.getMotivo() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar el motivo"),
                    HttpStatus.BAD_REQUEST);

        if (observacionDdjjDto.getTipoDph() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar si es de tipo DPH"),
                    HttpStatus.BAD_REQUEST);

        if (observacionDdjjDto.getIdUsuario() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar el id del usuario"),
                    HttpStatus.BAD_REQUEST);

        if (observacionDdjjDto.getIdDdjj() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar el id de la ddjj"),
                    HttpStatus.BAD_REQUEST);
        
        if (observacionDdjjDto.getFechaCreacion() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la fecha de creacion"),
                    HttpStatus.BAD_REQUEST);

        if (observacionDdjjDto.getHoraCreacion() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar la hora de creacion"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    public ObservacionDdjj createUpdate(ObservacionDdjj observacionDdjj, ObservacionDdjjDto observacionDdjjDto) {

        if (observacionDdjj.getMotivo() != observacionDdjjDto.getMotivo())
            observacionDdjj.setMotivo(observacionDdjjDto.getMotivo());

        if (observacionDdjj.getTipoDph() != observacionDdjjDto.getTipoDph())
            observacionDdjj.setTipoDph(observacionDdjjDto.getTipoDph());

        if (observacionDdjj.getUsuario() == null
                || !Objects.equals(observacionDdjj.getUsuario().getId(), observacionDdjjDto.getIdUsuario()))
            observacionDdjj.setUsuario(usuarioService.findById(observacionDdjjDto.getIdUsuario()).get());

        if (observacionDdjj.getDdjj() == null
                || !Objects.equals(observacionDdjj.getDdjj().getId(), observacionDdjjDto.getIdDdjj()))
            observacionDdjj.setDdjj(ddjjService.findById(observacionDdjjDto.getIdDdjj()).get());

        if (observacionDdjj.getFechaCreacion() != observacionDdjjDto.getFechaCreacion())
            observacionDdjj.setFechaCreacion(observacionDdjjDto.getFechaCreacion());
        
        if (observacionDdjj.getHoraCreacion() != observacionDdjjDto.getHoraCreacion())
            observacionDdjj.setHoraCreacion(observacionDdjjDto.getHoraCreacion());
        observacionDdjj.setActivo(true);

        return observacionDdjj;
    }

    public void save(ObservacionDdjj observacionDdjj) {
        observacionDdjjRepository.save(observacionDdjj);
    }

    public boolean existsById(Long id) {
        return observacionDdjjRepository.existsById(id);
    }

    public void deleteById(Long id) {
        observacionDdjjRepository.deleteById(id);
    }

    public ObservacionDdjjUltimoDto getUltimaObservacionByDdjjAndTipoDph(Long idDdjj, Boolean tipoDph) {

        List<ObservacionDdjj> observaciones = observacionDdjjRepository
                .findUltimaObservacion(idDdjj, tipoDph);

        System.out.println("Cantidad de observaciones encontradas: " + observaciones.size());

        if (observaciones.isEmpty()) {
            return null;
        }

        ObservacionDdjj obs = observaciones.get(0);
        System.out.println("Última observación - ID: " + obs.getId());

        // Tomamos la primera observación (que es la última por el orden DESC)
        ObservacionDdjjUltimoDto resultado = convertToDto(observaciones.get(0));

        return resultado;
    }

    private ObservacionDdjjUltimoDto convertToDto(ObservacionDdjj observacion) {

        String nombre = "No disponible";
        String apellido = "No disponible";

        if (observacion.getUsuario() != null) {

            if (observacion.getUsuario().getPerson() != null) {
                nombre = observacion.getUsuario().getPerson().getNombre();
                apellido = observacion.getUsuario().getPerson().getApellido();
            } else {
                System.out.println("ADVERTENCIA: Usuario no tiene persona asociada");
            }
        } else {
            System.out.println("ADVERTENCIA: Observación no tiene usuario asociado");
        }

        ObservacionDdjjUltimoDto dto = new ObservacionDdjjUltimoDto(
                observacion.getId(),
                observacion.getMotivo(),
                nombre,
                apellido,
                observacion.getFechaCreacion(),
                observacion.getHoraCreacion());

        return dto;
    }

    public List<ObservacionDdjjUltimoDto> getAllObservacionesActivasByDdjjAndTipoDph(Long idDdjj, Boolean tipoDph) {

        List<ObservacionDdjj> observaciones = observacionDdjjRepository
                .findAllObservacionesActivas(idDdjj, tipoDph);

        System.out.println("Cantidad total de observaciones activas encontradas: " + observaciones.size());

        if (observaciones.isEmpty()) {
            return Collections.emptyList();
        }

        // Convertimos todas las observaciones a DTO
        return observaciones.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ObservacionDdjj crearConAdjunto(ObservacionDdjjDto dto, MultipartFile archivo) throws IOException {
        // 1. Crear la entidad base (sin guardar aún o guardando lo básico)
        ObservacionDdjj nuevaObservacion = new ObservacionDdjj();
        nuevaObservacion = this.createUpdate(nuevaObservacion, dto); // Tu método existente de mapeo

        // 2. Lógica del archivo
        if (archivo != null && !archivo.isEmpty()) {
            
            // A. Validaciones específicas (PDF / Excel)
            validarFormatoArchivo(archivo);
            if (archivo.getSize() > 10 * 1024 * 1024) { // 10MB limite por ejemplo
                 throw new IOException("El archivo es demasiado grande (Máx 10MB)");
            }

            // B. Preparar carpetas (Estilo de tu ejemplo: Carpeta por ID de DDJJ para ordenar)
            // Usamos el ID de la DDJJ para agrupar los archivos de rechazo de esa DDJJ
            String nombreCarpeta = "ddjj_" + dto.getIdDdjj(); 
            Path rutaCarpeta = Paths.get(rootUploadDir, nombreCarpeta);
            
            if (!Files.exists(rutaCarpeta)) {
                Files.createDirectories(rutaCarpeta);
            }

            // C. Generar nombre seguro (Mezcla de tu ejemplo + UUID)
            String nombreOriginalLimpios = archivo.getOriginalFilename()
                    .replaceAll("[^a-zA-Z0-9\\.\\-]", "_"); // Solo letras, numeros, puntos y guiones
            
            // Agregamos UUID para evitar colisiones si suben dos veces "archivo.pdf"
            String nombreFinal = UUID.randomUUID().toString().substring(0, 8) + "_" + nombreOriginalLimpios;

            // D. Guardar Físicamente
            Path rutaArchivo = rutaCarpeta.resolve(nombreFinal);
            Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("💾 Archivo guardado en: " + rutaArchivo.toString());

            // E. Guardar la ruta relativa en la BD (para que sea portátil)
            // Guardamos: "ddjj_123/a1b2c3d4_rechazo.pdf"
            nuevaObservacion.setDocumentoRespaldo(nombreCarpeta + "/" + nombreFinal);
        }

        // 3. Guardar cambios finales en BD
        return observacionDdjjRepository.save(nuevaObservacion);
    }

    private void validarFormatoArchivo(MultipartFile archivo) throws IOException {
        String contentType = archivo.getContentType();
        String nombre = archivo.getOriginalFilename();
        
        // Validación robusta: Chequear extensión Y Content-Type
        boolean esPdf = contentType.equals("application/pdf") || nombre.endsWith(".pdf");
        boolean esExcel = contentType.contains("excel") || contentType.contains("spreadsheet") || nombre.endsWith(".xls") || nombre.endsWith(".xlsx");

        if (!esPdf && !esExcel) {
            throw new IOException("Formato no válido. Solo se permiten PDF o Excel.");
        }
    }
    
}
