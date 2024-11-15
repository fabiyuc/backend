package com.guardias.backend.service;

import java.time.LocalDate;
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
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.LegajoRepository;
import com.guardias.backend.repository.TipoGuardiaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AsistencialService {

    @Autowired
    AsistencialRepository asistencialRepository;
    @Autowired
    TipoGuardiaRepository tipoGuardiaRepository;
    @Autowired
    LegajoRepository legajoRepository;
    @Autowired
    EfectorService efectorService;

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
        // Buscar el Asistencial por su ID
        Asistencial asistencial = asistencialRepository.findById(idAsistencial)
                .orElseThrow(() -> new EntityNotFoundException("Asistencial no encontrado"));

        // Buscar el TipoGuardia por su ID
        TipoGuardia tipoGuardia = tipoGuardiaRepository.findById(idTipoGuardia)
                .orElseThrow(() -> new EntityNotFoundException("TipoGuardia no encontrado"));

        // Buscar o crear el Legajo para este Asistencial
        Legajo legajo = asistencial.getLegajos().stream()
                .filter(l -> l.getFechaFinal() == null) // Suponiendo que un legajo activo no tiene fecha de
                                                        // finalización
                .findFirst()
                .orElseGet(() -> {
                    Legajo nuevoLegajo = new Legajo();
                    nuevoLegajo.setPersona(asistencial);
                    nuevoLegajo.setFechaInicio(LocalDate.now()); // Establece la fecha de inicio actual
                    asistencial.getLegajos().add(nuevoLegajo); // Agregar el nuevo legajo a la lista del asistencial
                    return nuevoLegajo;
                });

        // Agregar el TipoGuardia al Legajo
        legajo.getTipoGuardias().add(tipoGuardia);

        // Guardar los cambios en la base de datos
        legajoRepository.save(legajo);
    }

    

    // Método para obtener la lista de Asistenciales y convertirlos a
    // AsistencialSummaryDto
    public List<AsistencialSummaryDto> getAsistencialSummaryList() {
        // Obtiene la lista de Asistenciales activos
        List<Asistencial> asistenciales = asistencialRepository.findByActivoTrue().orElse(new ArrayList<>());
        // Crea una lista de AsistencialSummaryDto
        List<AsistencialSummaryDto> summaryDtoList = new ArrayList<>();
        // Recorre la lista de Asistenciales
        for (Asistencial asistencial : asistenciales) {
            // Mapea los nombres de los tipos de guardia a una lista de strings
            List<String> nombresTiposGuardias = asistencial.getLegajos().stream()
                    .filter(legajo -> legajo.getFechaFinal() == null) // Legajos activos
                    .flatMap(legajo -> legajo.getTipoGuardias().stream()) // Obtener tipos de guardia de cada legajo
                                                                          // activo
                    .map(tipoGuardia -> tipoGuardia.getNombre().name()) // Usa el método name() del enum
                    .collect(Collectors.toList()); // Convierte el stream a una lista
            // Crea el DTO
            AsistencialSummaryDto dto = new AsistencialSummaryDto(
                    asistencial.getId(),
                    asistencial.getNombre(),
                    asistencial.getApellido(),
                    nombresTiposGuardias);
            // Agrega el DTO a la lista
            summaryDtoList.add(dto);
        }
        // Retorna la lista de DTOs
        return summaryDtoList;
    }

    // Método para obtener la lista de Asistenciales y convertirlos a
    // AsistencialListDto
    public List<AsistencialListDto> getAsistencialList() {
        // Obtiene la lista de Asistenciales activos
        List<Asistencial> asistenciales = asistencialRepository.findByActivoTrue().orElse(new ArrayList<>());
        List<AsistencialListDto> dtoList = new ArrayList<>();

        for (Asistencial asistencial : asistenciales) {
            // Mapea los nombres de los tipos de guardia activos a una lista de strings
            List<String> nombresTiposGuardias = asistencial.getLegajos().stream()
                    .filter(legajo -> legajo.getFechaFinal() == null) // Legajos activos
                    .flatMap(legajo -> legajo.getTipoGuardias().stream()) // Obtener tipos de guardia de cada legajo
                                                                          // activo
                    .map(tipoGuardia -> tipoGuardia.getNombre().name()) // Usa el método name() del enum
                    .collect(Collectors.toList());

            // crea el DTO
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

            // Agrega el DTO a la lista
            dtoList.add(dto);
        }

        return dtoList;
    }

    // Verifica si alguno de los tipos de guardias del Asistencial es
    // 'CONTRAFACTURA' para excluirlo de la lista
    public List<AsistencialListForLegajosDto> getAsistencialListForLegajos() {

        List<Asistencial> asistenciales = asistencialRepository.findByActivoTrue().orElse(new ArrayList<>());

        List<AsistencialListForLegajosDto> dtoList = new ArrayList<>();

        for (Asistencial asistencial : asistenciales) {

            // Verifica si alguno de los tipos de guardia de los legajos activos es
            // 'CONTRAFACTURA'
            boolean hasContrafactura = asistencial.getLegajos().stream()
                    .filter(legajo -> legajo.getFechaFinal() == null) // Solo legajos activos
                    .flatMap(legajo -> legajo.getTipoGuardias().stream()) // Extrae tipos de guardia de cada legajo
                                                                          // activo
                    .anyMatch(tipoGuardia -> tipoGuardia.getNombre().name().equals("CONTRAFACTURA"));

            if (!hasContrafactura) {
                // Mapea los nombres de los tipos de guardia activos a una lista de strings
                List<String> nombresTiposGuardias = asistencial.getLegajos().stream()
                        .filter(legajo -> legajo.getFechaFinal() == null) // Solo legajos activos
                        .flatMap(legajo -> legajo.getTipoGuardias().stream())
                        .map(tipoGuardia -> tipoGuardia.getNombre().name())
                        .collect(Collectors.toList());

                AsistencialListForLegajosDto dto = new AsistencialListForLegajosDto(
                        asistencial.getId(),
                        asistencial.getNombre(),
                        asistencial.getApellido(),
                        nombresTiposGuardias);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    // filtra ASISTENCIALES por tipos de guardia CARGO o AGRUPACION y mapea a
    // AsistencialSummaryDto
    private List<AsistencialSummaryDto> filterAndMapByCargoOrAgrupacion(List<Asistencial> asistenciales) {
        List<AsistencialSummaryDto> dtoList = new ArrayList<>();

        for (Asistencial asistencial : asistenciales) {
            // Filtra por tipos de guardia CARGO o AGRUPACION
            boolean hasCargoOrAgrupacion = asistencial.getLegajos().stream()
                    .filter(legajo -> legajo.getFechaFinal() == null) // Solo legajos activos
                    .flatMap(legajo -> legajo.getTipoGuardias().stream()) // Extrae tipos de guardia de cada legajo
                                                                          // activo
                    .anyMatch(tipoGuardia -> tipoGuardia.getNombre().name().equals("CARGO") ||
                            tipoGuardia.getNombre().name().equals("AGRUPACION"));

            if (hasCargoOrAgrupacion) {
                // Mapea los nombres de los tipos de guardia
                List<String> nombresTiposGuardias = asistencial.getLegajos().stream()
                        .filter(legajo -> legajo.getFechaFinal() == null) // Solo legajos activos
                        .flatMap(legajo -> legajo.getTipoGuardias().stream())
                        .map(tipoGuardia -> tipoGuardia.getNombre().name())
                        .collect(Collectors.toList());

                // Crea el DTO
                AsistencialSummaryDto dto = new AsistencialSummaryDto(
                        asistencial.getId(),
                        asistencial.getNombre(),
                        asistencial.getApellido(),
                        nombresTiposGuardias);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    // Asistenciales por Udo y tipoGuardia CARGO y AGRUPACION
    public List<AsistencialSummaryDto> getAsistencialesByUdoAndTipoGuardia(Long udoId) {
        List<Asistencial> asistenciales = asistencialRepository.findByUdoAndActivoTrue(udoId);
        return filterAndMapByCargoOrAgrupacion(asistenciales);
    }

    // Asistenciales por Efector y tipoGuardia CARGO y AGRUPACION
    public List<AsistencialSummaryDto> getAsistencialesByEfectorAndTipoGuardia(Long efectorId) {
        List<Asistencial> asistenciales = asistencialRepository.findByEfectorAndActivoTrue(efectorId);
        return filterAndMapByCargoOrAgrupacion(asistenciales);
    }

    public boolean esPlanta(Long idAsistencial, Long idEfector) {

        if (idAsistencial == null || idEfector == null) {
            throw new IllegalArgumentException("Los IDs de Asistencial y Efector no pueden ser nulos.");
        }

        if (!asistencialRepository.existsById(idAsistencial)) {
            throw new EntityNotFoundException("El asistencial con ID " + idAsistencial + " no existe.");
        }
       
        if (!efectorService.existsById(idEfector)) {
            throw new EntityNotFoundException("El efector con ID " + idEfector + " no existe.");
        }

        // Filtramos el legajo activo y verificamos los efectores asociados
        Asistencial asistencial = asistencialRepository.findById(idAsistencial).get();
        return asistencial.getLegajos().stream()
                .filter(Legajo::isActivo) // Solo consideramos el legajo activo
                .flatMap(legajo -> legajo.getEfectores().stream())
                .anyMatch(efector -> efector.getId().equals(idEfector));
    }

}