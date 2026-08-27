package com.guardias.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.adicional.AdicionalListDto;
import com.guardias.backend.dto.asistencial.AsistencialListForRmensualDto;
import com.guardias.backend.dto.categoria.CategoriaListDto;
import com.guardias.backend.dto.cronogramaDefinitivo.CronogramaDefinitivoListDto;
import com.guardias.backend.dto.legajo.LegajoListDto;
import com.guardias.backend.dto.novedadPersonal.NovedadPersonalListDto;
import com.guardias.backend.dto.registroActividad.RegistroActividadListDto;
import com.guardias.backend.dto.registroMensual.RegistroMensualListDto;
import com.guardias.backend.dto.revista.RevistaListDto;
import com.guardias.backend.dto.tipoLicencia.TipoLicenciaListDto;
import com.guardias.backend.dto.tipoRevista.TipoRevistaListDto;
import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.CronogramaDefinitivoRepository;
import com.guardias.backend.repository.DdjjRepository;

@Service
@Transactional
public class CronogramaDefinitivoService {

    @Autowired
    CronogramaDefinitivoRepository cronogramaDefinitivoRepository;
    @Autowired
    AsistencialService asistencialService;
    @Autowired
    EfectorService efectorService;
    @Autowired
    DdjjRepository ddjjRepository;
    @Autowired
    RegistroMensualService registroMensualService;

    public Optional<List<CronogramaDefinitivo>> findByActivoTrue() {
        return cronogramaDefinitivoRepository.findByActivoTrue();
    }

    public List<CronogramaDefinitivo> findAll() {
        return cronogramaDefinitivoRepository.findAll();
    }

    public Optional<CronogramaDefinitivo> findById(Long id) {
        return cronogramaDefinitivoRepository.findById(id);
    }

    public List<CronogramaDefinitivo> findByAnioAndMesAndIdEfectorAndActivoTrue(int anio, MesesEnum mes,
            Long idEfector) {
        return cronogramaDefinitivoRepository.findByAnioAndMesAndEfectorIdAndActivoTrue(anio, mes, idEfector);
    }

    public boolean existsByAnioAndMes(int anio, MesesEnum mes) {
        return cronogramaDefinitivoRepository.existsByAnioAndMes(anio, mes);
    }

    public boolean existsById(Long id) {
        return cronogramaDefinitivoRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (cronogramaDefinitivoRepository.existsById(id)
                && cronogramaDefinitivoRepository.findById(id).get().isActivo());
    }

    public void save(CronogramaDefinitivo cronogramaDefinitivo) {
        cronogramaDefinitivoRepository.save(cronogramaDefinitivo);
    }

    public void deleteById(Long id) {
        cronogramaDefinitivoRepository.deleteById(id);
    }

    /* public ResponseEntity<?> validations(CronogramaDefinitivoDto cronogramaDefinitivoDto) {

        if (cronogramaDefinitivoDto.getMes() == null)
            return new ResponseEntity(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);

        if (cronogramaDefinitivoDto.getAnio() < 1991)
            return new ResponseEntity(new Mensaje("El año es incorrecto"), HttpStatus.BAD_REQUEST);

        if (cronogramaDefinitivoDto.getIdDdjjs() == null)
            return new ResponseEntity(new Mensaje("la lista de ddjj no debe ser nula"), HttpStatus.BAD_REQUEST);

        // Verificar que las DDJJ coincidan en mes y año
        ResponseEntity<?> validacionDdjjs = validarCoincidenciaMesAnioDdjjs(
                cronogramaDefinitivoDto.getIdDdjjs(),
                cronogramaDefinitivoDto.getMes(),
                cronogramaDefinitivoDto.getAnio());

        if (validacionDdjjs.getStatusCode() != HttpStatus.OK) {
            return validacionDdjjs;
        }
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    } */

