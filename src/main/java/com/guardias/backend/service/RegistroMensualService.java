package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.repository.RegistroMensualRepository;

@Service
@Transactional
public class RegistroMensualService {

    @Autowired
    RegistroMensualRepository registroMensualRepository;

    public List<RegistroMensual> findByActivoTrue() {
        return registroMensualRepository.findByActivoTrue();
    }

    public List<RegistroMensual> findAll() {
        return registroMensualRepository.findAll();
    }

    public List<RegistroMensual> findByAnioAndMesAndEfectorId(int anio, MesesEnum mes, Long idEfector) {
        return registroMensualRepository.findByAnioAndMesAndEfectorId(anio, mes, idEfector);
    }

    public Optional<RegistroMensual> findByIdAsistencialAndMes(Long idAsistencial, Long idEfector, MesesEnum mes,
            int anio) {
        return registroMensualRepository.findByIdAsistencialAndMes(idAsistencial, idEfector, mes, anio);
    }

    public Optional<Long> idByIdAsistencialAndMes(Long idAsistencial, Long idEfector, MesesEnum mes, int anio) {
        return registroMensualRepository.idByIdAsistencialAndMes(idAsistencial, idEfector, mes, anio);
    }

    public Optional<RegistroMensual> findById(Long id) {
        return registroMensualRepository.findById(id);
    }

    boolean existsByMes(String mes) {
        return registroMensualRepository.existsByMes(mes);
    }

    public boolean existsByIdAsistencial(Long idAsistencial) {
        return registroMensualRepository.existsByIdAsistencial(idAsistencial);
    }

    public boolean existsById(Long id) {
        return registroMensualRepository.existsById(id);
    }

    public boolean activo(Long id) {
        return (registroMensualRepository.existsById(id) && registroMensualRepository.findById(id).get().isActivo());
    }

    public void save(RegistroMensual registroMensual) {
        registroMensualRepository.save(registroMensual);
    }

    public void deleteById(Long id) {
        registroMensualRepository.deleteById(id);
    }

}
