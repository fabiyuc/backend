package com.guardias.backend.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.guardias.backend.dto.DdjjDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ddjj.AutoridadImagenDto;
import com.guardias.backend.dto.ddjj.DdjjListDto;
import com.guardias.backend.dto.ddjj.EstadoDdjjDto;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.security.entity.Usuario;
import com.guardias.backend.security.service.UsuarioService;
import com.guardias.backend.service.DdjjService;
import com.guardias.backend.service.LegajoService;
import com.guardias.backend.service.ValorGmiService;

import jakarta.validation.ValidationException;

@Controller
@RequestMapping("/ddjj")
@CrossOrigin(origins = "http://localhost:4200")
public class DdjjController {
    @Autowired
    DdjjService ddjjService;

    @Autowired
    ValorGmiService valorGmiService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    LegajoService legajoService;

    @GetMapping("/list")
    public ResponseEntity<List<Ddjj>> list() {
        List<Ddjj> list = ddjjService.findByActivoTrue();
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Ddjj>> listAll() {
        List<Ddjj> list = ddjjService.findAll();
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Ddjj> getById(@PathVariable("id") Long id) {
        if (!ddjjService.activo(id))
            return new ResponseEntity(new Mensaje("Valor no encontrado"), HttpStatus.NOT_FOUND);
        Ddjj ddjj = ddjjService.findById(id).get();
        return new ResponseEntity(ddjj, HttpStatus.OK);
    }

    @GetMapping("/listEfectorMes/{idEfector}/{mes}/{anio}")
    public ResponseEntity<List<Ddjj>> listEfectorMes(@PathVariable("idEfector") Long idEfector,
            @PathVariable("mes") String mes, @PathVariable("anio") int anio) {

        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        if (!ddjjService.existsByAnioAndMes(anio, mesEnum))
            return new ResponseEntity(new Mensaje("La DDJJ no existe"), HttpStatus.NOT_FOUND);

        List<Ddjj> list = ddjjService.findByEfectorIdAndMesAndAnio(idEfector, mesEnum, anio);
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);

    }

    @GetMapping("/listAnioMes/{mes}/{anio}")
    public ResponseEntity<List<Ddjj>> listAnioMes(@PathVariable("mes") String mes, @PathVariable("anio") int anio) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);

        if (!ddjjService.existsByAnioAndMes(anio, mesEnum))
            return new ResponseEntity(new Mensaje("La DDJJ no existe"), HttpStatus.NOT_FOUND);

