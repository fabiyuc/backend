package com.guardias.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.RegistrosPendientes;
import com.guardias.backend.repository.RegistrosPendientesRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegistrosPendientesService {
    @Autowired
    RegistrosPendientesRepository registrosPendientesRepository;

    public List<RegistrosPendientes> findByActivo() {
        return registrosPendientesRepository.findByActivoTrue();
    }

    public List<RegistrosPendientes> findAll() {
        return registrosPendientesRepository.findAll();
    }

    public Optional<RegistrosPendientes> findById(Long id) {
        return registrosPendientesRepository.findById(id);
    }

    public List<RegistrosPendientes> findByEfector(Long idEfector) {
        return registrosPendientesRepository.findByEfector(idEfector);
    }

    public List<RegistrosPendientes> findByEfectorAndFecha(Long idEfector, LocalDate fecha) {
        return registrosPendientesRepository.findByEfectorAndFecha(idEfector, fecha);
    }

    public void save(RegistrosPendientes registrosPendientes) {
        registrosPendientesRepository.save(registrosPendientes);
    }

    public void deleteById(Long id) {
        registrosPendientesRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return registrosPendientesRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (registrosPendientesRepository.existsById(id)
                && registrosPendientesRepository.findById(id).get().isActivo());
    }

}
