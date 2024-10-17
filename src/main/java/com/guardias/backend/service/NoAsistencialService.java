package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.noAsistencial.NoAsistencialListDto;
import com.guardias.backend.dto.noAsistencial.NoAsistencialSummaryDto;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.repository.NoAsistencialRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NoAsistencialService {

    @Autowired
    NoAsistencialRepository noAsistencialRepository;

    public Optional<List<NoAsistencial>> findByActivoTrue() {
        return noAsistencialRepository.findByActivoTrue();
    }

    public List<NoAsistencial> findAll() {
        return noAsistencialRepository.findAll();
    }

    public Optional<NoAsistencial> findById(Long id) {
        return noAsistencialRepository.findById((Long) id);
    }

    public Optional<NoAsistencial> findByCuil(String cuil) {
        return noAsistencialRepository.findByCuil(cuil);
    }

    public boolean existsById(Long id) {
        return noAsistencialRepository.existsById(id);
    }

    public boolean existsByDni(int dni) {
        return noAsistencialRepository.existsByDni(dni);
    }

    public boolean existsByCuil(String cuil) {
        return noAsistencialRepository.existsByCuil(cuil);
    }

    public Optional<NoAsistencial> findByDni(int dni) {
        return noAsistencialRepository.findByDni(dni);
    }

    public void save(NoAsistencial noAsistencial) {
        noAsistencialRepository.save(noAsistencial);
    }

    public void deleteById(Long id) {
        noAsistencialRepository.deleteById(id);
    }

    public boolean activo(Long id) {
        return (noAsistencialRepository.existsById(id)
                && noAsistencialRepository.findById(id).get().isActivo());
    }

    public boolean activoDni(int dni) {
        return (noAsistencialRepository.existsByDni(dni) && noAsistencialRepository.findByDni(dni).get().isActivo());
    }

    public List<NoAsistencialListDto> getNoAsistencialList() {
        List<NoAsistencial> noAsistenciales = noAsistencialRepository.findByActivoTrue().orElse(new ArrayList<>());
        List<NoAsistencialListDto> DtoList = new ArrayList<>();

        for (NoAsistencial noAsistencial : noAsistenciales) {

            NoAsistencialListDto dto = new NoAsistencialListDto(
                    noAsistencial.getId(),
                    noAsistencial.getNombre(),
                    noAsistencial.getApellido(),
                    noAsistencial.getDni(),
                    noAsistencial.getCuil(),
                    noAsistencial.getFechaNacimiento(),
                    noAsistencial.getSexo(),
                    noAsistencial.getTelefono(),
                    noAsistencial.getEmail(),
                    noAsistencial.getDomicilio());

            DtoList.add(dto);
        }
        return DtoList;
    }

    /*
     * public List<NoAsistencial> getNoAsistencialesByEfector(Long idEfector) {
     * return noAsistencialRepository.findByEfectorAndActivoTrue(idEfector);
     * }
     */
    public List<NoAsistencialSummaryDto> getNoAsistencialesByEfector(Long idEfector) {
        List<NoAsistencial> noAsistenciales = noAsistencialRepository.findByActivoTrue().orElse(new ArrayList<>());
        List<NoAsistencialSummaryDto> EfectorList = new ArrayList<>();
        for (NoAsistencial noAsistencial : noAsistenciales) {
            NoAsistencialSummaryDto dto = new NoAsistencialSummaryDto(
                    noAsistencial.getId(),
                    noAsistencial.getNombre(),
                    noAsistencial.getApellido());
            EfectorList.add(dto);
        }
        return EfectorList;
    }

}
