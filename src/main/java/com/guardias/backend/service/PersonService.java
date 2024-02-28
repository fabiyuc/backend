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
import com.guardias.backend.repository.DistribucionHorariaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PersonService {
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    NoAsistencialService noAsistencialService;
    @Autowired
    LegajoService legajoService;
    @Autowired
    NovedadPersonalService novedadPersonalService;
    @Autowired
    DistribucionHorariaRepository distribucionHorariaService; // !TODO CORREGIR ESTO: crear dist. horaria service
    @Autowired
    AutoridadService autoridadService;

    private Person findPerson(Long idPersona) {
        Person persona = asistencialService.findById(idPersona).orElse(null);

        if (persona == null) {
            persona = noAsistencialService.findById(idPersona).orElse(null);
        }
        return persona;
    }

    private void savePersona(Person persona) {
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
        Person persona = findPerson(idPersona);

        Legajo legajo = legajoService.findById(idLegajo).get();
        legajo.setPersona(persona);
        legajoService.save(legajo);
        persona.getLegajos().add(legajo);

        savePersona(persona);
    }

    // Para NovedadPersonal y para suplente!!!!!!!!!!!!!!!!!
    @Transactional
    public void agregarNovedadPersonal(Long idPersona, Long idNovedadPersonal) {
        Person persona = findPerson(idPersona);

        NovedadPersonal novedadPersonal = novedadPersonalService.findById(idNovedadPersonal).get();
        novedadPersonal.setPersona(persona);
        novedadPersonalService.save(novedadPersonal);
        persona.getNovedadesPersonales().add(novedadPersonal);

        savePersona(persona);
    }

    @Transactional
    public void agregarDistribucionHoraria(Long idPersona, Long idDistribucionHoraria) {

        Person persona = findPerson(idPersona);

        DistribucionHoraria distribucionHoraria = distribucionHorariaService.findById(idDistribucionHoraria).get();
        distribucionHoraria.setPersona(persona);
        distribucionHorariaService.save(distribucionHoraria);
        persona.getDistribucionesHorarias().add(distribucionHoraria);

        savePersona(persona);
    }

    @Transactional
    public void agregarAutoridad(Long idPersona, Long idAutoridad) {

        Person persona = findPerson(idPersona);

        Autoridad autoridad = autoridadService.findById(idAutoridad).get();
        autoridad.setPersona(persona);
        autoridadService.save(autoridad);
        persona.getAutoridades().add(autoridad);

        savePersona(persona);
    }
}
