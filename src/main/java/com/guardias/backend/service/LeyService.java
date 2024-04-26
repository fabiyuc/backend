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
        boolean exists = (articuloRepository.existsByNumero(numero)
                && articuloRepository.findByNumero(numero).get().isActivo());
        if (!exists)
            exists = (incisoRepository.existsByNumero(numero)
                    && incisoRepository.findByNumero(numero).get().isActivo());
        return exists;
    }

    public boolean existsByDenominacion(String denominacion) {
        boolean exists = (articuloRepository.existsByDenominacion(denominacion)
                && articuloRepository.findByDenominacion(denominacion).get().isActivo());
        if (!exists)
            exists = (incisoRepository.existsByDenominacion(denominacion)
                    && incisoRepository.findByDenominacion(denominacion).get().isActivo());

        return exists;
    }

    public Ley findById(Long id) {
        Ley ley = articuloRepository.findById(id).orElse(null);
        if (ley == null) {
            ley = incisoRepository.findById(id).orElse(null);
        }

        if (ley.isActivo())
            return ley;
        else
            return null;
    }

    public Ley findByDenominacion(String denominacion) {
        Ley ley = articuloRepository.findByDenominacion(denominacion).orElse(null);
        if (ley == null) {
            ley = incisoRepository.findByDenominacion(denominacion).orElse(null);
        }
        if (ley.isActivo())
            return ley;
        else
            return null;
    }

    public Ley findByNumero(String numero) {
        Ley ley = articuloRepository.findByNumero(numero).orElse(null);
        if (ley == null) {
            ley = incisoRepository.findByNumero(numero).orElse(null);
        }
        if (ley.isActivo())
            return ley;
        else
            return null;
    }

}
