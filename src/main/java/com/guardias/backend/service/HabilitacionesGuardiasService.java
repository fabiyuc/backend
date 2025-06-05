package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.HabilitacionesGuardiasDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asistencial.AsistencialListNombreTGDto;
import com.guardias.backend.dto.asistencial.AsistencialSummaryDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.HabilitacionesGuardia;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.CapsRepository;
import com.guardias.backend.repository.HabilitacionesGuardiasRepository;
import com.guardias.backend.repository.HospitalRepository;
import com.guardias.backend.repository.MinisterioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class HabilitacionesGuardiasService {

    @Autowired
    HospitalRepository hospitalRepository;
    @Autowired
    MinisterioRepository ministerioRepository;
    @Autowired
    CapsRepository capsRepository;
    @Autowired
    AsistencialRepository asistencialRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    PersonService personService;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    HabilitacionesGuardiasRepository habilitacionesGuardiasRepository;
    @Autowired
    CapsService capsService;
    @Autowired
    HospitalService hospitalService;

    public Optional<List<HabilitacionesGuardia>> findByActivoTrue() {
        return habilitacionesGuardiasRepository.findByActivoTrue();
    }

    public List<HabilitacionesGuardia> findAll() {
        return habilitacionesGuardiasRepository.findAll();
    }

    public boolean activo(Long id) {
        return (habilitacionesGuardiasRepository.existsById(id)
                && habilitacionesGuardiasRepository.findById(id).get().isActivo());
    }

    public Optional<HabilitacionesGuardia> findById(Long id) {
        return habilitacionesGuardiasRepository.findById(id);
    }

    public boolean activoByAsistencial(Long idAsistencial) {
        return (habilitacionesGuardiasRepository.existsByAsistencialId(idAsistencial)
                && habilitacionesGuardiasRepository.findByAsistencialId(idAsistencial).get().isActivo());
    }

    public Optional<HabilitacionesGuardia> findByAsistencial(Long idAsistencial) {
        return habilitacionesGuardiasRepository.findByAsistencialId(idAsistencial);
    }

    public Optional<HabilitacionesGuardia> findActivoByAsistencial(Long idAsistencial) {
        return habilitacionesGuardiasRepository.findByAsistencialIdAndActivoTrue(idAsistencial);
    }

    public ResponseEntity<?> validations(HabilitacionesGuardiasDto permisosDto) {
        if (permisosDto.getIdAsistencial() == null) {
            return new ResponseEntity<>(
                    new Mensaje("El id del asistencial es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        }

        if (permisosDto.getIdEfectores() == null || permisosDto.getIdEfectores().isEmpty()) {
            return new ResponseEntity<>(
                    new Mensaje("El idEfectores es obligatorio"),
                    HttpStatus.BAD_REQUEST);
        }

        // Aquí ya no se valida si pertenece a CAPS o HOSPITAL
        return new ResponseEntity<>(
                new Mensaje("valido"),
                HttpStatus.OK);

    }

    public HabilitacionesGuardia createUpdate(HabilitacionesGuardia habilitacionesGuardias,
            HabilitacionesGuardiasDto habilitacionesGuardiasDto) {

        if (habilitacionesGuardias.getAsistencial() == null
                || !Objects.equals(habilitacionesGuardias.getAsistencial().getId(),
                        habilitacionesGuardiasDto.getIdAsistencial()))
            habilitacionesGuardias
                    .setAsistencial(asistencialService.findById(habilitacionesGuardiasDto.getIdAsistencial()).get());

        if (habilitacionesGuardiasDto.getIdEfectores() != null) {

            if (habilitacionesGuardias.getEfectores() == null) {
                habilitacionesGuardias.setEfectores(new ArrayList<>());
            }

            // Crea una nueva lista para almacenar los efectores actualizados
            List<Efector> efectoresActualizados = new ArrayList<>();
            for (Efector efector : habilitacionesGuardias.getEfectores()) {
                if (habilitacionesGuardiasDto.getIdEfectores().contains(efector.getId())) {
                    efectoresActualizados.add(efector);
                } else {
                    // Remover el legajo de los efectores que se eliminarán
                    efector.getHabilitacionesGuardias().remove(habilitacionesGuardias);
                }
            }
            habilitacionesGuardias.setEfectores(efectoresActualizados);

            // agrega nuevos efectores si no estan presentes
            for (Long id : habilitacionesGuardiasDto.getIdEfectores()) {
                boolean found = false;
                for (Efector efector : habilitacionesGuardias.getEfectores()) {
                    if (efector.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Efector efectorToAdd = efectorService.findById(id);
                    if (efectorToAdd != null) {
                        habilitacionesGuardias.getEfectores().add(efectorToAdd);
                        efectorToAdd.getHabilitacionesGuardias().add(habilitacionesGuardias);
                    } else {
                        throw new RuntimeException("No se encontró el efector con ID: " + id);
                    }
                }
            }
        }

        /*
         * if (habilitacionesGuardiasDto.getTipoEfectorEx() != null) {
         * habilitacionesGuardias.setTipoEfectorEx(habilitacionesGuardiasDto.
         * getTipoEfectorEx());
         * System.out.println("tipoEfector: " +
         * habilitacionesGuardiasDto.getTipoEfectorEx()); // Verificar el valor
         * 
         * }
         */

        habilitacionesGuardias.setActivo(habilitacionesGuardiasDto.getActivo());
        return habilitacionesGuardias;
    }

    public void save(HabilitacionesGuardia permisos) {
        habilitacionesGuardiasRepository.save(permisos);
    }

    public boolean existsById(Long id) {
        return habilitacionesGuardiasRepository.existsById(id);
    }

    public void deleteById(Long id) {
        habilitacionesGuardiasRepository.deleteById(id);
    }

    public boolean tieneHabilitacionesGuardias(Long idAsistencial, Long idEfector) {

        if (!personService.activoById(idAsistencial)) {
            throw new EntityNotFoundException("El asistencial con ID " + idAsistencial + " no existe.");
        }

        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con ID " + idEfector + " no existe.");
        }

        Optional<HabilitacionesGuardia> optionalHabilitacion = habilitacionesGuardiasRepository
                .findByAsistencialIdAndActivoTrue(idAsistencial);

        if (optionalHabilitacion.isPresent()) {

            HabilitacionesGuardia habilitacion = optionalHabilitacion.get();
            // Verificar si la lista de efectores contiene el idEfector
            return habilitacion.getEfectores() != null &&
                    habilitacion.getEfectores().stream()
                            .anyMatch(efector -> efector.getId().equals(idEfector));
        }
        return false; // Retorna false si no hay una habilitacion de guardia activa o no se encuentra
                      // el idEfector

    }

    public List<HabilitacionesGuardia> getHabilitacionesGuardiasByEfectorAndAsistencial(Long idEfector) {
        if (idEfector == null || idEfector <= 0) {
            throw new IllegalArgumentException("El idEfector no es válido.");
        }
        return habilitacionesGuardiasRepository.findHabilitacionesGuardiasByEfectorAndAsistencial(idEfector);
    }

    public List<AsistencialSummaryDto> getAsistencialesByEfectorAndTG(Long idEfector, String tipoGuardia) {

        TipoGuardiaEnum tipoGuardiaEnum;

        if (idEfector == null || idEfector <= 0) {
            throw new IllegalArgumentException("El idEfector no es válido.");
        }
        try {
            tipoGuardiaEnum = TipoGuardiaEnum.valueOf(tipoGuardia.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El tipo de guardia proporcionado no es válido: " + tipoGuardia);
        }

        List<Asistencial> asistenciales = habilitacionesGuardiasRepository.findByEfectorAndActivoTrueAndTG(idEfector,
                tipoGuardiaEnum);
        List<AsistencialSummaryDto> dtoList = new ArrayList<>();
        for (Asistencial asistencial : asistenciales) {
            // Obtiene el legajo activo
            Optional<Legajo> legajoActivo = asistencial.getLegajos().stream()
                    .filter(legajo -> legajo.getFechaFinal() == null)
                    .findFirst();

            // Obtiene el nombre de la profesión (o null si no hay legajo activo o
            // profesión)
            String profesion = legajoActivo
                    .map(legajo -> legajo.getProfesion() != null ? legajo.getProfesion().getNombre() : null)
                    .orElse(null);

            // Mapea los nombres de los tipos de guardia
            List<String> nombresTiposGuardias = asistencial.getLegajos().stream()
                    .filter(legajo -> legajo.getFechaFinal() == null) // Solo legajos activos
                    .flatMap(legajo -> legajo.getTipoGuardias().stream())
                    .map(tguardia -> tguardia.getNombre().name())
                    .collect(Collectors.toList());

            // Crea el DTO
            AsistencialSummaryDto dto = new AsistencialSummaryDto(
                    asistencial.getId(),
                    asistencial.getNombre(),
                    asistencial.getApellido(),
                    asistencial.getCuil(),
                    profesion,
                    nombresTiposGuardias);
            dtoList.add(dto);
        }

        return dtoList;
    }

    public List<AsistencialListNombreTGDto> getAsistencialesWithCfAndExtraByEfector(Long idEfector) {
        List<Asistencial> asistenciales = habilitacionesGuardiasRepository
                .findAsistencialesWithCfAndExtraByEfector(idEfector);

        return asistenciales.stream().map(asistencial -> {
            AsistencialListNombreTGDto dto = new AsistencialListNombreTGDto();

            dto.setId(asistencial.getId());
            dto.setNombre(asistencial.getNombre());
            dto.setApellido(asistencial.getApellido());
            dto.setCuil(asistencial.getCuil());
            dto.setEsAsistencial(asistencial.isEsAsistencial());
            dto.setActivo(asistencial.isActivo()); // o según lo que determine tu lógica

            // Legajos activos
            List<Long> legajoIds = asistencial.getLegajos().stream()
                    .filter(l -> l.getFechaFinal() == null)
                    .map(Legajo::getId)
                    .collect(Collectors.toList());

            dto.setIdLegajos(legajoIds);

            // Si necesitás más campos (fechaNacimiento, email, etc), y están en la entidad
            // Person, podés mapearlos acá si están disponibles
            dto.setDni(asistencial.getDni());
            dto.setFechaNacimiento(asistencial.getFechaNacimiento());
            dto.setSexo(asistencial.getSexo());
            dto.setTelefono(asistencial.getTelefono());
            dto.setEmail(asistencial.getEmail());
            dto.setDomicilio(asistencial.getDomicilio());
            dto.setNombresTiposGuardias(asistencial.getLegajos().stream()
                    .filter(l -> l.getFechaFinal() == null)
                    .flatMap(l -> l.getTipoGuardias().stream())
                    .map(tg -> tg.getNombre().name())
                    .collect(Collectors.toList()));

            return dto;
        }).collect(Collectors.toList());
    }

}
