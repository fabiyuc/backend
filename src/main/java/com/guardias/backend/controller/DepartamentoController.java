package com.guardias.backend.controller;

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
import com.guardias.backend.dto.DepartamentoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Departamento;
import com.guardias.backend.service.DepartamentoService;
import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/departamento")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartamentoController {
    @Autowired
    DepartamentoService departamentoService;

    @GetMapping("/list")
    public ResponseEntity<List<Departamento>> list() {
        List<Departamento> list = departamentoService.findByActivo();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Departamento>> listAll() {
        List<Departamento> list = departamentoService.findAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<List<Departamento>> getById(@PathVariable("id") Long id) {
        if (!departamentoService.existsById(id))
            return new ResponseEntity(new Mensaje("departamento no existe"), HttpStatus.NOT_FOUND);
        Departamento departamento = departamentoService.findById(id).get();
        return new ResponseEntity(departamento, HttpStatus.OK);
    }

    @GetMapping("/detailnombre/{nombre}")
    public ResponseEntity<List<Departamento>> getByNombre(@PathVariable("nombre") String nombre) {
        if (!departamentoService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("departamento no existe"), HttpStatus.NOT_FOUND);
        Departamento departamento = departamentoService.getByNombre(nombre).get();
        return new ResponseEntity(departamento, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DepartamentoDto departamentoDto) {
        if (StringUtils.isBlank(departamentoDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (departamentoService.existsByNombre(departamentoDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(departamentoDto.getCodigoPostal()))
            return new ResponseEntity(new Mensaje("el codigo postal es obligatorio"),
                    HttpStatus.BAD_REQUEST);

        if (departamentoService.existsByCodigoPostal(departamentoDto.getCodigoPostal()))
            return new ResponseEntity(new Mensaje("ese CP ya existe"),
                    HttpStatus.BAD_REQUEST);

        // ******************** el control de provincia valida es necesario???? lo
        // obtiene de un select al valor
        if (departamentoDto.getProvincia() == null)
            return new ResponseEntity(new Mensaje("la provincia  es obligatoria"),
                    HttpStatus.BAD_REQUEST);

        Departamento departamento = new Departamento();
        departamento.setNombre(departamentoDto.getNombre());
        departamento.setCodigoPostal(departamentoDto.getCodigoPostal());

        // ******* no necesito guardar ni modificar la listas */
        // departamento.setLocalidades(departamentoDto.getLocalidades());

        departamento.setProvincia(departamentoDto.getProvincia());

        departamentoService.save(departamento);
        return new ResponseEntity(new Mensaje("Departamento creado"), HttpStatus.OK);
    }

    @PutMapping(("/update/{id}"))
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody DepartamentoDto departamentoDto) {
        if (!departamentoService.existsById(id))
            return new ResponseEntity(new Mensaje("El departamento no existe"), HttpStatus.NOT_FOUND);

        if (departamentoService.existsByNombre(departamentoDto.getNombre()) &&
                departamentoService.getByNombre(departamentoDto.getNombre()).get().getId() != id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(departamentoDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(departamentoDto.getCodigoPostal()))
            return new ResponseEntity(new Mensaje("el CP es obligatorio"), HttpStatus.BAD_REQUEST);

        if (departamentoService.existsByCodigoPostal(departamentoDto.getCodigoPostal())
                && departamentoService.getByCodigoPostal(departamentoDto.getCodigoPostal()).get().getId() != id)
            return new ResponseEntity(new Mensaje("ese CP ya existe"), HttpStatus.BAD_REQUEST);

        if (departamentoDto.getProvincia() == null)
            return new ResponseEntity(new Mensaje("indicar la provincia"),
                    HttpStatus.BAD_REQUEST);

        Departamento departamento = departamentoService.findById(id).get();

        // ******* La validacion antes de setear los valores me gusta que sea en la
        // misma linea pero no muestra mensajes de error

        // ******* Ahora está mostrando los msjs de error por la validacion previa, ver
        // como queda para limpiar el codigo */

        if (!departamentoDto.getNombre().equals(departamento.getNombre()))
            departamento.setNombre(departamentoDto.getNombre());
        if (!departamentoDto.getCodigoPostal().equals(departamento.getCodigoPostal()))
            departamento.setCodigoPostal(departamentoDto.getCodigoPostal());
        if (!departamentoDto.getProvincia().equals(departamento.getProvincia()))
            departamento.setProvincia(departamentoDto.getProvincia());

        // ******* no necesito guardar ni modificar la listas */
        // if (!departamentoDto.getLocalidades().equals(departamento.getLocalidades()))
        // departamento.setLocalidades(departamentoDto.getLocalidades());

        departamentoService.save(departamento);
        return new ResponseEntity(new Mensaje("Departamento actualizado"), HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!departamentoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el departamento"), HttpStatus.NOT_FOUND);

        Departamento departamento = departamentoService.findById(id).get();
        departamento.setActivo(false);
        departamentoService.save(departamento);
        return new ResponseEntity(new Mensaje("departamento eliminado"), HttpStatus.OK);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") Long id) {

        if (!departamentoService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe el departamento"), HttpStatus.NOT_FOUND);
        departamentoService.deleteById(id);
        return new ResponseEntity(new Mensaje("departamento eliminado"), HttpStatus.OK);
    }
}
