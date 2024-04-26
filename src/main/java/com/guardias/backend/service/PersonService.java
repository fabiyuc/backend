package com.guardias.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.entity.Person;
import com.guardias.backend.repository.AsistencialRepository;
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

    public Person findById(Long idPersona) {
        Person persona = asistencialService.findById(idPersona).orElse(null);

        if (persona == null) {
            persona = noAsistencialService.findById(idPersona).orElse(null);
        }

        if (persona.isActivo())
            return persona;
        else
            return null;
    }

    public Person findByDni(int dni) {
        Person persona = asistencialService.findByDni(dni).orElse(null);

        if (persona == null) {
            persona = noAsistencialService.findByDni(dni).orElse(null);
        }

        if (persona.isActivo())
            return persona;
        else
            return null;
    }

    public Person findByCuil(String cuil) {
        Person persona = asistencialService.findByCuil(cuil).orElse(null);

        if (persona == null) {
            persona = noAsistencialService.findByCuil(cuil).orElse(null);
        }

        if (persona.isActivo())
            return persona;
        else
            return null;
    }

    public boolean activoById(Long id) {
        boolean exists = (asistencialRepository.existsById(id) && asistencialRepository.findById(id).get().isActivo());
        if (!exists)
            exists = (noAsistencialRepository.existsById(id) && noAsistencialRepository.findById(id).get().isActivo());
        return exists;
    }

    public boolean existsByDni(int dni) {
        boolean exists = (asistencialRepository.existsByDni(dni)
                && asistencialRepository.findByDni(dni).get().isActivo());
        if (!exists)
            exists = (noAsistencialRepository.existsByDni(dni)
                    && noAsistencialRepository.findByDni(dni).get().isActivo());
        return exists;
    }

    public boolean existsByCuil(String cuil) {
        boolean exists = (asistencialRepository.existsByCuil(cuil)
                && asistencialRepository.findByCuil(cuil).get().isActivo());
        if (!exists)
            exists = (noAsistencialRepository.existsByCuil(cuil)
                    && noAsistencialRepository.findByCuil(cuil).get().isActivo());
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
}
