package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.PendientesSemanalDto;
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

        if (pendientesSemanalDto.getIdRegistrosPendientes() != null) {
            List<Long> idList = pendientesSemanalDto.getIdRegistrosPendientes();
            if (pendientesSemanal.getRegistrosPendientes() == null) {
                pendientesSemanal.setRegistrosPendientes(new ArrayList<>());
            }

            for (Long id : idList) {
                Optional<RegistrosPendientes> registroOpt = registrosPendientesService.findById(id);
                if (registroOpt.isPresent()) {
                    RegistrosPendientes registro = registroOpt.get();
                    if (!pendientesSemanal.getRegistrosPendientes().contains(registro)) {
                        pendientesSemanal.getRegistrosPendientes().add(registro);
                        registro.setPendientesSemanal(pendientesSemanal);
                    }
                }
            }
        }

        pendientesSemanal.setActivo(true);
        return pendientesSemanal;
    }

    public void createPendienteSemanal() {
        List<RegistrosPendientes> registrosPendientes = registrosPendientesService.findAll();
        if (registrosPendientes != null && !registrosPendientes.isEmpty()) {
            Map<Long, List<RegistrosPendientes>> registrosPorEfector = registrosPendientes.stream()
                    .collect(Collectors.groupingBy(registro -> registro.getEfector().getId()));

            for (Map.Entry<Long, List<RegistrosPendientes>> entry : registrosPorEfector.entrySet()) {
                Long idEfector = entry.getKey();
                List<RegistrosPendientes> registros = entry.getValue();

                PendientesSemanalDto pendientesSemanalDto = new PendientesSemanalDto();
                pendientesSemanalDto.setFechaInicio(LocalDate.now());
                pendientesSemanalDto.setFechaFin(LocalDate.now().plusDays(7));
                pendientesSemanalDto.setAnio(LocalDate.now().getYear());
                pendientesSemanalDto.setIdEfector(idEfector);
                pendientesSemanalDto.setIdRegistrosPendientes(
                        registros.stream().map(RegistrosPendientes::getId).collect(Collectors.toList()));

                PendientesSemanal pendienteSemanal = createUpdate(new PendientesSemanal(), pendientesSemanalDto);
                save(pendienteSemanal);
                System.out.println("***************************INICIO**************************************");
                System.out.println("Id del pendiente semanal: " + pendienteSemanal.getId());
                System.out.println("Fechas del pendiente semanal: " + pendienteSemanal.getFechaInicio() + " - "
                        + pendienteSemanal.getFechaFin());
                System.out.println("Efector del pendiente semanal: " +
                        (pendienteSemanal.getEfector() != null ? pendienteSemanal.getEfector().getNombre() : "NULL"));

                System.out.println("***************************FIN**************************************");
            }
        } else {
            System.out.println("No hay registros pendientes en la semana");
        }
    }
}
