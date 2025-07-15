package com.guardias.backend.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.guardias.backend.dto.Mensaje;
import com.guardias.backend.dto.ObservacionDdjjDto;
import com.guardias.backend.entity.ObservacionDdjj;
import com.guardias.backend.repository.ObservacionDdjjRepository;
import com.guardias.backend.security.service.UsuarioService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ObservacionDdjjService {

    @Autowired
    ObservacionDdjjRepository observacionDdjjRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    DdjjService ddjjService;
    
    public List<ObservacionDdjj> findByActivoTrue() {
        return observacionDdjjRepository.findByActivoTrue();
    }

    public List<ObservacionDdjj> findAll() {
        return observacionDdjjRepository.findAll();
    }

    public boolean activo(Long id) {
        return (observacionDdjjRepository.existsById(id) && observacionDdjjRepository.findById(id).get().isActivo());
    }

    public Optional<ObservacionDdjj> findById(Long id) {
        return observacionDdjjRepository.findById(id);
    }
    
    public ResponseEntity<?> validations(ObservacionDdjjDto observacionDdjjDto, Long id) {

        if (observacionDdjjDto.getMotivo() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar el motivo"),
                    HttpStatus.BAD_REQUEST);

        if (observacionDdjjDto.getIdUsuario() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar el id del usuario"),
                    HttpStatus.BAD_REQUEST);

        if (observacionDdjjDto.getIdDdjj() == null)
            return new ResponseEntity<Mensaje>(new Mensaje("indicar el id de la ddjj"),
                    HttpStatus.BAD_REQUEST);

        return new ResponseEntity(new Mensaje("valido"), HttpStatus.OK);

    }

    public ObservacionDdjj createUpdate(ObservacionDdjj observacionDdjj, ObservacionDdjjDto observacionDdjjDto) {

        if (observacionDdjj.getMotivo() != observacionDdjjDto.getMotivo())
            observacionDdjj.setMotivo(observacionDdjjDto.getMotivo());

        if (observacionDdjj.getUsuario() == null || !Objects.equals(observacionDdjj.getUsuario().getId(), observacionDdjjDto.getIdUsuario()))
            observacionDdjj.setUsuario(usuarioService.findById(observacionDdjjDto.getIdUsuario()).get());
        
        if (observacionDdjj.getDdjj() == null || !Objects.equals(observacionDdjj.getDdjj().getId(), observacionDdjjDto.getIdDdjj()))
            observacionDdjj.setDdjj(ddjjService.findById(observacionDdjjDto.getIdDdjj()).get());

        observacionDdjj.setActivo(true);

        return observacionDdjj;
    }

    public void save(ObservacionDdjj observacionDdjj) {
        observacionDdjjRepository.save(observacionDdjj);
    }

    public boolean existsById(Long id) {
        return observacionDdjjRepository.existsById(id);
    }

    public void deleteById(Long id) {
        observacionDdjjRepository.deleteById(id);
    }

}
