package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guardias.backend.dto.PendientesSemanalDto;
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

    public List<PendientesSemanal> findByActivo() {
        return pendientesSemanalRepository.findByActivoTrue();
    }

    public List<PendientesSemanal> findAll() {
        return pendientesSemanalRepository.findAll();
    }

    public Optional<PendientesSemanal> findById(Long id) {
        return pendientesSemanalRepository.findById(id);
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

    public PendientesSemanal createUpdate(PendientesSemanal pendientesSemanal,
            PendientesSemanalDto pendientesSemanalDto) {

        if (pendientesSemanalDto.getFechaInicio() != null
                && !pendientesSemanalDto.getFechaInicio().equals(pendientesSemanal.getFechaInicio())) {
            pendientesSemanal.setFechaInicio(pendientesSemanalDto.getFechaInicio());
        }

        if (pendientesSemanalDto.getFechaFin() != null
                && !pendientesSemanalDto.getFechaFin().equals(pendientesSemanal.getFechaFin())) {
            pendientesSemanal.setFechaFin(pendientesSemanalDto.getFechaFin());
        }

        if (pendientesSemanalDto.getMes() != null
                && !pendientesSemanalDto.getMes().equals(pendientesSemanal.getMes())) {
            pendientesSemanal.setMes(pendientesSemanalDto.getMes());
        }

        if (pendientesSemanalDto.getAnio() != pendientesSemanal.getAnio()) {
            pendientesSemanal.setAnio(pendientesSemanalDto.getAnio());
        }

        if (pendientesSemanalDto.getIdEfector() != null) {
            if (pendientesSemanal.getEfector() == null ||
                    !pendientesSemanalDto.getIdEfector().equals(pendientesSemanal.getEfector().getId())) {
                pendientesSemanal.setEfector(efectorService.findById(pendientesSemanalDto.getIdEfector()));
            }
        }

        pendientesSemanal.setActivo(true);
        return pendientesSemanal;
    }

    public void addRegistrosPendientes(RegistrosPendientes registrosPendientes) {

    }

    public void createPendienteSemanal() {
        List<RegistrosPendientes> registrosPendientes = registrosPendientesService.findAll();
        if (registrosPendientes != null && !registrosPendientes.isEmpty()) {
            Map<Long, List<RegistrosPendientes>> registrosPorEfector = registrosPendientes.stream()
                    .collect(Collectors.groupingBy(registro -> registro.getEfector().getId()));

            for (Map.Entry<Long, List<RegistrosPendientes>> entry : registrosPorEfector.entrySet()) {
                Long idEfector = entry.getKey();
                List<RegistrosPendientes> registros = entry.getValue();

                PendientesSemanal pendienteSemanal = new PendientesSemanal();

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    // Convertir el Efector del primer registro a JSON
                    Efector efector = registros.get(0).getEfector();
                    String efectorJson = objectMapper.writeValueAsString(efector);
                    pendienteSemanal.setEfectorJson(efectorJson);

                    // Convertir la lista de RegistrosPendientes a JSON
                    String registrosJson = objectMapper.writeValueAsString(registros);
                    pendienteSemanal.setRegistrosPendientesJson(registrosJson);

                    // Configurar las otras propiedades de PendientesSemanal
                    pendienteSemanal.setFechaInicio(LocalDate.now());
                    pendienteSemanal.setFechaFin(LocalDate.now().plusDays(7));
                    pendienteSemanal.setAnio(LocalDate.now().getYear());
                    pendienteSemanal.setActivo(true);

                    // Guardar el objeto PendientesSemanal
                    save(pendienteSemanal);

                    // Imprimir la información para verificar
                    System.out.println("***************************INICIO**************************************");
                    System.out.println("Id del pendiente semanal: " + pendienteSemanal.getId());
                    System.out.println("Fechas del pendiente semanal: " + pendienteSemanal.getFechaInicio() + " - "
                            + pendienteSemanal.getFechaFin());
                    System.out.println("Efector del pendiente semanal: " + efector.getNombre());
                    System.out.println("***************************FIN**************************************");
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    // Manejar la excepción de manera adecuada según tus necesidades
                }
            }
        } else {
            System.out.println("No hay registros pendientes en la semana");
        }
    }
}
