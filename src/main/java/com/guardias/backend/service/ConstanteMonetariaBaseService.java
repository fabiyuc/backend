package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.ConstanteMonetariaBaseDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.ConstanteMonetariaBase;
import com.guardias.backend.enums.FamiliaValorBaseEnum;
import com.guardias.backend.repository.ConstateMonetariaBaseRepository;

import io.micrometer.common.util.StringUtils;

@Service
@Transactional
public class ConstanteMonetariaBaseService {
    @Autowired
    ConstateMonetariaBaseRepository constanteMonetariaBaseRepository;

    public Optional<List<ConstanteMonetariaBase>> findByActivoTrue() {
        return constanteMonetariaBaseRepository.findByActivoTrue();
    }

    public List<ConstanteMonetariaBase> findAll() {
        return constanteMonetariaBaseRepository.findAll();
    }

    public Optional<ConstanteMonetariaBase> findById(Long id) {
        return constanteMonetariaBaseRepository.findById(id);
    }

    public void save(ConstanteMonetariaBase constanteMonetariaBase) {
        constanteMonetariaBaseRepository.save(constanteMonetariaBase);
    }

    public void deleteById(Long id) {
        constanteMonetariaBaseRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return constanteMonetariaBaseRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (constanteMonetariaBaseRepository.existsById(id)
                && constanteMonetariaBaseRepository.findById(id).get().isActivo());
    }

    public Optional<List<ConstanteMonetariaBase>> getByFecha(LocalDate fecha) {
        return constanteMonetariaBaseRepository.getByFecha(fecha);
    }

    public Optional<ConstanteMonetariaBase> getByFechaAndFamilia(LocalDate fecha, FamiliaValorBaseEnum familia) {
        return constanteMonetariaBaseRepository.getByFechaAndFamilia(fecha, familia);
    }

    public ResponseEntity<?> validations(ConstanteMonetariaBaseDto constanteMonetariaBaseDto) {

        if (constanteMonetariaBaseDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        if (constanteMonetariaBaseDto.getMonto().compareTo(BigDecimal.ZERO) < 0)
            return new ResponseEntity(new Mensaje("Monto incorrecto"), HttpStatus.BAD_REQUEST);

        /*
         * if (constanteMonetariaBaseDto.getTipoGuardia() == null)
         * return new ResponseEntity(new Mensaje("El tipo de guardia es obligatorio"),
         * HttpStatus.BAD_REQUEST);
         */
        if (constanteMonetariaBaseDto.getFamiliaValorBase() == null)
            return new ResponseEntity(new Mensaje("La familia del valor base es obligatoria"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(constanteMonetariaBaseDto.getDocumentoLegal())) {
            return new ResponseEntity<>(new Mensaje("es obligatorio indicar el documento legal"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public ConstanteMonetariaBase createUpdate(ConstanteMonetariaBase constanteMonetariaBase,
            ConstanteMonetariaBaseDto constanteMonetariaBaseDto) {

        // Cierre automático: si hay un vigente de la misma familia sin fechaFin, se cierra
        if (constanteMonetariaBaseDto.getFechaInicio() != null
                && constanteMonetariaBaseDto.getFamiliaValorBase() != null) {
            constanteMonetariaBaseRepository
                    .getByFechaAndFamilia(constanteMonetariaBaseDto.getFechaInicio().minusDays(1),
                            constanteMonetariaBaseDto.getFamiliaValorBase())
                    .ifPresent(vigenteAnterior -> {
                        if (!vigenteAnterior.getId().equals(constanteMonetariaBase.getId())
                                && vigenteAnterior.getFechaFin() == null) {
                            vigenteAnterior.setFechaFin(constanteMonetariaBaseDto.getFechaInicio().minusDays(1));
                            constanteMonetariaBaseRepository.save(vigenteAnterior);
                        }
                    });
        }
        if (constanteMonetariaBaseDto.getFechaInicio() != null
                && !constanteMonetariaBaseDto.getFechaInicio().equals(constanteMonetariaBase.getFechaInicio()))
            constanteMonetariaBase.setFechaInicio(constanteMonetariaBaseDto.getFechaInicio());

        if (constanteMonetariaBaseDto.getFechaFin() != null
                && !constanteMonetariaBaseDto.getFechaFin().equals(constanteMonetariaBase.getFechaFin()))
            constanteMonetariaBase.setFechaFin(constanteMonetariaBaseDto.getFechaFin());

        if (constanteMonetariaBaseDto.getMonto() != null
                && !constanteMonetariaBaseDto.getMonto().equals(constanteMonetariaBase.getMonto()))
            constanteMonetariaBase.setMonto(constanteMonetariaBaseDto.getMonto());

        /*
         * if (constanteMonetariaBaseDto.getTipoGuardia() != null &&
         * !constanteMonetariaBaseDto.getTipoGuardia().equals(constanteMonetariaBase.
         * getTipoGuardia()))
         * constanteMonetariaBase.setTipoGuardia(constanteMonetariaBaseDto.
         * getTipoGuardia());
         */

        if (constanteMonetariaBaseDto.getFamiliaValorBase() != null)
            constanteMonetariaBase.setFamiliaValorBase(constanteMonetariaBaseDto.getFamiliaValorBase());

        if (constanteMonetariaBaseDto.getDocumentoLegal() != null
                && !constanteMonetariaBaseDto.getDocumentoLegal().equals(constanteMonetariaBase.getDocumentoLegal())
                && !constanteMonetariaBaseDto.getDocumentoLegal().isEmpty())
            constanteMonetariaBase.setDocumentoLegal(constanteMonetariaBaseDto.getDocumentoLegal());

        constanteMonetariaBase.setActivo(true);
        return constanteMonetariaBase;
    }

    public ResponseEntity<?> logicDelete(Long id) {
        ConstanteMonetariaBase constanteMonetariaBase = findById(id).get();
        constanteMonetariaBase.setActivo(false);
        save(constanteMonetariaBase);
        return new ResponseEntity(new Mensaje("Valor actualizado correctamente"), HttpStatus.OK);
    }

}
