package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.ValorGuardiaExtrayCF;
import com.guardias.backend.repository.HospitalRepository;
import com.guardias.backend.repository.ValorGuardiaExtraYcfRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ValorGuardiaExtraYcfService {
    
    @Autowired
    ValorGuardiaExtraYcfRepository valorGuardiaExtraYcfRepository;
    @Autowired
    HospitalRepository hospitalRepository;

    public Optional<List<ValorGuardiaExtrayCF>> findByActivoTrue() {
        return valorGuardiaExtraYcfRepository.findByActivoTrue();
    }

     public List<ValorGuardiaExtrayCF> findAll() {
        return valorGuardiaExtraYcfRepository.findAll();
    }

    public Optional<ValorGuardiaExtrayCF> findById(Long id) {
        return valorGuardiaExtraYcfRepository.findById(id);
    }
    
    /* public Optional<ValorGuardiaExtrayCF> buscarPorIdEfector(Long idEfector) {
        return valorGuardiaExtraYcfRepository.buscarPorIdEfector(idEfector);
    } */

    public boolean existsById(Long id) {
        return valorGuardiaExtraYcfRepository.existsById(id);
    }

    public void save(ValorGuardiaExtrayCF valorGuardiaCargoYagrup) {
        valorGuardiaExtraYcfRepository.save(valorGuardiaCargoYagrup);
    }

    public void deleteById(Long id) {
        valorGuardiaExtraYcfRepository.deleteById(id);
    }

    public boolean activo(Long id) {
        return (valorGuardiaExtraYcfRepository.existsById(id) && valorGuardiaExtraYcfRepository.findById(id).get().isActivo());
    }

    public Optional<ValorGuardiaExtrayCF> obtenerValorGuardiaExtraPorHospital(Long idHospital) {
        // Verificar existencia del hospital

        if (!hospitalRepository.existsById(idHospital))
            return Optional.empty();

        // 1. Buscar valor específico para el hospital
        Optional<ValorGuardiaExtrayCF> valorEspecifico = valorGuardiaExtraYcfRepository
                .findByHospitalesIdAndActivoTrue(idHospital);

        if (valorEspecifico.isPresent()) {
            return valorEspecifico;
        }

        // 2. Buscar valor genérico (sin hospitales asignados)
        return valorGuardiaExtraYcfRepository.findByActivoTrueAndHospitalesIsEmpty();
    }

}
