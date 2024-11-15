package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.LegajoDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.AutoridadRepository;
import com.guardias.backend.repository.LegajoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LegajoService {

    @Autowired
    LegajoRepository legajoRepository;

    @Autowired
    AutoridadRepository autoridadRepository;
    @Autowired
    AsistencialRepository asistencialRepository;
    @Autowired
    TipoGuardiaService tipoGuardiaService;

    public List<Legajo> findByActivoTrue() {
        return legajoRepository.findByActivoTrue();
    }

    public List<Legajo> findAll() {
        return legajoRepository.findAll();
    }

    public Optional<Legajo> findById(Long id) {
        return legajoRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return legajoRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (legajoRepository.existsById(id) && legajoRepository.findById(id).get().isActivo());
    }

    public void save(Legajo legajo) {
        legajoRepository.save(legajo);
    }

    public void deleteById(Long id) {
        legajoRepository.deleteById(id);
    }

    public boolean esAutoridad(Long asistencialId) {
        // Implementa la lógica para verificar si el Asistencial está en la tabla de
        // autoridades
        return autoridadRepository.existsByPersonaIdAndActivoTrue(asistencialId);
    }

    // Método para verificar si una persona es contrafactura
    public boolean esContraFactura(Long idPersona) {

        Optional<Asistencial> asistencialOpt = asistencialRepository.findById(idPersona);
        if (asistencialOpt.isPresent()) {
            Asistencial persona = asistencialOpt.get();

            // Si la persona no tiene legajos, retorna false para permitir la creación
            if (persona.getLegajos() == null || persona.getLegajos().isEmpty()) {
                return true;
            }
            // Recorre los legajos activos del asistencial y verifica si alguno tiene un
            // TipoGuardia de CONTRAFACTURA
            return persona.getLegajos().stream()
                    .filter(legajo -> legajo.getFechaFinal() == null) // modificar esto, debe validar que activo = true
                    .flatMap((Legajo legajo) -> legajo.getTipoGuardias().stream()) // Obtener los tipos de guardias de
                                                                                   // cada legajo activo
                    .anyMatch(tipoGuardia -> tipoGuardia.getNombre() == TipoGuardiaEnum.CONTRAFACTURA);
        }
        // Si no existe la persona, retorna false
        return false;
    }

    public void updateTipoGuardias(Legajo legajo, LegajoDto legajoDto) {
        if (legajo.getTipoGuardias() == null) {
                legajo.setTipoGuardias(new ArrayList<>());
            }

            // crea una nueva lista para almacenar los tipos de guardias actualizados
            List<TipoGuardia> tipoGuardiasActualizados = new ArrayList<>();
            for (TipoGuardia tipoGuardia : legajo.getTipoGuardias()) {
                if (legajoDto.getIdTipoGuardias().contains(tipoGuardia.getId())) {
                    tipoGuardiasActualizados.add(tipoGuardia);
                } else {
                    // Remover el legajo de los tipos de guardias que se eliminarán
                    tipoGuardia.getLegajos().remove(legajo);
                }
            }
            legajo.setTipoGuardias(tipoGuardiasActualizados);

            // agregar nuevos tipos de guardia si no estan presentes
            for (Long id : legajoDto.getIdTipoGuardias()) {
                boolean found = false;
                for (TipoGuardia tipoGuardia : legajo.getTipoGuardias()) {
                    if (tipoGuardia.getId().equals(id)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    TipoGuardia tipoGuardiaToAdd = tipoGuardiaService.findById(id).get();
                    if (tipoGuardiaToAdd != null) {
                        legajo.getTipoGuardias().add(tipoGuardiaToAdd);
                        tipoGuardiaToAdd.getLegajos().add(legajo);
                    } else {
                        throw new RuntimeException("No se encontró el tipo de guardia con ID: " + id);
                    }
                }
            }
    }

    /*
     * public List<ProfesionLegajoDto> findProfesiones() {
     * return legajoRepository.findAll().stream()
     * .map(legajo -> new ProfesionLegajoDto(legajo.getProfesion().getNombre()))
     * .distinct()
     * .collect(Collectors.toList());
     * }
     */
}