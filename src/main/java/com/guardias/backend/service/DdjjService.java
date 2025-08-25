package com.guardias.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.DdjjDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ddjj.DdjjListDto;
import com.guardias.backend.dto.ddjj.EstadoDdjjDto;
import com.guardias.backend.dto.registroMensual.RegistroMensualListDto;
import com.guardias.backend.entity.CronogramaDefinitivo;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.ObservacionDdjj;
import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.EstadoDdjjEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.CronogramaTentativoRepository;
import com.guardias.backend.repository.DdjjRepository;
import com.guardias.backend.repository.ObservacionDdjjRepository;
import com.guardias.backend.repository.RegistroMensualRepository;
import com.guardias.backend.repository.TipoGuardiaRepository;
import com.guardias.backend.security.entity.Usuario;
import com.guardias.backend.security.repository.UsuarioRepository;

import jakarta.validation.ValidationException;

@Service
@Transactional
public class DdjjService {
    @Autowired
    DdjjRepository ddjjRepository;
    @Autowired
    EfectorService efectorService;
    @Autowired
    RegistroMensualService registroMensualService;
    @Autowired
    CronogramaTentativoRepository cronogramaTentativoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    RegistroMensualRepository registroMensualRepository;
    @Autowired
    TipoGuardiaRepository tipoGuardiaRepository;
    @Autowired
    ObservacionDdjjRepository observacionDdjjRepository;
    @Autowired
    TipoGuardiaService tipoGuardiaService;
    @Autowired
    CronogramaDefinitivoService cronogramaDefinitivoService;
    @Autowired
    RegistroActividadService registroActividadService;

    public boolean existsById(Long id) {
        return ddjjRepository.existsById(id);
    }

    public Optional<Ddjj> findById(Long id) {
        return ddjjRepository.findById(id);
    }

    public List<Ddjj> findAll() {
        return ddjjRepository.findAll();
    }

    public List<Ddjj> findByActivoTrue() {
        return ddjjRepository.findByActivoTrue();
    }

    public boolean activo(Long id) {
        return ddjjRepository.existsById(id) && ddjjRepository.findById(id).get().isActivo();
    }

    public boolean existsByAnioAndMes(int anio, MesesEnum mes) {
        return ddjjRepository.existsByAnioAndMes(anio, mes);
    }

    public boolean existsByAnio(int anio) {
        return ddjjRepository.existsByAnio(anio);
    }

    public List<Ddjj> findByByAnioAndMes(int anio, MesesEnum mes) {
        return ddjjRepository.findByAnioAndMes(anio, mes);
    }

    public List<Ddjj> findByEfectorIdAndMesAndAnio(Long efectorId, MesesEnum mes, int anio) {
        return ddjjRepository.findByEfectorIdAndMesAndAnio(efectorId, mes, anio);
    }

    public List<Ddjj> findByByAnio(int anio) {
        return ddjjRepository.findByAnio(anio);
    }

    public void save(Ddjj ddjj) {
        ddjjRepository.save(ddjj);
    }

    public void deleteById(Long id) {
        ddjjRepository.deleteById(id);
    }

