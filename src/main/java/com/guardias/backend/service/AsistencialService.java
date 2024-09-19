package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.asistencial.AsistencialListDto;
import com.guardias.backend.dto.asistencial.AsistencialListForLegajosDto;
import com.guardias.backend.dto.asistencial.AsistencialSummaryDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.TipoGuardiaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AsistencialService {

    @Autowired
    AsistencialRepository asistencialRepository;
    @Autowired
    TipoGuardiaRepository tipoGuardiaRepository;

    public Optional<List<Asistencial>> findByActivoTrue() {
        return asistencialRepository.findByActivoTrue();
    }

    public List<Asistencial> findAll() {
        return asistencialRepository.findAll();
    }

    public Optional<Asistencial> findById(Long id) {
        return asistencialRepository.findById(id);
    }

    public Optional<Asistencial> findByCuil(String cuil) {
        return asistencialRepository.findByCuil(cuil);
    }

    public boolean existsById(Long id) {
        return asistencialRepository.existsById(id);
    }

    public boolean existsByDni(int dni) {
        return asistencialRepository.existsByDni(dni);
    }

    public boolean existsByCuil(String cuil) {
        return asistencialRepository.existsByCuil(cuil);
    }

    public Optional<Asistencial> findByDni(int dni) {
        return asistencialRepository.findByDni(dni);
    }

    public void save(Asistencial asistencial) {
        asistencialRepository.save(asistencial);
    }

    public void deleteById(Long id) {
        asistencialRepository.deleteById(id);
    }

    public boolean activo(Long id) {
        return (asistencialRepository.existsById(id) && asistencialRepository.findById(id).get().isActivo());
    }

    public boolean activoDni(int dni) {
        return (asistencialRepository.existsByDni(dni) && asistencialRepository.findByDni(dni).get().isActivo());
    }

    public void agregarTipoGuardia(Long idAsistencial, Long idTipoGuardia) {

        Asistencial asistencial = asistencialRepository.findById(idAsistencial).get();
        TipoGuardia tipoGuardia = tipoGuardiaRepository.findById(idTipoGuardia).get();

        tipoGuardia.getAsistenciales().add(asistencial);
        asistencial.getTiposGuardias().add(tipoGuardia);

    }

    public boolean esContraFactura(Long idPersona) {
        Optional<Asistencial> asistencial = asistencialRepository.findById(idPersona);
        if (asistencial.isPresent()) {
            Asistencial persona = asistencial.get();
            return persona.getTiposGuardias().stream()
                    .anyMatch(tipoGuardia -> tipoGuardia.getNombre() == TipoGuardiaEnum.CONTRAFACTURA);
        }
        return false;
    }

    // Método para obtener la lista de Asistenciales y convertirlos a AsistencialSummaryDto
    public List<AsistencialSummaryDto> getAsistencialSummaryList() {
        List<Asistencial> asistenciales = asistencialRepository.findByActivoTrue().orElse(new ArrayList<>());
        List<AsistencialSummaryDto> summaryDtoList = new ArrayList<>();

        for (Asistencial asistencial : asistenciales) {
            List<String> nombresTiposGuardias = asistencial.getTiposGuardias().stream()
                    .map(tipoGuardia -> tipoGuardia.getNombre().name()) // Usa el método name() del enum
                    .collect(Collectors.toList());

            AsistencialSummaryDto dto = new AsistencialSummaryDto(
                    asistencial.getId(),
                    asistencial.getNombre(),
                    asistencial.getApellido(),
                    nombresTiposGuardias);
            summaryDtoList.add(dto);
        }
        return summaryDtoList;
    }

    public List<AsistencialListDto> getAsistencialList() {
        List<Asistencial> asistenciales = asistencialRepository.findByActivoTrue().orElse(new ArrayList<>());
        List<AsistencialListDto> DtoList = new ArrayList<>();

        for (Asistencial asistencial : asistenciales) {
            List<String> nombresTiposGuardias = asistencial.getTiposGuardias().stream()
            /* List<Long> idTiposGuardias = asistencial.getTiposGuardias().stream() */
                   // .map(tipoGuardia -> tipoGuardia.getId()) 
                   .map(tipoGuardia -> tipoGuardia.getNombre().name()) // Usa el método name() del enum
                    .collect(Collectors.toList());

            AsistencialListDto dto = new AsistencialListDto(
                    asistencial.getId(),
                    asistencial.getNombre(),
                    asistencial.getApellido(),
                    asistencial.getDni(),
                    asistencial.getCuil(),
                    asistencial.getFechaNacimiento(),
                    asistencial.getSexo(),
                    asistencial.getTelefono(),
                    asistencial.getEmail(),
                    asistencial.getDomicilio(),
                    nombresTiposGuardias);

                    DtoList.add(dto);
        }

        return DtoList;
    }

    // Verifica si alguno de los tipos de guardias del Asistencial es 'CONTRAFACTURA' para excluirlo de la lista
    public List<AsistencialListForLegajosDto> getAsistencialListForLegajos() {
    
    List<Asistencial> asistenciales = asistencialRepository.findByActivoTrue().orElse(new ArrayList<>());

    List<AsistencialListForLegajosDto> dtoList = new ArrayList<>();

    for (Asistencial asistencial : asistenciales) {
        
        boolean hasContrafactura = asistencial.getTiposGuardias().stream()
            .anyMatch(tipoGuardia -> tipoGuardia.getNombre().name().equals("CONTRAFACTURA"));

        if (!hasContrafactura) {
            // Mapea los nombres de los tipos de guardia a una lista de strings
            List<String> nombresTiposGuardias = asistencial.getTiposGuardias().stream()
                .map(tipoGuardia -> tipoGuardia.getNombre().name())
                .collect(Collectors.toList());

            AsistencialListForLegajosDto dto = new AsistencialListForLegajosDto(
                asistencial.getId(),
                asistencial.getNombre(),
                asistencial.getApellido(),
                nombresTiposGuardias
            );

            dtoList.add(dto);
        }
    }

    return dtoList; 
    }

    public List<AsistencialSummaryDto> getAsistencialesByUdoAndTipoGuardia(Long udoId) {

        List<Asistencial> asistenciales = asistencialRepository.findByUdoAndActivoTrue(udoId);
    
        List<AsistencialSummaryDto> dtoList = new ArrayList<>();
    
        for (Asistencial asistencial : asistenciales) {
            
            boolean hasCargoOrAgrupacion = asistencial.getTiposGuardias().stream()
                .anyMatch(tipoGuardia -> tipoGuardia.getNombre().name().equals("CARGO") || tipoGuardia.getNombre().name().equals("AGRUPACION"));
    
            if (hasCargoOrAgrupacion) {
                // Mapeam los nombres de los tipos de guardia a una lista de strings
                List<String> nombresTiposGuardias = asistencial.getTiposGuardias().stream()
                    .map(tipoGuardia -> tipoGuardia.getNombre().name())
                    .collect(Collectors.toList());
    
                AsistencialSummaryDto dto = new AsistencialSummaryDto(
                    asistencial.getId(),
                    asistencial.getNombre(),
                    asistencial.getApellido(),
                    nombresTiposGuardias
                );
    
                dtoList.add(dto);
            }
        }
    
        return dtoList;
    }
    

}