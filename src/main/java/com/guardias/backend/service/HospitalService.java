package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.caps.CapsNameDto;
import com.guardias.backend.dto.efector.EfectorHospitalDto;
import com.guardias.backend.dto.efector.EfectorSummaryDto;
import com.guardias.backend.dto.servicio.ServicioSummaryDto;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.repository.HospitalRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class HospitalService {

    @Autowired
    HospitalRepository hospitalRepository;

    public Optional<List<Hospital>> findByActivoTrue() {
        return hospitalRepository.findByActivoTrue();
    }

    public List<Hospital> findAll() {
        return hospitalRepository.findAll();
    }

    public List<Hospital> findByAdmitePasiva() {
        return hospitalRepository.findByAdmitePasiva();
    }

    public Optional<Hospital> findById(Long id) {
        return hospitalRepository.findById((Long) id);
    }

    public Optional<Hospital> findByNombre(String nombre) {
        return hospitalRepository.findByNombre(nombre);
    }

    public boolean activo(Long id) {
        return (hospitalRepository.existsById(id) && hospitalRepository.findById(id).get().isActivo());
    }

    public void save(Hospital hospital) {
        hospitalRepository.save(hospital);
    }

    public void deleteById(Long id) {
        hospitalRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return hospitalRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre) {
        return hospitalRepository.existsByNombre(nombre);
    }

    public boolean activoByNombre(String nombre) {
        return (hospitalRepository.existsByNombre(nombre) && hospitalRepository.findByNombre(nombre).get().isActivo());
    }

    // Encuentra todos los hospitales por nivel de complejidad

    public List<Hospital> findHospitalesPorNivel(int nivelComplejidad) {
        return hospitalRepository.findHospitalesPorNivel(nivelComplejidad);
    }

    public List<Hospital> findHospitalesPorNivelExcluyendo(int nivelComplejidad, String nombreAExcluir) {
        return hospitalRepository.findHospitalesPorNivelExcluyendo(nivelComplejidad, nombreAExcluir);
    }

    public List<CapsNameDto> findActiveCapsByHospitalId(Long hospitalId) {
        Optional<Hospital> hospital = hospitalRepository.findByIdAndActivoTrue(hospitalId);

        if (hospital.isPresent()) {
            List<CapsNameDto> capsList = hospital.get().getCaps().stream()
                    .filter(caps -> caps.isActivo()) // Filtrar solo los CAPS activos
                    .map(caps -> new CapsNameDto(caps.getId(), caps.getNombre())) // Mapear a DTO
                    .collect(Collectors.toList());
            return capsList;
        }

        return null;
    }

    public boolean isHospital(Long id) {
        Optional<Hospital> hospital = hospitalRepository.findById(id);
        return hospital.isPresent() && hospital.get().isActivo();
    }

    public List<ServicioSummaryDto> getActiveServiciosByHospitalId(Long idHospital) {
        if (!hospitalRepository.existsByIdAndActivoTrue(idHospital)) {
            throw new EntityNotFoundException("Hospital no encontrado o inactivo con ID: " + idHospital);
        }

        return hospitalRepository.findActiveServiciosByHospitalId(idHospital);
    }

    public List<EfectorSummaryDto> findActiveEfectors() {
        // Convierte la lista de Hospital a EfectorSummaryDto
        return hospitalRepository.findByActivoTrue()
                .orElse(new ArrayList<>())
                .stream()
                .map(hospital -> new EfectorSummaryDto(hospital.getId(), hospital.getNombre()))
                .collect(Collectors.toList());
    }

    public Optional<EfectorHospitalDto> findByIdNombre(Long id) {
        Optional<Hospital> hospital = hospitalRepository.findById(id);
        if (hospital.isPresent()) {
            return Optional.of(new EfectorHospitalDto(
                    hospital.get().getId(),
                    hospital.get().getNombre(),
                    hospital.get().getNivelComplejidad()));
        }
        return Optional.empty();
    }

}
