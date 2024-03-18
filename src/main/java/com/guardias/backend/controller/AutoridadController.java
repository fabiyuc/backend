package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

import com.guardias.backend.dto.AutoridadDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.service.AutoridadService;
import com.guardias.backend.service.EfectorService;
import com.guardias.backend.service.PersonService;

@RestController
@RequestMapping("/autoridad")
@CrossOrigin(origins = "http://localhost:4200")
public class AutoridadController {

    @Autowired
    AutoridadService autoridadService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    PersonService personService;

    @GetMapping("/list")
    public ResponseEntity<List<Autoridad>> list() {
        List<Autoridad> list = autoridadService.findByActivoTrue().get();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Autoridad>> listAll() {
        List<Autoridad> list = autoridadService.findAll();
        return new ResponseEntity<List<Autoridad>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Autoridad> getById(@PathVariable("id") Long id) {
        if (!autoridadService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la autoridad"), HttpStatus.NOT_FOUND);
        Autoridad autoridad = autoridadService.findById(id).get();
        return new ResponseEntity<Autoridad>(autoridad, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<Autoridad> getByNombre(@PathVariable("nombre") String nombre) {
        if (!autoridadService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("No existe la autoridad"), HttpStatus.NOT_FOUND);
        Autoridad autoridad = autoridadService.findByNombre(nombre).get();
        return new ResponseEntity(autoridad, HttpStatus.OK);
    }

    @GetMapping("/list/{fechaInicio}")
    public ResponseEntity<List<Autoridad>> getByFechainicio(
            @PathVariable("fechaInicio") LocalDate fechaInicio) {
        List<Autoridad> list = autoridadService.findByFechaInicio(fechaInicio);
        return new ResponseEntity<List<Autoridad>>(list, HttpStatus.OK);
    }

    @GetMapping("/detailefector/{idEfector}")
    public ResponseEntity<List<Autoridad>> getByEfector(@PathVariable("idEfector") Long idEfector) {
        if (!autoridadService.existsByEfectorId(idEfector))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);
        List<Autoridad> autoridad = autoridadService.findByEfectorId(idEfector).get();
        return new ResponseEntity<>(autoridad, HttpStatus.OK);
    }

    @GetMapping("/detailpersona/{idPersona}")
    public ResponseEntity<List<Autoridad>> getByPersona(@PathVariable("idPersona") Long idPersona) {
        if (!autoridadService.existsByPersonaId(idPersona))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);
        List<Autoridad> autoridad = autoridadService.findByPersonaId(idPersona).get();
        return new ResponseEntity<>(autoridad, HttpStatus.OK);
    }

    /*
     * // TODO hacer las validaciones y el createUpdate
     * public ResponseEntity<?> validations(AutoridadDto autoridadDto) {
     * if (StringUtils.isBlank(autoridadDto.getNombre()))
     * return new ResponseEntity<>(new Mensaje("El Nombre es obligatorio"),
     * HttpStatus.BAD_REQUEST);
     * 
     * if (autoridadService.existsByNombre(autoridadDto.getNombre()))
     * return new ResponseEntity<>(new Mensaje("El Nombre ya existe"),
     * HttpStatus.BAD_REQUEST);
     * 
     * if (autoridadDto.getFechaInicio() == null)
     * return new ResponseEntity<>(new Mensaje("La fecha de inicio es obligatoria"),
     * HttpStatus.BAD_REQUEST);
     * 
     * return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
     * }
     * 
     * private Autoridad createUpdate(Autoridad autoridad, AutoridadDto
     * autoridadDto) {
     * if (autoridad.getNombre() != autoridadDto.getNombre() &&
     * autoridadDto.getNombre() != null)
     * autoridad.setNombre(autoridadDto.getNombre());
     * 
     * if (autoridad.getFechaInicio() != autoridadDto.getFechaInicio() &&
     * autoridadDto.getFechaInicio() != null)
     * autoridad.setFechaInicio(autoridadDto.getFechaInicio());
     * 
     * if (autoridad.getFechaFinal() != autoridadDto.getFechaFinal() &&
     * autoridadDto.getFechaFinal() != null)
     * autoridad.setFechaFinal(autoridadDto.getFechaFinal());
     * 
     * autoridad.setEsActual(autoridadDto.isEsActual());
     * 
     * autoridad.setEsRegional(autoridadDto.isEsRegional());
     * 
     * if (autoridad.getEfector() != autoridadDto.getEfector() &&
     * autoridadDto.getEfector() != null)
     * autoridad.setEfector(autoridadDto.getEfector());
     * 
     * if (autoridad.getPersona() != autoridadDto.getPersona() &&
     * autoridadDto.getPersona() != null)
     * autoridad.setPersona(autoridadDto.getPersona());
     * 
     * return autoridad;
     * }
     */

    public ResponseEntity<?> validationsCreate(AutoridadDto autoridadDto) {
        ResponseEntity<?> respuestaValidaciones = validations(autoridadDto);

        if (autoridadService.existsByNombre(autoridadDto.getNombre()))
            return new ResponseEntity<Mensaje>(new Mensaje("Ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        return respuestaValidaciones;
    }

    public ResponseEntity<?> validationsUpdate(AutoridadDto autoridadDto) {

        // Verificar si la fecha de finalización es nula
        if (autoridadDto.getFechaFinal() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La fecha de finalización es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        // Verificar si el ID del efector es nulo
        if (autoridadDto.getIdEfector() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El efector es obligatorio"), HttpStatus.BAD_REQUEST);

        // Verificar si el ID de la persona es nulo
        if (autoridadDto.getIdPersona() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La persona es obligatoria"), HttpStatus.BAD_REQUEST);

        // Todas las validaciones pasaron, retornar OK
        return new ResponseEntity<>(new Mensaje("Validación exitosa"), HttpStatus.OK);
    }

    public ResponseEntity<?> validations(AutoridadDto autoridadDto) {
        if (autoridadDto.getNombre() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El Nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (autoridadService.existsByNombre(autoridadDto.getNombre()))
            return new ResponseEntity<Mensaje>(new Mensaje("El Nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (autoridadDto.getFechaInicio() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La fecha de inicio es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (autoridadDto.getFechaFinal() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("La fecha de finalización es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        if (autoridadDto.getIdEfector() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("El efector es obligatorio"), HttpStatus.BAD_REQUEST);

        if (autoridadDto.getIdPersona() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("la persona es obligatoria"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Autoridad createUpdate(Autoridad autoridad, AutoridadDto autoridadDto) {
        if (!Objects.equals(autoridadDto.getNombre(), autoridad.getNombre()))
            autoridad.setNombre(autoridadDto.getNombre());

        if (autoridad.getFechaInicio() != null)
            autoridadDto.setFechaInicio(autoridad.getFechaInicio());

        if (autoridad.getFechaFinal() != autoridadDto.getFechaFinal() && autoridadDto.getFechaFinal() != null)
            autoridad.setFechaFinal(autoridadDto.getFechaFinal());

        autoridad.setEsRegional(autoridadDto.isEsRegional());

        if (autoridad.getEfector() == null ||
                (autoridadDto.getIdEfector() != null &&
                        !Objects.equals(autoridad.getEfector().getId(),
                                autoridadDto.getIdEfector()))) {
            autoridad.setEfector(efectorService.findEfector(autoridadDto.getIdEfector()));
        }

        if (autoridad.getPersona() == null ||
                (autoridadDto.getIdPersona() != null &&
                        !Objects.equals(autoridad.getPersona().getId(),
                                autoridadDto.getIdPersona()))) {
            autoridad.setPersona(personService.findPerson(autoridadDto.getIdPersona()));
        }

        return autoridad;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AutoridadDto autoridadDto) {

        ResponseEntity<?> respuestaValidaciones = validations(autoridadDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {
            Autoridad autoridad = createUpdate(new Autoridad(), autoridadDto);
            autoridad.setEsActual(true);
            autoridad.setActivo(true);
            autoridadService.save(autoridad);

            return new ResponseEntity<>(new Mensaje("asistencial creado"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody AutoridadDto autoridadDto) {
        // Verificar si la autoridad existe y está activa
        if (!autoridadService.activo(id))
            return new ResponseEntity(new Mensaje("No existe la autoridad"), HttpStatus.NOT_FOUND);

        // Obtener la autoridad a actualizar
        Autoridad autoridadExistente = autoridadService.findById(id).orElse(null);
        if (autoridadExistente == null) {
            return new ResponseEntity(new Mensaje("No se encontró la autoridad a actualizar"), HttpStatus.NOT_FOUND);
        }

        // Verificar si el nombre enviado es null, si es así, mantener el nombre actual
        String nuevoNombre = autoridadDto.getNombre();
        if (nuevoNombre == null) {
            nuevoNombre = autoridadExistente.getNombre();
        } else {
            // Verificar si el nuevo nombre ya está siendo utilizado por otra autoridad
            // activa
            if (autoridadService.existsByNombreAndIdNot(nuevoNombre, id)) {
                return new ResponseEntity(new Mensaje("El nombre ya está siendo utilizado por otra autoridad"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        // Validar el formato de la fecha de finalización, y la existencia de efector y
        // persona
        ResponseEntity<?> respuestaValidaciones = validationsUpdate(autoridadDto);
        if (respuestaValidaciones.getStatusCode() != HttpStatus.OK) {
            return respuestaValidaciones;
        }

        // Actualizar la autoridad con el nombre actualizado
        autoridadDto.setNombre(nuevoNombre);
        Autoridad autoridadActualizada = createUpdate(autoridadExistente, autoridadDto);

        // Mantener la fecha de inicio existente
        autoridadActualizada.setFechaInicio(autoridadExistente.getFechaInicio());

        autoridadService.save(autoridadActualizada);

        return new ResponseEntity<>(new Mensaje("Autoridad actualizada"), HttpStatus.OK);
    }

    /*
     * @PostMapping("/{idAutoridad}/addEfector/{idEfector}")
     * public ResponseEntity<?> agregarEfector(@PathVariable("idAutoridad") Long
     * idAutoridad,
     * 
     * @PathVariable("idEfector") Long idEfector) {
     * try {
     * autoridadService.agregarEfector(idAutoridad, idEfector);
     * return new ResponseEntity<>(new Mensaje("Efector agregado"), HttpStatus.OK);
     * } catch (EntityNotFoundException e) {
     * return new ResponseEntity<>(new Mensaje("No se pudo agregar el efector"),
     * HttpStatus.NOT_FOUND);
     * } catch (Exception e) {
     * e.printStackTrace();
     * return new ResponseEntity<>(new Mensaje("Error al agregar el efector"),
     * HttpStatus.BAD_REQUEST);
     * }
     * }
     * 
     * @PostMapping("/{idAutoridad}/addPersona/{idPersona}")
     * public ResponseEntity<?> agregarPersona(@PathVariable("idAutoridad") Long
     * idAutoridad,
     * 
     * @PathVariable("idPersona") Long idPersona) {
     * try {
     * autoridadService.agregarPersona(idAutoridad, idPersona);
     * return new ResponseEntity<>(new Mensaje("Persona agregada"), HttpStatus.OK);
     * } catch (EntityNotFoundException e) {
     * return new ResponseEntity<>(new Mensaje("No se pudo agregar la persona"),
     * HttpStatus.NOT_FOUND);
     * } catch (Exception e) {
     * return new ResponseEntity<>(new Mensaje("Error al agregar la persona"),
     * HttpStatus.BAD_REQUEST);
     * }
     * }
     */
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!autoridadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);
        Autoridad autoridad = autoridadService.findById(id).get();
        autoridad.setActivo(false);
        autoridadService.save(autoridad);
        return new ResponseEntity(new Mensaje("autoridad eliminada"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {

        if (!autoridadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe la autoridad"), HttpStatus.NOT_FOUND);
        autoridadService.deleteById(id);
        return new ResponseEntity(new Mensaje("autoridad eliminada FISICAMENTE"), HttpStatus.OK);
    }
}
