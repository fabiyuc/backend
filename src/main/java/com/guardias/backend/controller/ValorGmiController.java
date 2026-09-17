package com.guardias.backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.guardias.backend.dto.ConstanteMonetariaBaseDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.ConstanteMonetariaBase;
import com.guardias.backend.enums.FamiliaValorBaseEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.service.ConstanteMonetariaBaseService;
import com.guardias.backend.service.DdjjService;
import com.guardias.backend.service.ValorGuardiaCargoYagrupService;

@RestController
@RequestMapping("/valorGmi")
@CrossOrigin(origins = "http://localhost:4200")
public class ValorGmiController {

    @Autowired
    ConstanteMonetariaBaseService constanteMonetariaBaseService;

    @Autowired 
    ValorGuardiaCargoYagrupService valorGuardiaCargoYagrupService;
    @Autowired
    DdjjService ddjjService;

    @GetMapping("/list")
    public ResponseEntity<List<ConstanteMonetariaBase>> list() {
        List<ConstanteMonetariaBase> list = constanteMonetariaBaseService.findByActivoTrue().get();
        return new ResponseEntity<List<ConstanteMonetariaBase>>(list, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ConstanteMonetariaBase>> listAll() {
        List<ConstanteMonetariaBase> list = constanteMonetariaBaseService.findAll();
        return new ResponseEntity<List<ConstanteMonetariaBase>>(list, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ConstanteMonetariaBase> getById(@PathVariable("id") Long id) {

        if (!constanteMonetariaBaseService.activo(id))
            return new ResponseEntity(new Mensaje("Valor no encontrado"), HttpStatus.NOT_FOUND);
        ConstanteMonetariaBase valorGmi = constanteMonetariaBaseService.findById(id).get();
        return new ResponseEntity(valorGmi, HttpStatus.OK);
    }

    @GetMapping("/detailByFechaAndTipoGuardia/{fecha}/{tipoGuardia}")
    public ResponseEntity<ConstanteMonetariaBase> getByFechaAndTipoGuardia(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @PathVariable("tipoGuardia") String tipoGuardia) {
        FamiliaValorBaseEnum guardia = FamiliaValorBaseEnum.valueOf(tipoGuardia.toUpperCase());
        Optional<ConstanteMonetariaBase> valorGmi = constanteMonetariaBaseService.getByFechaAndFamilia(fecha, guardia);
        return valorGmi.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/detailByFecha/{fecha}")
    public ResponseEntity<List<ConstanteMonetariaBase>> getByFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        Optional<List<ConstanteMonetariaBase>> valorGmi = constanteMonetariaBaseService.getByFecha(fecha);
        return valorGmi.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ConstanteMonetariaBaseDto constanteMonetariaBaseDto) {
        /*Valida el DTO recibido */
        ResponseEntity<?> respuestaValidaciones = constanteMonetariaBaseService.validations(constanteMonetariaBaseDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            /*Crea/actualiza un ValorGmi con los datos del DTO */
            ConstanteMonetariaBase valorGmi = constanteMonetariaBaseService.createUpdate(new ConstanteMonetariaBase(), constanteMonetariaBaseDto);
            constanteMonetariaBaseService.save(valorGmi);

            /*Genera valores asociados */
            // Llama para crear registros de ValorGuardiaCargoYagrup vinculados al ValorGmi recien creado 
            //constanteMonetariaBaseService.inicializarValoresGuardia();

            return new ResponseEntity(new Mensaje("Valor creado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ConstanteMonetariaBaseDto constanteMonetariaBaseDto) {

        if (!constanteMonetariaBaseService.activo(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);
        ResponseEntity<?> respuestaValidaciones = constanteMonetariaBaseService.validations(constanteMonetariaBaseDto);

        if (respuestaValidaciones.getStatusCode() == HttpStatus.OK) {

            ConstanteMonetariaBase valorGmi = constanteMonetariaBaseService.createUpdate(constanteMonetariaBaseService.findById(id).get(), constanteMonetariaBaseDto);
            constanteMonetariaBaseService.save(valorGmi);
            return new ResponseEntity(new Mensaje("Valor actualizado correctamente"), HttpStatus.OK);
        } else {
            return respuestaValidaciones;
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> logicDelete(@PathVariable("id") Long id) {
        if (!constanteMonetariaBaseService.activo(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);

        return constanteMonetariaBaseService.logicDelete(id);
    }

    @DeleteMapping("/fisicdelete/{id}")
    public ResponseEntity<?> fisicDelete(@PathVariable("id") long id) {
        if (!constanteMonetariaBaseService.existsById(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);

        constanteMonetariaBaseService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("Valor eliminado FISICAMENTEE"), HttpStatus.OK);
    }
}
