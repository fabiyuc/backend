package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.dto.tipoGuardia.TipoGuardiaListDto;
import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.TipoGuardiaEnum;
import com.guardias.backend.repository.TipoGuardiaRepository;

@Service
@Transactional
public class TipoGuardiaService {

    @Autowired
    TipoGuardiaRepository tipoGuardiaRepository;

    public Optional<TipoGuardia> findByDescripcion(String descripcion) {
        return tipoGuardiaRepository.findByDescripcion(descripcion);
    }

    public boolean existsByDescripcion(String descripcion) {
        return tipoGuardiaRepository.existsByDescripcion(descripcion);
    }

    public boolean activoByDescripcion(String descripcion) {
        return (tipoGuardiaRepository.existsByDescripcion(descripcion)
                && tipoGuardiaRepository.findByDescripcion(descripcion).get().isActivo());
    }

    public Optional<TipoGuardia> findByNombre(String nombre) {
        TipoGuardiaEnum nombreEnum = TipoGuardiaEnum.valueOf(nombre.toUpperCase());
        return tipoGuardiaRepository.findByNombre(nombreEnum);
    }

    public boolean existsByNombre(String nombre) {
        TipoGuardiaEnum nombreEnum = TipoGuardiaEnum.valueOf(nombre.toUpperCase());
        return tipoGuardiaRepository.existsByNombre(nombreEnum);
    }

    public boolean activoByNombre(String nombre) {
        TipoGuardiaEnum nombreEnum = TipoGuardiaEnum.valueOf(nombre.toUpperCase());
        return (tipoGuardiaRepository.existsByNombre(nombreEnum)
                && tipoGuardiaRepository.findByNombre(nombreEnum).get().isActivo());
    }

    public Optional<List<TipoGuardia>> findByActivoTrue() {
        return tipoGuardiaRepository.findByActivoTrue();
    }

    public List<TipoGuardia> findAll() {
        return tipoGuardiaRepository.findAll();
    }

    public List<TipoGuardiaListDto> findTipoGuardiaAll() {
        return tipoGuardiaRepository.findAll()
                .stream()
                .map(tg -> new TipoGuardiaListDto(tg.getId(), tg.getNombre().name()))
                .toList();
    }

    public Optional<TipoGuardia> findById(Long id) {
        return tipoGuardiaRepository.findById(id);
    }

    public void save(TipoGuardia tipoGuardia) {
        tipoGuardiaRepository.save(tipoGuardia);
    }

    public void deleteById(Long id) {
        tipoGuardiaRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return tipoGuardiaRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (tipoGuardiaRepository.existsById(id) && tipoGuardiaRepository.findById(id).get().isActivo());
    }

    public List<TipoGuardiaListDto> findAllActivo() {
        return tipoGuardiaRepository.findByActivoTrue()
                .orElse(List.of())
                .stream()
                .map(tg -> new TipoGuardiaListDto(tg.getId(), tg.getNombre().name()))
                .toList();
    }

}