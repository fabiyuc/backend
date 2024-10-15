package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.caps.CapsNameDto;
import com.guardias.backend.entity.Hospital;
import com.guardias.backend.repository.HospitalRepository;

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
        return hospitalRepository.findHospitalesPorNivelExcluyendo(nivelComplejidad,  nombreAExcluir);
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

}
