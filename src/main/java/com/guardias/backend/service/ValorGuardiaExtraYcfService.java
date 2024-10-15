package com.guardias.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.ValorGuardiaExtrayCF;
import com.guardias.backend.repository.ValorGuardiaExtraYcfRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ValorGuardiaExtraYcfService {
    
    @Autowired
    ValorGuardiaExtraYcfRepository valorGuardiaExtraYcfRepository;

    public Optional<List<ValorGuardiaExtrayCF>> findByActivoTrue() {
        return valorGuardiaExtraYcfRepository.findByActivoTrue();
    }

     public List<ValorGuardiaExtrayCF> findAll() {
        return valorGuardiaExtraYcfRepository.findAll();
    }

    public Optional<ValorGuardiaExtrayCF> findById(Long id) {
        return valorGuardiaExtraYcfRepository.findById(id);
    }
    
    public Optional<ValorGuardiaExtrayCF> findByEfectorNombre(String nombreEfector) {
        return valorGuardiaExtraYcfRepository.findByEfectorNombre(nombreEfector);
    }

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
}
