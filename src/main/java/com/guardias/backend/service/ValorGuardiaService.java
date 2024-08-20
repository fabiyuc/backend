package com.guardias.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ValorGuardiaDto;
import com.guardias.backend.entity.ValorGuardia;
import com.guardias.backend.repository.ValorGuardiaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ValorGuardiaService {
    
    @Autowired
    ValorGuardiaRepository valorGuardiaRepository;

     public Optional<List<ValorGuardia>> findByActivoTrue() {
        return valorGuardiaRepository.findByActivoTrue();
    }

    public List<ValorGuardia> findAll() {
        return valorGuardiaRepository.findAll();
    }

    public Optional<ValorGuardia> findById(Long id) {
        return valorGuardiaRepository.findById(id);
    }

    public void save(ValorGuardia valorGmi) {
        valorGuardiaRepository.save(valorGmi);
    }

    public void deleteById(Long id) {
        valorGuardiaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return valorGuardiaRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (valorGuardiaRepository.existsById(id) && valorGuardiaRepository.findById(id).get().isActivo());
    }

    /* public Optional<List<ValorGuardia>> getByFecha(LocalDate fecha) {
        return valorGuardiaRepository.getByFecha(fecha);
    } */


   /*  public ResponseEntity<?> validations(ValorGuardiaDto valorGuardiaDto) {

        if (valorGuardiaDto.getFechaInicio() == null)
            return new ResponseEntity(new Mensaje("la fecha de inicio es obligatoria"), HttpStatus.BAD_REQUEST);

        //FALTA VALIDAR SEGUN EL NIVEL QUE PUEDE O NO SER NULL
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    } */
/* 
    public ValorGuardia createUpdate(ValorGuardia valorGuardia, ValorGuardiaDto valorGuardiaDto) {

        valorGuardia.setActivo(true);

        valorGuardia.setNivelComplejidad(valorGuardiaDto.getNivelComplejidad());
        
        if (valorGuardiaDto.getFechaInicio() != null && !valorGuardiaDto.getFechaInicio().equals(valorGuardia.getFechaInicio()))
            valorGuardia.setFechaInicio(valorGuardiaDto.getFechaInicio());

        if (valorGuardiaDto.getFechaFin() != null && !valorGuardiaDto.getFechaFin().equals(valorGuardia.getFechaFin()))
        valorGuardia.setFechaFin(valorGuardiaDto.getFechaFin());
        
        
        if (valorGuardiaDto.getDecreto1178Lav() != null && !valorGuardiaDto.getDecreto1178Lav().equals(valorGuardia.getDecreto1178Lav())){
            valorGuardia.setDecreto1178Lav(valorGuardiaDto.getDecreto1178Lav());

            valorGuardia.setDecreto1178Sdf(valorGuardiaDto.getDecreto1178Lav().add(valorGuardiaDto.getDecreto1178Lav().multiply(new BigDecimal("0.10")))); //suma al valor original (Decreto1178Lav) un 10% del mismo valor, utilizando el método add junto con multiply.
        }

        if (valorGuardiaDto.getBono1657Lav() != null && !valorGuardiaDto.getBono1657Lav().equals(valorGuardia.getBono1657Lav())){
            valorGuardia.setBono1657Lav(valorGuardiaDto.getBono1657Lav());

            valorGuardia.setBono1657Sdf(valorGuardiaDto.getBono1657Lav().add(valorGuardiaDto.getBono1657Lav().multiply(new BigDecimal("0.10")))); //suma al valor original (Bono1657Lsv) un 10% del mismo valor, utilizando el método add junto con multiply.
        }

        if (valorGuardiaDto.getBono1580Lav() != null && !valorGuardiaDto.getBono1580Lav().equals(valorGuardia.getBono1580Lav())){
            valorGuardia.setBono1580Lav(valorGuardiaDto.getBono1580Lav());

            valorGuardia.setBono1580Sdf(valorGuardiaDto.getBono1580Lav().add(valorGuardiaDto.getBono1580Lav().multiply(new BigDecimal("0.10")))); //suma al valor original (Bono1580Lsv) un 10% del mismo valor, utilizando el método add junto con multiply.
        }

        if (valorGuardiaDto.getResolucion2575Lav() != null && !valorGuardiaDto.getResolucion2575Lav().equals(valorGuardia.getResolucion2575Lav())){
            valorGuardia.setResolucion2575Lav(valorGuardiaDto.getResolucion2575Lav());

            valorGuardia.setResolucion2575Sdf(valorGuardiaDto.getResolucion2575Lav().add(valorGuardiaDto.getResolucion2575Lav().multiply(new BigDecimal("0.10")))); //suma al valor original (Resolucion2575Lav) un 10% del mismo valor, utilizando el método add junto con multiply.
        }
        
        



       /* falta hacer calculos del total segun el nivel de complejidad y tipo de guardia */

        
    /*     return valorGmi;
    } */ 


}