        List<Ddjj> list = ddjjService.findByByAnioAndMes(anio, mesEnum);
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAnio/{anio}")
    public ResponseEntity<List<Ddjj>> listAnio(@PathVariable("anio") int anio) {

        if (!ddjjService.existsByAnio(anio))
            return new ResponseEntity(new Mensaje("La DDJJ no existe"), HttpStatus.NOT_FOUND);

        List<Ddjj> list = ddjjService.findByByAnio(anio);
        return new ResponseEntity<List<Ddjj>>(list, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DdjjDto ddjjDto) {
        try {
            // 1. Validación general
            ddjjService.validations(ddjjDto);

            // 2. Creación
            Ddjj nuevaDdjj = ddjjService.createUpdate(new Ddjj(), ddjjDto);

            // 3. Respuesta con exito
            return new ResponseEntity<>(new Mensaje("DDJJ creada correctamente"), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // Esto imprimirá el error en la consola
            return new ResponseEntity<>(
                    new Mensaje("Error interno: " + e.getClass().getSimpleName() + ": " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody DdjjDto ddjjDto) {

        if (!ddjjService.activo(id))
            return new ResponseEntity(new Mensaje("DDJJ no existe"), HttpStatus.NOT_FOUND);

        ResponseEntity<?> respuestaValidaciones = ddjjService.validations(ddjjDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            Ddjj ddjj = ddjjService.createUpdate(ddjjService.findById(id).get(), ddjjDto);
            ddjjService.save(ddjj);
            return new ResponseEntity(new Mensaje("Declaracion Jurada modificada correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!ddjjService.activo(id))
            return new ResponseEntity(new Mensaje("DDJJ no existe"), HttpStatus.NOT_FOUND);
        Ddjj ddjj = ddjjService.findById(id).get();
        ddjj.setActivo(false);
        ddjjService.save(ddjj);
        return new ResponseEntity(new Mensaje("Declaracion Jurada eliminada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!ddjjService.existsById(id))
            return new ResponseEntity(new Mensaje("DDJJ no existe"), HttpStatus.NOT_FOUND);

        ddjjService.deleteById(id);
        return new ResponseEntity(new Mensaje("Declaracion Jurada eliminada FISICAMENTE"), HttpStatus.OK);
    }

    @PutMapping("/cambiarEstado")
    public ResponseEntity<?> cambiarEstado(@RequestBody EstadoDdjjDto estadoDdjjDto) {

        try {
            boolean resultado = ddjjService.cambiarEstado(estadoDdjjDto);
            if (resultado) {
                return new ResponseEntity<>(new Mensaje("Se cambió el estado de la Ddjj correctamente."),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Mensaje("No se pudo cambiar el estado de la Ddjj."),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            return new ResponseEntity<>(new Mensaje(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/actualizar-estado-pendienteDPH")
    public ResponseEntity<Mensaje> actualizarEstadoAPendiente(
            @RequestBody List<Long> idsDdjj) {

        try {
            ddjjService.actualizarEstadoAPendiente(idsDdjj);
            return new ResponseEntity<>(
                    new Mensaje(idsDdjj.size() + " DDJJ actualizadas a estado PENDIENTE"),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new Mensaje(e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Mensaje("Error al actualizar estados: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listByEfectorAndEstadoPendiente/{idEfector}")
    public ResponseEntity<?> listByEfectorAndEstadoPendiente(@PathVariable("idEfector") Long idEfector) {
        List<Ddjj> ddjjs = ddjjService.findByEfectorAndEstadoPendiente(idEfector);
        if (ddjjs.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("No se encontraron DDJJ pendientes para el efector"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ddjjs, HttpStatus.OK);
    }

    @GetMapping("/listByEfectorAndEstadoPendienteDph/{idEfector}")
    public ResponseEntity<?> listByEfectorAndEstadoPendienteDph(@PathVariable("idEfector") Long idEfector) {
        List<Ddjj> ddjjs = ddjjService.findByEfectorAndEstadoPendienteDph(idEfector);
        if (ddjjs.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("No se encontraron DDJJ pendientes para el efector"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ddjjs, HttpStatus.OK);
    }

    @GetMapping("/listByEfectorAndEstadoAprobado/{idDirector}/{idEfector}")
    public ResponseEntity<?> listByEfectorAndEstadoAprobado(@PathVariable("idDirector") Long idDirector,
            @PathVariable("idEfector") Long idEfector) {
        List<Ddjj> ddjjs = ddjjService.findByEfectorAndEstadoAprobado(idDirector, idEfector);
        if (ddjjs.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("No se encontraron DDJJ aprobadas para el efector"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ddjjs, HttpStatus.OK);
    }

    @GetMapping("/listByEfectorAndEstadoAprobadoDph/{idEfector}")
    public ResponseEntity<?> listByEfectorAndEstadoAprobadoDph(@PathVariable("idEfector") Long idEfector) {
        List<Ddjj> ddjjs = ddjjService.findByEfectorAndEstadoAprobadoDph(idEfector);
        if (ddjjs.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("No se encontraron DDJJ aprobadas para el efector"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ddjjs, HttpStatus.OK);
    }

    @GetMapping("/existsDdjj/{anio}/{mes}/{idEfector}/{idtipoGuardia}")
    public ResponseEntity<Boolean> existsDdjj(@PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector,
            @PathVariable Long idtipoGuardia) {

        System.out.println("=== INICIO existsDdjj ===");
        System.out.println("Parámetros recibidos:");
        System.out.println(" - anio: " + anio);
        System.out.println(" - mes: " + mes);
        System.out.println(" - idEfector: " + idEfector);
        System.out.println(" - idtipoGuardia: " + idtipoGuardia);
        try {
            MesesEnum mesEnum = MesesEnum.valueOf(mes.toUpperCase());
            System.out.println("Mes convertido a enum: " + mesEnum);
            boolean exists = ddjjService.existsByAnioMesEfectorAndTipoGuardia(anio, mesEnum, idEfector, idtipoGuardia);

            System.out.println("Resultado de la consulta: " + exists);
            System.out.println("=== FIN existsDdjj ===");

            return ResponseEntity.ok(exists);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Mes inválido - " + mes);
            System.out.println("Mensaje de error: " + e.getMessage());
            System.out.println("=== FIN existsDdjj (con error) ===");
            return ResponseEntity.badRequest().body(false);
        }
    }

    /* @GetMapping("/listDdjjCargoyAgrup/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<Ddjj>> listDdjjCargoyAgrup(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<Ddjj> ddjjs = ddjjService
                    .findDdjjCargoyAgrup(anio, mesEnum, idEfector);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de Cargo y Agrup no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    } */

    /* @GetMapping("/listDdjjCargoyaAgrupServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<Ddjj>> listDdjjCargoyAgrupAndServicio(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector,
            @PathVariable("idServicio") Long idServicio) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<Ddjj> ddjjs = ddjjService
                    .findDdjjCargoyAgrupServicio(anio, mesEnum, idEfector, idServicio);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de Cargo y Agrup no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    } */

    /* @GetMapping("/listDdjjExtra/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<Ddjj>> listDdjjExtra(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<Ddjj> ddjjs = ddjjService
                    .findDdjjExtra(anio, mesEnum, idEfector);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de Cargo y Agrup no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    } */

    /* @GetMapping("/listDdjjExtraServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<Ddjj>> listDdjjExtraAndServicio(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector,
            @PathVariable("idServicio") Long idServicio) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<Ddjj> ddjjs = ddjjService
                    .findDdjjExtraServicio(anio, mesEnum, idEfector, idServicio);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de Cargo y Agrup no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    } */

    /* @GetMapping("/listDdjjCf/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<Ddjj>> listDdjjCf(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<Ddjj> ddjjs = ddjjService
                    .findDdjjCf(anio, mesEnum, idEfector);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de Cargo y Agrup no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    } */

    /* @GetMapping("/listDdjjCfServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<Ddjj>> listDdjjCfAndServicio(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector,
            @PathVariable("idServicio") Long idServicio) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<Ddjj> ddjjs = ddjjService
                    .findDdjjCfServicio(anio, mesEnum, idEfector, idServicio);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de Cargo y Agrup no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    } */

    @GetMapping("/getAutoridadImageUrl/{idUsuario}")
    public ResponseEntity<?> getAutoridadImageUrl(@PathVariable("idUsuario") Long idUsuario) {
        try {
            System.out.println("🔍 Buscando usuario con ID: " + idUsuario);

            // Verificar que el usuario existe y está activo
            Optional<Usuario> usuarioOpt = usuarioService.findById(idUsuario);

            System.out.println("📋 Usuario encontrado: " + usuarioOpt.isPresent());
            if (usuarioOpt.isPresent()) {
                System.out.println("✅ Usuario activo: " + usuarioOpt.get().getActivo());
                System.out.println("👤 Nombre usuario: " + usuarioOpt.get().getNombreUsuario());
            }

            if (usuarioOpt.isEmpty() || !usuarioOpt.get().getActivo()) {
                return new ResponseEntity<>(new Mensaje("Usuario no encontrado o inactivo"), HttpStatus.NOT_FOUND);
            }

            Usuario usuario = usuarioOpt.get();

            // Verificar que el usuario tiene una persona asociada
            if (usuario.getPerson() == null) {
                return new ResponseEntity<>(new Mensaje("El usuario no tiene una persona asociada"),
                        HttpStatus.NOT_FOUND);
            }

            Long idPersona = usuario.getPerson().getId();

            // Buscar el legajo activo de autoridad para esa persona
            Optional<Legajo> legajoAutoridadOpt = legajoService.findLegajoAutoridadByPersonaId(idPersona);

            if (legajoAutoridadOpt.isEmpty()) {
                return new ResponseEntity<>(new Mensaje("No se encontró legajo de autoridad activo para esta persona"),
                        HttpStatus.NOT_FOUND);
            }

            Legajo legajoAutoridad = legajoAutoridadOpt.get();

            // Verificar si tiene URL de imagen
            if (legajoAutoridad.getUrl() == null || legajoAutoridad.getUrl().isEmpty()) {
                return new ResponseEntity<>(new Mensaje("La autoridad no tiene imagen asociada"), HttpStatus.NOT_FOUND);
            }

            // Crear y retornar el DTO con la información básica requerida
            AutoridadImagenDto autoridadImagenDto = new AutoridadImagenDto(
                    legajoAutoridad.getUrl(),
                    legajoAutoridad.getPersona().getNombre() + " " + legajoAutoridad.getPersona().getApellido(),
                    legajoAutoridad.getCargo() != null ? legajoAutoridad.getCargo().getDescripcion() : null);

            return new ResponseEntity<>(autoridadImagenDto, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("❌ Error al obtener URL de imagen de autoridad: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(new Mensaje("Error al obtener la URL de imagen: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/existCompleteSet/{anio}/{mes}/{idEfector}")
    public ResponseEntity<Boolean> checkCompleteDdjjSet(
            @PathVariable int anio,
            @PathVariable MesesEnum mes,
            @PathVariable Long idEfector) {

        boolean existsCompleteSet = ddjjService.existsCompleteSetOfDdjj(mes, anio, idEfector);
        return new ResponseEntity<>(existsCompleteSet, HttpStatus.OK);
    }

    @GetMapping("/listCargoyaAgrupServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<DdjjListDto>> listCargoyAgrupAndServicio(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector,
            @PathVariable("idServicio") Long idServicio) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<DdjjListDto> ddjjs = ddjjService
                    .findCargoyAgrupServicio(anio, mesEnum, idEfector, idServicio);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de Cargo y Agrup no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listCargoyaAgrup/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<DdjjListDto>> listCargoyAgrupAnd(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<DdjjListDto> ddjjs = ddjjService
                    .findCargoyAgrup(anio, mesEnum, idEfector);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de Cargo y Agrup no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listExtraServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<DdjjListDto>> listExtraAndServicio(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector,
            @PathVariable("idServicio") Long idServicio) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<DdjjListDto> ddjjs = ddjjService
                    .findExtraServicio(anio, mesEnum, idEfector, idServicio);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de extra no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listExtra/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<DdjjListDto>> listExtraAndServicio(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<DdjjListDto> ddjjs = ddjjService
                    .findExtra(anio, mesEnum, idEfector);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de extra no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listCfServicio/{anio}/{mes}/{idEfector}/{idServicio}")
    public ResponseEntity<List<DdjjListDto>> listCfAndServicio(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector,
            @PathVariable("idServicio") Long idServicio) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<DdjjListDto> ddjjs = ddjjService
                    .findCfServicio(anio, mesEnum, idEfector, idServicio);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de CF no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listCf/{anio}/{mes}/{idEfector}")
    public ResponseEntity<List<DdjjListDto>> listCfAnd(
            @PathVariable int anio,
            @PathVariable String mes,
            @PathVariable Long idEfector) {
        MesesEnum mesEnum = MesesEnum.valueOf(mes);
        try {
            List<DdjjListDto> ddjjs = ddjjService
                    .findCf(anio, mesEnum, idEfector);

            return new ResponseEntity<>(ddjjs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Mensaje("Ddjj de CF no encontrada"),
                    HttpStatus.NOT_FOUND);
        }
    }

}
