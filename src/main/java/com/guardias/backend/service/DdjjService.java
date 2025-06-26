package com.guardias.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.CronogramaTentativoRepository;
import com.guardias.backend.repository.DdjjRepository;
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

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);
    }

    public Ddjj createUpdate(Ddjj ddjj, DdjjDto ddjjDto) {

        if (ddjjDto.getMes() != null && !ddjjDto.getMes().equals(ddjj.getMes()))
            ddjj.setMes(ddjjDto.getMes());

        if (ddjjDto.getAnio() != ddjj.getAnio())
            ddjj.setAnio(ddjjDto.getAnio());

        if (ddjjDto.getSubtotal() != ddjj.getSubtotal())
            ddjj.setSubtotal(ddjjDto.getSubtotal());

        if (ddjjDto.getTotal() != ddjj.getTotal())
            ddjj.setTotal(ddjjDto.getTotal());

        /*
         * if (ddjjDto.getIdValorGmi() != null && (ddjj.getValorGmi() == null
         * || !Objects.equals(ddjj.getValorGmi().getId(), ddjjDto.getIdValorGmi()))) {
         * ddjj.setValorGmi(valorGmiService.findById(ddjjDto.getIdValorGmi()).get());
         * }
         */

        if (ddjjDto.getIdEfector() != null && (ddjj.getEfector() == null
                || !Objects.equals(ddjj.getEfector().getId(), ddjjDto.getIdEfector()))) {
            ddjj.setEfector(efectorService.findById(ddjjDto.getIdEfector()));
        }

        if (ddjjDto.getIdRegistrosMensuales() != null) {
            List<Long> idList = new ArrayList<Long>();
            if (ddjj.getRegistrosMensuales() != null) {
                for (RegistroMensual registroMensual : ddjj.getRegistrosMensuales()) {
                    for (Long id : ddjjDto.getIdRegistrosMensuales()) {
                        if (!registroMensual.getId().equals(id)) {
                            idList.add(id);
                        }
                    }
                }
            } else {
                ddjj.setRegistrosMensuales(new ArrayList<>());
            }
            List<Long> idsToAdd = idList.isEmpty() ? ddjjDto.getIdRegistrosMensuales() : idList;
            for (Long id : idsToAdd) {
                ddjj.getRegistrosMensuales().add(registroMensualService.findById(id).get());
                registroMensualService.findById(id).get().setDdjj(ddjj);
            }
        }

        ddjj.setDirector(usuarioRepository.findById(ddjjDto.getIdDirector()).get());
        ddjj.setDirectorDPH(usuarioRepository.findById(ddjjDto.getIdDirectorDPH()).get());

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
        return ddjj;
    }

    public boolean cambiarEstado(EstadoDdjjDto estadoDdjjDto) {
        // Busca la DDJJ por ID y que esté activa
        Optional<Ddjj> ddjjOptional = ddjjRepository.findByIdAndActivoTrue(estadoDdjjDto.getIdDddjj());

        if (!ddjjOptional.isPresent()) {
            throw new IllegalArgumentException("No se encontró la DDJJ con ID: " + estadoDdjjDto.getIdDddjj());
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

}
