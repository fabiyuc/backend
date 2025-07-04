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
import com.guardias.backend.dto.ddjj.EstadoDdjjDto;
import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.EstadoDdjjEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.CronogramaTentativoRepository;
import com.guardias.backend.repository.DdjjRepository;
import com.guardias.backend.repository.RegistroMensualRepository;
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

    public boolean existsByAnioAndMesAndEfector(int anio, MesesEnum mes, Long idEfector) {
        return ddjjRepository.existsByAnioAndMesAndEfector_Id(anio, mes, idEfector);
    }

}
