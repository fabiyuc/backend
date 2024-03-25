package com.guardias.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Autoridad;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.entity.Person;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.DistribucionHorariaRepository;
import com.guardias.backend.repository.NoAsistencialRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PersonService {
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    NoAsistencialService noAsistencialService;
    @Autowired
    AsistencialRepository asistencialRepository;
    @Autowired
    NoAsistencialRepository noAsistencialRepository;

    @Autowired
    LegajoService legajoService;
    @Autowired
    NovedadPersonalService novedadPersonalService;
    @Autowired
    DistribucionHorariaRepository distribucionHorariaService; // !TODO CORREGIR ESTO: crear dist. horaria service
    @Autowired
    AutoridadService autoridadService;

    public Person findById(Long idPersona) {
        Person persona = asistencialService.findById(idPersona).orElse(null);

        if (persona == null) {
            persona = noAsistencialService.findById(idPersona).orElse(null);
        }
        return persona;
    }

    public boolean activoById(Long id) {
        boolean exists = asistencialRepository.existsById(id);
        if (!exists)
            exists = noAsistencialRepository.existsById(id);
        return exists;
    }

    public boolean existsByDni(int dni) {
        boolean exists = asistencialRepository.existsByDni(dni);
        if (!exists)
            exists = noAsistencialRepository.existsByDni(dni);
        return exists;
    }

    public boolean existsByCui(String cuil) {
        boolean exists = asistencialRepository.existsByCuil(cuil);
        if (!exists)
            exists = noAsistencialRepository.existsByCuil(cuil);
        return exists;
    }

    public void savePersona(Person persona) {
        if (persona instanceof Asistencial) {
            Asistencial asistencial = (Asistencial) persona;
            asistencialService.save(asistencial);
        } else {
            NoAsistencial noAsistencial = (NoAsistencial) persona;
            noAsistencialService.save(noAsistencial);
        }
    }

    @Transactional
    public void agregarLegajo(Long idPersona, Long idLegajo) {
        Person persona = findById(idPersona);

        Legajo legajo = legajoService.findById(idLegajo).get();
        legajo.setPersona(persona);
        legajoService.save(legajo);
        persona.getLegajos().add(legajo);

        savePersona(persona);
    }

    // Para NovedadPersonal y para suplente!!!!!!!!!!!!!!!!!
    @Transactional
    public void agregarNovedadPersonal(Long idPersona, Long idNovedadPersonal) {
        Person persona = findById(idPersona);

        NovedadPersonal novedadPersonal = novedadPersonalService.findById(idNovedadPersonal).get();
        novedadPersonal.setPersona(persona);
        novedadPersonalService.save(novedadPersonal);
        persona.getNovedadesPersonales().add(novedadPersonal);

        savePersona(persona);
    }

    @Transactional
    public void agregarDistribucionHoraria(Long idPersona, Long idDistribucionHoraria) {

        Person persona = findById(idPersona);

        DistribucionHoraria distribucionHoraria = distribucionHorariaService.findById(idDistribucionHoraria).get();
        distribucionHoraria.setPersona(persona);
        distribucionHorariaService.save(distribucionHoraria);
        persona.getDistribucionesHorarias().add(distribucionHoraria);

        savePersona(persona);
    }

    @Transactional
    public void agregarAutoridad(Long idPersona, Long idAutoridad) {

        Person persona = findById(idPersona);

        Autoridad autoridad = autoridadService.findById(idAutoridad).get();
        autoridad.setPersona(persona);
        autoridadService.save(autoridad);
        persona.getAutoridades().add(autoridad);

        savePersona(persona);
    }
}
