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

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ValorGmiDto;
import com.guardias.backend.entity.ValorGmi;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.ValorGmiRepository;

@Service
@Transactional
public class ValorGmiService {
    @Autowired
    ValorGmiRepository valorGmiRepository;

    public Optional<List<ValorGmi>> findByActivoTrue() {
        return valorGmiRepository.findByActivoTrue();
    }

    public List<ValorGmi> findAll() {
        return valorGmiRepository.findAll();
    }

    public Optional<ValorGmi> findById(Long id) {
        return valorGmiRepository.findById(id);
    }

    public void save(ValorGmi valorGmi) {
        valorGmiRepository.save(valorGmi);
    }

    public void deleteById(Long id) {
        valorGmiRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return valorGmiRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (valorGmiRepository.existsById(id) && valorGmiRepository.findById(id).get().isActivo());
    }

    public Optional<List<ValorGmi>> getByFecha(LocalDate fecha) {
        return valorGmiRepository.getByFecha(fecha);
    }

    public Optional<ValorGmi> getByFechaAndTipoGuardia(LocalDate fecha, TipoGuardiaEnum tipoGuardia) {
        return valorGmiRepository.getByFechaAndTipoGuardia(fecha, tipoGuardia);
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

        if (valorGmiDto.getFechaInicio() != null && !valorGmiDto.getFechaInicio().equals(valorGmi.getFechaInicio()))
            valorGmi.setFechaInicio(valorGmiDto.getFechaInicio());

        if (valorGmiDto.getFechaFin() != null && !valorGmiDto.getFechaFin().equals(valorGmi.getFechaFin()))
            valorGmi.setFechaFin(valorGmiDto.getFechaFin());

        if (valorGmiDto.getMonto() != null && !valorGmiDto.getMonto().equals(valorGmi.getMonto()))
            valorGmi.setMonto(valorGmiDto.getMonto());

        if (valorGmiDto.getTipoGuardia() != null && !valorGmiDto.getTipoGuardia().equals(valorGmi.getTipoGuardia()))
            valorGmi.setTipoGuardia(valorGmiDto.getTipoGuardia());

        valorGmi.setActivo(true);
        return valorGmi;
    }

    public ResponseEntity<?> logicDelete(Long id) {
        ValorGmi valorGmi = findById(id).get();
        valorGmi.setActivo(false);
        save(valorGmi);
        return new ResponseEntity(new Mensaje("Valor actualizado correctamente"), HttpStatus.OK);
    }

}
