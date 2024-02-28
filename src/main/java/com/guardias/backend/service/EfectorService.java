package com.guardias.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Autoridad;
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
    AutoridadService autoridadService;

    private Efector findEfector(Long idEfector) {
        Efector efector = capsService.findById(idEfector).orElse(null);

        if (efector == null)
            efector = hospitalService.findById(idEfector).orElse(null);

        if (efector == null)
            efector = ministerioService.findById(idEfector).orElse(null);

        return efector;
    }

    private void saveEfector(Efector efector) {
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

    @Transactional
    public void agregarAutoridad(Long idEfector, Long idAutoridad) {

        Efector efector = findEfector(idEfector);

        Autoridad autoridad = autoridadService.findById(idAutoridad).get();
        autoridad.setEfector(efector);
        autoridadService.save(autoridad);
        efector.getAutoridades().add(autoridad);

        saveEfector(efector);
    }

}
