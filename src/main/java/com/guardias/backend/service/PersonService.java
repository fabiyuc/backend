package com.guardias.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.entity.NovedadPersonal;
import com.guardias.backend.entity.Person;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.DistribucionHorariaRepository;
import com.guardias.backend.repository.LegajoRepository;
import com.guardias.backend.repository.NoAsistencialRepository;
import com.guardias.backend.repository.NovedadPersonalRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PersonService {
    @Autowired
    AsistencialRepository asistencialRepository;
    @Autowired
    NoAsistencialRepository noAsistencialRepository;
    @Autowired
    LegajoRepository legajoRepository;
    @Autowired
    NovedadPersonalRepository novedadPersonalRepository;
    @Autowired
    DistribucionHorariaRepository distribucionHorariaRepository;

    private Person findPerson(Long idPersona) {
        Person persona = asistencialRepository.findById(idPersona).orElse(null);

        if (persona == null) {
            persona = noAsistencialRepository.findById(idPersona).orElse(null);
        }
        return persona;
    }

    private void savePersona(Person persona) {
        if (persona instanceof Asistencial) {
            Asistencial asistencial = (Asistencial) persona;
            asistencialRepository.save(asistencial);
        } else {
            NoAsistencial noAsistencial = (NoAsistencial) persona;
            noAsistencialRepository.save(noAsistencial);
        }
    }

    @Transactional
    public void agregarLegajo(Long idPersona, Long idLegajo) {
        Person persona = findPerson(idPersona);

        Legajo legajo = legajoRepository.findById(idLegajo).get();
        legajo.setPersona(persona);
        legajoRepository.save(legajo);
        persona.getLegajos().add(legajo);

        savePersona(persona);
    }

    // Para NovedadPersonal y para supolente!!!!!!!!!!!!!!!!!
    @Transactional
    public void agregarNovedadPersonal(Long idPersona, Long idNovedadPersonal) {
        Person persona = findPerson(idPersona);

        NovedadPersonal novedadPersonal = novedadPersonalRepository.findById(idNovedadPersonal).get();
        novedadPersonal.setPersona(persona);
        novedadPersonalRepository.save(novedadPersonal);
        persona.getNovedadesPersonales().add(novedadPersonal);

        savePersona(persona);
    }

    // TODO corregir esto!!!!!
    @Transactional
    public void agregarDistribucionHoraria(Long idPersona, Long idDistribucionHoraria) {

        Person persona = findPerson(idPersona);

        DistribucionHoraria distribucionHoraria = distribucionHorariaRepository.findById(idDistribucionHoraria).get();
        distribucionHoraria.setPersona(persona);
        distribucionHorariaRepository.save(distribucionHoraria);
        persona.getDistribucionesHorarias().add(distribucionHoraria);

        savePersona(persona);
    }
}
