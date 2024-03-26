package com.guardias.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guardias.backend.entity.Ley;
import com.guardias.backend.repository.ArticuloRepository;
import com.guardias.backend.repository.IncisoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LeyService {
    @Autowired
    ArticuloRepository articuloRepository;
    @Autowired
    IncisoRepository incisoRepository;

    public boolean existsByNumero(String numero) {
        boolean exists = articuloRepository.existsByNumero(numero);
        if (!exists)
            exists = incisoRepository.existsByNumero(numero);
        return exists;
    }

    public boolean existsByDenominacion(String denominacion) {
        boolean exists = articuloRepository.existsByDenominacion(denominacion);
        if (!exists)
            exists = incisoRepository.existsByDenominacion(denominacion);

        return exists;
    }

    public Ley findById(Long id) {
        Ley ley = articuloRepository.findById(id).orElse(null);
        if (ley == null) {
            ley = incisoRepository.findById(id).orElse(null);
        }
        return ley;
    }
}
