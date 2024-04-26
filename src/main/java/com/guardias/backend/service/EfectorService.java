package com.guardias.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Caps;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.entity.Ministerio;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EfectorService {
    @Autowired
    CapsService capsService;
    @Autowired
    HospitalService hospitalService;
    @Autowired
    MinisterioService ministerioService;
    @Autowired
    LegajoService legajoService;

    public Efector findById(Long idEfector) {
        Efector efector = capsService.findById(idEfector).orElse(null);

        if (efector == null)
            efector = hospitalService.findById(idEfector).orElse(null);

        if (efector == null)
            efector = ministerioService.findById(idEfector).orElse(null);

        if (efector.isActivo())
            return efector;
        else
            return null;
    }

    public Efector findByNombre(String nombre) {
        Efector efector = capsService.findByNombre(nombre).orElse(null);

        if (efector == null)
            efector = hospitalService.findByNombre(nombre).orElse(null);

        if (efector == null)
            efector = ministerioService.findByNombre(nombre).orElse(null);

        if (efector.isActivo())
            return efector;
        else
            return null;
    }

    public boolean existsByName(String nombre) {
        boolean exists = capsService.existsByNombre(nombre);

        if (exists == false)
            exists = hospitalService.existsByNombre(nombre);

        if (exists == false)
            exists = ministerioService.existsByNombre(nombre);

        return exists;
    }

    public boolean existsById(Long id) {
        boolean exists = capsService.existsById(id);

        if (exists == false)
            exists = hospitalService.existsById(id);

        if (exists == false)
            exists = ministerioService.existsById(id);

        return exists;
    }

    public boolean activoById(Long id) {
        boolean exists = capsService.activo(id);

        if (exists == false)
            exists = hospitalService.activo(id);

        if (exists == false)
            exists = ministerioService.activo(id);

        return exists;
    }

    public void saveEfector(Efector efector) {
        if (efector instanceof Caps) {
            Caps caps = (Caps) efector;
            capsService.save(caps);
        } else if (efector instanceof Hospital) {
            Hospital hospital = (Hospital) efector;
            hospitalService.save(hospital);
        } else {
            Ministerio ministerio = (Ministerio) efector;
            ministerioService.save(ministerio);
        }
    }
}
