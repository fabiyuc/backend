package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.RegistroMensualRepository;

@Service
@Transactional
public class RegistroMensualService {

    @Autowired
    RegistroMensualRepository registroMensualRepository;

    public Optional<List<RegistroMensual>> findByActivoTrue() {
        return registroMensualRepository.findByActivoTrue();
    }

    public List<RegistroMensual> findAll() {
        return registroMensualRepository.findAll();
    }

    public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaCargoReagrupacion(int anio, MesesEnum mes,
            Long idEfector) {
        List<RegistroMensual> registrosMensuales = registroMensualRepository.findByAnioMesEfector(anio, mes, idEfector);

        for (RegistroMensual registroMensual : registrosMensuales) {
            List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                    .filter(actividad -> actividad.getTipoGuardia().getId() == 1
                            || actividad.getTipoGuardia().getId() == 2)
                    .collect(Collectors.toList());
            registroMensual.setRegistroActividad(actividadesFiltradas);
        }

        return registrosMensuales;
    }

    public List<RegistroMensual> findByAnioMesEfectorAndTipoGuardiaExtra(int anio, MesesEnum mes,
            Long idEfector) {
        List<RegistroMensual> registrosMensuales = registroMensualRepository.findByAnioMesEfector(anio, mes, idEfector);

        for (RegistroMensual registroMensual : registrosMensuales) {
            List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad().stream()
                    .filter(actividad -> actividad.getTipoGuardia().getId() == 3)
                    .collect(Collectors.toList());
            registroMensual.setRegistroActividad(actividadesFiltradas);
        }

        return registrosMensuales;
    }

    public Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnio(Long asistencialId, Long efectorId,
            MesesEnum mes, int anio) {
        return registroMensualRepository.findByAsistencialIdAndEfectorIdAndMesAndAnio(asistencialId, efectorId, mes,
                anio);
    }

    // public Optional<Long> idByIdAsistencialAndMes(Long idAsistencial, Long
    // idEfector, MesesEnum mes, int anio) {
    // return registroMensualRepository.idByIdAsistencialAndMes(idAsistencial,
    // idEfector, mes, anio);
    // }

    public Optional<RegistroMensual> findById(Long id) {
        return registroMensualRepository.findById(id);
    }

    boolean existsByAnioAndMes(int anio, MesesEnum mes) {
        return registroMensualRepository.existsByAnioAndMes(anio, mes);
    }

    public boolean existsByAsistencialId(Long idAsistencial) {
        return registroMensualRepository.existsByAsistencialId(idAsistencial);
    }

    public boolean existsById(Long id) {
        return registroMensualRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (registroMensualRepository.existsById(id) && registroMensualRepository.findById(id).get().isActivo());
    }

    public void save(RegistroMensual registroMensual) {
        registroMensualRepository.save(registroMensual);
    }

    public void deleteById(Long id) {
        registroMensualRepository.deleteById(id);
    }

}
