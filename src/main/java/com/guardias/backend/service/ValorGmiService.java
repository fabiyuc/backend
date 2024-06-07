package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ValorGmiDto;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.ValorGmi;
import com.guardias.backend.repository.ValorGmiRepository;

@Service
@Transactional
public class ValorGmiService {
    @Autowired
    ValorGmiRepository valorGmiRepository;
    @Autowired
    DdjjService ddjjService;

    public boolean existsById(Long id) {
        return valorGmiRepository.existsById(id);
    }

    public Optional<ValorGmi> findById(Long id) {
        return valorGmiRepository.findById(id);
    }

    public List<ValorGmi> findAll() {
        return valorGmiRepository.findAll();
    }

    public ValorGmi findByActivoTrue() {
        return valorGmiRepository.findByActivoTrue();
    }

    // public List<ValorGmi> findByDate(LocalDate fecha) {
    // return valorGmiRepository.findByDate(fecha);
    // }

    public boolean activo(Long id) {
        return valorGmiRepository.existsById(id) && valorGmiRepository.findById(id).get().isActivo();
    }

    public void save(ValorGmi valorGmi) {
        valorGmiRepository.save(valorGmi);
    }

    public void deleteById(Long id) {
        valorGmiRepository.deleteById(id);
    }

    public ResponseEntity<?> validations(ValorGmiDto valorGmiDto) {

        if (valorGmiDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        if (valorGmiDto.getMonto().compareTo(BigDecimal.ZERO) < 0)
            return new ResponseEntity(new Mensaje("Monto incorrecto"), HttpStatus.BAD_REQUEST);

        if (valorGmiDto.getTipoGuardia() == null)
            return new ResponseEntity(new Mensaje("El tipo de guardia es obligatorio"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public ValorGmi createUpdate(ValorGmi valorGmi, ValorGmiDto valorGmiDto) {

        logicDelete(findByActivoTrue().getId());

        if (valorGmiDto.getFechaInicio() != null && !valorGmiDto.getFechaInicio().equals(valorGmi.getFechaInicio()))
            valorGmi.setFechaInicio(valorGmiDto.getFechaInicio());

        if (valorGmiDto.getFechaFin() != null && !valorGmiDto.getFechaFin().equals(valorGmi.getFechaFin()))
            valorGmi.setFechaFin(valorGmiDto.getFechaFin());

        if (valorGmiDto.getMonto() != null && !valorGmiDto.getMonto().equals(valorGmi.getMonto()))
            valorGmi.setMonto(valorGmiDto.getMonto());

        if (valorGmiDto.getTipoGuardia() != null && !valorGmiDto.getTipoGuardia().equals(valorGmi.getTipoGuardia()))
            valorGmi.setTipoGuardia(valorGmiDto.getTipoGuardia());

        if (valorGmiDto.getIdDdjjs() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (valorGmi.getDdjjs() != null) {
                for (Ddjj ddjj : valorGmi.getDdjjs()) {
                    for (Long id : valorGmiDto.getIdDdjjs()) {
                        if (!ddjj.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                valorGmi.setDdjjs(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? valorGmiDto.getIdDdjjs() : idList;
            for (Long id : idsToAdd) {
                valorGmi.getDdjjs().add(ddjjService.findById(id).get());
                ddjjService.findById(id).get().setValorGmi(valorGmi);
            }
        }

        valorGmi.setActivo(true);
        return valorGmi;
    }

    public ResponseEntity<?> logicDelete(Long id) {
        if (!activo(id))
            return new ResponseEntity(new Mensaje("El valor no existe"), HttpStatus.NOT_FOUND);

        ValorGmi valorGmi = findById(id).get();
        valorGmi.setFechaFin(LocalDate.now());
        valorGmi.setActivo(false);
        save(valorGmi);
        return new ResponseEntity(new Mensaje("Valor actualizado correctamente"), HttpStatus.OK);
    }
}
