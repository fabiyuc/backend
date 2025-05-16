package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.noAsistencial.NoAsistencialListDto;
import com.guardias.backend.dto.noAsistencial.NoAsistencialListEfectorDto;
import com.guardias.backend.dto.noAsistencial.NoAsistencialSummaryDto;
import com.guardias.backend.entity.Legajo;
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

    public List<NoAsistencial> findByEfectorAndActivoTrue(Long idEfector) {
        return noAsistencialRepository.findByEfectorAndActivoTrue(idEfector);
    }

    public boolean existsById(Long id) {
        return noAsistencialRepository.existsById(id);
    }

    public boolean existsByDniAndActivoTrue(int dni) {
        return noAsistencialRepository.existsByDniAndActivoTrue(dni);
    }

    public boolean existsByCuil(String cuil) {
        return noAsistencialRepository.existsByCuil(cuil);
    }

    public Optional<NoAsistencial> findByDniAndActivoTrue(int dni) {
        return noAsistencialRepository.findByDniAndActivoTrue(dni);
    }

    public Optional<NoAsistencial> findByEmailAndActivoTrue(String email) {
        return noAsistencialRepository.findByEmailAndActivoTrue(email);
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

    /*
     * public boolean activoDni(int dni) {
     * return (noAsistencialRepository.existsByDni(dni) &&
     * noAsistencialRepository.findByDni(dni).get().isActivo());
     * }
     */

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
                    noAsistencial.getDomicilio(),
                    noAsistencial.isEsAsistencial());

            DtoList.add(dto);
        }
        return DtoList;
    }

    public List<NoAsistencialListEfectorDto> filterNoAsistencialByEfector(
            List<NoAsistencial> noAsistenciales) {
        List<NoAsistencialListEfectorDto> EfectorList = new ArrayList<>();
        for (NoAsistencial noAsistencial : noAsistenciales) {

            NoAsistencialListEfectorDto dto = new NoAsistencialListEfectorDto(
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
            EfectorList.add(dto);
        }
        return EfectorList;
    }

    public List<NoAsistencialListEfectorDto> findByEfectorByActivoTrue(Long idEfector) {
        List<NoAsistencial> noAsistenciales = noAsistencialRepository.findByEfectorByActivoTrue(idEfector);
        return filterNoAsistencialByEfector(noAsistenciales);
    }

    /*
     * public List<NoAsistencial> getNoAsistencialesByEfector(Long idEfector) {
     * return noAsistencialRepository.findByEfectorAndActivoTrue(idEfector);
     * }
     */
    public List<NoAsistencialSummaryDto> filterNoAsistencialesByEfector(List<NoAsistencial> noAsistenciales) {
        List<NoAsistencialSummaryDto> EfectorList = new ArrayList<>();
        for (NoAsistencial noAsistencial : noAsistenciales) {
            NoAsistencialSummaryDto dto = new NoAsistencialSummaryDto(
                    noAsistencial.getId(),
                    noAsistencial.getNombre(),
                    noAsistencial.getApellido(),
                    noAsistencial.getDni(),
                    noAsistencial.getCuil(),
                    noAsistencial.getFechaNacimiento(),
                    noAsistencial.getSexo(),
                    noAsistencial.getTelefono(),
                    noAsistencial.getEmail(),
                    noAsistencial.getDomicilio(),
                    noAsistencial.getLegajos().stream()
                            .filter(Legajo::isActivo)
                            .collect(Collectors.toList()));
            EfectorList.add(dto);
        }
        return EfectorList;
    }

    public List<NoAsistencialSummaryDto> getNoAsistencialesByEfector(Long efectorId) {
        List<NoAsistencial> noAsistenciales = noAsistencialRepository.findByEfectorAndActivoTrue(efectorId);
        return filterNoAsistencialesByEfector(noAsistenciales);
    }

    public List<NoAsistencialListDto> getNoAsistencialListAutoridadesByEfector(Long idEfector) {
        List<NoAsistencial> noAsistenciales = noAsistencialRepository.findByActivoTrue().orElse(new ArrayList<>());

        return noAsistenciales.stream()
                .filter(noAsistencial -> noAsistencial.getAutoridades() != null
                        && !noAsistencial.getAutoridades().isEmpty())
                .filter(noAsistencial -> noAsistencial.getLegajos().stream()
                        .anyMatch(legajo -> legajo.getFechaFinal() == null &&
                                legajo.getEfectores().stream().anyMatch(e -> e.getId().equals(idEfector))))
                .map(noAsistencial -> new NoAsistencialListDto(
                        noAsistencial.getId(),
                        noAsistencial.getNombre(),
                        noAsistencial.getApellido(),
                        noAsistencial.getDni(),
                        noAsistencial.getCuil(),
                        noAsistencial.getFechaNacimiento(),
                        noAsistencial.getSexo(),
                        noAsistencial.getTelefono(),
                        noAsistencial.getEmail(),
                        noAsistencial.getDomicilio(),
                        noAsistencial.isEsAsistencial()))
                .collect(Collectors.toList());
    }

    public List<NoAsistencialListDto> getNoAsistencialListAutoridadesRegionalesByEfector(Long idEfector) {
        List<NoAsistencial> noAsistenciales = noAsistencialRepository.findByActivoTrue().orElse(new ArrayList<>());

        return noAsistenciales.stream()
                .filter(noAsistencial -> noAsistencial.getAutoridades() != null
                        && !noAsistencial.getAutoridades().isEmpty())
                .filter(noAsistencial -> noAsistencial.getLegajos().stream()
                        .anyMatch(legajo -> legajo.getFechaFinal() == null &&
                                Boolean.TRUE.equals(legajo.getEsRegional()) &&
                                legajo.getEfectores().stream().anyMatch(e -> e.getId().equals(idEfector))))
                .map(noAsistencial -> new NoAsistencialListDto(
                        noAsistencial.getId(),
                        noAsistencial.getNombre(),
                        noAsistencial.getApellido(),
                        noAsistencial.getDni(),
                        noAsistencial.getCuil(),
                        noAsistencial.getFechaNacimiento(),
                        noAsistencial.getSexo(),
                        noAsistencial.getTelefono(),
                        noAsistencial.getEmail(),
                        noAsistencial.getDomicilio(),
                        noAsistencial.isEsAsistencial()))
                .collect(Collectors.toList());
    }

    public List<NoAsistencialListDto> getNoAsistencialSinLegajo() {
        List<NoAsistencial> noAsistenciales = noAsistencialRepository.findNoAsistencialSinLegajo();

        return noAsistenciales.stream()

                .map(noAsistencial -> new NoAsistencialListDto(
                        noAsistencial.getId(),
                        noAsistencial.getNombre(),
                        noAsistencial.getApellido(),
                        noAsistencial.getDni(),
                        noAsistencial.getCuil(),
                        noAsistencial.getFechaNacimiento(),
                        noAsistencial.getSexo(),
                        noAsistencial.getTelefono(),
                        noAsistencial.getEmail(),
                        noAsistencial.getDomicilio(),
                        noAsistencial.isEsAsistencial()))
                .collect(Collectors.toList());
    }

    public List<NoAsistencialSummaryDto> getNoAsistencialListAll() {
        List<NoAsistencial> noAsistenciales = noAsistencialRepository.findByActivoTrue().orElse(new ArrayList<>());
        List<NoAsistencial> noAsistencialesConLegajoActivo = noAsistenciales.stream()
                .filter(noAsistencial -> noAsistencial.getLegajos().stream().anyMatch(Legajo::isActivo))
                .collect(Collectors.toList());

        List<NoAsistencialSummaryDto> EfectorList = new ArrayList<>();
        for (NoAsistencial noAsistencial : noAsistencialesConLegajoActivo) {

            NoAsistencialSummaryDto dto = new NoAsistencialSummaryDto(
                    noAsistencial.getId(),
                    noAsistencial.getNombre(),
                    noAsistencial.getApellido(),
                    noAsistencial.getDni(),
                    noAsistencial.getCuil(),
                    noAsistencial.getFechaNacimiento(),
                    noAsistencial.getSexo(),
                    noAsistencial.getTelefono(),
                    noAsistencial.getEmail(),
                    noAsistencial.getDomicilio(),
                    noAsistencial.getLegajos().stream()
                            .filter(Legajo::isActivo)
                            .collect(Collectors.toList())

            );
            EfectorList.add(dto);
        }
        return EfectorList;
    }

}
