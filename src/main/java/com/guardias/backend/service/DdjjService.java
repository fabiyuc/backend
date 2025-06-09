package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.DdjjDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.DdjjRepository;

@Service
@Transactional
public class DdjjService {
    @Autowired
    DdjjRepository ddjjRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    ValorGmiService valorGmiService;
    @Autowired
    RegistroMensualService registroMensualService;

    public boolean existsById(Long id) {
        return ddjjRepository.existsById(id);
    }

    public Optional<Ddjj> findById(Long id) {
        return ddjjRepository.findById(id);
    }

    public List<Ddjj> findAll() {
        return ddjjRepository.findAll();
    }

    public List<Ddjj> findByActivoTrue() {
        return ddjjRepository.findByActivoTrue();
    }

    public boolean activo(Long id) {
        return ddjjRepository.existsById(id) && ddjjRepository.findById(id).get().isActivo();
    }

    public boolean existsByAnioAndMes(int anio, MesesEnum mes) {
        return ddjjRepository.existsByAnioAndMes(anio, mes);
    }

    public boolean existsByAnio(int anio) {
        return ddjjRepository.existsByAnio(anio);
    }

    public List<Ddjj> findByByAnioAndMes(int anio, MesesEnum mes) {
        return ddjjRepository.findByAnioAndMes(anio, mes);
    }

    public List<Ddjj> findByEfectorIdAndMesAndAnio(Long efectorId, MesesEnum mes, int anio) {
        return ddjjRepository.findByEfectorIdAndMesAndAnio(efectorId, mes, anio);
    }

    public List<Ddjj> findByByAnio(int anio) {
        return ddjjRepository.findByAnio(anio);
    }

    public void save(Ddjj ddjj) {
        ddjjRepository.save(ddjj);
    }

    public void deleteById(Long id) {
        ddjjRepository.deleteById(id);
    }

    public ResponseEntity<?> validations(DdjjDto ddjjDto) {

        if (ddjjDto.getMes() == null)
            return new ResponseEntity(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getAnio() < 1991)
            return new ResponseEntity(new Mensaje("El año es incorrecto"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getIdEfector() < 1)
            return new ResponseEntity(new Mensaje("El id del efector es incorrecto"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getEstadoDdjj() == null)
            return new ResponseEntity(new Mensaje("El estado es obligatorio"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Ddjj createUpdate(Ddjj ddjj, DdjjDto ddjjDto) {

        if (ddjjDto.getMes() != null && !ddjjDto.getMes().equals(ddjj.getMes()))
            ddjj.setMes(ddjjDto.getMes());

        if (ddjjDto.getAnio() != ddjj.getAnio())
            ddjj.setAnio(ddjjDto.getAnio());

        if (ddjjDto.getIdEfector() != null && (ddjj.getEfector() == null
                || !Objects.equals(ddjj.getEfector().getId(), ddjjDto.getIdEfector()))) {
            ddjj.setEfector(efectorService.findById(ddjjDto.getIdEfector()));
        }

        /* if (ddjjDto.getIdValorGmi() != null && (ddjj.getValorGmi() == null
                || !Objects.equals(ddjj.getValorGmi().getId(), ddjjDto.getIdValorGmi()))) {
            ddjj.setValorGmi(valorGmiService.findById(ddjjDto.getIdValorGmi()).get());
        } */

        if (ddjjDto.getEstadoDdjj() != null && !ddjjDto.getEstadoDdjj().equals(ddjj.getEstadoDdjj()))
            ddjj.setEstadoDdjj(ddjjDto.getEstadoDdjj());

        if (ddjjDto.getSubtotal() != null && !ddjjDto.getSubtotal().equals(ddjj.getSubtotal()))
            ddjj.setSubtotal(ddjjDto.getSubtotal());

        if (ddjjDto.getTotal() != null && !ddjjDto.getTotal().equals(ddjj.getTotal()))
            ddjj.setTotal(ddjjDto.getTotal());

        if (ddjjDto.getIdRegistrosMensuales() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (ddjj.getRegistrosMensuales() != null) {
                for (RegistroMensual registroMensual : ddjj.getRegistrosMensuales()) {
                    for (Long id : ddjjDto.getIdRegistrosMensuales()) {
                        if (!registroMensual.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                ddjj.setRegistrosMensuales(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? ddjjDto.getIdRegistrosMensuales() : idList;
            for (Long id : idsToAdd) {
                ddjj.getRegistrosMensuales().add(registroMensualService.findById(id).get());
                registroMensualService.findById(id).get().setDdjj(ddjj);
            }
        }

        ddjj.setActivo(true);
        return ddjj;
    }

}
