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

    // @Autowired
    // LegajoService legajoService;
    // @Autowired
    // NovedadPersonalService novedadPersonalService;
    // @Autowired
    // DistribucionHorariaRepository distribucionHorariaService; // !TODO CORREGIR
    // ESTO: crear dist. horaria service
    // @Autowired
    // AutoridadService autoridadService;

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
}