    public ResponseEntity<?> validations(DdjjDto ddjjDto) {

        if (ddjjDto.getMes() == null)
            return new ResponseEntity(new Mensaje("El mes es obligatorio"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getAnio() < 1991)
            return new ResponseEntity(new Mensaje("El año es incorrecto"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getSubtotal().compareTo(BigDecimal.ZERO) < 0)
            return new ResponseEntity(new Mensaje("Monto del subtotal incorrecto"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getTotal().compareTo(BigDecimal.ZERO) < 0)
            return new ResponseEntity(new Mensaje("Monto del total incorrecto"), HttpStatus.BAD_REQUEST);

        if (ddjjDto.getIdEfector() < 1)
            return new ResponseEntity(new Mensaje("El id del efector es incorrecto"), HttpStatus.BAD_REQUEST);

        /* ver si es valida esta comprobacion */
        if (ddjjDto.getIdRegistrosMensuales() == null)
            return new ResponseEntity(new Mensaje("La lista de registros mensuales no puede ser vacia"),
                    HttpStatus.BAD_REQUEST);

        if (ddjjDto.getEnPosesionDirector() == null)
            return new ResponseEntity(new Mensaje("es obligatorio indicar la posesion en director"),
                    HttpStatus.BAD_REQUEST);

        if (!efectorService.activoById(ddjjDto.getIdEfector())) {
            throw new IllegalArgumentException("El efector no existe");
        }

        if (ddjjDto.getIdTipoGuardia() == null)
            return new ResponseEntity(new Mensaje("es obligatorio indicar el tipo de guardia"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Ddjj createUpdate(Ddjj ddjj, DdjjDto ddjjDto) {

        // ===== 1. VALIDACIÓN INMEDIATA DE LOS IDs =====
        validateRegistrosMensuales(ddjjDto.getIdRegistrosMensuales());

        // ===== 2. MAPEO DE CAMPOS BÁSICOS =====
        mapBasicFields(ddjj, ddjjDto);

        // ===== 3. MANEJO DE DIRECTORES =====
        processDirectores(ddjj, ddjjDto);

        // ===== 4. CARGA EFICIENTE DE REGISTROS MENSUALES =====
        processRegistrosMensuales(ddjj, ddjjDto);

        // ===== 5. GUARDADO FINAL =====
        return ddjjRepository.save(ddjj);

    }

    // ---- Métodos auxiliares ----
    private void validateRegistrosMensuales(List<Long> idsRegistros) {
        if (idsRegistros == null || idsRegistros.isEmpty()) {
            throw new IllegalArgumentException("La lista de registros mensuales no puede estar vacía");
        }

        // erifica existencia en una sola query
        List<Long> idsExistentes = registroMensualRepository.findExistingIds(idsRegistros);

        if (idsExistentes.size() != idsRegistros.size()) {
            List<Long> idsFaltantes = new ArrayList<>(idsRegistros);
            idsFaltantes.removeAll(idsExistentes);
            throw new IllegalArgumentException("Los siguientes IDs de registros no existen: " + idsFaltantes);
        }
    }

    private void mapBasicFields(Ddjj ddjj, DdjjDto ddjjDto) {

        if (ddjjDto.getMes() != null && !ddjjDto.getMes().equals(ddjj.getMes()))
            ddjj.setMes(ddjjDto.getMes());

        if (ddjjDto.getAnio() != ddjj.getAnio())
            ddjj.setAnio(ddjjDto.getAnio());

        if (ddjjDto.getSubtotal() != ddjj.getSubtotal())
            ddjj.setSubtotal(ddjjDto.getSubtotal());

        if (ddjjDto.getTotal() != ddjj.getTotal())
            ddjj.setTotal(ddjjDto.getTotal());

        if (ddjjDto.getIdEfector() != null
                && (ddjj.getEfector() == null || !Objects.equals(ddjj.getEfector().getId(), ddjjDto.getIdEfector()))) {
            ddjj.setEfector(efectorService.findById(ddjjDto.getIdEfector()));
        }

        if (ddjjDto.getIdRegistrosMensuales() != null) {
            // 1. Primero guarda la DDJJ si es nueva (sin los registros)
            if (ddjj.getId() == null) {
                ddjj = ddjjRepository.save(ddjj);
            }

            // 2. Manejo de registros existentes (para actualización)
            if (ddjj.getRegistrosMensuales() != null) {
                // Rompe la relación con registros que ya no están en la lista nueva
                List<RegistroMensual> toRemove = new ArrayList<>();
                for (RegistroMensual rm : ddjj.getRegistrosMensuales()) {
                    if (!ddjjDto.getIdRegistrosMensuales().contains(rm.getId())) {
                        rm.setDdjj(null);
                        toRemove.add(rm);
                    }
                }
                ddjj.getRegistrosMensuales().removeAll(toRemove);
            } else {
                ddjj.setRegistrosMensuales(new ArrayList<>());
            }

            // 3. Agrega los nuevos registros
            for (Long id : ddjjDto.getIdRegistrosMensuales()) {
                boolean exists = ddjj.getRegistrosMensuales().stream()
                        .anyMatch(rm -> rm.getId().equals(id));

                if (!exists) {
                    Optional<RegistroMensual> rmOpt = registroMensualService.findById(id);
                    if (rmOpt.isPresent()) {
                        RegistroMensual rm = rmOpt.get();
                        rm.setDdjj(ddjj); // Establece la relación inversa
                        ddjj.getRegistrosMensuales().add(rm);
                    }
                }
            }
        }

        if (ddjjDto.getIdObservacionesDdjj() != null) {
            // 1. Primero guarda la DDJJ si es nueva (sin los registros)
            if (ddjj.getId() == null) {
                ddjj = ddjjRepository.save(ddjj);
            }

            // 2. Manejo de registros existentes (para actualización)
            if (ddjj.getObservacionesDdjj() != null) {
                // Rompe la relación con registros que ya no están en la lista nueva
                List<ObservacionDdjj> toRemove = new ArrayList<>();
                for (ObservacionDdjj od : ddjj.getObservacionesDdjj()) {
                    if (!ddjjDto.getIdObservacionesDdjj().contains(od.getId())) {
                        od.setDdjj(null);
                        toRemove.add(od);
                    }
                }
                ddjj.getObservacionesDdjj().removeAll(toRemove);
            } else {
                ddjj.setObservacionesDdjj(new ArrayList<>());
            }

            // 3. Agrega las nuevas observaciones
            for (Long id : ddjjDto.getIdObservacionesDdjj()) {
                boolean exists = ddjj.getObservacionesDdjj().stream()
                        .anyMatch(od -> od.getId().equals(id));

                if (!exists) {
                    Optional<ObservacionDdjj> odOpt = observacionDdjjRepository.findById(id);
                    if (odOpt.isPresent()) {
                        ObservacionDdjj od = odOpt.get();
                        od.setDdjj(ddjj); // Establece la relación inversa
                        ddjj.getObservacionesDdjj().add(od);
                    }
                }
            }
        }

        if (ddjjDto.getEstadoDdjjDirector() != null
                && !ddjjDto.getEstadoDdjjDirector().equals(ddjj.getEstadoDdjjDirector()))
            ddjj.setEstadoDdjjDirector(ddjjDto.getEstadoDdjjDirector());

        if (ddjjDto.getEstadoDdjjDirectorDPH() != null
                && !ddjjDto.getEstadoDdjjDirectorDPH().equals(ddjj.getEstadoDdjjDirectorDPH()))
            ddjj.setEstadoDdjjDirectorDPH(ddjjDto.getEstadoDdjjDirectorDPH());

        ddjj.setEnPosesionDirector(ddjjDto.getEnPosesionDirector());
        ddjj.setEnPosesionDirectorDPH(ddjjDto.getEnPosesionDirectorDPH());

        ddjj.setMotivoDirector(ddjjDto.getMotivoDirector());
        ddjj.setMotivoDirectorDPH(ddjjDto.getMotivoDirectorDPH());

        if (ddjj.getTipoGuardia() == null ||
                (ddjjDto.getIdTipoGuardia() != null &&
                        !Objects.equals(ddjj.getTipoGuardia().getId(),
                                ddjjDto.getIdTipoGuardia()))) {
            ddjj
                    .setTipoGuardia(tipoGuardiaService.findById(ddjjDto.getIdTipoGuardia()).get());
        }

        if (ddjjDto.getIdCronogramasDefinitivos() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (ddjj.getCronogramasDefinitivos() != null) {
                for (CronogramaDefinitivo cronograma : ddjj.getCronogramasDefinitivos()) {
                    for (Long id : ddjjDto.getIdCronogramasDefinitivos()) {
                        if (!cronograma.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                ddjj.setCronogramasDefinitivos(new ArrayList<CronogramaDefinitivo>());
            }

            List<Long> idsToAdd = idList.isEmpty() ? ddjjDto.getIdCronogramasDefinitivos() : idList;

            for (Long id : idsToAdd) {
                ddjj.getCronogramasDefinitivos().add(cronogramaDefinitivoService.findById(id).get());
                cronogramaDefinitivoService.findById(id).get().getDdjjs().add(ddjj);
            }
        }

        /*
         * if (ddjjDto.getIdCronogramaDefinitivo() != null) {
         * if (ddjj.getCronogramaDefinitivo() == null ||
         * !Objects.equals(ddjj.getCronogramaDefinitivo().getId(),
         * ddjjDto.getIdCronogramaDefinitivo())) {
         * ddjj.setCronogramaDefinitivo(
         * cronogramaDefinitivoService.findById(ddjjDto.getIdCronogramaDefinitivo())
         * .orElse(null));
         * }
         * } else {
         * ddjj.setCronogramaDefinitivo(null); // O mantener el existente si lo hay
         * }
         */
        ddjj.setActivo(true);
    }

    private void processRegistrosMensuales(Ddjj ddjj, DdjjDto ddjjDto) {
        // 1. Carga batch de registros (1 query)
        List<RegistroMensual> registros = registroMensualRepository.findAllById(ddjjDto.getIdRegistrosMensuales());

        // 2. Caso CREACIÓN (inicialización simple)
        if (ddjj.getId() == null) {
            ddjj.setRegistrosMensuales(new ArrayList<>());
            registros.forEach(rm -> {
                rm.setDdjj(ddjj);
                ddjj.getRegistrosMensuales().add(rm);
            });
            return;
        }
        // 3. Caso EDICIÓN
        Set<Long> nuevosIds = registros.stream()
                .map(RegistroMensual::getId)
                .collect(Collectors.toSet());

        // a) Elimina solo los registros que ya no están en la nueva lista
        ddjj.getRegistrosMensuales().removeIf(rm -> {
            if (!nuevosIds.contains(rm.getId())) {
                rm.setDdjj(null); // Rompe relación
                return true;
            }
            return false;
        });

        // b) Agrega solo los registros nuevos (no existentes)
        registros.forEach(rm -> {
            if (ddjj.getRegistrosMensuales().stream()
                    .noneMatch(existente -> existente.getId().equals(rm.getId()))) {
                rm.setDdjj(ddjj);
                ddjj.getRegistrosMensuales().add(rm);
            }
        });
    }

    private void processDirectores(Ddjj ddjj, DdjjDto ddjjDto) {
        if (ddjjDto.getIdDirector() != null) {
            Usuario director = usuarioRepository.findById(ddjjDto.getIdDirector())
                    .orElseThrow(() -> new IllegalArgumentException("Director no encontrado"));
            ddjj.setDirector(director);
        }

        if (ddjjDto.getIdDirectorDPH() != null) {
            Usuario directorDPH = usuarioRepository.findById(ddjjDto.getIdDirectorDPH())
                    .orElseThrow(() -> new IllegalArgumentException("Director DPH no encontrado"));
            ddjj.setDirectorDPH(directorDPH);
        }
    }

    public boolean cambiarEstado(EstadoDdjjDto estadoDdjjDto) {
        // Busca la DDJJ por ID y que esté activa
        Optional<Ddjj> ddjjOptional = ddjjRepository.findByIdAndActivoTrue(estadoDdjjDto.getIdDdjj());

        if (!ddjjOptional.isPresent()) {
            throw new IllegalArgumentException("No se encontró la DDJJ con ID: " + estadoDdjjDto.getIdDdjj());
        }

        Ddjj ddjj = ddjjOptional.get();

        // Actualiza los campos según el DTO recibido
        if (estadoDdjjDto.getIdDirector() != null) {
            Optional<Usuario> directorOptional = usuarioRepository.findById(estadoDdjjDto.getIdDirector());
            if (directorOptional.isPresent()) {
                ddjj.setDirector(directorOptional.get());
            } else {
                throw new ValidationException(
                        "No se encontró el usuario director con ID: " + estadoDdjjDto.getIdDirector());
            }
        }

        if (estadoDdjjDto.getIdDirectorDPH() != null) {
            Optional<Usuario> directorDPHOptional = usuarioRepository.findById(estadoDdjjDto.getIdDirectorDPH());
            if (directorDPHOptional.isPresent()) {
                ddjj.setDirectorDPH(directorDPHOptional.get());
            } else {
                throw new ValidationException(
                        "No se encontró el usuario director DPH con ID: " + estadoDdjjDto.getIdDirectorDPH());
            }
        }

        if (estadoDdjjDto.getEstadoDdjjDirector() != null) {
            ddjj.setEstadoDdjjDirector(estadoDdjjDto.getEstadoDdjjDirector());
        }

        if (estadoDdjjDto.getEstadoDdjjDirectorDPH() != null) {
            ddjj.setEstadoDdjjDirectorDPH(estadoDdjjDto.getEstadoDdjjDirectorDPH());
        }

        if (estadoDdjjDto.getEnPosesionDirector() != null) {
            ddjj.setEnPosesionDirector(estadoDdjjDto.getEnPosesionDirector());
        }

        if (estadoDdjjDto.getEnPosesionDirectorDPH() != null) {
            ddjj.setEnPosesionDirectorDPH(estadoDdjjDto.getEnPosesionDirectorDPH());
        }

        if (estadoDdjjDto.getMotivoDirector() != null) {
            ddjj.setMotivoDirector(estadoDdjjDto.getMotivoDirector());
        }

        if (estadoDdjjDto.getMotivoDirectorDPH() != null) {
            ddjj.setMotivoDirectorDPH(estadoDdjjDto.getMotivoDirectorDPH());
        }

        try {
            // Guardar los cambios
            ddjjRepository.save(ddjj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Ddjj> findByEfectorAndEstadoPendienteDph(Long idEfector) {
        return ddjjRepository.findByEfectorIdAndEstadoDdjjDirectorDPHAndActivoTrue(idEfector, EstadoDdjjEnum.PENDIENTE);
    }

    public List<Ddjj> findByEfectorAndEstadoPendiente(Long idEfector) {
        return ddjjRepository.findByEfectorIdAndEstadoDdjjDirectorAndActivoTrue(idEfector, EstadoDdjjEnum.PENDIENTE);
    }

    public List<Ddjj> findByEfectorAndEstadoAprobado(Long idDirector, Long idEfector) {
        return ddjjRepository.findByEfectorIdAndDirectorIdAndEstadoDdjjDirectorAndActivoTrue(
                idEfector,
                idDirector,
                EstadoDdjjEnum.APROBADO);
    }

    public List<Ddjj> findByEfectorAndEstadoAprobadoDph(Long idEfector) {
        return ddjjRepository.findByEfectorIdAndEstadoDdjjDirectorDPHAndActivoTrue(idEfector, EstadoDdjjEnum.APROBADO);
    }

    public boolean existsByAnioMesEfectorAndTipoGuardia(int anio, MesesEnum mes, Long idEfector, Long idTipoGuardia) {

        System.out.println("=== INICIO Servicio (con nuevo atributo) ===");

        Optional<TipoGuardia> tipoGuardiaOptional = tipoGuardiaRepository.findById(idTipoGuardia);

        if (tipoGuardiaOptional.isPresent()) {
            TipoGuardiaEnum tipo = tipoGuardiaOptional.get().getNombre();
            System.out.println("Tipo de guardia: " + tipo);

            if (tipo == TipoGuardiaEnum.CARGO || tipo == TipoGuardiaEnum.AGRUPACION) {
                // Nueva consulta usando el atributo directo
                System.out.println("Buscando cualquier DDJJ activa con tipo CARGO o AGRUPACION");
                boolean result = ddjjRepository.existsByAnioAndMesAndEfectorIdAndTipoGuardiaIdAndActivoTrue(
                        anio, mes, idEfector, 1L);
                System.out.println("Resultado: " + result);
                return result;
            } else {
                // Consulta específica por tipo de guardia
                System.out.println("Buscando DDJJ con tipo específico: " + tipo);
                boolean result = ddjjRepository.existsByAnioAndMesAndEfectorIdAndTipoGuardiaIdAndActivoTrue(
                        anio, mes, idEfector, idTipoGuardia);
                System.out.println("Resultado: " + result);
                return result;
            }
        }

        System.out.println("TipoGuardia no encontrado");
        return false;
    }

    public List<Ddjj> findDdjjCargoyAgrup(int anio, MesesEnum mes, Long idEfector) {
        List<Ddjj> ddjjs = ddjjRepository.findByAnioMesEfector(anio, mes, idEfector);

        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                (tipoGuardiaDdjj == TipoGuardiaEnum.CARGO ||
                                                        tipoGuardiaDdjj == TipoGuardiaEnum.AGRUPACION))
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return ddjj;
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public List<Ddjj> findDdjjCargoyAgrupServicio(int anio, MesesEnum mes, Long idEfector, Long idServicio) {
        List<Ddjj> ddjjs = ddjjRepository.findByEfectorIdAndMesAndAnioServicio(anio, mes, idEfector, idServicio);
        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                (tipoGuardiaDdjj == TipoGuardiaEnum.CARGO ||
                                                        tipoGuardiaDdjj == TipoGuardiaEnum.AGRUPACION))
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return ddjj;
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public List<Ddjj> findDdjjExtra(int anio, MesesEnum mes, Long idEfector) {
        List<Ddjj> ddjjs = ddjjRepository.findByAnioMesEfector(anio, mes, idEfector);

        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                tipoGuardiaDdjj == TipoGuardiaEnum.EXTRA)
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return ddjj;
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public List<Ddjj> findDdjjExtraServicio(int anio, MesesEnum mes, Long idEfector, Long idServicio) {
        List<Ddjj> ddjjs = ddjjRepository.findByEfectorIdAndMesAndAnioServicio(anio, mes, idEfector, idServicio);
        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                tipoGuardiaDdjj == TipoGuardiaEnum.EXTRA)
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return ddjj;
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public List<Ddjj> findDdjjCf(int anio, MesesEnum mes, Long idEfector) {
        List<Ddjj> ddjjs = ddjjRepository.findByAnioMesEfector(anio, mes, idEfector);

        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                tipoGuardiaDdjj == TipoGuardiaEnum.CONTRAFACTURA)
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return ddjj;
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public List<Ddjj> findDdjjCfServicio(int anio, MesesEnum mes, Long idEfector, Long idServicio) {
        List<Ddjj> ddjjs = ddjjRepository.findByEfectorIdAndMesAndAnioServicio(anio, mes, idEfector, idServicio);
        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                tipoGuardiaDdjj == TipoGuardiaEnum.CONTRAFACTURA)
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return ddjj;
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public boolean existsCompleteSetOfDdjj(MesesEnum mes, int anio, Long idEfector) {

        EstadoDdjjEnum estadoRequerido = EstadoDdjjEnum.APROBADO;

        // Verifico si existen las 3 ddjj requeridas
        boolean hasCargo = ddjjRepository.countActiveByMesAnioEfectorAndTipoGuardia(
                mes, anio, idEfector, TipoGuardiaEnum.CARGO, estadoRequerido) > 0;

        boolean hasExtra = ddjjRepository.countActiveByMesAnioEfectorAndTipoGuardia(
                mes, anio, idEfector, TipoGuardiaEnum.EXTRA, estadoRequerido) > 0;

        boolean hasContrafactura = ddjjRepository.countActiveByMesAnioEfectorAndTipoGuardia(
                mes, anio, idEfector, TipoGuardiaEnum.CONTRAFACTURA, estadoRequerido) > 0;

        return hasCargo && hasExtra && hasContrafactura;
    }

    public boolean puedeGenerarCronogramaDefinitivo(Long idEfector, MesesEnum mes, int anio) {

        // 2. Obtener todas las DDJJ del efector para ese período
        List<Ddjj> ddjjs = ddjjRepository.findByEfectorAndMesAndAnio(idEfector, mes, anio);

        if (ddjjs.isEmpty()) {
            return false; // No hay DDJJ creadas (pero podría no ser obligatorio)
        }

        // 3. Verificar aprobación de todas las DDJJ existentes
        for (Ddjj ddjj : ddjjs) {
            if (ddjj.getEstadoDdjjDirector() != EstadoDdjjEnum.APROBADO) {
                return false; // Hay al menos una DDJJ no aprobada
            }
        }

        return true; // Cumple todas las condiciones
    }

    public int actualizarDdjjAPendiente(Long idEfector, int mes, int anio) {
        // 1. Primero obtenemos los IDs de las DDJJ que cumplen los requisitos
        List<Long> idsDdjjValidas = registroActividadService.obtenerIdsDdjjAprobadas(idEfector, mes, anio);

        // 2. Si no hay DDJJ válidas, retornamos 0
        if (idsDdjjValidas.isEmpty()) {
            return 0;
        }

        // 3. Actualizamos el estado de las DDJJ encontradas
        return ddjjRepository.updateEstadoDdjjDirectorDPHByIds(
                idsDdjjValidas,
                EstadoDdjjEnum.PENDIENTE);
    }

    public void actualizarEstadoAPendiente(List<Long> idsDdjj) {
        if (idsDdjj == null || idsDdjj.isEmpty()) {
            throw new IllegalArgumentException("La lista de IDs de DDJJ no puede estar vacía");
        }

        // Verificar que todas las DDJJ existen
        List<Ddjj> ddjjs = ddjjRepository.findAllById(idsDdjj);
        if (ddjjs.size() != idsDdjj.size()) {
            throw new IllegalArgumentException("Algunas DDJJ no existen");
        }

        // Actualizar estado
        ddjjs.forEach(ddjj -> {
            ddjj.setEstadoDdjjDirectorDPH(EstadoDdjjEnum.PENDIENTE);
            // Opcional: Registrar quién hizo el cambio
            // ddjj.setUltimaActualizacion(LocalDateTime.now());
        });

        ddjjRepository.saveAll(ddjjs);
    }

    public List<DdjjListDto> findCargoyAgrupServicio(int anio, MesesEnum mes, Long idEfector, Long idServicio) {

        List<Ddjj> ddjjs = ddjjRepository.findByEfectorIdAndMesAndAnioServicio(anio, mes, idEfector, idServicio);
        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosFiltrados = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(registroMensual -> {
                                List<RegistroActividad> actividadesFiltradas = registroMensual.getRegistroActividad()
                                        .stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                (tipoGuardiaDdjj == TipoGuardiaEnum.CARGO ||
                                                        tipoGuardiaDdjj == TipoGuardiaEnum.AGRUPACION))
                                        .collect(Collectors.toList());
                                registroMensual.setRegistroActividad(actividadesFiltradas);
                                return registroMensual;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosFiltrados);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public List<DdjjListDto> findCargoyAgrup(int anio, MesesEnum mes, Long idEfector) {

        List<Ddjj> ddjjs = ddjjRepository.findByAnioMesEfector(anio, mes, idEfector);
        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosMensualesDto = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(rm -> {
                                List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad().stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                (tipoGuardiaDdjj == TipoGuardiaEnum.CARGO ||
                                                        tipoGuardiaDdjj == TipoGuardiaEnum.AGRUPACION))
                                        .collect(Collectors.toList());
                                rm.setRegistroActividad(actividadesFiltradas);
                                return rm;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosMensualesDto);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public List<DdjjListDto> findExtraServicio(int anio, MesesEnum mes, Long idEfector, Long idServicio) {

        List<Ddjj> ddjjs = ddjjRepository.findByEfectorIdAndMesAndAnioServicio(anio, mes, idEfector, idServicio);
        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosMensualesDto = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(rm -> {
                                List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad().stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                tipoGuardiaDdjj == TipoGuardiaEnum.EXTRA)
                                        .collect(Collectors.toList());
                                rm.setRegistroActividad(actividadesFiltradas);
                                return rm;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosMensualesDto);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public List<DdjjListDto> findExtra(int anio, MesesEnum mes, Long idEfector) {

        List<Ddjj> ddjjs = ddjjRepository.findByAnioMesEfector(anio, mes, idEfector);
        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosMensualesDto = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(rm -> {
                                List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad().stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                tipoGuardiaDdjj == TipoGuardiaEnum.EXTRA)
                                        .collect(Collectors.toList());
                                rm.setRegistroActividad(actividadesFiltradas);
                                return rm;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosMensualesDto);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public List<DdjjListDto> findCfServicio(int anio, MesesEnum mes, Long idEfector, Long idServicio) {
        List<Ddjj> ddjjs = ddjjRepository.findByEfectorIdAndMesAndAnioServicio(anio, mes, idEfector, idServicio);
        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosMensualesDto = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(rm -> {
                                List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad().stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                tipoGuardiaDdjj == TipoGuardiaEnum.CONTRAFACTURA)
                                        .collect(Collectors.toList());
                                rm.setRegistroActividad(actividadesFiltradas);
                                return rm;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosMensualesDto);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    public List<DdjjListDto> findCf(int anio, MesesEnum mes, Long idEfector) {
        List<Ddjj> ddjjs = ddjjRepository.findByAnioMesEfector(anio, mes, idEfector);
        return ddjjs.stream()
                .filter(Ddjj::isActivo)
                .map(ddjj -> {
                    TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre()
                            : null;
                    List<RegistroMensual> registrosMensualesDto = ddjj.getRegistrosMensuales().stream()
                            .filter(RegistroMensual::isActivo)
                            .map(rm -> {
                                List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad().stream()
                                        .filter(actividad -> actividad.isActivo() &&
                                                tipoGuardiaDdjj == TipoGuardiaEnum.CONTRAFACTURA)
                                        .collect(Collectors.toList());
                                rm.setRegistroActividad(actividadesFiltradas);
                                return rm;
                            })
                            .filter(rm -> !rm.getRegistroActividad().isEmpty())
                            .collect(Collectors.toList());
                    ddjj.setRegistrosMensuales(registrosMensualesDto);
                    return convertirADdjjListDto(ddjj);
                })
                .filter(ddjj -> !ddjj.getRegistrosMensuales().isEmpty())
                .collect(Collectors.toList());
    }

    private DdjjListDto convertirADdjjListDto(Ddjj ddjj) {
        DdjjListDto dto = new DdjjListDto();
        dto.setId(ddjj.getId());
        dto.setMes(ddjj.getMes());
        dto.setAnio(ddjj.getAnio());

        // Convertir registros mensuales usando el servicio de RegistroMensual
        TipoGuardiaEnum tipoGuardiaDdjj = ddjj.getTipoGuardia() != null ? ddjj.getTipoGuardia().getNombre() : null;
        List<RegistroMensualListDto> registrosMensualesDto = ddjj.getRegistrosMensuales().stream()
                .map(rm -> {
                    List<RegistroActividad> actividadesFiltradas = rm.getRegistroActividad().stream()
                            .filter(actividad -> actividad.isActivo() &&
                                    (tipoGuardiaDdjj == TipoGuardiaEnum.CARGO ||
                                            tipoGuardiaDdjj == TipoGuardiaEnum.AGRUPACION ||
                                            tipoGuardiaDdjj == TipoGuardiaEnum.EXTRA ||
                                            tipoGuardiaDdjj == TipoGuardiaEnum.CONTRAFACTURA))
                            .collect(Collectors.toList());
                    return registroMensualService.convertirARegistroMensualCompletoDTO(rm, actividadesFiltradas);
                })
                .collect(Collectors.toList());

        dto.setRegistrosMensuales(registrosMensualesDto);

        // Director y Director DPH
        if (ddjj.getDirector() != null) {
            dto.setIdDirector(ddjj.getDirector().getId());
        }
        if (ddjj.getDirectorDPH() != null) {
            dto.setIdDirectorDPH(ddjj.getDirectorDPH().getId());
        }

        dto.setEstadoDdjjDirector(ddjj.getEstadoDdjjDirector());
        dto.setEstadoDdjjDirectorDPH(ddjj.getEstadoDdjjDirectorDPH());
        dto.setEnPosesionDirector(ddjj.getEnPosesionDirector());
        dto.setEnPosesionDirectorDPH(ddjj.getEnPosesionDirectorDPH());
        dto.setMotivoDirector(ddjj.getMotivoDirector());
        dto.setMotivoDirectorDPH(ddjj.getMotivoDirectorDPH());

        if (ddjj.getTipoGuardia() != null) {
            dto.setIdTipoGuardia(ddjj.getTipoGuardia().getId());
        }

        return dto;
    }

}