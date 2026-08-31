package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.guardias.backend.dto.FeriadoDto;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.entity.Feriado;
import com.guardias.backend.repository.FeriadoRepository;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class FeriadoService {
    @Autowired
    FeriadoRepository feriadoRepository;
    @Autowired
    EfectorService efectorService;

    public Optional<List<Feriado>> findByActivoTrue() {
        return feriadoRepository.findByActivoTrue();
    }

    public List<Feriado> findAll() {
        return feriadoRepository.findAll();
    }

    public Optional<Feriado> findById(Long id) {
        return feriadoRepository.findById(id);
    }

    public Optional<Feriado> findByMotivo(String motivo) {
        return feriadoRepository.findByMotivo(motivo);
    }

    public Optional<Feriado> getByMotivo(String motivo) {
        return feriadoRepository.findByMotivo(motivo);
    }

    public Optional<Feriado> getByFecha(LocalDate fecha) {
        return feriadoRepository.findByFecha(fecha);
    }

    public ResponseEntity<?> validations(FeriadoDto feriadoDto, Long id) {
        if (StringUtils.isBlank(feriadoDto.getMotivo()))
            return new ResponseEntity(new Mensaje("El motivo es obligatorio"), HttpStatus.BAD_REQUEST);
        if (feriadoDto.getTipoFeriado() == null)
            return new ResponseEntity(new Mensaje("El tipo de feriado es obligatorio"), HttpStatus.BAD_REQUEST);
        if (feriadoDto.getFecha() == null)
            return new ResponseEntity(new Mensaje("La fecha es obligatoria"), HttpStatus.BAD_REQUEST);

        /*
         * if (existsByMotivo(feriadoDto.getMotivo())
         * && (findByMotivo(feriadoDto.getMotivo()).get().getId() != id))
         * return new ResponseEntity(new Mensaje("ese motivo ya existe"),
         * HttpStatus.BAD_REQUEST);
         */
        // Buscar si existe otro feriado con el mismo motivo
        Optional<Feriado> feriadoExistenteOpt = findByMotivo(feriadoDto.getMotivo());

        // Si el motivo existe y pertenece a otro feriado, lanzar error
        if (feriadoExistenteOpt.isPresent() && !feriadoExistenteOpt.get().getId().equals(id)) {
            return new ResponseEntity<>(new Mensaje("Ese motivo ya existe"), HttpStatus.BAD_REQUEST);
        }

        if (feriadoDto.getEsPatronal() == null)
            return new ResponseEntity<>(new Mensaje("Indicar si es patronal"), HttpStatus.BAD_REQUEST);

        if (feriadoDto.getEsPatronal() == true) {
            if (feriadoDto.getIdEfector() == null) {
                return new ResponseEntity<>(new Mensaje("Indicar el efector del feriado regional"),
                        HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Feriado createUpdate(Feriado feriado, FeriadoDto feriadoDto) {
        if (!feriadoDto.getFecha().equals(feriado.getFecha()))
            feriado.setFecha(feriadoDto.getFecha());

        if (!feriadoDto.getMotivo().equals(feriado.getMotivo()))
            feriado.setMotivo(feriadoDto.getMotivo());

        if (!feriadoDto.getTipoFeriado().equals(feriado.getTipoFeriado()))
            feriado.setTipoFeriado(feriadoDto.getTipoFeriado());

        if (!feriadoDto.getDescripcion().equals(feriado.getDescripcion()))
            feriado.setDescripcion(feriadoDto.getDescripcion());

        if (!feriadoDto.getEsPatronal().equals(feriado.getEsPatronal()))
            feriado.setEsPatronal(feriadoDto.getEsPatronal());

        if (feriadoDto.getEsPatronal() == true) {
            if (feriado.getEfector() == null ||
                    (feriadoDto.getIdEfector() != null &&
                            !Objects.equals(feriado.getEfector().getId(),
                                    feriadoDto.getIdEfector()))) {
                feriado.setEfector(efectorService.findById(feriadoDto.getIdEfector()));
            }
        }

        if (feriadoDto.getEsPatronal() == false) {
            feriado.setEfector(null);
        }

        feriado.setActivo(true);

        return feriado;
    }

    public void save(Feriado feriado) {
        feriadoRepository.save(feriado);
    }

    public void deleteById(Long id) {
        feriadoRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return feriadoRepository.existsById(id);
    }

    public boolean existsByMotivo(String motivo) {
        return feriadoRepository.existsByMotivo(motivo);
    }

    public boolean existsByFecha(LocalDate fecha) {
        return feriadoRepository.existsByFecha(fecha);
    }

    public boolean activo(Long id) {
        return (feriadoRepository.existsById(id) && feriadoRepository.findById(id).get().isActivo());
    }

    public boolean esFeriadoHabil(LocalDate fecha) { 
        return feriadoRepository.findFeriadoHabilPorFecha(fecha).isPresent(); }

}
