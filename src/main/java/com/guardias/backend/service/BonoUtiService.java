package com.guardias.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.BonoUtiDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.BonoUti;
import com.guardias.backend.repository.BonoUtiRepository;

import io.micrometer.common.util.StringUtils;

@Service
@Transactional
public class BonoUtiService {
    @Autowired
    BonoUtiRepository bonoUtiRepository;
    
    public Optional<List<BonoUti>> findByActivoTrue() {
        return bonoUtiRepository.findByActivoTrue();
    }

    public List<BonoUti> findAll() {
        return bonoUtiRepository.findAll();
    }

    public Optional<BonoUti> findById(Long id) {
        return bonoUtiRepository.findById(id);
    }

    public void save(BonoUti bonoUti) {
        bonoUtiRepository.save(bonoUti);
    }

    public void deleteById(Long id) {
        bonoUtiRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return bonoUtiRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (bonoUtiRepository.existsById(id) && bonoUtiRepository.findById(id).get().isActivo());
    }

    public ResponseEntity<?> validations(BonoUtiDto bonoUtiDto) {

        if (bonoUtiDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        if (bonoUtiDto.getMonto().compareTo(BigDecimal.ZERO) < 0)
            return new ResponseEntity(new Mensaje("Monto incorrecto"), HttpStatus.BAD_REQUEST);

        if (StringUtils.isBlank(bonoUtiDto.getDocumentoLegal())) {
            return new ResponseEntity<>(new Mensaje("es obligatorio indicar el documento legal"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public BonoUti createUpdate(BonoUti bonoUti, BonoUtiDto bonoUtiDto) {

        if (bonoUtiDto.getFechaInicio() != null && !bonoUtiDto.getFechaInicio().equals(bonoUti.getFechaInicio()))
            bonoUti.setFechaInicio(bonoUtiDto.getFechaInicio());

        if (bonoUtiDto.getFechaFin() != null && !bonoUtiDto.getFechaFin().equals(bonoUti.getFechaFin()))
            bonoUti.setFechaFin(bonoUtiDto.getFechaFin());

        if (bonoUtiDto.getMonto() != null && !bonoUtiDto.getMonto().equals(bonoUti.getMonto()))
            bonoUti.setMonto(bonoUtiDto.getMonto());
        
        if (bonoUtiDto.getDocumentoLegal() != null && !bonoUtiDto.getDocumentoLegal().equals(bonoUti.getDocumentoLegal())
            && !bonoUtiDto.getDocumentoLegal().isEmpty())
        bonoUti.setDocumentoLegal(bonoUtiDto.getDocumentoLegal());

        bonoUti.setActivo(true);
        return bonoUti;
    }

    public ResponseEntity<?> logicDelete(Long id) {
        BonoUti bonoUti = findById(id).get();
        bonoUti.setActivo(false);
        save(bonoUti);
        return new ResponseEntity(new Mensaje("Valor actualizado correctamente"), HttpStatus.OK);
    }
}