    private ResponseEntity<?> validarCoincidenciaMesAnioDdjjs(List<Long> idDdjjs, MesesEnum mes, int anio) {
        if (idDdjjs == null || idDdjjs.isEmpty()) {
            return new ResponseEntity(new Mensaje("La lista de DDJJ está vacía"), HttpStatus.BAD_REQUEST);
        }

        for (Long idDdjj : idDdjjs) {
            Ddjj ddjj = ddjjRepository.findById(idDdjj).orElse(null);

            if (ddjj == null) {
                return new ResponseEntity(new Mensaje("DDJJ no encontrada con ID: " + idDdjj), HttpStatus.BAD_REQUEST);
            }

            if (ddjj.getMes() != mes) {
                return new ResponseEntity(
                        new Mensaje("La DDJJ con ID " + idDdjj + " es del mes " + ddjj.getMes() +
                                " pero se esperaba " + mes),
                        HttpStatus.BAD_REQUEST);
            }

            if (ddjj.getAnio() != anio) {
                return new ResponseEntity(
                        new Mensaje("La DDJJ con ID " + idDdjj + " es del año " + ddjj.getAnio() +
                                " pero se esperaba " + anio),
                        HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity(new Mensaje("DDJJ válidas"), HttpStatus.OK);
    }

    /* public CronogramaDefinitivo createUpdate(CronogramaDefinitivo cronogramaDefinitivo,
            CronogramaDefinitivoDto cronogramaDefinitivoDto) {

        if (cronogramaDefinitivoDto.getMes() != null
                && !cronogramaDefinitivoDto.getMes().equals(cronogramaDefinitivo.getMes()))
            cronogramaDefinitivo.setMes(cronogramaDefinitivoDto.getMes());

        if (cronogramaDefinitivoDto.getAnio() != cronogramaDefinitivo.getAnio())
            cronogramaDefinitivo.setAnio(cronogramaDefinitivoDto.getAnio());

        if (cronogramaDefinitivoDto.getIdEfector() != null && (cronogramaDefinitivo.getEfector() == null
                || !Objects.equals(cronogramaDefinitivo.getEfector().getId(),
                        cronogramaDefinitivoDto.getIdEfector()))) {
            cronogramaDefinitivo.setEfector(efectorService.findById(cronogramaDefinitivoDto.getIdEfector()));
        }

        // Validar si idDdjjs no es null
        if (cronogramaDefinitivoDto.getIdDdjjs() != null) {
            // Si no es null, procesar las ddjj
            for (Long idDdjj : cronogramaDefinitivoDto.getIdDdjjs()) {
                // Lógica para procesar cada idDdjj
                Ddjj ddjj = ddjjRepository.findById(idDdjj).orElse(null);
                if (ddjj != null && !cronogramaDefinitivo.getDdjjs().contains(ddjj)) {
                    cronogramaDefinitivo.getDdjjs().add(ddjj);
                    ddjj.getCronogramasDefinitivos().add(cronogramaDefinitivo);
                }
            }
        }

        cronogramaDefinitivo.setActivo(true);
        return cronogramaDefinitivo;
    } */

    /*
     * public CronogramaDefinitivo createUpdateDefinitivo(CronogramaDefinitivoDto
     * dto) {
     * 
     * // según tipo de quincena
     * if (dto.getQuincena() == QuincenaEnum.PRIMERA) {
     * return processPrimeraQuincena(dto);
     * } else if (dto.getQuincena() == QuincenaEnum.SEGUNDA) {
     * return processSegundaQuincena(dto);
     * } else if (dto.getQuincena() == QuincenaEnum.FUERA_DE_TERMINO) {
     * return processFueraDeTermino(dto);
     * } else {
     * throw new IllegalArgumentException("Tipo de quincena no válido: " +
     * dto.getQuincena());
     * }
     * }
     */

    /* public CronogramaDefinitivo createUpdateDefinitivo(CronogramaDefinitivoDto dto) {

        return createNewCronograma(dto);
    } */

    /*
     * private CronogramaDefinitivo processPrimeraQuincena(CronogramaDefinitivoDto
     * dto) {
     * // Verificar si ya existe primera quincena
     * Optional<CronogramaDefinitivo> existente = cronogramaDefinitivoRepository
     * .findByEfectorIdAndMesAndAnioAndQuincenaAndActivoTrue(
     * dto.getIdEfector(), dto.getMes(), dto.getAnio(), QuincenaEnum.PRIMERA);
     * 
     * if (existente.isPresent()) {
     * throw new
     * IllegalArgumentException("Ya existe un cronograma activo para la primera quincena"
     * );
     * }
     * 
     * // Crear nueva primera quincena
     * return createNewCronograma(dto);
     * }
     */

    /*
     * private CronogramaDefinitivo processSegundaQuincena(CronogramaDefinitivoDto
     * dto) {
     * // Buscar primera quincena existente
     * Optional<CronogramaDefinitivo> primeraQuincenaOpt =
     * cronogramaDefinitivoRepository
     * .findByEfectorIdAndMesAndAnioAndQuincenaAndActivoTrue(
     * dto.getIdEfector(), dto.getMes(), dto.getAnio(), QuincenaEnum.PRIMERA);
     * 
     * if (primeraQuincenaOpt.isPresent()) {
     * // Fusionar en cronograma SEGUNDA
     * CronogramaDefinitivo cronogramaSegunda =
     * createCronogramaFusionado(primeraQuincenaOpt.get(), dto,
     * QuincenaEnum.SEGUNDA);
     * 
     * // Desactivar primera quincena
     * primeraQuincenaOpt.get().setActivo(false);
     * cronogramaDefinitivoRepository.save(primeraQuincenaOpt.get());
     * 
     * return cronogramaSegunda;
     * } else {
     * // Crear con estado SEGUNDA(sin fusion)
     * return createNewCronograma(dto);
     * }
     * }
     */

    /*
     * private CronogramaDefinitivo createCronogramaCompleto(CronogramaDefinitivo
     * primeraQuincena,
     * CronogramaDefinitivoDto completarDto) {
     * CronogramaDefinitivo completo = new CronogramaDefinitivo();
     * 
     * // Configurar datos base usando el método existente createUpdate
     * completo = createUpdate(completo, completarDto);
     * completo.setQuincena(QuincenaEnum.SEGUNDA);
     * 
     * // Combinar DDJJs de ambas quincenas
     * Set<Ddjj> todasDdjjs = new HashSet<>(primeraQuincena.getDdjjs());
     * 
     * for (Long idDdjj : completarDto.getIdDdjjs()) {
     * Ddjj ddjj = ddjjRepository.findById(idDdjj)
     * .orElseThrow(() -> new IllegalArgumentException("DDJJ no encontrada: " +
     * idDdjj));
     * todasDdjjs.add(ddjj);
     * }
     * 
     * completo.setDdjjs(new ArrayList<>(todasDdjjs));
     * return completo;
     * }
     */

   /*  private CronogramaDefinitivo createNewCronograma(CronogramaDefinitivoDto dto) {
        CronogramaDefinitivo nuevo = new CronogramaDefinitivo();
        return createUpdate(nuevo, dto);
    } */

    /*
     * private CronogramaDefinitivo processFueraDeTermino(CronogramaDefinitivoDto
     * dto) {
     * // Busco en orden: SEGUNDA -> PRIMERA (el más reciente primero)
     * 
     * // 1. Buscar SEGUNDA quincena (que ya incluye PRIMERA si existía)
     * Optional<CronogramaDefinitivo> segundaQuincenaOpt =
     * cronogramaDefinitivoRepository
     * .findByEfectorIdAndMesAndAnioAndQuincenaAndActivoTrue(
     * dto.getIdEfector(), dto.getMes(), dto.getAnio(), QuincenaEnum.SEGUNDA);
     * 
     * if (segundaQuincenaOpt.isPresent()) {
     * // Fusionar SEGUNDA en FUERA_DE_TERMINO
     * CronogramaDefinitivo cronogramaFueraTermino = createCronogramaFusionado(
     * segundaQuincenaOpt.get(), dto, QuincenaEnum.FUERA_DE_TERMINO);
     * 
     * // Desactivar SEGUNDA quincena
     * segundaQuincenaOpt.get().setActivo(false);
     * cronogramaDefinitivoRepository.save(segundaQuincenaOpt.get());
     * 
     * return cronogramaFueraTermino;
     * }
     * // 2. Si no existe SEGUNDA, buscar PRIMERA quincena
     * Optional<CronogramaDefinitivo> primeraQuincenaOpt =
     * cronogramaDefinitivoRepository
     * .findByEfectorIdAndMesAndAnioAndQuincenaAndActivoTrue(
     * dto.getIdEfector(), dto.getMes(), dto.getAnio(), QuincenaEnum.PRIMERA);
     * 
     * if (primeraQuincenaOpt.isPresent()) {
     * // Fusionar PRIMERA en FUERA_DE_TERMINO
     * CronogramaDefinitivo cronogramaFueraTermino = createCronogramaFusionado(
     * primeraQuincenaOpt.get(), dto, QuincenaEnum.FUERA_DE_TERMINO);
     * 
     * // Desactivar PRIMERA quincena
     * primeraQuincenaOpt.get().setActivo(false);
     * cronogramaDefinitivoRepository.save(primeraQuincenaOpt.get());
     * 
     * return cronogramaFueraTermino;
     * }
     * 
     * // 3. Si no existe ninguno, crear directamente FUERA_DE_TERMINO
     * return createNewCronograma(dto);
     * }
     */

    // Método genérico para fusionar cronogramas
    /*
     * private CronogramaDefinitivo createCronogramaFusionado(CronogramaDefinitivo
     * cronogramaAnterior,
     * CronogramaDefinitivoDto nuevoDto, QuincenaEnum nuevoEstado) {
     * CronogramaDefinitivo fusionado = new CronogramaDefinitivo();
     * 
     * // Configurar datos base usando el método existente createUpdate
     * fusionado = createUpdate(fusionado, nuevoDto);
     * fusionado.setQuincena(nuevoEstado);
     * 
     * // Combinar DDJJs del cronograma anterior con las nuevas
     * Set<Ddjj> todasDdjjs = new HashSet<>(cronogramaAnterior.getDdjjs());
     * 
     * for (Long idDdjj : nuevoDto.getIdDdjjs()) {
     * Ddjj ddjj = ddjjRepository.findById(idDdjj)
     * .orElseThrow(() -> new IllegalArgumentException("DDJJ no encontrada: " +
     * idDdjj));
     * todasDdjjs.add(ddjj);
     * }
     * 
     * fusionado.setDdjjs(new ArrayList<>(todasDdjjs));
     * return fusionado;
     * }
     */

    /*
     * private CronogramaDefinitivo
     * createCronogramaFueraDeTermino(CronogramaDefinitivo cronogramaAnterior,
     * CronogramaDefinitivoDto nuevoDto) {
     * CronogramaDefinitivo fueraTermino = new CronogramaDefinitivo();
     * 
     * // Configurar datos base usando el método existente createUpdate
     * fueraTermino = createUpdate(fueraTermino, nuevoDto);
     * fueraTermino.setQuincena(QuincenaEnum.COMPLETO);
     * 
     * // Combinar DDJJs del cronograma anterior con las nuevas
     * Set<Ddjj> todasDdjjs = new HashSet<>(cronogramaAnterior.getDdjjs());
     * 
     * for (Long idDdjj : nuevoDto.getIdDdjjs()) {
     * Ddjj ddjj = ddjjRepository.findById(idDdjj)
     * .orElseThrow(() -> new IllegalArgumentException("DDJJ no encontrada: " +
     * idDdjj));
     * todasDdjjs.add(ddjj);
     * }
     * 
     * fueraTermino.setDdjjs(new ArrayList<>(todasDdjjs));
     * return fueraTermino;
     * }
     */

    public CronogramaDefinitivo createCronogramaDefinitivo(Long idAsistencial, Long idEfector, MesesEnum mesEnum,
            int anio) {

        CronogramaDefinitivo cronogramaDefinitivo = new CronogramaDefinitivo();
        cronogramaDefinitivo.setMes(mesEnum);
        cronogramaDefinitivo.setAnio(anio);
        cronogramaDefinitivo.setEfector(efectorService.findById(idEfector));
        cronogramaDefinitivo.setActivo(true);

        try {
            save(cronogramaDefinitivo);
            return cronogramaDefinitivo;
        } catch (Exception e) {
            System.out.println(
                    "error al crear el cronograma definitivo-  registroMensualService Ln196 -- " + e.getMessage());
            return null;
        }
    }

    public List<CronogramaDefinitivoListDto> findByAnioMesIdEfectorTipoGuardiaAndActivoTrue(
            int anio, MesesEnum mes, Long idEfector, Long idTipoGuardia) {

        List<CronogramaDefinitivo> cronogramas = cronogramaDefinitivoRepository
                .findByAnioAndMesAndEfectorIdAndActivoTrueAndTipoGuardia(anio, mes, idEfector, idTipoGuardia);

        return cronogramas.stream()
                .map(this::convertirACronogramaDefinitivoListDto)
                .collect(Collectors.toList());
    }
    /* public List<CronogramaDefinitivoListDto> findByAnioMesIdEfectorTipoGuardiaAndActivoTrue(
            int anio, MesesEnum mes, Long idEfector, Long idTipoGuardia) {

        List<CronogramaDefinitivo> cronogramas = cronogramaDefinitivoRepository
                .findByAnioAndMesAndEfectorIdAndActivoTrue(anio, mes, idEfector);

        System.out.println("=== INICIO findByAnioMesIdEfectorTipoGuardiaAndActivoTrue ===");
        System.out.println("Parámetros - anio: " + anio + ", mes: " + mes + ", idEfector: " + idEfector
                + ", idTipoGuardia: " + idTipoGuardia);
        System.out.println("Total cronogramas encontrados: " + cronogramas.size());

        return cronogramas.stream()
                .map(cronograma -> {
                    System.out.println("\n--- Procesando Cronograma ID: " + cronograma.getId() + " ---");                    

                    // Filtramos DDJJs por tipoGuardia (Cuando idTipoGuardia == 1, incluimos ambos
                    // tipos (1 y 2))
                    List<DdjjListDto> ddjjsFiltradas = cronograma.getDdjjs().stream()
                            .filter(ddjj -> {
                                boolean pasaFiltro = idTipoGuardia == null ||
                                        (ddjj.getTipoGuardia() != null &&
                                                (ddjj.getTipoGuardia().getId().equals(idTipoGuardia) ||
                                                        (idTipoGuardia == 1L && ddjj.getTipoGuardia().getId() == 2L)));

                                System.out.println("🔍 FILTRO DDJJ - ID: " + ddjj.getId() +
                                        ", TipoGuardia: "
                                        + (ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getId() : "null") +
                                        ", Pasa filtro: " + pasaFiltro);

                                return pasaFiltro;
                            })
                            .map(ddjj -> {
                                System.out.println("\n  🗂️  Procesando DDJJ ID: " + ddjj.getId() +
                                        ", TipoGuardia: "
                                        + (ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getId() : "null"));
                                System.out.println(
                                        "  Total RegistrosMensuales en DDJJ: " + ddjj.getRegistrosMensuales().size());

                                // Aplicamos filtro adicional para tipoGuardia == 1
                                List<RegistroMensualListDto> registros = idTipoGuardia != null && idTipoGuardia == 1L
                                        ? filtrarRegistrosConNovedades(ddjj.getRegistrosMensuales())
                                        : registroMensualService.mapToDtoList(ddjj.getRegistrosMensuales(),
                                                idTipoGuardia);

                                System.out.println(
                                        "  Total RegistrosMensuales después del procesamiento: " + registros.size());

                                // Verificar si los registros tienen actividades
                                registros.forEach(registro -> {
                                    System.out.println("    📊 RegistroMensual ID: " + registro.getId() +
                                            ", Total actividades: "
                                            + (registro.getRegistroActividad() != null
                                                    ? registro.getRegistroActividad().size()
                                                    : 0));
                                });

                                return new DdjjListDto(
                                        ddjj.getId(),
                                        ddjj.getMes(),
                                        ddjj.getAnio(),
                                        registros,
                                        ddjj.getDirector() != null ? ddjj.getDirector().getId() : null,
                                        ddjj.getDirectorDPH() != null ? ddjj.getDirectorDPH().getId() : null,
                                        ddjj.getEstadoDdjjDirector(),
                                        ddjj.getEstadoDdjjDirectorDPH(),
                                        ddjj.getEnPosesionDirector(),
                                        ddjj.getEnPosesionDirectorDPH(),
                                        ddjj.getMotivoDirector(),
                                        ddjj.getMotivoDirectorDPH(),
                                        ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getId() : null);
                            })
                            .collect(Collectors.toList());

                    System.out.println("DDJJs después del filtro: " + ddjjsFiltradas.size());

                    return new CronogramaDefinitivoListDto(
                            cronograma.getId(),
                            cronograma.getMes(),
                            cronograma.getAnio(),
                            ddjjsFiltradas);
                })
                .collect(Collectors.toList());
    }
 */
    
    public List<CronogramaDefinitivoListDto> findByAnioMesIdEfectorAndActivoTrue(
            int anio, MesesEnum mes, Long idEfector) {

        List<CronogramaDefinitivo> cronogramas = cronogramaDefinitivoRepository
                .findByAnioAndMesAndEfectorIdAndActivoTrue(anio, mes, idEfector);

        return cronogramas.stream()
                .map(this::convertirACronogramaDefinitivoListDto)
                .collect(Collectors.toList());
    }

    private CronogramaDefinitivoListDto convertirACronogramaDefinitivoListDto(CronogramaDefinitivo cronograma) {

        List<RegistroActividadListDto> registrosDto = cronograma.getRegistrosActividades().stream()
                .map(this::convertirARegistroActividadListDto)
                .collect(Collectors.toList());

        return new CronogramaDefinitivoListDto(
                cronograma.getId(),
                cronograma.getMes(),
                cronograma.getAnio(),
                registrosDto);
    }

    private RegistroActividadListDto convertirARegistroActividadListDto(RegistroActividad actividad) {

        AsistencialListForRmensualDto asistencialDto = null;
        if (actividad.getAsistencial() != null) {
            asistencialDto = new AsistencialListForRmensualDto();
            asistencialDto.setId(actividad.getAsistencial().getId());
            asistencialDto.setApellido(actividad.getAsistencial().getApellido());
            asistencialDto.setNombre(actividad.getAsistencial().getNombre());
            asistencialDto.setDni(actividad.getAsistencial().getDni());
            asistencialDto.setCuil(actividad.getAsistencial().getCuil());

            // Legajos (idéntico a como se armaba en convertirARegistroMensualCompletoDTO)
            asistencialDto.setLegajos(actividad.getAsistencial().getLegajos().stream()
                    .map(legajo -> {
                        LegajoListDto legajoDTO = new LegajoListDto();
                        if (legajo.getRevista() != null) {
                            RevistaListDto revistaDTO = new RevistaListDto();

                            if (legajo.getRevista().getTipoRevista() != null) {
                                revistaDTO.setTipoRevista(new TipoRevistaListDto(
                                        legajo.getRevista().getTipoRevista().getNombre()));
                            }

                            if (legajo.getRevista().getCategoria() != null) {
                                revistaDTO.setCategoria(new CategoriaListDto(
                                        legajo.getRevista().getCategoria().getNombre()));
                            }

                            if (legajo.getRevista().getAdicional() != null) {
                                revistaDTO.setAdicional(new AdicionalListDto(
                                        legajo.getRevista().getAdicional().getNombre()));
                            }

                            legajoDTO.setRevista(revistaDTO);
                        }
                        return legajoDTO;
                    })
                    .collect(Collectors.toList()));

            // Novedades (idéntico también)
            asistencialDto.setNovedadesPersonales(actividad.getAsistencial().getNovedadesPersonales().stream()
                    .map(novedad -> new NovedadPersonalListDto(
                            novedad.getId(),
                            novedad.getFechaInicio(),
                            novedad.getFechaFinal(),
                            novedad.getHoraInicio(),
                            novedad.getHoraFinal(),
                            new TipoLicenciaListDto(novedad.getTipoLicencia().getId(),
                                    novedad.getTipoLicencia().getNombre())))
                    .collect(Collectors.toList()));
        }

        return new RegistroActividadListDto(
                actividad.getId(),
                asistencialDto,
                actividad.getTipoGuardia() != null ? actividad.getTipoGuardia().getId() : null,
                actividad.getFechaIngreso(),
                actividad.getFechaEgreso(),
                actividad.getHoraIngreso(),
                actividad.getHoraEgreso(),
                actividad.getEsGuardiaIncompleta());
    }

    /*
     * private List<RegistroMensualListDto>
     * filtrarRegistrosConNovedades(List<RegistroMensual> registros) {
     * return registros.stream()
     * .filter(rm -> !tieneNovedadCompensatoriaOLAO(rm))
     * .map(rm -> registroMensualService.convertirARegistroMensualCompletoDTO(
     * rm,
     * rm.getRegistroActividad().stream()
     * .filter(act -> act.getTipoGuardia() != null && act.getTipoGuardia().getId()
     * == 1L)
     * .collect(Collectors.toList())))
     * .collect(Collectors.toList());
     * }
     */

    private List<RegistroMensualListDto> filtrarRegistrosConNovedades(List<RegistroMensual> registros) {
        return registros.stream()
                .filter(rm -> !tieneNovedadCompensatoriaOLAO(rm))
                .map(rm -> {
                    // Usar la misma lógica de filtrado que en mapToDtoList
                    List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad()
                            .stream()
                            .filter(actividad -> actividad.getTipoGuardia() != null &&
                                    (actividad.getTipoGuardia().getId().equals(1L) ||
                                            actividad.getTipoGuardia().getId().equals(2L)))
                            .collect(Collectors.toList());

                    return registroMensualService.convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                })
                .collect(Collectors.toList());
    }

    private boolean tieneNovedadCompensatoriaOLAO(RegistroMensual registro) {
        if (registro.getAsistencial() == null) {
            return false;
        }

        return registro.getAsistencial().getNovedadesPersonales().stream()
                .filter(n -> n.isActivo())
                .anyMatch(novedad -> {
                    String tipoLicencia = novedad.getTipoLicencia() != null
                            ? novedad.getTipoLicencia().getNombre()
                            : null;

                    boolean esLicenciaRelevante = "Compensatorio".equalsIgnoreCase(tipoLicencia)
                            || "LAO".equalsIgnoreCase(tipoLicencia);

                    if (!esLicenciaRelevante) {
                        return false;
                    }

                    return haySolapamiento(
                            registro.getAnio(), registro.getMes(),
                            novedad.getFechaInicio(), novedad.getHoraInicio(),
                            novedad.getFechaFinal(), novedad.getHoraFinal());
                });
    }

    private boolean haySolapamiento(
            int anioAct, MesesEnum mesAct,
            LocalDate fechaIniNov, LocalTime horaIniNov,
            LocalDate fechaFinNov, LocalTime horaFinNov) {

        // Asumimos que el registro mensual es para todo el mes
        LocalDateTime inicioMes = LocalDateTime.of(LocalDate.of(anioAct, mesAct.ordinal() + 1, 1), LocalTime.MIN);
        LocalDateTime finMes = inicioMes.plusMonths(1).minusSeconds(1);

        LocalDateTime inicioNov = LocalDateTime.of(fechaIniNov, horaIniNov != null ? horaIniNov : LocalTime.MIN);
        LocalDateTime finNov = LocalDateTime.of(fechaFinNov, horaFinNov != null ? horaFinNov : LocalTime.MAX);

        return inicioNov.isBefore(finMes) && finNov.isAfter(inicioMes);
    }

    public List<Long> getTiposGuardia(Long idCronograma) {
        return cronogramaDefinitivoRepository.findByIdAndActivoTrue(idCronograma)
                .map(cronograma -> cronograma.getRegistrosActividades().stream()
                        .filter(ra -> ra.getTipoGuardia() != null)
                        .map(ra -> ra.getTipoGuardia().getId())
                        .distinct()
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList()); // Devuelve lista vacía si no existe
    }

    /* public List<CronogramaDefinitivoListDto> findByAnioMesIdEfectorAndActivoTrueDto(int anio, MesesEnum mes,
            Long idEfector) {
        List<CronogramaDefinitivo> cronogramas = findByAnioAndMesAndIdEfectorAndActivoTrue(anio, mes, idEfector);
        return cronogramas.stream()
                .map(cronograma -> new CronogramaDefinitivoListDto(
                        cronograma.getId(),
                        cronograma.getMes(),
                        cronograma.getAnio(),
                        cronograma.getDdjjs().stream()
                                .map(ddjj -> new DdjjListDto(
                                        ddjj.getId(),
                                        ddjj.getMes(),
                                        ddjj.getAnio(),
                                        registroMensualService.mapToDtoList(ddjj.getRegistrosMensuales(),
                                                ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getId() : null),
                                        ddjj.getDirector() != null ? ddjj.getDirector().getId() : null,
                                        ddjj.getDirectorDPH() != null ? ddjj.getDirectorDPH().getId() : null,
                                        ddjj.getEstadoDdjjDirector(),
                                        ddjj.getEstadoDdjjDirectorDPH(),
                                        ddjj.getEnPosesionDirector(),
                                        ddjj.getEnPosesionDirectorDPH(),
                                        ddjj.getMotivoDirector(),
                                        ddjj.getMotivoDirectorDPH(),
                                        ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getId() : null))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
 */
    /**
     * Busca o crea el CronogramaDefinitivo para efector/mes/año y vincula el
     * registro de actividad.
     */
    public RegistroActividad setCronogramaDefinitivo(RegistroActividad registroActividad) {

        Long idEfector = registroActividad.getEfector().getId();
        MesesEnum mesEnum = MesesEnum.fromNumeroMes(registroActividad.getFechaIngreso().getMonthValue());
        int anio = registroActividad.getFechaIngreso().getYear();

        Optional<CronogramaDefinitivo> existente = cronogramaDefinitivoRepository
                .findByEfectorIdAndMesAndAnioAndActivoTrue(idEfector, mesEnum, anio);

        CronogramaDefinitivo cronogramaDefinitivo;

        if (existente.isPresent()) {
            cronogramaDefinitivo = existente.get();
        } else {
            cronogramaDefinitivo = new CronogramaDefinitivo();
            cronogramaDefinitivo.setEfector(registroActividad.getEfector());
            cronogramaDefinitivo.setMes(mesEnum);
            cronogramaDefinitivo.setAnio(anio);
            cronogramaDefinitivo.setActivo(true);
            cronogramaDefinitivoRepository.save(cronogramaDefinitivo);
        }

        registroActividad.setCronogramaDefinitivo(cronogramaDefinitivo);
        return registroActividad;
    }

}
