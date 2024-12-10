package com.guardias.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.efector.EfectorSummaryDto;
import com.guardias.backend.dto.person.PersonBasicPanelDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Legajo;
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

    public Person findByDniAndActivoTrue(int dni) {
        Person persona = asistencialService.findByDniAndActivoTrue(dni).orElse(null);

        if (persona == null) {
            persona = noAsistencialService.findByDniAndActivoTrue(dni).orElse(null);
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

    public boolean existsByDniAndActivoTrue(int dni) {
        boolean exists = asistencialRepository.existsByDniAndActivoTrue(dni);
        if (!exists)
            exists = noAsistencialRepository.existsByDni(dni);
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

    public boolean existsByEmailAndActivoTrue(String email) {
        boolean exists = (asistencialRepository.existsByEmailAndActivoTrue(email)
                && asistencialRepository.findByEmailAndActivoTrue(email).get().isActivo());
        if (!exists)
            exists = (noAsistencialRepository.existsByEmailAndActivoTrue(email)
                    && noAsistencialRepository.findByEmailAndActivoTrue(email).get().isActivo());
        return exists;
    }

    public Person findByEmailAndActivoTrue(String email) {
        Person persona = asistencialService.findByEmailAndActivoTrue(email).orElse(null);

        if (persona == null) {
            persona = noAsistencialService.findByEmailAndActivoTrue(email).orElse(null);
        }

        if (persona.isActivo())
            return persona;
        else
            return null;
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

    public PersonBasicPanelDto convertirAPersonaBasicaPanelDTO(Person persona) {
        PersonBasicPanelDto dto = new PersonBasicPanelDto();
        dto.setId(persona.getId());
        dto.setNombre(persona.getNombre());
        dto.setApellido(persona.getApellido());

        // Obtener el Legajo actual
        Legajo legajoActual = persona.getLegajos().stream()
                .filter(legajo -> legajo.isActivo() && !legajo.getEsAutoridad())
                .findFirst()
                .orElse(null);

        if (legajoActual != null) {

            // Asignar el EfectorSummaryDto para la UDO
            if (legajoActual.getUdo() != null) {
                EfectorSummaryDto udoDto = new EfectorSummaryDto(legajoActual.getUdo().getId(),
                        legajoActual.getUdo().getNombre());
                dto.setUdo(udoDto);
            } else {
                dto.setUdo(null);
            }

            // Obtiene los nombres de todos los efectores
            List<EfectorSummaryDto> efectores = legajoActual.getEfectores().stream()
                    .map(efector -> new EfectorSummaryDto(efector.getId(), efector.getNombre()))
                    .collect(Collectors.toList());

            dto.setEfectores(efectores);
        }

        return dto;
    }
}
