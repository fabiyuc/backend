package com.guardias.backend.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.PendientesSemanal;
import com.guardias.backend.entity.RegistrosPendientes;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.PendientesSemanalRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PendientesSemanalService {
    @Autowired
    PendientesSemanalRepository pendientesSemanalRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    RegistrosPendientesService registrosPendientesService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PendientesSemanal> findByActivo() {
        return pendientesSemanalRepository.findByActivoTrue();
    }

    public List<PendientesSemanal> findAll() {
        return pendientesSemanalRepository.findAll();
    }

    public Optional<PendientesSemanal> findById(Long id) {
        return pendientesSemanalRepository.findById(id);
    }

    Optional<PendientesSemanal> findByEfectorAndFechaInicio(Efector efector, LocalDate fechaInicio) {
        return pendientesSemanalRepository.findByEfectorAndFechaInicio(efector, fechaInicio);
    }

    public void save(PendientesSemanal pendientesSemanal) {
        pendientesSemanalRepository.save(pendientesSemanal);
    }

    public void deleteById(Long id) {
        pendientesSemanalRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return pendientesSemanalRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (pendientesSemanalRepository.existsById(id)
                && pendientesSemanalRepository.findById(id).get().isActivo());
    }

    public List<PendientesSemanal> findByAnioAndMesAndEfectorId(int anio, MesesEnum mes, Long idEfector) {
        return pendientesSemanalRepository.findByAnioAndMesAndEfectorId(anio, mes, idEfector);
    }

    public PendientesSemanal create(RegistrosPendientes registrosPendientes) {
        PendientesSemanal pendientesSemanal = new PendientesSemanal();

        // trae el sabado anterior al actual (incluso si hoy es sabado)
        LocalDate previousSaturday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SATURDAY));
        MesesEnum mes = MesesEnum.fromNumeroMes(previousSaturday.getMonthValue());

        pendientesSemanal.setFechaFin(previousSaturday.plusDays(7));
        pendientesSemanal.setMes(mes);
        pendientesSemanal.setAnio(previousSaturday.getYear());
        pendientesSemanal.setEfector(registrosPendientes.getEfector());

        pendientesSemanal.setActivo(true);
        save(pendientesSemanal);
        return pendientesSemanal;
    }

    public void addRegistrosPendientes(PendientesSemanal pendientesSemanal, RegistrosPendientes registrosPendientes) {

        try {
            if (pendientesSemanal.getEfectorJson() == null) {
                pendientesSemanal.setEfectorJson(objectMapper.writeValueAsString(pendientesSemanal.getEfector()));
            }

            List<RegistrosPendientes> registrosPendientesList = new ArrayList<>();
            String registrosPendientesJson = pendientesSemanal.getRegistrosPendientesJson();

            if (registrosPendientesJson != null && !registrosPendientesJson.isEmpty()) {
                registrosPendientesList = objectMapper.readValue(registrosPendientesJson,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, RegistrosPendientes.class));
            }
            registrosPendientesList.add(registrosPendientes);
            String updatedRegistrosPendientesJson = objectMapper.writeValueAsString(registrosPendientesList);
            pendientesSemanal.setRegistrosPendientesJson(updatedRegistrosPendientesJson);

            save(pendientesSemanal);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}