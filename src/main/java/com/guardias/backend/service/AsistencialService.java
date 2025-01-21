package com.guardias.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.controller.PersonController;
import com.guardias.backend.dto.AsistencialDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.asistencial.AsistencialEfectorDto;
import com.guardias.backend.dto.asistencial.AsistencialListDto;
import com.guardias.backend.dto.asistencial.AsistencialListForLegajosDto;
import com.guardias.backend.dto.asistencial.AsistencialSummaryDto;
import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.Person;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.repository.AsistencialRepository;
import com.guardias.backend.repository.LegajoRepository;
import com.guardias.backend.repository.RegistroActividadRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AsistencialService {

    @Autowired
    AsistencialRepository asistencialRepository;
    @Autowired
    LegajoRepository legajoRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    @Lazy
    PersonController personController;
    /*
     * @Autowired
     * RegistroActividadService registroActividadService;
     */
    @Autowired
    RegistroActividadRepository registroActividadRepository;

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

    public boolean existsByDniAndActivoTrue(int dni) {
        return asistencialRepository.existsByDniAndActivoTrue(dni);
    }

    public boolean existsByCuil(String cuil) {
        return asistencialRepository.existsByCuil(cuil);
    }

    public Optional<Asistencial> findByDniAndActivoTrue(int dni) {
        return asistencialRepository.findByDniAndActivoTrue(dni);
    }

    public Optional<Asistencial> findByEmailAndActivoTrue(String email) {
        return asistencialRepository.findByEmailAndActivoTrue(email);
    }

    public ResponseEntity<?> validations(AsistencialDto asistencialDto, Long id) {
        ResponseEntity<?> respuestaValidaciones = personController.validations(asistencialDto, id);

        if (respuestaValidaciones.getStatusCode() != HttpStatus.OK) {
            return respuestaValidaciones;
        }
        return new ResponseEntity<>(new Mensaje("valido"), HttpStatus.OK);
    }

    public Asistencial createUpdate(Asistencial asistencial, AsistencialDto asistencialDto) {
        Person person = personController.createUpdate(asistencial, asistencialDto);
        asistencial = (Asistencial) person;

        if (asistencialDto.getIdRegistrosActividades() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (asistencial.getRegistrosActividades() != null) {
                for (RegistroActividad registro : asistencial.getRegistrosActividades()) {
                    for (Long id : asistencialDto.getIdRegistrosActividades()) {
                        if (!registro.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            }
            List<Long> idsToAdd = idList.isEmpty() ? asistencialDto.getIdRegistrosActividades() : idList;
            for (Long id : idsToAdd) {
                asistencial.getRegistrosActividades().add(registroActividadRepository.findById(id).get());
                registroActividadRepository.findById(id).get().setAsistencial(asistencial);
            }
        }
        asistencial.setActivo(true);
        return asistencial;
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

    /*
     * public void agregarTipoGuardia(Long idAsistencial, Long idTipoGuardia) {
     * // Buscar el Asistencial por su ID
     * Asistencial asistencial = asistencialRepository.findById(idAsistencial)
     * .orElseThrow(() -> new EntityNotFoundException("Asistencial no encontrado"));
     * 
     * // Buscar el TipoGuardia por su ID
     * TipoGuardia tipoGuardia = tipoGuardiaRepository.findById(idTipoGuardia)
     * .orElseThrow(() -> new EntityNotFoundException("TipoGuardia no encontrado"));
     * 
     * // Buscar o crear el Legajo para este Asistencial
     * Legajo legajo = asistencial.getLegajos().stream()
     * .filter(l -> l.getFechaFinal() == null) // Suponiendo que un legajo activo no
     * tiene fecha de
     * // finalización
     * .findFirst()
     * .orElseGet(() -> {
     * Legajo nuevoLegajo = new Legajo();
     * nuevoLegajo.setPersona(asistencial);
     * nuevoLegajo.setFechaInicio(LocalDate.now()); // Establece la fecha de inicio
     * actual
     * asistencial.getLegajos().add(nuevoLegajo); // Agregar el nuevo legajo a la
     * lista del asistencial
     * return nuevoLegajo;
     * });
     * 
     * // Agregar el TipoGuardia al Legajo
     * legajo.getTipoGuardias().add(tipoGuardia);
     * 
     * // Guardar los cambios en la base de datos
     * legajoRepository.save(legajo);
     * }
     * 
     */

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

    // filtrar asistenciales por tipo de guardia CARGO y mapea a
    // AsistencialSummaryDto
    public List<AsistencialSummaryDto> getAsistencialesByTipoGuardiaCargo(List<Asistencial> asistenciales) {
        List<AsistencialSummaryDto> dtoList = new ArrayList<>();

        for (Asistencial asistencial : asistenciales) {
            // Filtra por tipos de guardia CARGO
            boolean hasCargo = asistencial.getLegajos().stream()
                    .filter(legajo -> legajo.getFechaFinal() == null) // Solo legajos activos
                    .flatMap(legajo -> legajo.getTipoGuardias().stream()) // Extrae tipos de guardia de cada legajo
                                                                          // activo
                    .anyMatch(tipoGuardia -> tipoGuardia.getNombre().name().equals("CARGO"));

            if (hasCargo) {
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

    // filtrar asislenciales por tipo de guardia AGRUPACION y mapea a
    // AsistencialSummaryDto
    public List<AsistencialSummaryDto> getAsistencialesByTipoGuardiaAgrupacion(List<Asistencial> asistenciales) {
        List<AsistencialSummaryDto> dtoList = new ArrayList<>();

        for (Asistencial asistencial : asistenciales) {
            // Filtra por tipos de guardia AGRUPACION
            boolean hasAgrupacion = asistencial.getLegajos().stream()
                    .filter(legajo -> legajo.getFechaFinal() == null) // Solo legajos activos
                    .flatMap(legajo -> legajo.getTipoGuardias().stream()) // Extrae tipos de guardia de cada legajo
                                                                          // activo
                    .anyMatch(tipoGuardia -> tipoGuardia.getNombre().name().equals("AGRUPACION"));

            if (hasAgrupacion) {
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

    public List<AsistencialSummaryDto> getAsistencialesByEfectorAndTipoGuardiaExtraHabilitado(Long idEfector) {
        // Obtiene la lista de asistenciales activos relacionados con el efector
        List<Asistencial> asistenciales = asistencialRepository.findByEfectorAndActivoTrue(idEfector);

        return asistenciales.stream()
                // Filtra asistenciales con legajos activos y tipo de guardia "EXTRA"
                .filter(asistencial -> asistencial.getLegajos().stream()
                        .filter(legajo -> legajo.getFechaFinal() == null) // Legajos activos
                        .flatMap(legajo -> legajo.getTipoGuardias().stream())
                        .anyMatch(tipoGuardia -> "EXTRA".equals(tipoGuardia.getNombre().name()))) // Filtra por "EXTRA"
                // Filtra asistenciales con habilitaciones válidas para el efector especificado
                .filter(asistencial -> asistencial.getHabilitacionesGuardias().stream()
                        .anyMatch(habilitacion -> habilitacion.getEfectores().stream()
                                .anyMatch(efector -> efector.getId().equals(idEfector)))) // Verifica relación con
                                                                                          // efectores
                // Mapea los datos al DTO
                .map(asistencial -> new AsistencialSummaryDto(
                        asistencial.getId(),
                        asistencial.getNombre(),
                        asistencial.getApellido(),
                        asistencial.getLegajos().stream()
                                .flatMap(legajo -> legajo.getTipoGuardias().stream())
                                .map(tipoGuardia -> tipoGuardia.getNombre().name())
                                .distinct()
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public List<AsistencialSummaryDto> getAsistencialesByEfectorAndCargo(Long efectorId) {
        List<Asistencial> asistenciales = asistencialRepository.findByEfectorAndActivoTrue(efectorId);
        return getAsistencialesByTipoGuardiaCargo(asistenciales);
    }

    public List<AsistencialSummaryDto> getAsistencialesByEfectorAndAgrupacion(Long efectorId) {
        List<Asistencial> asistenciales = asistencialRepository.findByEfectorAndActivoTrue(efectorId);
        return getAsistencialesByTipoGuardiaAgrupacion(asistenciales);
    }

    public List<AsistencialEfectorDto> filterAsistencialesByEfector(List<Asistencial> asistenciales) {
        List<AsistencialEfectorDto> EfectorList = new ArrayList<>();

        for (Asistencial asistencial : asistenciales) {
            AsistencialEfectorDto dto = new AsistencialEfectorDto(
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
                    asistencial.isActivo(),
                    asistencial.isActivo());
            EfectorList.add(dto);
        }
        return EfectorList;
    }

    public List<AsistencialEfectorDto> getAsistencialesByEfector(Long efectorId) {
        List<Asistencial> asistenciales = asistencialRepository.findByEfectorAndActivoTrue(efectorId);
        return filterAsistencialesByEfector(asistenciales);
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