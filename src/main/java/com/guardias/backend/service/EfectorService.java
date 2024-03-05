package com.guardias.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.entity.Caps;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.entity.Legajo;
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
    // @Autowired
    // NotificacionService notificacionService;
    @Autowired
    LegajoService legajoService;

    public Efector findEfector(Long idEfector) {
        Efector efector = capsService.findById(idEfector).orElse(null);

        if (efector == null)
            efector = hospitalService.findById(idEfector).orElse(null);

        if (efector == null)
            efector = ministerioService.findById(idEfector).orElse(null);

        return efector;
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

    // !TODO falta la relacion con distribucionesHorarias

    // @Transactional
    // public void agregarNotificacion(Long idEfector, Long idNotificacion) {
    // Efector efector = findEfector(idEfector);
    // Notificacion notificacion =
    // notificacionService.findById(idNotificacion).get();

    // notificacionService.agregarEfector(idNotificacion, idEfector);
    // efector.getNotificaciones().add(notificacion);
    // saveEfector(efector);
    // }

    @Transactional
    public void agregarLegajo(Long idEfector, Long idLegajo) {
        Legajo legajo = legajoService.findById(idLegajo).get();
        Efector efector = findEfector(idEfector);

        legajo.getEfectores().add(efector);
        efector.getLegajos().add(legajo);
        saveEfector(efector);

    }

    @Transactional
    public void agregarLegajoUdo(Long idEfector, Long idLegajoUdo) {
        Efector efector = findEfector(idEfector);

        Legajo legajo = legajoService.findById(idLegajoUdo).get();
        legajo.setUdo(efector);
        legajoService.save(legajo);
        efector.getLegajosUdo().add(legajo);
        saveEfector(efector);
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
